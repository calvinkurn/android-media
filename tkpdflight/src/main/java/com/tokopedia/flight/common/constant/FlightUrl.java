package com.tokopedia.flight.common.constant;

/**
 * Created by nathan on 10/24/17.
 */

public class FlightUrl {

    public static final String FLIGHT_PATH = "travel/v1/flight/";
    public static final String FLIGHT_AIRPORT_PATH = FLIGHT_PATH + "dropdown/airport";
    public static final String FLIGHT_AIRLINE_PATH = FLIGHT_PATH + "dropdown/airline";
    public static final String FLIGHT_SEARCH_SINGLE = FLIGHT_PATH + "search/single";
    public static final String FLIGHT_CLASS_PATH = FLIGHT_PATH + "dropdown/class";
    public static final String FLIGHT_CART_PATH = FLIGHT_PATH + "cart";
    public static final String FLIGHT_CHECK_VOUCHER_CODE = FLIGHT_PATH + "voucher/check";
    public static final String FLIGHT_VERIFY_BOOKING = "travel/v1/oms/verify";
    public static final String FLIGHT_CHECKOUT_BOOKING = "travel/v1/oms/checkout";
    public static final String FLIGHT_ORDERS = FLIGHT_PATH + "order/list";
    public static final String FLIGHT_ORDER = FLIGHT_PATH + "order/{id}";
    public static String BASE_URL = "https://api-staging.tokopedia.com";
    public static String WEB_DOMAIN = "https://www.tokopedia.com/";
    public static String ALL_PROMO_LINK = WEB_DOMAIN + "promo/";
    public static String PULSA_BASE_URL = "https://pulsa-api.tokopedia.com/";
    public static String BANNER_PATH = PULSA_BASE_URL + "v1.4/banner";

    public static String getUrlPdf(String orderId){
        return BASE_URL + FLIGHT_PATH + "pdf/generate?order_id="+ orderId +"&pdf=filename.pdf";
    }
}
