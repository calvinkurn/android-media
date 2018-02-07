package com.tokopedia.seller;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.seller.selling.view.activity.ActivitySellingTransaction;
import com.tokopedia.seller.selling.view.fragment.FragmentSellingNewOrder;
import com.tokopedia.seller.shopsettings.edit.view.ShopEditorActivity;
import com.tokopedia.seller.shop.open.view.activity.ShopOpenRoutingActivity;
import com.tokopedia.seller.shopsettings.edit.presenter.ShopSettingView;
import com.tokopedia.seller.shopsettings.FragmentSettingShop;
import com.tokopedia.seller.shopsettings.ManageShopActivity;


public class TkpdSeller {
    public static Intent getIntentCreateEditShop(Context context, boolean isCreate, boolean logOutOnBack){
        Intent intent;  
        if (isCreate) {
            intent = ShopOpenRoutingActivity.getIntent(context);
        } else {
            intent = new Intent(context, ShopEditorActivity.class);
            intent.putExtra(ShopSettingView.FRAGMENT_TO_SHOW, ShopSettingView.EDIT_SHOP_FRAGMENT_TAG);
            if (logOutOnBack) {
                intent.putExtra(ShopSettingView.ON_BACK, ShopSettingView.LOG_OUT);
            } else {
                intent.putExtra(ShopSettingView.ON_BACK, ShopSettingView.FINISH);
            }
        }
        return intent;
    }

    public static Intent getIntentManageShop(Context context){
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
        return ActivitySellingTransaction.createIntent(context,
                ActivitySellingTransaction.TAB_POSITION_SELLING_NEW_ORDER);
    }

    public static Intent getActivitySellingTransactionConfirmShipping(Context context) {
        return ActivitySellingTransaction.createIntent(context,
                ActivitySellingTransaction.TAB_POSITION_SELLING_CONFIRM_SHIPPING);
    }

    public static Intent getActivitySellingTransactionShippingStatus(Context context) {
        return ActivitySellingTransaction.createIntent(context,
                ActivitySellingTransaction.TAB_POSITION_SELLING_SHIPPING_STATUS);
    }

    public static Intent getActivitySellingTransactionList(Context context) {
        return ActivitySellingTransaction.createIntent(context,
                ActivitySellingTransaction.TAB_POSITION_SELLING_TRANSACTION_LIST);
    }

    public static Intent getActivitySellingTransactionOpportunity(Context context) {
        return ActivitySellingTransaction.createIntent(context,
                ActivitySellingTransaction.TAB_POSITION_SELLING_OPPORTUNITY);
    }
}