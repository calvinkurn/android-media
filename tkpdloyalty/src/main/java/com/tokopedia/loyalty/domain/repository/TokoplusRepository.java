package com.tokopedia.loyalty.domain.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.loyalty.domain.apiservice.TokoplusService;
import com.tokopedia.loyalty.domain.entity.request.RequestBodyCouponRedeem;
import com.tokopedia.loyalty.domain.entity.request.RequestBodyValidateRedeem;
import com.tokopedia.loyalty.domain.entity.response.CouponListDataResponse;
import com.tokopedia.loyalty.domain.entity.response.TokoplusResponse;
import com.tokopedia.loyalty.view.data.CouponData;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 27/11/17.
 */

public class TokoplusRepository implements ITokoplusRepository {

    private final TokoplusService tokoplusService;
    private final TokoplusResponseMapper tokoplusResponseMapper;

    @Inject
    public TokoplusRepository(TokoplusService tokoplusService,
                              TokoplusResponseMapper tokoplusResponseMapper) {
        this.tokoplusService = tokoplusService;
        this.tokoplusResponseMapper = tokoplusResponseMapper;
    }

    @Override
    public Observable<List<CouponData>> getCouponList(TKPDMapParam<String, String> param) {
        return tokoplusService.getApi().getCouponList(param)
                .map(new Func1<Response<TokoplusResponse>, List<CouponData>>() {
                    @Override
                    public List<CouponData> call(Response<TokoplusResponse> tokoplusResponseResponse) {
                        return tokoplusResponseMapper.convertCouponListData(
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
    public Observable<String> getPointDrawer(TKPDMapParam<String, String> param) {
        return null;
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
