package com.tokopedia.loyalty.domain.apiservice;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.loyalty.domain.entity.response.TokoPointResponse;

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

public interface TokoPointApi {
    @GET(TkpdBaseURL.TokoPoint.GET_COUPON_LIST)
    Observable<Response<TokoPointResponse>> getCouponList(@QueryMap Map<String, String> params);

    @POST(TkpdBaseURL.TokoPoint.POST_COUPON_VALIDATE_REDEEM)
    @Headers({"Content-Type: application/json"})
    Observable<Response<TokoPointResponse>> postCouponValidateRedeem(@Body JsonObject requestBody);

    @POST(TkpdBaseURL.TokoPoint.POST_COUPON_REDEEM)
    @Headers({"Content-Type: application/json"})
    Observable<Response<TokoPointResponse>> postCouponRedeem(@Body JsonObject requestBody);

    @GET(TkpdBaseURL.TokoPoint.GET_POINT_RECENT_HISTORY)
    Observable<Response<TokoPointResponse>> getPointRecentHistory(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.TokoPoint.GET_POINT_MAIN)
    Observable<Response<TokoPointResponse>> getPointMain(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.TokoPoint.GET_POINT_DRAWER)
    Observable<Response<TokoPointResponse>> getPointDrawer(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.TokoPoint.GET_POINT_STATUS)
    Observable<Response<TokoPointResponse>> getPointStatus(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.TokoPoint.GET_CATALOG_LIST)
    Observable<Response<TokoPointResponse>> getCatalogList(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.TokoPoint.GET_CATALOG_DETAIL)
    Observable<Response<TokoPointResponse>> getCatalogDetail(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.TokoPoint.GET_CATALOG_FILTER_CATEGORY)
    Observable<Response<TokoPointResponse>> getCalatogFilterCategory(@QueryMap Map<String, String> params);

}
