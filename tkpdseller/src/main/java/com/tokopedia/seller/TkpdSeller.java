package com.tokopedia.seller;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.seller.selling.view.activity.ActivitySellingTransaction;


public class TkpdSeller {

    public static Class getSellingActivityClass() {
        return ActivitySellingTransaction.class;
    }

    public static Intent getActivitySellingTransactionNewOrder(Context context) {
        return RouteManager.getIntent(context, ApplinkConst.SELLER_NEW_ORDER);
    }

    public static Intent getActivitySellingTransactionConfirmShipping(Context context) {
        return RouteManager.getIntent(context, ApplinkConst.SELLER_SHIPMENT);
    }

    public static Intent getActivitySellingTransactionShippingStatus(Context context) {
        return RouteManager.getIntent(context, ApplinkConst.SELLER_STATUS);
    }

    public static Intent getActivitySellingTransactionList(Context context) {
        return RouteManager.getIntent(context, ApplinkConst.SELLER_HISTORY);
    }

    public static Intent getActivitySellingTransactionOpportunity(Context context, String query) {
        return RouteManager.getIntent(context, ApplinkConst.SELLER_OPPORTUNITY);
    }
}