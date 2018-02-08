package com.tokopedia.ride.common.ride.data.source.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;

/**
 * Created by alvarisi on 3/16/17.
 */

public interface RideUrl {
    String BASE_URL = TkpdBaseURL.RIDE_DOMAIN;

    String PRODUCT = "/uber/product/detail";
    String PRODUCTS = "/uber/products";
    String ESTIMATED_PRICE = "/uber/estimates/price";
    String ESTIMATED_TIME = "/uber/estimates/time";
    String ESTIMATED_FARE = "/uber/estimates/fare";
    String REQUEST_CREATE = "v2/uber/request";
    String REQUEST_DETAIL = "/uber/request/details";
    String REQUEST_CANCEL = "/uber/request/cancel";
    String RECEIPT_DETAIL = "/uber/request/receipt";
    String PROMO = "/uber/offers";
    String REQUEST_MAP = "/uber/request/map";
    String TRANSACTIONS_ALL = "/uber/request/history";
    String TRANSACTIONS_ALL_V2 = "/v2/uber/request/history"; //with pagination
    String TRANSACTION = "/uber/request/history";
    String REQUEST_CURRENT = "/uber/request/current";
    String RIDE_ADDRESS = "/user/address";
    String RIDE_RATING = "/uber/rating";
    String CANCEL_REASONS = "/uber/cancel/reasons";
    String UPDATE_REQUEST = "/uber/request/update";
    String SEND_TIP = "/uber/tip/send";
    String PAYMENT_METHOD_LIST = "v1/uber/payment_method/list";
    String GET_NEARBY_CARS = "/uber/cars";
    String PAY_PENDING_AMOUNT = "v1/uber/pending/pay";
    String GET_PENDING_AMOUNT = "v1/uber/pending/get";
}
