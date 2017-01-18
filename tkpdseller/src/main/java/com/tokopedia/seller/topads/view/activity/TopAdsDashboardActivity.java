package com.tokopedia.seller.topads.view.activity;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;

import com.tkpd.library.ui.floatbutton.FabSpeedDial;
import com.tkpd.library.ui.floatbutton.ListenerFabClick;
import com.tkpd.library.ui.floatbutton.SimpleMenuListenerAdapter;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.seller.R2;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.presenter.TopAdsDatePickerPresenterImpl;
import com.tokopedia.seller.topads.view.adapter.TopAdsDashboardPagerAdapter;
import com.tokopedia.seller.topads.view.fragment.TopAdsDashboardFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsDashboardProductFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsDashboardShopFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Nathaniel on 11/22/2016.
 */

public class TopAdsDashboardActivity extends BasePresenterActivity implements TopAdsDashboardFragment.Callback {

    @BindView(R2.id.pager)
    ViewPager viewPager;
    @BindView(R2.id.indicator)
    TabLayout indicator;
    @BindView(R2.id.fab_speed_dial)
    FabSpeedDial fabSpeedDial;

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
        datePickerPresenter.resetDate();
        viewPager.setAdapter(getViewPagerAdapter());
        viewPager.setOffscreenPageLimit(TopAdsConstant.OFFSCREEN_PAGE_LIMIT);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(indicator));
        indicator.setOnTabSelectedListener(new GlobalMainTabSelectedListener(viewPager));
        indicator.addTab(indicator.newTab().setText(R.string.title_top_ads_product));
        indicator.addTab(indicator.newTab().setText(R.string.title_top_ads_store));
        fabSpeedDial.setListenerFabClick(new ListenerFabClick() {
            @Override
            public void onFabClick() {
                if (!fabSpeedDial.isShown()) {
                    fabSpeedDial.setVisibility(View.VISIBLE);
                }
            }
        });
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_add_promo_group) {
                    Intent intent = new Intent(TopAdsDashboardActivity.this, TopAdsNewPromoActivity.class);
                    intent.putExtra(TopAdsExtraConstant.EXTRA_NEW_PROMO_CHOICE, TopAdsExtraConstant.TYPE_NEW_PROMO_GROUP);
                    startActivity(intent);
                } else if (menuItem.getItemId() == R.id.action_add_promo_product) {
                    Intent intent = new Intent(TopAdsDashboardActivity.this, TopAdsNewPromoActivity.class);
                    intent.putExtra(TopAdsExtraConstant.EXTRA_NEW_PROMO_CHOICE, TopAdsExtraConstant.TYPE_NEW_PROMO_EXIST_GROUP);
                    startActivity(intent);
                }
                return false;
            }
        });
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