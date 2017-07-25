package com.tokopedia.payment.listener;

import android.content.Intent;

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

    String getStringFromResource(int resId);

    PaymentPassData getPaymentPassData();

    void navigateToActivity(Intent intentCart);
}
