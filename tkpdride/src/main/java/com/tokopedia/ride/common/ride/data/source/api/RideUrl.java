package com.tokopedia.ride.common.ride.data.source.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;

/**
 * Created by alvarisi on 3/16/17.
 */

public interface RideUrl {
    String BASE_URL = TkpdBaseURL.RIDE_DOMAIN;

    String PRODUCTS = "/uber/products";
    String ESTIMATED_TIME = "/uber/estimates/time";
    String ESTIMATED_FARE = "/uber/estimates/fare";
    String REQUEST_CREATE = "/uber/request";
    String REQUEST_DETAIL = "/uber/request/details";
    String REQUEST_CANCEL = "/uber/request/cancel";
    String RECEIPT_DETAIL = "/uber/request/receipt";
    String PROMO = "/uber/offers";
    String PROMO_APPLY = "/uber/promo/apply";
    String REQUEST_MAP = "/requests/{request_id}/map";
    String TRANSACTIONS_ALL = "/uber/request/history";
    String TRANSACTION = "/uber/request/history";
    String REQUEST_CURRENT = "/uber/request/current";
    String RIDE_ADDRESS = "/user/address";
}
