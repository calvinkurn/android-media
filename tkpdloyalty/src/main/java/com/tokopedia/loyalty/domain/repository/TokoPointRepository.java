package com.tokopedia.loyalty.domain.repository;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.core.drawer2.data.viewmodel.TopPointDrawerData;
import com.tokopedia.core.network.apiservices.transaction.TXService;
import com.tokopedia.core.network.apiservices.transaction.TXVoucherService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.loyalty.domain.apiservice.TokoPointService;
import com.tokopedia.loyalty.domain.entity.request.RequestBodyCouponRedeem;
import com.tokopedia.loyalty.domain.entity.request.RequestBodyValidateRedeem;
import com.tokopedia.loyalty.domain.entity.response.CouponListDataResponse;
import com.tokopedia.loyalty.domain.entity.response.TokoPointDrawerDataResponse;
import com.tokopedia.loyalty.domain.entity.response.TokoPointResponse;
import com.tokopedia.loyalty.domain.entity.response.ValidateRedeemCouponResponse;
import com.tokopedia.loyalty.domain.entity.response.VoucherResponse;
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
    private final TXService txService;
    private final TokoPointResponseMapper tokoPointResponseMapper;

    @Inject
    public TokoPointRepository(TokoPointService tokoPointService,
                               TXVoucherService txVoucherService,
                               TXService txService,
                               TokoPointResponseMapper tokoPointResponseMapper) {
        this.tokoPointService = tokoPointService;
        this.tokoPointResponseMapper = tokoPointResponseMapper;
        this.txService = txService;
        this.txVoucherService = txVoucherService;
    }

    @Override
    public Observable<List<CouponData>> getCouponList(TKPDMapParam<String, String> param) {
        return tokoPointService.getApi().getCouponList(param)
                .map(new Func1<Response<TokoPointResponse>, List<CouponData>>() {
                    @Override
                    public List<CouponData> call(Response<TokoPointResponse> tokoplusResponseResponse) {
                        return tokoPointResponseMapper.convertCouponListData(
                                tokoplusResponseResponse.body().convertDataObj(
                                        CouponListDataResponse.class
                                )
                        );
                    }
                });
    }

    /*@Override
    public Observable<List<CouponData>> getCouponList(TKPDMapParam<String, String> param) {
        return txService.getApi().getCouponList(param)
                .map(new Func1<Response<String>, List<CouponData>>() {
                    @Override
                    public List<CouponData> call(Response<String> tokoplusResponseResponse) {
                        TokoPointResponse convertedResponse = new Gson()
                                .fromJson(tokoplusResponseResponse.body(),TokoPointResponse.class);
                        return tokoPointResponseMapper.convertCouponListData(
                                convertedResponse.convertDataObj(CouponListDataResponse.class)
                        );
                    }
                });
    }*/

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
    public Observable<TopPointDrawerData> getPointDrawer(TKPDMapParam<String, String> param) {
        return tokoPointService.getApi().getPointDrawer(param).map(
                new Func1<Response<TokoPointResponse>, TopPointDrawerData>() {
                    @Override
                    public TopPointDrawerData call(Response<TokoPointResponse> tokoplusResponseResponse) {
                        return tokoPointResponseMapper.convertTokoplusPointDrawer(
                                tokoplusResponseResponse.body().convertDataObj(
                                        TokoPointDrawerDataResponse.class
                                )
                        );
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
                VoucherViewModel viewModel = new VoucherViewModel();
                viewModel.setAmount(voucherResponse.getVoucher().getVoucherAmountIdr());
                viewModel.setMessage(voucherResponse.getVoucher().getVoucherPromoDesc());
                viewModel.setCode(voucherCode);
                return viewModel;
            }
        });
    }

    @Override
    public Observable<CouponViewModel> checkCouponValidity(
            TKPDMapParam<String, String> param,
            final String voucherCode, final String couponTitle
    ) {
        return txVoucherService.getApi().checkVoucherCode(param).map(
                new Func1<Response<TkpdResponse>, CouponViewModel>() {
            @Override
            public CouponViewModel call(Response<TkpdResponse> networkResponse) {
                VoucherResponse voucherResponse = new Gson().fromJson(
                        networkResponse.body().getStringData(), VoucherResponse.class
                );
                CouponViewModel viewModel = new CouponViewModel();
                viewModel.setAmount(voucherResponse.getVoucher().getVoucherAmountIdr());
                viewModel.setMessage(voucherResponse.getVoucher().getVoucherPromoDesc());
                viewModel.setCode(voucherCode);
                viewModel.setTitle(couponTitle);
                return viewModel;
            }
        });
    }
}
