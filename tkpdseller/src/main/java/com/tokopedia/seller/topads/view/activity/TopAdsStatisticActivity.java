package com.tokopedia.seller.topads.view.activity;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.datasource.TopAdsCacheDataSourceImpl;
import com.tokopedia.seller.topads.datasource.TopAdsDbDataSourceImpl;
import com.tokopedia.seller.topads.interactor.TopAdsProductAdInteractorImpl;
import com.tokopedia.seller.topads.lib.datepicker.SetDateActivity;
import com.tokopedia.seller.topads.lib.datepicker.SetDateFragment;
import com.tokopedia.seller.topads.model.data.Cell;
import com.tokopedia.seller.topads.network.apiservice.TopAdsManagementService;
import com.tokopedia.seller.topads.presenter.TopAdsStatisticActivityPresenter;
import com.tokopedia.seller.topads.presenter.TopAdsStatisticActivityPresenterImpl;
import com.tokopedia.seller.topads.view.adapter.TopAdsStatisticPagerAdapter;
import com.tokopedia.seller.topads.view.fragment.TopAdsDatePickerFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsStatisticAvgFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsStatisticConversionFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsStatisticCtrFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsStatisticKlikFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsStatisticImprFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsStatisticSpentFragment;
import com.tokopedia.seller.topads.view.listener.TopAdsStatisticActivityViewListener;
import com.tokopedia.seller.topads.view.listener.TopAdsStatisticViewListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

public abstract class TopAdsStatisticActivity extends BasePresenterActivity<TopAdsStatisticActivityPresenter> implements TopAdsStatisticActivityViewListener {
    private static final int REQUEST_CODE_DATE = 5;

    @BindView(R2.id.pager)
    ViewPager viewPager;
    @BindView(R2.id.tab)
    TabLayout tabLayout;

    private List<Cell> cells;
    int currentPositonPager;
    ProgressDialog progressDialog;
    private Date startDate;
    private Date endDate;
    SnackbarRetry snackbarRetry;

    @Override
    protected void setupURIPass(Uri uri) {

    }

    @Override
    protected void setupBundlePass(Bundle bundle) {
        currentPositonPager = bundle.getInt(TopAdsExtraConstant.EXTRA_STATISTIC_POSITION_KEY);
    }

    @Override
    protected void initialPresenter() {
        presenter = new TopAdsStatisticActivityPresenterImpl(this, new TopAdsProductAdInteractorImpl
                (new TopAdsManagementService(), new TopAdsDbDataSourceImpl(), new TopAdsCacheDataSourceImpl(this)), this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_top_ads_statistic;
    }

    @Override
    protected void initView() {
        viewPager.setAdapter(getViewPagerAdapter());
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(currentPositonPager);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.title_loading));
        snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(this, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                loadData();
            }
        });
    }

    @Override
    protected void setViewListener() {
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (presenter.isDateUpdated(startDate, endDate)) {
            startDate = presenter.getStartDate();
            endDate = presenter.getEndDate();
            loadData();
        }
    }

    private void loadData() {
        presenter.getStatisticFromNet(getTypeStatistic(), SessionHandler.getShopID(this));
        getSupportActionBar().setSubtitle(presenter.getRangeDateFormat(presenter.getStartDate(), presenter.getEndDate()));
    }

    @Override
    protected void initVar() {
    }

    @Override
    protected void setActionVar() {

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
        if(snackbarRetry.isShown()){
            snackbarRetry.hideRetrySnackbar();
        }
        snackbarRetry.showRetrySnackbar();
    }

    @Override
    public void updateDataCell(List<Cell> cells) {
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

    private void openDatePicker() {
        Intent intent = presenter.getDatePickerIntent(this, startDate, endDate);
        startActivityForResult(intent, REQUEST_CODE_DATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE_DATE && intent != null) {
            long startDateTime = intent.getLongExtra(SetDateFragment.START_DATE, -1);
            long endDateTime = intent.getLongExtra(SetDateFragment.END_DATE, -1);
            int selectionDatePickerType = intent.getIntExtra(SetDateActivity.SELECTION_TYPE, 0);
            int selectionDatePeriodIndex = intent.getIntExtra(SetDateActivity.SELECTION_PERIOD, 0);
            if (startDateTime > 0 && endDateTime > 0) {
                presenter.saveDate(new Date(startDateTime), new Date(endDateTime));
                presenter.saveSelectionDatePicker(selectionDatePickerType, selectionDatePeriodIndex);
            }
        }
    }

    protected abstract int getTypeStatistic();
}