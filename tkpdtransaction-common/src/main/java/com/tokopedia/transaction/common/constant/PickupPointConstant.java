package com.tokopedia.transaction.common.constant;

public interface PickupPointConstant {

    String INTENT_REQ_PARAMS = "params";
    String INTENT_DISTRICT_NAME = "district_name";
    String INTENT_DATA_STORE = "store";
    String INTENT_CART_ITEM = "cart_item";
    String INTENT_DATA_POSITION = "position";

    interface Params {
        String PARAM_TOKEN = "token";
        String PARAM_PAGE = "page";
        String PARAM_UT = "ut";
        String PARAM_QUERY = "query";
        String PARAM_DISTRICT_ID = "district_id";
        String DEFAULT_PAGE = "0";
    }

}
