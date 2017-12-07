package com.tokopedia.loyalty.domain.repository;

import com.tokopedia.core.drawer2.data.viewmodel.TokoPointDrawerData;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.loyalty.domain.apiservice.TokoPointService;
import com.tokopedia.loyalty.domain.entity.request.RequestBodyCouponRedeem;
import com.tokopedia.loyalty.domain.entity.request.RequestBodyValidateRedeem;
import com.tokopedia.loyalty.domain.entity.response.CouponListDataResponse;
import com.tokopedia.loyalty.domain.entity.response.TokoPointDrawerDataResponse;
import com.tokopedia.loyalty.domain.entity.response.TokoPointResponse;
import com.tokopedia.loyalty.view.data.CouponData;

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
    private final TokoPointResponseMapper tokoPointResponseMapper;
    private final ITokoPointDBService tokoPointDBService;

    @Inject
    public TokoPointRepository(TokoPointService tokoPointService,
                               ITokoPointDBService tokoPointDBService,
                               TokoPointResponseMapper tokoPointResponseMapper) {
        this.tokoPointService = tokoPointService;
        this.tokoPointResponseMapper = tokoPointResponseMapper;
        this.tokoPointDBService = tokoPointDBService;
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

    @Override
    public Observable<String> postCouponValidateRedeem(RequestBodyValidateRedeem requestBodyValidateRedeem) {
        return null;
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
                return tokoPointService.getApi().getPointDrawer(param).map(
                        new Func1<Response<TokoPointResponse>, TokoPointDrawerData>() {
                            @Override
                            public TokoPointDrawerData call(Response<TokoPointResponse> tokoplusResponseResponse) {
                                return tokoPointResponseMapper.convertTokoplusPointDrawer(
                                        tokoplusResponseResponse.body().convertDataObj(
                                                TokoPointDrawerDataResponse.class
                                        )
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
}
