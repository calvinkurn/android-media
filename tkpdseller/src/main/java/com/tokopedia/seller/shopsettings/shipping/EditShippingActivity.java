package com.tokopedia.seller.shopsettings.shipping;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.seller.R;
import com.tokopedia.seller.shopsettings.shipping.fragment.FragmentEditShipping;

@DeepLink({ApplinkConst.SELLER_SHIPPING_EDITOR})
public class EditShippingActivity extends BaseSimpleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLayout(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.main_view, FragmentEditShipping.createInstance()).commit();
        }
    }

    @Nullable
    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_shipping_shop_editor;
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_CONFIG_S_SHIPPING;
    }

    @Override
    protected int getToolbarResourceID() {
        return R.id.shipping_shop_editor_toolbar;
    }

    @Override
    protected void setupStatusBar() {
        if (GlobalConfig.isSellerApp()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                getWindow().setStatusBarColor(Color.WHITE);
            }
        }
    }
}
