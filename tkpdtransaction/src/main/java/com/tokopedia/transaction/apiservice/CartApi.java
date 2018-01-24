package com.tokopedia.transaction.apiservice;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.constants.TkpdBaseURL;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author anggaprasetiyo on 24/01/18.
 */

public interface CartApi {

    @POST(TkpdBaseURL.Cart.PATH_ADD_TO_CART)
    @Headers({"Content-Type: application/json"})
    Observable<Response<CartResponse>> postAddToCart(@Body JsonObject requestBody);

    @GET(TkpdBaseURL.Cart.PATH_CART_LIST)
    Observable<Response<CartResponse>> getCartList(@QueryMap Map<String, String> params);

//    public static final String PATH_REMOVE_FROM_CART = "api/" + VERSION + "/remove_product_cart";
//    public static final String PATH_UPDATE_CART = "api/" + VERSION + "/update_cart";
//    public static final String PATH_CHECK_PROMO_CODE_CART_LIST = "api/" + VERSION + "/check_promo_code";
//    public static final String PATH_SHIPPING_ADDRESS = "api/" + VERSION + "/shipping_address";
//    public static final String PATH_SHIPMENT_ADDRESS_FORM_DIRECT = "api/" + VERSION + "/shipment_address_form";
//    public static final String PATH_CHECK_PROMO_CODE_CART_COURIER = "api/" + VERSION + "/check_promo_code_final";
//    public static final String PATH_CHECKOUT = "api/" + VERSION + "/checkout";
//    public static final String PATH_RESET_CART = "api/" + VERSION + "/reset_cart_cache";
//    public static final String PATH_UPDATE_STATE_BY_PAYMENT = "api/" + VERSION + "/update_state_by_payment";
//    public static final String PATH_NOTIFICATION_COUNTER = "api/" + VERSION + "/counter";
//    public static final String PATH_COUPON_LIST = "api/" + VERSION + "/coupon_list";
//    public static final String PATH_SAVE_PICKUP_STORE_POINT = "api/" + VERSION + "/save_pickup_store_point";

}
