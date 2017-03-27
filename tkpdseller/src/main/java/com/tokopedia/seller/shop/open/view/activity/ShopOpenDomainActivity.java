package com.tokopedia.seller.shop.open.view.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.R;
import com.tokopedia.seller.shop.open.view.fragment.ShopOpenDomainFragment;

/**
 * Created by Nathaniel on 3/16/2017.
 */

public class ShopOpenDomainActivity extends BasePresenterActivity implements HasComponent<AppComponent> {

    public static void start(Activity activity){
        Intent intent = new Intent(activity, ShopOpenDomainActivity.class);
        activity.startActivity(intent);
    }

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
        FragmentManager fm = getFragmentManager();
        if (fm.findFragmentByTag(ShopOpenDomainFragment.TAG) == null) {
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.add(
                    com.tokopedia.core.R.id.container,
                    ShopOpenDomainFragment.newInstance(), ShopOpenDomainFragment.TAG);
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }
}

