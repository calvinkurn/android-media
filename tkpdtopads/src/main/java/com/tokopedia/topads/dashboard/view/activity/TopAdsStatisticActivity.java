package com.tokopedia.topads.dashboard.view.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.base.view.fragment.BaseDatePickerFragment;
import com.tokopedia.topads.R;
import com.tokopedia.seller.common.datepicker.view.constant.DatePickerConstant;
import com.tokopedia.topads.common.view.presenter.BaseDatePickerPresenter;
import com.tokopedia.topads.common.view.presenter.BaseDatePickerPresenterImpl;
import com.tokopedia.topads.dashboard.constant.TopAdsConstant;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.data.Cell;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.TopAdsManagementService;
import com.tokopedia.topads.dashboard.data.source.local.TopAdsCacheDataSourceImpl;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsProductAdInteractorImpl;
import com.tokopedia.topads.dashboard.view.adapter.TopAdsStatisticPagerAdapter;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsStatisticAvgFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsStatisticConversionFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsStatisticCtrFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsStatisticFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsStatisticImprFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsStatisticKlikFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsStatisticSpentFragment;
import com.tokopedia.topads.dashboard.view.listener.TopAdsStatisticActivityViewListener;
import com.tokopedia.topads.dashboard.view.listener.TopAdsStatisticViewListener;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsStatisticActivityPresenter;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsStatisticActivityPresenterImpl;

import java.util.ArrayList;
import java.util.List;

public abstract class TopAdsStatisticActivity extends TopAdsDatePickerActivity<TopAdsStatisticActivityPresenter> implements TopAdsStatisticActivityViewListener {

    public static final int OFFSCREEN_PAGE_LIMIT = 12;
    ViewPager viewPager;
    TabLayout tabLayout;
    SwipeToRefresh swipeToRefresh;

    private List<Cell> cells;
    int currentPositonPager;
    ProgressDialog progressDialog;
    SnackbarRetry snackbarRetry;
    private FragmentPagerAdapter viewPagerAdapter;

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }

    @Override
    protected BaseDatePickerPresenter getDatePickerPresenter() {
        BaseDatePickerPresenterImpl baseDatePickerPresenter = new BaseDatePickerPresenterImpl(this);
        return baseDatePickerPresenter;
    }

    @Override
    protected void setupBundlePass(Bundle bundle) {
        currentPositonPager = bundle.getInt(TopAdsExtraConstant.EXTRA_STATISTIC_POSITION_KEY);
    }

    @Override
    protected void initialPresenter() {
        super.initialPresenter();
        presenter = new TopAdsStatisticActivityPresenterImpl(this, new TopAdsProductAdInteractorImpl
                (new TopAdsManagementService(new SessionHandler(this).getAccessToken(this)), new TopAdsCacheDataSourceImpl(this)), this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_top_ads_statistic;
    }

    @Override
    protected void initView() {
        super.initView();
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tab);
        swipeToRefresh = (SwipeToRefresh) findViewById(R.id.swipe_refresh_layout);
        viewPagerAdapter = getViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(OFFSCREEN_PAGE_LIMIT);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(currentPositonPager);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.title_loading));
        snackbarRetry = getSnackbarWithAction();
    }

    @Override
    protected void setViewListener() {
        super.setViewListener();
        viewPager.setOffscreenPageLimit(TopAdsConstant.OFFSCREEN_PAGE_LIMIT);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                trackingStatisticBar(position);
                Fragment fragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
                if (fragment != null && fragment instanceof TopAdsStatisticViewListener && cells != null) {
                    ((TopAdsStatisticViewListener) fragment).updateDataCell(cells);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.addOnTabSelectedListener(new GlobalMainTabSelectedListener(viewPager));
        RefreshHandler refresh = new RefreshHandler(this, getWindow().getDecorView().getRootView(), new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                swipeToRefresh.setRefreshing(true);
                loadData();
            }
        });
    }

    public View getDateLabelView(){
        Fragment fragment = viewPagerAdapter.getItem(viewPager.getCurrentItem());
        if(fragment != null && fragment.isVisible() && fragment instanceof TopAdsStatisticFragment){
            return ((TopAdsStatisticFragment) fragment).getDateLabelView();
        }else{
            return null;
        }
    }

    protected void loadData() {
        presenter.getStatisticFromNet(startDate, endDate, getTypeStatistic(), SessionHandler.getShopID(this));
    }

    @Override
    public String getScreenName() {
        return null;
    }

    public FragmentPagerAdapter getViewPagerAdapter() {
        return new TopAdsStatisticPagerAdapter(getSupportFragmentManager(), getFragmentList());
    }

    public List<Fragment> getFragmentList() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(TopAdsStatisticImprFragment.createInstance());
        fragmentList.add(TopAdsStatisticKlikFragment.createInstance());
        fragmentList.add(TopAdsStatisticCtrFragment.createInstance());
        fragmentList.add(TopAdsStatisticConversionFragment.createInstance());
        fragmentList.add(TopAdsStatisticAvgFragment.createInstance());
        fragmentList.add(TopAdsStatisticSpentFragment.createInstance());
        return fragmentList;
    }

    @Override
    public void onError(Throwable throwable) {
        snackbarRetry = getSnackbarWithAction();
        snackbarRetry.showRetrySnackbar();
    }

    @NonNull
    private SnackbarRetry getSnackbarWithAction() {
        SnackbarRetry snackBar = NetworkErrorHelper.createSnackbarWithAction(this, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                loadData();
            }
        });
        snackBar.setColorActionRetry(ContextCompat.getColor(this, R.color.green_400));
        return snackBar;
    }

    @Override
    public void updateDataCell(List<Cell> cells) {
        swipeToRefresh.setRefreshing(false);
        snackbarRetry.hideRetrySnackbar();
        this.cells = cells;
        Fragment fragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
        if (fragment != null && fragment instanceof TopAdsStatisticViewListener) {
            ((TopAdsStatisticViewListener) fragment).updateDataCell(cells);
        }
    }

    @Override
    public List<Cell> getDataCell() {
        return this.cells;
    }

    @Override
    public void showLoading() {
        progressDialog.show();
    }

    @Override
    public void dismissLoading() {
        progressDialog.dismiss();
        swipeToRefresh.setRefreshing(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top_ads_statistic, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.menu_date) {
            openDatePicker();
        }
        return super.onOptionsItemSelected(item);
    }

    protected abstract int getTypeStatistic();

    private void trackingStatisticBar(int position) {
        switch (position) {
            case 0:
                onImpressionSelected();
                break;
            case 1:
                onClickSelected();
                break;
            case 2:
                onCtrSelected();
                break;
            case 3:
                onConversionSelected();
                break;
            case 4:
                onAverageConversionSelected();
                break;
            case 5:
                onCostSelected();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDatePickerChoose(int selectionDatePickerType, int selectionDatePeriodIndex) {
        super.onDatePickerChoose(selectionDatePickerType, selectionDatePeriodIndex);
        trackingDateTopAds(selectionDatePeriodIndex, selectionDatePickerType);
    }

    private void trackingDateTopAds(int lastSelection, int selectionType) {
        if(selectionType == DatePickerConstant.SELECTION_TYPE_CUSTOM_DATE){
            UnifyTracking.eventTopAdsProductStatistikDateCustom();
        }else if(selectionType == DatePickerConstant.SELECTION_TYPE_PERIOD_DATE) {
            switch (lastSelection){
                case 0:
                    UnifyTracking.eventTopAdsProductStatistikDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_TODAY);
                    break;
                case 1:
                    UnifyTracking.eventTopAdsProductStatistikDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_YESTERDAY);
                    break;
                case 2:
                    UnifyTracking.eventTopAdsProductStatistikDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_LAST_7_DAY);
                    break;
                case 3:
                    UnifyTracking.eventTopAdsProductStatistikDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_LAST_1_MONTH);
                    break;
                case 4:
                    UnifyTracking.eventTopAdsProductStatistikDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_THIS_MONTH);
                    break;
                default:
                    break;
            }
        }
    }

    protected abstract void onCostSelected();

    protected abstract void onAverageConversionSelected();

    protected abstract void onConversionSelected();

    protected abstract void onCtrSelected();

    protected abstract void onClickSelected();

    protected abstract void onImpressionSelected();
}