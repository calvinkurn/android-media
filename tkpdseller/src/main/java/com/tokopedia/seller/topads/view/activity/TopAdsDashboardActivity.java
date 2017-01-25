package com.tokopedia.seller.topads.view.activity;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;

import com.tkpd.library.ui.floatbutton.FabSpeedDial;
import com.tkpd.library.ui.floatbutton.ListenerFabClick;
import com.tkpd.library.ui.floatbutton.SimpleMenuListenerAdapter;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.presenter.TopAdsDatePickerPresenterImpl;
import com.tokopedia.seller.topads.view.adapter.TopAdsDashboardPagerAdapter;
import com.tokopedia.seller.topads.view.fragment.TopAdsDashboardFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsDashboardProductFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsDashboardShopFragment;
import com.tokopedia.seller.topads.view.listener.TopAdsDashboardTabListener;

import java.util.ArrayList;
import java.util.List;

import static com.tokopedia.seller.topads.view.fragment.TopAdsDashboardShopFragment.showCreateAdsAlert;

/**
 * Created by Nathaniel on 11/22/2016.
 */

public class TopAdsDashboardActivity extends DrawerPresenterActivity implements TopAdsDashboardFragment.Callback {

    ViewPager viewPager;
    TabLayout tabLayout;
    FloatingActionButton fabSpeedDial;

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
        drawer.setDrawerPosition(TkpdState.DrawerPosition.SELLER_TOP_ADS);
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
                switch (positon){
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
        fabSpeedDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateAdsAlert(TopAdsDashboardActivity.this);
            }
        });
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