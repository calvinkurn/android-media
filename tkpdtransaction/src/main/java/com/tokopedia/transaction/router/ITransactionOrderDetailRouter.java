package com.tokopedia.transaction.router;

import android.app.Activity;
import android.content.Intent;

import java.util.Map;

import com.tokopedia.transaction.purchase.detail.activity.OrderDetailActivity;

/**
 * @author anggaprasetiyo on 15/05/18.
 */
public interface ITransactionOrderDetailRouter {

    Intent transactionOrderDetailRouterGetIntentUploadAwb(String urlUpload);

    Intent getCartIntent(Activity activity);

    boolean getBooleanRemoteConfig(String key, boolean defaultValue);

    boolean isToggleBuyAgainOn();
}
