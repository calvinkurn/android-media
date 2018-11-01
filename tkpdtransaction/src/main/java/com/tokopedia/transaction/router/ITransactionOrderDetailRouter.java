package com.tokopedia.transaction.router;

import android.content.Intent;

import java.util.Map;

/**
 * @author anggaprasetiyo on 15/05/18.
 */
public interface ITransactionOrderDetailRouter {

    Intent transactionOrderDetailRouterGetIntentUploadAwb(String urlUpload);

    void sendEventTrackingOrderDetail(Map<String, Object> eventTracking);

    void sendEventTracking(String event, String category, String action, String label);

}
