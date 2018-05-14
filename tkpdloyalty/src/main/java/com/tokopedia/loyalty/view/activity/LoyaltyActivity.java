package com.tokopedia.loyalty.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.tokopedia.abstraction.constant.IRouterConstant;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.R2;
import com.tokopedia.loyalty.di.component.DaggerLoyaltyViewComponent;
import com.tokopedia.loyalty.di.component.LoyaltyViewComponent;
import com.tokopedia.loyalty.di.module.LoyaltyViewModule;
import com.tokopedia.loyalty.listener.LoyaltyActivityTabSelectedListener;
import com.tokopedia.loyalty.view.adapter.LoyaltyPagerAdapter;
import com.tokopedia.loyalty.view.data.LoyaltyPagerItem;
import com.tokopedia.loyalty.view.fragment.PromoCodeFragment;
import com.tokopedia.loyalty.view.fragment.PromoCouponFragment;
import com.tokopedia.showcase.ShowCaseBuilder;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;

import java.util.ArrayList;
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
        PromoCouponFragment.ChooseCouponListener {

    public static final String DEFAULT_COUPON_TAB_SELECTED = "coupon";
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
        this.isCouponActive = extras.getBoolean(
                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_COUPON_ACTIVE
        );
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
        indicator.setVisibility(View.GONE);
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
        indicator.setOnTabSelectedListener(new LoyaltyActivityTabSelectedListener(viewPager));
        setShowCase();
        if (getIntent().hasExtra(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_SELECTED_TAB)) {
            viewPager.setCurrentItem(getIntent().getIntExtra(
                    IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_SELECTED_TAB,
                    IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_TAB));
        }
    }

    private void setShowCase() {
        ShowCaseObject showCase = new ShowCaseObject(
                ((ViewGroup) indicator.getChildAt(0)).getChildAt(1),
                getString(R.string.show_case_title),
                getString(R.string.show_case_text),
                ShowCaseContentPosition.UNDEFINED);

        ArrayList<ShowCaseObject> showCaseObjectList = new ArrayList<>();

        showCaseObjectList.add(showCase);

        ShowCaseDialog showCaseDialog = createShowCaseDialog();

        showCaseDialog.setShowCaseStepListener(new ShowCaseDialog.OnShowCaseStepListener() {
            @Override
            public boolean onShowCaseGoTo(int previousStep, int nextStep, ShowCaseObject showCaseObject) {
                return false;
            }
        });
        if (!ShowCasePreference.hasShown(this, LoyaltyActivity.class.getName()))
            showCaseDialog.show(this, LoyaltyActivity.class.getName(), showCaseObjectList);

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
        bundle.putString(
                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_CODE, voucherCode
        );
        bundle.putString(
                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_MESSAGE, voucherMessage
        );
        bundle.putString(
                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_AMOUNT, voucherAmount
        );
        intent.putExtras(bundle);
        setResult(IRouterConstant.LoyaltyModule.ResultLoyaltyActivity.VOUCHER_RESULT_CODE, intent);
        finish();
    }

    @Override
    public void onDigitalCodeSuccess(String voucherCode,
                                     String voucherMessage,
                                     long discountAmount,
                                     long cashBackAmount) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_CODE, voucherCode);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_MESSAGE, voucherMessage);
        bundle.putLong(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_DISCOUNT_AMOUNT, discountAmount);
        bundle.putLong(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_CASHBACK_AMOUNT, cashBackAmount);
        intent.putExtras(bundle);
        setResult(IRouterConstant.LoyaltyModule.ResultLoyaltyActivity.VOUCHER_RESULT_CODE, intent);
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
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_CODE, promoCode);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_MESSAGE, promoMessage);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_AMOUNT, amount);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_TITLE, couponTitle);
        intent.putExtras(bundle);
        setResult(IRouterConstant.LoyaltyModule.ResultLoyaltyActivity.COUPON_RESULT_CODE, intent);
        finish();
    }

    @Override
    public void onDigitalCouponSuccess(String promoCode,
                                       String promoMessage,
                                       String couponTitle,
                                       long discountAmount,
                                       long cashbackAmount) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_CODE, promoCode);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_MESSAGE, promoMessage);
        bundle.putLong(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_DISCOUNT_AMOUNT, discountAmount);
        bundle.putLong(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_CASHBACK_AMOUNT, cashbackAmount);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_TITLE, couponTitle);
        intent.putExtras(bundle);
        setResult(IRouterConstant.LoyaltyModule.ResultLoyaltyActivity.COUPON_RESULT_CODE, intent);
        finish();
    }

    public static Intent newInstanceCouponActive(Activity activity, String platform, String categoryId, String cartId) {
        Intent intent = new Intent(activity, LoyaltyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_COUPON_ACTIVE, true);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_PLATFORM, platform);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_CATEGORY, categoryId);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_CART_ID, cartId);
        intent.putExtras(bundle);
        return intent;
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

    @Deprecated
    public static Intent newInstanceCouponActiveAndSelected(Context context, String platform, String categoryId) {
        Intent intent = new Intent(context, LoyaltyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_COUPON_ACTIVE, true);
        bundle.putInt(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_SELECTED_TAB,
                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_TAB);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_PLATFORM, platform);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_CATEGORY, categoryId);
        intent.putExtras(bundle);
        return intent;
    }

    @Deprecated
    public static Intent newInstanceCouponActive(Context context, String platform, String category) {
        Intent intent = new Intent(context, LoyaltyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_COUPON_ACTIVE, true);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_PLATFORM, platform);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_CATEGORY, category);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newInstanceCouponActive(Context context, String platform, String category, String defaultSelectedTab) {
        Intent intent = new Intent(context, LoyaltyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_COUPON_ACTIVE, true);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_PLATFORM, platform);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_CATEGORY, category);
        if (!TextUtils.isEmpty(defaultSelectedTab) && DEFAULT_COUPON_TAB_SELECTED.equalsIgnoreCase(defaultSelectedTab)) {
            bundle.putInt(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_SELECTED_TAB,
                    IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_TAB);
        }
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newInstanceCouponNotActive(Context context, String platform, String category) {
        Intent intent = new Intent(context, LoyaltyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_COUPON_ACTIVE, false);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_PLATFORM, platform);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_CATEGORY, category);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newInstanceNewCheckoutCartListCouponNotActive(Context context) {
        Intent intent = new Intent(context, LoyaltyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_COUPON_ACTIVE, false);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_PLATFORM,
                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.MARKETPLACE_CART_LIST_STRING);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newInstanceNewCheckoutCartListCouponActive(Context context) {
        Intent intent = new Intent(context, LoyaltyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_COUPON_ACTIVE, true);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_PLATFORM,
                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.MARKETPLACE_CART_LIST_STRING);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newInstanceNewCheckoutCartShipmentCouponNotActive(
            Context context, String cartShipmentDataString
    ) {
        Intent intent = new Intent(context, LoyaltyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_COUPON_ACTIVE, false);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_PLATFORM,
                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.MARKETPLACE_CART_SHIPMENT_STRING);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_ADDITIONAL_DATA,
                cartShipmentDataString);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newInstanceNewCheckoutCartShipmentCouponActive(
            Context context, String cartShipmentDataString
    ) {
        Intent intent = new Intent(context, LoyaltyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_COUPON_ACTIVE, true);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_PLATFORM,
                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.MARKETPLACE_CART_SHIPMENT_STRING);
        bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_ADDITIONAL_DATA,
                cartShipmentDataString);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    private ShowCaseDialog createShowCaseDialog() {
        return new ShowCaseBuilder()
                .customView(R.layout.show_case_hachiko)
                .titleTextColorRes(R.color.white)
                .spacingRes(R.dimen.spacing_show_case)
                .arrowWidth(R.dimen.arrow_width_show_case)
                .textColorRes(R.color.grey_400)
                .shadowColorRes(R.color.shadow)
                .backgroundContentColorRes(R.color.black)
                .circleIndicatorBackgroundDrawableRes(R.drawable.selector_circle_green)
                .textSizeRes(R.dimen.fontvs)
                .finishStringRes(R.string.show_case_finish)
                .useCircleIndicator(true)
                .clickable(true)
                .useArrow(true)
                .build();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UnifyTracking.eventCouponPageClosed();
    }
}
