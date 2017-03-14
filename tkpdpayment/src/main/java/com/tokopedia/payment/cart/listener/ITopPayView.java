package com.tokopedia.payment.cart.listener;

import android.support.annotation.StringRes;

/**
 * @author anggaprasetiyo on 12/8/16.
 */

public interface ITopPayView {
    void renderWebViewPostUrl(String url, byte[] postData);

    void showToastMessageWithForceCloseView(String message);

    void setPaymentId(String paymentId);

    String getStringFromResource(@StringRes int resId);

}
