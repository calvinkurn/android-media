package com.tokopedia.payment.listener;

import android.support.annotation.StringRes;

import com.tokopedia.payment.model.PaymentPassData;

/**
 * @author anggaprasetiyo on 12/8/16.
 */

public interface ITopPayView {
    void renderWebViewPostUrl(String url, byte[] postData);

    void showToastMessageWithForceCloseView(String message);

    void showToastMessage(String message);

    void callbackPaymentCanceled();

    void callbackPaymentFailed();

    void callbackPaymentSucceed();

    void hideProgressBar();

    void showProgressBar();

    void showTimeoutErrorOnUiThread();

    void setWebPageTitle(String title);

    void backStackAction();

    String getStringFromResource(@StringRes int resId);

    PaymentPassData getPaymentPassData();
}
