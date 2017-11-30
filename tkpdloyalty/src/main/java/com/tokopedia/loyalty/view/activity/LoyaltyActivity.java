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
import com.tokopedia.loyalty.di.component.DaggerLoyaltyViewComponent;
import com.tokopedia.loyalty.di.component.LoyaltyViewComponent;
import com.tokopedia.loyalty.di.module.LoyaltyViewModule;
import com.tokopedia.loyalty.view.adapter.LoyaltyPagerAdapter;
import com.tokopedia.loyalty.view.data.LoyaltyPagerItem;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;

/**
 * @author anggaprasetiyo on 27/11/17.
 */

public class LoyaltyActivity extends BasePresenterActivity implements HasComponent<AppComponent> {
    @BindView(R2.id.pager)
    ViewPager viewPager;
    @BindView(R2.id.indicator)
    TabLayout indicator;

    @Inject
    LoyaltyPagerAdapter loyaltyPagerAdapter;

    @Inject
    @Named("coupon_active")
    List<LoyaltyPagerItem> loyaltyPagerItemListCouponActive;

    @Inject
    @Named("coupon_not_active")
    List<LoyaltyPagerItem> loyaltyPagerItemListCouponNotActive;

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {
        LoyaltyViewComponent loyaltyViewComponent = DaggerLoyaltyViewComponent.builder()
                .loyaltyViewModule(new LoyaltyViewModule(this))
                .build();
        loyaltyViewComponent.inject(this);
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
