package com.tokopedia.transaction.cart.listener;

import android.app.Activity;
import android.content.Intent;

import com.tokopedia.core.product.listener.ViewListener;

/**
 * @author anggaprasetiyo on 12/8/16.
 */

public interface ITopPayView extends ViewListener {
    void renderWebViewPostUrl(String url, byte[] postData);

    void renderErrorManipulateUriPayment(String message);

    Activity getContextActivity();

    void executeService(Intent intent);

    void showToastMessageWithForceCloseView(String message);

    void setPaymentId(String paymentId);
}
