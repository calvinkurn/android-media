package com.tokopedia.loyalty.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
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
import com.tokopedia.loyalty.view.fragment.PromoCodeFragment;
import com.tokopedia.loyalty.view.fragment.PromoCouponFragment;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;

/**
 * @author anggaprasetiyo on 27/11/17.
 */

public class LoyaltyActivity extends BasePresenterActivity
        implements HasComponent<AppComponent>,
        PromoCodeFragment.ManualInsertCodeListener,
        PromoCouponFragment.ChooseCouponListener
{
    public static final String EXTRA_COUPON_ACTIVE = "EXTRA_COUPON_ACTIVE";
    public static final String EXTRA_PLATFORM = "EXTRA_PLATFORM";
    public static final String EXTRA_CATEGORY = "EXTRA_CATEGORY";
    public static final String MARKETPLACE_STRING = "marketplace";
    public static final String DIGITAL_STRING = "digital";
    public static final String VOUCHER_CODE = "voucher_code";
    public static final String VOUCHER_MESSAGE = "voucher_message";
    public static final String VOUCHER_AMOUNT = "voucher_amount";
    public static final int LOYALTY_REQUEST_CODE = 77;
    public static final int VOUCHER_RESULT_CODE = 12;
    public static final int COUPON_RESULT_CODE = 15;
    public static final String COUPON_CODE = "coupon_code";
    public static final String COUPON_MESSAGE = "coupon_message";
    public static final String COUPON_AMOUNT = "coupon_amount";
    public static final String COUPON_TITLE = "coupon_title";

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
        this.isCouponActive = extras.getBoolean(EXTRA_COUPON_ACTIVE);
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
        setTabProperties();
        viewPager.setOffscreenPageLimit(loyaltyPagerItemListCouponNotActive.size());
        loyaltyPagerAdapter.addAllItem(loyaltyPagerItemListCouponNotActive);
        viewPager.setAdapter(loyaltyPagerAdapter);
        viewPager.addOnPageChangeListener(new OnTabPageChangeListener(indicator));
        indicator.setOnTabSelectedListener(new GlobalMainTabSelectedListener(viewPager));
    }

    private void renderViewWithCouponTab() {
        for (LoyaltyPagerItem loyaltyPagerItem : loyaltyPagerItemListCouponActive)
            indicator.addTab(indicator.newTab().setText(loyaltyPagerItem.getTabTitle()));
        setTabProperties();
        indicator.setTabMode(TabLayout.MODE_FIXED);
        viewPager.setOffscreenPageLimit(loyaltyPagerItemListCouponActive.size());
        loyaltyPagerAdapter.addAllItem(loyaltyPagerItemListCouponActive);
        viewPager.setAdapter(loyaltyPagerAdapter);
        viewPager.addOnPageChangeListener(new OnTabPageChangeListener(indicator));
        indicator.setOnTabSelectedListener(new GlobalMainTabSelectedListener(viewPager));
    }

    private void setTabProperties() {
        indicator.setTabMode(TabLayout.MODE_FIXED);
        indicator.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.tkpd_main_green));
        indicator.setTabTextColors(
                ContextCompat.getColor(this, R.color.black_38),
                ContextCompat.getColor(this, R.color.tkpd_main_green));
        indicator.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
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

    @Override
    public void onCodeSuccess(String voucherCode, String voucherMessage, String voucherAmount) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(VOUCHER_CODE, voucherCode);
        bundle.putString(VOUCHER_MESSAGE, voucherMessage);
        bundle.putString(VOUCHER_AMOUNT, voucherAmount);
        intent.putExtras(bundle);
        setResult(VOUCHER_RESULT_CODE, intent);
        finish();
    }

    @Override
    public void onCouponSuccess(
            String promoCode,
            String promoMessage,
            String amount,
            String couponTitle
    ) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(COUPON_CODE, promoCode);
        bundle.putString(COUPON_MESSAGE, promoMessage);
        bundle.putString(COUPON_AMOUNT, amount);
        bundle.putString(COUPON_TITLE, couponTitle);
        intent.putExtras(bundle);
        setResult(COUPON_RESULT_CODE, intent);
        finish();
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

    public static Intent newInstanceCouponActive(Context context, String platform, String category) {
        Intent intent = new Intent(context, LoyaltyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_COUPON_ACTIVE, true);
        bundle.putString(EXTRA_PLATFORM, platform);
        bundle.putString(EXTRA_CATEGORY, category);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newInstanceCouponNotActive(Context context, String platform, String category) {
        Intent intent = new Intent(context, LoyaltyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_COUPON_ACTIVE, true);
        bundle.putString(EXTRA_PLATFORM, platform);
        bundle.putString(EXTRA_CATEGORY, category);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
