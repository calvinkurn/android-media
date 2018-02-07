package com.tokopedia.seller.logistic.data.source.cloud.api;

import com.tokopedia.core.network.apiservices.shop.apis.model.openshopdistrict.OpenShopDistrictServiceModel;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.seller.common.data.response.DataResponse;
import com.tokopedia.seller.logistic.model.CouriersModel;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;


public interface WSLogisticApi {
    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_MY_SHOP + TkpdBaseURL.Shop.PATH_GET_OPEN_SHOP_FORM)
    Observable<Response<TkpdResponse>> getOpenShopForm(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_MY_SHOP + TkpdBaseURL.Shop.PATH_GET_OPEN_SHOP_FORM)
    Observable<Response<OpenShopDistrictServiceModel>> fetchDistrictData(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_MY_SHOP + TkpdBaseURL.Shop.PATH_GET_OPEN_SHOP_FORM)
    Observable<Response<DataResponse<CouriersModel>>> getLogisticAvailable(@FieldMap Map<String, String> params);

    @GET(TkpdBaseURL.Shop.PATH_MY_SHOP_SHIPMENT + TkpdBaseURL.Shop.PATH_GET_SHIPPING_INFO)
    Observable<Response<TkpdResponse>> getShippingList(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Shop.PATH_SHIPPING_WEBVIEW + TkpdBaseURL.Shop.PATH_GET_DETAIL_INFO_DETAIL)
    Observable<String> getShippingWebViewDetail(@QueryMap Map<String, String> param);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_MY_SHOP_SHIPMENT_ACTION + TkpdBaseURL.Shop.PATH_UPDATE_SHIPPING_INFO)
    Observable<Response<TkpdResponse>> updateShipmentInfo(@FieldMap Map<String, String> params);
}
