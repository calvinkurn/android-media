package com.tokopedia.transaction.cart.presenter;

import com.tokopedia.transaction.cart.model.toppaydata.TopPayParameterData;

/**
 * @author anggaprasetiyo on 12/16/16.
 */

public interface ITopPayPresenter {
    String CHARSET_UTF_8 = "UTF-8";
    String KEY_QUERY_PAYMENT_ID = "id";
    String KEY_QUERY_LD = "ld";

    void proccessUriPayment(TopPayParameterData topPayParameterData);

    void processRedirectUrlContainsTopPayCallbackUrl(String url);

    void processRedirectUrlContainsAccountUrl(String url);

    void processRedirectUrlContainsLoginUrl();

    void processVerifyPaymentId(String paymentId);

    void clearNotificationCart();
}
