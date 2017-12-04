package com.tokopedia.loyalty.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
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
    public static final String EXTRA_COUPON_ACTIVE = "EXTRA_COUPON_ACTIVE";

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

    private boolean isCouponActive;

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        this.isCouponActive = true;
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
        if (isCouponActive) renderViewWithCouponTab();
        else renderViewSingleTabPromoCode();
    }

    private void renderViewSingleTabPromoCode() {
        for (LoyaltyPagerItem loyaltyPagerItem : loyaltyPagerItemListCouponNotActive)
            indicator.addTab(indicator.newTab().setText(loyaltyPagerItem.getTabTitle()));
        viewPager.setOffscreenPageLimit(loyaltyPagerItemListCouponNotActive.size());
        loyaltyPagerAdapter.addAllItem(loyaltyPagerItemListCouponNotActive);
        viewPager.setAdapter(loyaltyPagerAdapter);
        viewPager.addOnPageChangeListener(new OnTabPageChangeListener(indicator));
        indicator.setOnTabSelectedListener(new GlobalMainTabSelectedListener(viewPager));
        indicator.setVisibility(View.GONE);
    }

    private void renderViewWithCouponTab() {
        for (LoyaltyPagerItem loyaltyPagerItem : loyaltyPagerItemListCouponActive)
            indicator.addTab(indicator.newTab().setText(loyaltyPagerItem.getTabTitle()));
        viewPager.setOffscreenPageLimit(loyaltyPagerItemListCouponActive.size());
        loyaltyPagerAdapter.addAllItem(loyaltyPagerItemListCouponActive);
        viewPager.setAdapter(loyaltyPagerAdapter);
        viewPager.addOnPageChangeListener(new OnTabPageChangeListener(indicator));
        indicator.setOnTabSelectedListener(new GlobalMainTabSelectedListener(viewPager));
        indicator.setVisibility(View.VISIBLE);
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

    private class OnTabPageChangeListener extends TabLayout.TabLayoutOnPageChangeListener {

        OnTabPageChangeListener(TabLayout tabLayout) {
            super(tabLayout);
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            hideKeyboard();
        }

        private void hideKeyboard() {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
        }
    }

    public static Intent newInstanceCouponActive(Context context) {
        Intent intent = new Intent(context, LoyaltyActivity.class);
        intent.putExtra(EXTRA_COUPON_ACTIVE, true);
        return intent;
    }

    public static Intent newInstanceCouponNotActive(Context context) {
        Intent intent = new Intent(context, LoyaltyActivity.class);
        intent.putExtra(EXTRA_COUPON_ACTIVE, false);
        return intent;
    }
}
