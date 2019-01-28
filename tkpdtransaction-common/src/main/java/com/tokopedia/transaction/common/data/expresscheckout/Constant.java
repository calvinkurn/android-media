package com.tokopedia.transaction.common.data.expresscheckout;

/**
 * Created by Irfan Khoirul on 04/01/19.
 */

public interface Constant {

    String CHECKOUT_TYPE_DEFAULT = "ncf";
    String CHECKOUT_TYPE_OCS = "ocs";
    String CHECKOUT_TYPE_EXPRESS = "express";

    int REQUEST_CODE_ATC_EXPRESS = 10;
    int RESULT_CODE_ERROR = -10;
    int RESULT_CODE_NAVIGATE_TO_OCS = 20;
    int RESULT_CODE_NAVIGATE_TO_NCF = 30;

    String EXTRA_MESSAGES_ERROR = "EXTRA_MESSAGES_ERROR";

}
