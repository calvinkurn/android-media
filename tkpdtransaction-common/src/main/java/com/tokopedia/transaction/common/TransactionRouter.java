package com.tokopedia.transaction.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by kris on 7/21/17. Tokopedia
 */

public interface TransactionRouter {

    void goToUserPaymentList(Activity activity);

    Intent goToOrderDetail(Context context, String orderId);

    Intent getInboxReputationIntent(Context context);

    Intent getDetailResChatIntentBuyer(Context context, String resoId, String shopName);

    Intent getShopPageIntent(Context context, String shopId);

    boolean getEnableFingerprintPayment();

    interface CartNotificationListener {
        void onDataReady();
    }
}
