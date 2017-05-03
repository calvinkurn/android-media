package com.tokopedia.seller.topads.view.activity;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.view.presenter.TopAdsDatePickerPresenterImpl;
import com.tokopedia.seller.topads.view.adapter.TopAdsDashboardPagerAdapter;
import com.tokopedia.seller.topads.view.fragment.TopAdsDashboardFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsDashboardProductFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsDashboardShopFragment;
import com.tokopedia.seller.topads.view.listener.TopAdsDashboardTabListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 11/22/2016.
 */

public class TopAdsDashboardActivity extends DrawerPresenterActivity implements TopAdsDashboardFragment.Callback {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton fabSpeedDial;

    private SnackbarRetry snackbarRetry;
    private TopAdsDashboardShopFragment dashboardShopFragment;
    private TopAdsDashboardProductFragment dashboardProductFragment;
    private TopAdsDatePickerPresenterImpl datePickerPresenter;
    private TopAdsDashboardTabListener topadsDashList;

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {
        datePickerPresenter = new TopAdsDatePickerPresenterImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_top_ads_dashboard;
    }

    @Override
    protected void initView() {
        super.initView();
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.indicator);
        fabSpeedDial = (FloatingActionButton) findViewById(R.id.top_ads_dashboard_fab);
        datePickerPresenter.resetDate();
        viewPager.setAdapter(getViewPagerAdapter());
        viewPager.setOffscreenPageLimit(TopAdsConstant.OFFSCREEN_PAGE_LIMIT);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        topadsDashList = new TopAdsDashboardTabListener(viewPager);
        topadsDashList.setTopAdsDashboardList(new TopAdsDashboardTabListener.TopAdsDashboardList() {
            @Override
            public void onSelected(int positon) {
                switch (positon) {
                    case 0:
                        fabSpeedDial.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                    default:
                        fabSpeedDial.setVisibility(View.GONE);
                        break;
                }
            }
        });
        tabLayout.setOnTabSelectedListener(topadsDashList);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.title_top_ads_product));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.title_top_ads_store));
        snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(this, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                dashboardShopFragment.loadData();
                dashboardProductFragment.loadData();
            }
        });
        snackbarRetry.setColorActionRetry(ContextCompat.getColor(this, R.color.green_400));
        fabSpeedDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getCurrentFragment()!= null && getCurrentFragment() instanceof TopAdsDashboardProductFragment){
                    Intent intent = new Intent(TopAdsDashboardActivity.this, TopAdsGroupNewPromoActivity.class);
                    getCurrentFragment().startActivityForResult(intent, TopAdsDashboardProductFragment.REQUEST_CODE_AD_STATUS);
                }
            }
        });
    }

    Fragment getCurrentFragment() {
        return (Fragment) viewPager.getAdapter().instantiateItem(viewPager,viewPager.getCurrentItem());
    }

    @Override
    protected int setDrawerPosition() {
        return TkpdState.DrawerPosition.SELLER_TOP_ADS;
    }

    public PagerAdapter getViewPagerAdapter() {
        dashboardProductFragment = TopAdsDashboardProductFragment.createInstance();
        dashboardProductFragment.setCallback(this);
        dashboardShopFragment = TopAdsDashboardShopFragment.createInstance();
        dashboardShopFragment.setCallback(this);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(dashboardProductFragment);
        fragmentList.add(dashboardShopFragment);
        return new TopAdsDashboardPagerAdapter(getFragmentManager(), fragmentList);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {
        actionSendAnalyticsIfFromPushNotif();
    }

    private void actionSendAnalyticsIfFromPushNotif() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constants.EXTRA_FROM_PUSH)) {
            if (intent.getBooleanExtra(Constants.EXTRA_FROM_PUSH, false)) {
                UnifyTracking.eventOpenTopadsPushNotification(
                        getIntent().getStringExtra(UnifyTracking.EXTRA_LABEL)
                );
            }
        }
    }

    @Override
    public void onLoadDataError() {
        snackbarRetry.showRetrySnackbar();
    }

    @Override
    public void onLoadDataSuccess() {
        snackbarRetry.hideRetrySnackbar();
    }

    @Override
    public void onCreditAdded() {
        dashboardShopFragment.populateDeposit();
        dashboardProductFragment.populateDeposit();
    }
}