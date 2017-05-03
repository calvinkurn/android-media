package com.tokopedia.seller.selling.network.apiservices.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.seller.selling.model.ResponseConfirmShipping;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
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
    @POST(TkpdBaseURL.Shop.PATH_PROCEED_SHIPPING)
    Observable<ResponseConfirmShipping> proceedShippingMulti(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_RETRY_PICKUP)
    Observable<Response<TkpdResponse>> retryPickUp(@FieldMap Map<String, String> params);
}
