package com.tokopedia.ride.common.ride.data.source.api;

/**
 * Created by alvarisi on 3/16/17.
 */

public interface RideUrl {
    String BASE_URL = "https://ride-staging.tokopedia.com/";

    String PRODUCTS = "/uber/products";
    String ESTIMATED_TIME = "/uber/estimates/time";
    String ESTIMATED_FARE = "/uber/estimates/fare";
    String REQUEST_CREATE = "/uber/request";
    String REQUEST_DETAIL = "/uber/request/details";
    String REQUEST_CANCEL = "/uber/request/cancel";
    String RECEIPT_DETAIL = "/uber/request/receipt";
    String PROMO = "/uber/promo";
    String REQUEST_MAP = "/requests/{request_id}/map";
    String TRANSACTIONS_ALL = "/uber/request/history";
}
