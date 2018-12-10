package com.tokopedia.transaction.router;

import android.app.Activity;
import android.content.Intent;

import com.tokopedia.transaction.purchase.detail.activity.OrderDetailActivity;

/**
 * @author anggaprasetiyo on 15/05/18.
 */
public interface ITransactionOrderDetailRouter {

    Intent transactionOrderDetailRouterGetIntentUploadAwb(String urlUpload);

    void sendEventTracking(String event, String category, String action, String label);

    Intent getCartIntent(Activity activity);
}
