package com.tokopedia.loyalty.view.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.R2;

import butterknife.BindView;

/**
 * @author anggaprasetiyo on 27/11/17.
 */

public class LoyaltyActivity extends BasePresenterActivity implements HasComponent<AppComponent> {
    @BindView(R2.id.pager)
    ViewPager viewPager;
    @BindView(R2.id.indicator)
    TabLayout indicator;

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
        return R.layout.activity_loyalty;
    }

    @Override
    protected void initView() {

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
