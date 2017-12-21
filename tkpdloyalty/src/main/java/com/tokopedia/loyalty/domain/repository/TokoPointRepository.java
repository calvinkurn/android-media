package com.tokopedia.loyalty.domain.repository;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.core.drawer2.data.viewmodel.TokoPointDrawerData;
import com.tokopedia.core.network.apiservices.transaction.TXVoucherService;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.loyalty.domain.apiservice.DigitalEndpointService;
import com.tokopedia.loyalty.domain.apiservice.TokoPointService;
import com.tokopedia.loyalty.domain.entity.request.RequestBodyCouponRedeem;
import com.tokopedia.loyalty.domain.entity.request.RequestBodyValidateRedeem;
import com.tokopedia.loyalty.domain.entity.response.CouponListDataResponse;
import com.tokopedia.loyalty.domain.entity.response.DigitalVoucherData;
import com.tokopedia.loyalty.domain.entity.response.TokoPointDrawerDataResponse;
import com.tokopedia.loyalty.domain.entity.response.TokoPointResponse;
import com.tokopedia.loyalty.domain.entity.response.ValidateRedeemCouponResponse;
import com.tokopedia.loyalty.domain.entity.response.VoucherResponse;
import com.tokopedia.loyalty.exception.LoyaltyErrorException;
import com.tokopedia.loyalty.view.data.CouponData;
import com.tokopedia.loyalty.view.data.CouponViewModel;
import com.tokopedia.loyalty.view.data.VoucherViewModel;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 27/11/17.
 */

public class TokoPointRepository implements ITokoPointRepository {

    private final TokoPointService tokoPointService;
    private final TXVoucherService txVoucherService;
    private final TokoPointResponseMapper tokoPointResponseMapper;
    private final ITokoPointDBService tokoPointDBService;
    private final DigitalEndpointService digitalService;

    @Inject
    public TokoPointRepository(TokoPointService tokoPointService,
                               ITokoPointDBService tokoPointDBService,
                               TokoPointResponseMapper tokoPointResponseMapper,
                               TXVoucherService txVoucherService,
                               DigitalEndpointService digitalService

    ) {
        this.tokoPointService = tokoPointService;
        this.tokoPointResponseMapper = tokoPointResponseMapper;
        this.tokoPointDBService = tokoPointDBService;
        this.txVoucherService = txVoucherService;
        this.digitalService = digitalService;
    }

    @Override
    public Observable<List<CouponData>> getCouponList(TKPDMapParam<String, String> param) {
        return tokoPointService.getApi().getCouponList(param)
                .map(new Func1<Response<TokoPointResponse>, List<CouponData>>() {
                    @Override
                    public List<CouponData> call(Response<TokoPointResponse> tokoplusResponseResponse) {
                        if (tokoplusResponseResponse.body() == null) {
                            throw new LoyaltyErrorException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        } else if (tokoplusResponseResponse
                                .body()
                                .getTokoPointHeaderResponse()
                                .getErrorCode() != null) {
                            throw new LoyaltyErrorException(tokoplusResponseResponse
                                    .body().getTokoPointHeaderResponse().getMessageFormatted());
                        }
                        return tokoPointResponseMapper.convertCouponListData(
                                tokoplusResponseResponse.body().convertDataObj(
                                        CouponListDataResponse.class
                                )
                        );
                    }
                });
    }

    @Override
    public Observable<String> postCouponValidateRedeem(RequestBodyValidateRedeem requestBodyValidateRedeem) {
        JsonElement requestJsonElement = new JsonParser().parse(new Gson().toJson(requestBodyValidateRedeem));
        JsonObject requestJson = new JsonObject();
        requestJson.add("data", requestJsonElement);
        return tokoPointService.getApi().postCouponValidateRedeem(requestJson).map(new Func1<Response<TokoPointResponse>, String>() {
            @Override
            public String call(Response<TokoPointResponse> tokoPointResponseResponse) {
                return tokoPointResponseMapper
                        .getSuccessValidateRedeemMessage(tokoPointResponseResponse
                                .body().convertDataObj(ValidateRedeemCouponResponse.class));
            }
        });
    }

    @Override
    public Observable<String> postCouponRedeem(RequestBodyCouponRedeem requestBodyCouponRedeem) {
        return null;
    }

    @Override
    public Observable<String> getPointRecentHistory(TKPDMapParam<String, String> param) {
        return null;
    }

    @Override
    public Observable<String> getPointMain(TKPDMapParam<String, String> param) {
        return null;
    }

    @Override
    public Observable<TokoPointDrawerData> getPointDrawer(final TKPDMapParam<String, String> param) {
        return tokoPointDBService.getPointDrawer().map(new Func1<TokoPointDrawerDataResponse, TokoPointDrawerData>() {
            @Override
            public TokoPointDrawerData call(TokoPointDrawerDataResponse tokoPointDrawerDataResponse) {
                return tokoPointResponseMapper.convertTokoplusPointDrawer(
                        tokoPointDrawerDataResponse
                );
            }
        }).onErrorResumeNext(new Func1<Throwable, Observable<? extends TokoPointDrawerData>>() {
            @Override
            public Observable<? extends TokoPointDrawerData> call(Throwable throwable) {
                throwable.printStackTrace();
                return tokoPointService.getApi().getPointDrawer(param)
                        .flatMap(new Func1<Response<TokoPointResponse>, Observable<TokoPointDrawerDataResponse>>() {
                            @Override
                            public Observable<TokoPointDrawerDataResponse> call(Response<TokoPointResponse> tokoPointResponseResponse) {
                                TokoPointDrawerDataResponse tokoPointDrawerDataResponse =
                                        tokoPointResponseResponse.body().convertDataObj(
                                                TokoPointDrawerDataResponse.class
                                        );
                                return tokoPointDBService.storePointDrawer(tokoPointDrawerDataResponse);
                            }
                        }).map(new Func1<TokoPointDrawerDataResponse, TokoPointDrawerData>() {
                            @Override
                            public TokoPointDrawerData call(TokoPointDrawerDataResponse tokoPointDrawerDataResponse) {
                                return tokoPointResponseMapper.convertTokoplusPointDrawer(
                                        tokoPointDrawerDataResponse
                                );
                            }
                        });
            }
        });

    }

    @Override
    public Observable<String> getPointStatus(TKPDMapParam<String, String> param) {
        return null;
    }

    @Override
    public Observable<String> getCatalogList(TKPDMapParam<String, String> param) {
        return null;
    }

    @Override
    public Observable<String> getCatalogDetail(TKPDMapParam<String, String> param) {
        return null;
    }

    @Override
    public Observable<String> getCatalogFilterCategory(TKPDMapParam<String, String> param) {
        return null;
    }

    @Override
    public Observable<VoucherViewModel> checkVoucherValidity(TKPDMapParam<String, String> param,
                                                             final String voucherCode) {
        return txVoucherService.getApi().checkVoucherCode(param).map(new Func1<Response<TkpdResponse>, VoucherViewModel>() {
            @Override
            public VoucherViewModel call(Response<TkpdResponse> networkResponse) {
                VoucherResponse voucherResponse = new Gson().fromJson(
                        networkResponse.body().getStringData(), VoucherResponse.class
                );
                if (networkResponse.body().isError()) {
                    throw new LoyaltyErrorException(networkResponse.body().getErrorMessageJoined());
                }
                return tokoPointResponseMapper.voucherViewModel(voucherResponse, voucherCode);
            }
        });
    }

    @Override
    public Observable<CouponViewModel> checkCouponValidity(
            TKPDMapParam<String, String> param,
            final String voucherCode, final String couponTitle
    ) {
        return txVoucherService.getApi().checkVoucherCode(param)
                .map(new Func1<Response<TkpdResponse>, CouponViewModel>() {
                    @Override
                    public CouponViewModel call(Response<TkpdResponse> networkResponse) {
                        VoucherResponse voucherResponse = new Gson().fromJson(
                                networkResponse.body().getStringData(), VoucherResponse.class
                        );
                        if (networkResponse.body().isError()) {
                            throw new LoyaltyErrorException(networkResponse.body().getErrorMessageJoined());
                        }
                        return tokoPointResponseMapper.couponViewModel(voucherResponse, voucherCode, couponTitle
                        );
                    }
                });
    }

    @Override
    public Observable<VoucherViewModel> checkDigitalVoucherValidity(
            TKPDMapParam<String, String> param, final String voucherCode
    ) {
        return digitalService.getApi().checkVoucher(param)
                .map(new Func1<Response<TkpdDigitalResponse>, VoucherViewModel>() {
                    @Override
                    public VoucherViewModel call(Response<TkpdDigitalResponse> tkpdDigitalResponseResponse) {
                        return tokoPointResponseMapper.digtialVoucherViewModel(
                                tkpdDigitalResponseResponse.body().convertDataObj(DigitalVoucherData.class),
                                voucherCode
                        );
                    }
                });
    }

    @Override
    public Observable<CouponViewModel> checkDigitalCouponValidity(
            TKPDMapParam<String, String> param, final String voucherCode, final String couponTitle
    ) {
        return digitalService.getApi().checkVoucher(param).
                map(new Func1<Response<TkpdDigitalResponse>, CouponViewModel>() {
                    @Override
                    public CouponViewModel call(Response<TkpdDigitalResponse> tkpdDigitalResponseResponse) {
                        return tokoPointResponseMapper.digitalCouponViewModel(
                                tkpdDigitalResponseResponse.body().convertDataObj(DigitalVoucherData.class),
                                voucherCode,
                                couponTitle
                        );
                    }
                });
    }
}
