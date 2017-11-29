package com.tokopedia.loyalty.domain.apiservice;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.loyalty.domain.entity.response.TokoplusResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author anggaprasetiyo on 27/11/17.
 */

public interface TokoplusApi {
    @GET(TkpdBaseURL.Tokoplus.GET_COUPON_LIST)
    Observable<Response<TokoplusResponse>> getCouponList(@QueryMap Map<String, String> params);

    @POST(TkpdBaseURL.Tokoplus.POST_COUPON_VALIDATE_REDEEM)
    @Headers({"Content-Type: application/json"})
    Observable<Response<TokoplusResponse>> postCouponValidateRedeem(@Body JsonObject requestBody);

    @POST(TkpdBaseURL.Tokoplus.POST_COUPON_REDEEM)
    @Headers({"Content-Type: application/json"})
    Observable<Response<TokoplusResponse>> postCouponRedeem(@Body JsonObject requestBody);

    @GET(TkpdBaseURL.Tokoplus.GET_POINT_RECENT_HISTORY)
    Observable<Response<TokoplusResponse>> getPointRecentHistory(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Tokoplus.GET_POINT_MAIN)
    Observable<Response<TokoplusResponse>> getPointMain(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Tokoplus.GET_POINT_DRAWER)
    Observable<Response<TokoplusResponse>> getPointDrawer(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Tokoplus.GET_POINT_STATUS)
    Observable<Response<TokoplusResponse>> getPointStatus(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Tokoplus.GET_CATALOG_LIST)
    Observable<Response<TokoplusResponse>> getCatalogList(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Tokoplus.GET_CATALOG_DETAIL)
    Observable<Response<TokoplusResponse>> getCatalogDetail(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Tokoplus.GET_CATALOG_FILTER_CATEGORY)
    Observable<Response<TokoplusResponse>> getCalatogFilterCategory(@QueryMap Map<String, String> params);

}
