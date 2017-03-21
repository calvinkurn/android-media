package com.tokopedia.payment.cart.listener;

import android.os.Bundle;

/**
 * Created by kris on 3/8/17. Tokopedia
 */

public interface CartActivityListener {

    void onBackKeyPressed(String paymentId);

    void processVerifyPaymentId(Bundle bundle);

    void processPaymentFailed(Bundle bundle);

    void showMessageWithForceCloseView(String errorMessage);

    void hideProgressBar();

    void showProgressBar();

    void showTimeoutErrorOnUiThread();

    void setWebPageTitle(String title);
}
