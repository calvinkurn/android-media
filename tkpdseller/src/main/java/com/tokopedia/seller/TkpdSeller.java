package com.tokopedia.seller;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.seller.selling.view.activity.ActivitySellingTransaction;
import com.tokopedia.seller.selling.view.fragment.FragmentSellingNewOrder;
import com.tokopedia.seller.shopsettings.FragmentSettingShop;
import com.tokopedia.seller.shopsettings.ManageShopActivity;


public class TkpdSeller {

    public static Intent getIntentManageShop(Context context) {
        return new Intent(context, ManageShopActivity.class);
    }

    public static android.app.Fragment getFragmentShopSettings() {
        return FragmentSettingShop.newInstance();
    }

    public static android.app.Fragment getFragmentSellingNewOrder() {
        return new FragmentSellingNewOrder();
    }

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