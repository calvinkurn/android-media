package com.tokopedia.payment.model;

/**
 * Created by kris on 3/14/17. Tokopedia
 */

public interface TopPayBaseModel {

    String getRedirectUrlToPass();

    String getQueryStringToPass();

    String getCallbackSuccessUrlToPass();

    String getCallbackFailedUrlToPass();

    String getTransactionIdToPass();

}
