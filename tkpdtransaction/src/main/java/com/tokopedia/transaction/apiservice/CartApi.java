package com.tokopedia.transaction.apiservice;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.transaction.checkout.domain.response.couponlist.CouponDataResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author anggaprasetiyo on 24/01/18.
 */

public interface CartApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Cart.PATH_ADD_TO_CART)
    Observable<Response<CartResponse>> postAddToCart(@FieldMap Map<String, String> params);

    @GET(TkpdBaseURL.Cart.PATH_CART_LIST)
    Observable<Response<CartResponse>> getCartList(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Cart.PATH_CART_LIST)
    Observable<Response<String>> getCartListString(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Cart.PATH_REMOVE_FROM_CART)
    Observable<Response<CartResponse>> postDeleteCart(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Cart.PATH_UPDATE_CART)
    Observable<Response<CartResponse>> postUpdateCart(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Cart.PATH_SHIPPING_ADDRESS)
    Observable<Response<CartResponse>> postSetShippingAddress(@FieldMap Map<String, String> params);

    @GET(TkpdBaseURL.Cart.PATH_SHIPMENT_ADDRESS_FORM_DIRECT)
    Observable<Response<CartResponse>> getShipmentAddressForm(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Cart.PATH_CHECKOUT)
    Observable<Response<CartResponse>> checkout(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Cart.PATH_RESET_CART)
    Observable<Response<CartResponse>> resetCart(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Cart.PATH_CHECK_PROMO_CODE_CART_LIST)
    Observable<Response<CartResponse>> checkPromoCodeCartList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Cart.PATH_CHECK_PROMO_CODE_CART_COURIER)
    Observable<Response<CartResponse>> checkPromoCodeCartShipment(@FieldMap Map<String, String> params);

    @GET(TkpdBaseURL.Cart.PATH_COUPON_LIST)
    Observable<Response<CartResponse>> getCouponList(@QueryMap Map<String, String> params);


//    public static final String PATH_UPDATE_STATE_BY_PAYMENT = "api/" + VERSION + "/update_state_by_payment";
//    public static final String PATH_NOTIFICATION_COUNTER = "api/" + VERSION + "/counter";
//    public static final String PATH_SAVE_PICKUP_STORE_POINT = "api/" + VERSION + "/save_pickup_store_point";

}
