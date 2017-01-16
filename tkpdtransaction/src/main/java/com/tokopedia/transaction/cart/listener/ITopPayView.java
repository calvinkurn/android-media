package com.tokopedia.transaction.cart.listener;

import android.app.IntentService;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.transaction.base.IBaseView;

/**
 * @author anggaprasetiyo on 12/8/16.
 */

public interface ITopPayView extends IBaseView {
    void renderWebViewPostUrl(String url, byte[] postData);

    void showToastMessageWithForceCloseView(String message);

    void setPaymentId(String paymentId);

    String getStringFromResource(@StringRes int resId);

    void executeIntentService(Bundle bundle, Class<? extends IntentService> clazz);

    LocalCacheHandler getLocalCacheHandlerNotification();

    @NonNull
    String getPaymentId();
}
