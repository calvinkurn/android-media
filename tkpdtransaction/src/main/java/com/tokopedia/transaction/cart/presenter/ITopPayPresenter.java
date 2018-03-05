package com.tokopedia.transaction.cart.presenter;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.transaction.cart.model.thankstoppaydata.ThanksTopPayData;
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

    void processRedirectUrlContainsTopPayRedirectUrl(String url);

    void processRedirectUrlContainsAccountUrl(String url);

    void processRedirectUrlContainsLoginUrl();

    void processVerifyPaymentId();

    void processVerifyPaymentIdByCancelTopPay(String paymentId);

    void clearNotificationCart();

    void processPaymentAnalytics(
            LocalCacheHandler localCacheHandler, ThanksTopPayData thanksTopPayData
    ) throws Exception;
}
