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
    public static final String FLIGHT_BANNER_VERSION = "v1.4/banner";
    public static final String PROMO_PATH =  "promo/";
    public static final String CONTACT_US_PATH = "contact-us";
    public static final String CONTACT_US_FLIGHT_PREFIX = "?pid=46&ivtype=4";
    public static String BASE_URL = "https://api-staging.tokopedia.com";
    public static String WEB_DOMAIN = "https://staging.tokopedia.com/";
    public static String ALL_PROMO_LINK = WEB_DOMAIN + "promo/";
    public static String CONTACT_US = WEB_DOMAIN + "contact-us";
    public static String CONTACT_US_FLIGHT_PREFIX_GLOBAL = WEB_DOMAIN + "contact-us?pid=46&ivtype=4";
    public static String PULSA_BASE_URL = "https://pulsa-api-staging.tokopedia.com/";
    public static String BANNER_PATH = PULSA_BASE_URL + "v1.4/banner";

    public static String getUrlPdf(String orderId, String filename, String userId) {
        return WEB_DOMAIN + "pesawat/pdf/generate?invoice_id=" + orderId + "&pdf=" + filename + "&user_id=" + userId;
    }

    public static String getUrlInvoice(String orderId, String userId) {
        return WEB_DOMAIN + "pesawat/invoice?invoice_id=" + orderId + "&user_id=" + userId;
    }
}
