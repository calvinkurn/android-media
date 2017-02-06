package com.tokopedia.seller.topads.view.activity;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.presenter.TopAdsDatePickerPresenterImpl;
import com.tokopedia.seller.topads.view.adapter.TopAdsDashboardPagerAdapter;
import com.tokopedia.seller.topads.view.fragment.TopAdsDashboardFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsDashboardProductFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsDashboardShopFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 11/22/2016.
 */

public class TopAdsDashboardActivity extends BasePresenterActivity implements TopAdsDashboardFragment.Callback {

    ViewPager viewPager;
    TabLayout tabLayout;

    private SnackbarRetry snackbarRetry;
    private TopAdsDashboardShopFragment dashboardShopFragment;
    private TopAdsDashboardProductFragment dashboardProductFragment;
    private TopAdsDatePickerPresenterImpl datePickerPresenter;

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
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.indicator);
        datePickerPresenter.resetDate();
        viewPager.setAdapter(getViewPagerAdapter());
        viewPager.setOffscreenPageLimit(TopAdsConstant.OFFSCREEN_PAGE_LIMIT);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new GlobalMainTabSelectedListener(viewPager));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.title_top_ads_product));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.title_top_ads_store));
        snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(this, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                dashboardShopFragment.loadData();
                dashboardProductFragment.loadData();
            }
        });
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

    }

    @Override
    public void onLoadDataError() {
        snackbarRetry.showRetrySnackbar();
    }

    @Override
    public void onLoadDataSuccess() {
        snackbarRetry.hideRetrySnackbar();
    }
}