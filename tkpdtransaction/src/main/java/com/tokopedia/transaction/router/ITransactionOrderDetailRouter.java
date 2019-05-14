package com.tokopedia.transaction.router;

import android.content.Intent;

/**
 * @author anggaprasetiyo on 15/05/18.
 */
public interface ITransactionOrderDetailRouter {

    Intent transactionOrderDetailRouterGetIntentUploadAwb(String urlUpload);

    boolean getBooleanRemoteConfig(String key, boolean defaultValue);

    boolean isToggleBuyAgainOn();
}
