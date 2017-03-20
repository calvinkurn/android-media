package com.tokopedia.seller.shop.open.view.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.tokopedia.seller.R;
import com.tokopedia.seller.app.BaseDiActivity;
import com.tokopedia.seller.shop.open.view.presenter.ShopOpenMandatoryPresenter;
import com.tokopedia.seller.shop.setting.di.component.DaggerShopSettingComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopSettingComponent;
import com.tokopedia.seller.shop.setting.di.module.ShopSettingModule;
import com.tokopedia.seller.shop.setting.view.fragment.ShopSettingLocationFragment;

/**
 * Created by Nathaniel on 3/16/2017.
 */

public class ShopOpenMandatoryActivity extends BaseDiActivity<ShopOpenMandatoryPresenter, ShopSettingComponent> {
    private FrameLayout container;
    private FragmentManager fragmentManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_simple_fragment;
    }

    @Override
    protected void initView() {
        container = (FrameLayout) findViewById(R.id.container);
    }

    @Override
    protected ShopSettingComponent initComponent() {
        return DaggerShopSettingComponent
                .builder()
                .shopSettingModule(new ShopSettingModule(this))
                .appComponent(getApplicationComponent())
                .build();
    }

    @Override
    protected ShopOpenMandatoryPresenter getPresenter() {
        return null;
    }

    @Override
    protected void initVar() {
        fragmentManager = getFragmentManager();
    }

    @Override
    protected void setActionVar() {
        Fragment fragment = fragmentManager.findFragmentByTag(ShopSettingLocationFragment.TAG);
        if (fragment == null) {
            fragment = ShopSettingLocationFragment.getInstance();
        }
        inflateFragment(fragment, false, ShopSettingLocationFragment.TAG);
    }

    private void inflateFragment(Fragment fragment, boolean isAddToBackStack, String tag) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.container, fragment, tag);
        if (isAddToBackStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

}
