package com.tokopedia.transaction.network.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by kris on 12/28/17. Tokopedia
 */

public interface MyShopOrderActApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_EDIT_SHIPPING_REF)
    Observable<Response<TkpdResponse>> editShippingRef(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_PROCEED_ORDER)
    Observable<Response<TkpdResponse>> proceedOrder(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_PROCEED_SHIPPING)
    Observable<Response<TkpdResponse>> proceedShipping(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_RETRY_PICKUP)
    Observable<Response<TkpdResponse>> retryPickUp(@FieldMap Map<String, String> params);
}
