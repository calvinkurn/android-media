package com.tokopedia.seller.shop.open.view.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.shop.open.view.presenter.ShopOpenMandatoryPresenter;
import com.tokopedia.seller.shop.setting.view.fragment.ShopSettingLocationFragment;

/**
 * Created by Nathaniel on 3/16/2017.
 */

public class ShopOpenMandatoryActivity extends BasePresenterActivity<ShopOpenMandatoryPresenter> {
    private FrameLayout container;
    private FragmentManager fragmentManager;

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_simple_fragment;
    }

    @Override
    protected void initView() {
        container = (FrameLayout) findViewById(R.id.container);
    }

    @Override
    protected void setViewListener() {

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
