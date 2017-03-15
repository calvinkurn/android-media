package com.tokopedia.payment.model;

/**
 * Created by kris on 3/14/17. Tokopedia
 */

public interface TopPayBaseModel {

    String getRedirectUrlToPass();

    String getQueryStringToPass();

    String getCallbackUrlToPass();

    String getTransactionIdToPass();

}
