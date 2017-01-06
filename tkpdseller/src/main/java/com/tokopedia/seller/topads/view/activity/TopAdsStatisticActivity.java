package com.tokopedia.seller.topads.view.activity;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.datasource.TopAdsCacheDataSourceImpl;
import com.tokopedia.seller.topads.datasource.TopAdsDbDataSourceImpl;
import com.tokopedia.seller.topads.interactor.TopAdsProductAdInteractorImpl;
import com.tokopedia.seller.topads.model.data.Cell;
import com.tokopedia.seller.topads.model.request.StatisticRequest;
import com.tokopedia.seller.topads.network.apiservice.TopAdsManagementService;
import com.tokopedia.seller.topads.presenter.TopAdsStatisticActivityPresenter;
import com.tokopedia.seller.topads.presenter.TopAdsStatisticActivityPresenterImpl;
import com.tokopedia.seller.topads.view.adapter.TopAdsStatisticPagerAdapter;
import com.tokopedia.seller.topads.view.fragment.TopAdsStatisticAvgFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsStatisticConversionFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsStatisticCtrFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsStatisticKlikFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsStatisticImprFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsStatisticSpentFragment;
import com.tokopedia.seller.topads.view.listener.TopAdsStatisticActivityViewListener;
import com.tokopedia.seller.topads.view.listener.TopAdsStatisticViewListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TopAdsStatisticActivity extends BasePresenterActivity<TopAdsStatisticActivityPresenter> implements TopAdsStatisticActivityViewListener {
    @BindView(R2.id.pager)
    ViewPager viewPager;
    @BindView(R2.id.tab)
    TabLayout tabLayout;

    StatisticRequest statisticRequest = new StatisticRequest();
    private List<Cell> cells;

    @Override
    protected void setupURIPass(Uri uri) {

    }

    @Override
    protected void setupBundlePass(Bundle bundle) {
        statisticRequest = bundle.getParcelable(TopAdsExtraConstant.EXTRA_STATISTIC_REQUEST);
    }

    @Override
    protected void initialPresenter() {
        presenter = new TopAdsStatisticActivityPresenterImpl(this, new TopAdsProductAdInteractorImpl
                (new TopAdsManagementService(), new TopAdsDbDataSourceImpl(), new TopAdsCacheDataSourceImpl(this)));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_top_ads_statistic;
    }

    @Override
    protected void initView() {
        viewPager.setAdapter(getViewPagerAdapter());
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void setViewListener() {
        viewPager.setOffscreenPageLimit(TopAdsConstant.OFFSCREEN_PAGE_LIMIT);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new GlobalMainTabSelectedListener(viewPager));
    }

    @Override
    protected void initVar() {
    }

    @Override
    protected void setActionVar() {
        presenter.getStatisticFromNet(statisticRequest);
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

    }

    @Override
    public void updateDataCell(List<Cell> cells) {
        this.cells = cells;
        Fragment fragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
        if(fragment != null && fragment instanceof TopAdsStatisticViewListener){
            ((TopAdsStatisticViewListener)fragment).updateDataCell(cells);
        }
    }

    @Override
    public List<Cell> getDataCell(){
        return this.cells;
    }
}
