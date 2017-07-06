package com.tokopedia.seller.shopsettings;

import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TkpdActivity;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.R;

/**
 * Use in reflection by SellerRouter
 * If you want to rename the class or refactor its package, rename also the route at SellerRouter
 */
public class ManageShopActivity extends TkpdActivity {

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_SETTING_MANAGE_SHOP;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_manage_shop);
    }

    @Override
    public int getDrawerPosition() {
        return TkpdState.DrawerPosition.MANAGE_SHOP;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode == RESULT_OK)
            super.RefreshDrawer();
        super.onActivityResult(requestCode, resultCode, data);
    }


}
