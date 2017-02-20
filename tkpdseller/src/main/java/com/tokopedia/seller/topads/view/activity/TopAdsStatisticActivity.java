package com.tokopedia.seller.topads.view.activity;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.datasource.TopAdsCacheDataSourceImpl;
import com.tokopedia.seller.topads.datasource.TopAdsDbDataSourceImpl;
import com.tokopedia.seller.topads.domain.interactor.TopAdsProductAdInteractorImpl;
import com.tokopedia.seller.topads.model.data.Cell;
import com.tokopedia.seller.topads.network.apiservice.TopAdsManagementService;
import com.tokopedia.seller.topads.view.presenter.TopAdsDatePickerPresenter;
import com.tokopedia.seller.topads.view.presenter.TopAdsDatePickerPresenterImpl;
import com.tokopedia.seller.topads.view.presenter.TopAdsStatisticActivityPresenter;
import com.tokopedia.seller.topads.view.presenter.TopAdsStatisticActivityPresenterImpl;
import com.tokopedia.seller.topads.view.adapter.TopAdsStatisticPagerAdapter;
import com.tokopedia.seller.topads.view.fragment.TopAdsStatisticAvgFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsStatisticConversionFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsStatisticCtrFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsStatisticImprFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsStatisticKlikFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsStatisticSpentFragment;
import com.tokopedia.seller.topads.view.listener.TopAdsStatisticActivityViewListener;
import com.tokopedia.seller.topads.view.listener.TopAdsStatisticViewListener;

import java.util.ArrayList;
import java.util.List;

public abstract class TopAdsStatisticActivity extends TopAdsDatePickerActivity<TopAdsStatisticActivityPresenter> implements TopAdsStatisticActivityViewListener {

    ViewPager viewPager;
    TabLayout tabLayout;
    SwipeToRefresh swipeToRefresh;

    private List<Cell> cells;
    int currentPositonPager;
    ProgressDialog progressDialog;
    SnackbarRetry snackbarRetry;

    @Override
    protected TopAdsDatePickerPresenter getDatePickerPresenter() {
        return new TopAdsDatePickerPresenterImpl(this);
    }

    @Override
    protected void setupBundlePass(Bundle bundle) {
        currentPositonPager = bundle.getInt(TopAdsExtraConstant.EXTRA_STATISTIC_POSITION_KEY);
    }

    @Override
    protected void initialPresenter() {
        super.initialPresenter();
        presenter = new TopAdsStatisticActivityPresenterImpl(this, new TopAdsProductAdInteractorImpl
                (new TopAdsManagementService(), new TopAdsDbDataSourceImpl(), new TopAdsCacheDataSourceImpl(this)), this);
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
        viewPager.setAdapter(getViewPagerAdapter());
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
                Fragment fragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
                if (fragment != null && fragment instanceof TopAdsStatisticViewListener && cells != null) {
                    ((TopAdsStatisticViewListener) fragment).updateDataCell(cells);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setOnTabSelectedListener(new GlobalMainTabSelectedListener(viewPager));
        RefreshHandler refresh = new RefreshHandler(this, getWindow().getDecorView().getRootView(), new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                swipeToRefresh.setRefreshing(true);
                loadData();
            }
        });
    }

    protected void loadData() {
        presenter.getStatisticFromNet(startDate, endDate, getTypeStatistic(), SessionHandler.getShopID(this));
        getSupportActionBar().setSubtitle(datePickerPresenter.getRangeDateFormat(startDate, endDate));
    }

    @Override
    public String getScreenName() {
        return null;
    }

    public PagerAdapter getViewPagerAdapter() {
        return new TopAdsStatisticPagerAdapter(getFragmentManager(), getFragmentList());
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
        getMenuInflater().inflate(R.menu.promo_topads_detail, menu);
        return true;
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
}