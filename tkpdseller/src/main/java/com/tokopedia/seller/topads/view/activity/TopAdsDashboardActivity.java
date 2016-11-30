package com.tokopedia.seller.topads.view.activity;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.view.adapter.TopAdsDashboardPagerAdapter;
import com.tokopedia.seller.topads.view.fragment.TopAdsProductFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Nathaniel on 11/22/2016.
 */

public class TopAdsDashboardActivity extends BasePresenterActivity {

    @Bind(R2.id.pager)
    ViewPager viewPager;
    @Bind(R2.id.indicator)
    TabLayout indicator;

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

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_top_ads_dashboard;
    }

    @Override
    protected void initView() {
        viewPager.setAdapter(getViewPagerAdapter());
        viewPager.setOffscreenPageLimit(TopAdsConstant.OFFSCREEN_PAGE_LIMIT);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(indicator));
        indicator.setOnTabSelectedListener(new GlobalMainTabSelectedListener(viewPager));
        indicator.addTab(indicator.newTab().setText(R.string.title_top_ads_product));
        indicator.addTab(indicator.newTab().setText(R.string.title_top_ads_store));

    }

    public PagerAdapter getViewPagerAdapter() {
        return new TopAdsDashboardPagerAdapter(getFragmentManager(), getFragmentList());
    }

    public List<Fragment> getFragmentList() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(TopAdsProductFragment.createInstance());
//        fragmentList.add(InboxMessageFragment.createInstance(MESSAGE_SENT));
//        fragmentList.add(InboxMessageFragment.createInstance(MESSAGE_ARCHIVE));
//        fragmentList.add(InboxMessageFragment.createInstance(MESSAGE_TRASH));
        return fragmentList;
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
}
