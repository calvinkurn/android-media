package com.tokopedia.seller.topads.view.activity;

import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.view.fragment.TopAdsFilterContentFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsFilterListFragment;

import java.util.List;

/**
 * Created by Nathaniel on 1/27/2017.
 */

public abstract class TopAdsFilterActivity extends BasePresenterActivity {

    public static final String FILTER_CONTENT_LIST = "FILTER_CONTENT_LIST";

    private TopAdsFilterListFragment topAdsFilterListFragment;
    private List<TopAdsFilterContentFragment> filterContentFragmentList;

    protected abstract List<TopAdsFilterContentFragment> getFilterContentList();

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
        return R.layout.activity_top_ads_filter;
    }

    @Override
    protected void initView() {
        filterContentFragmentList = getFilterContentList();
        topAdsFilterListFragment = TopAdsFilterListFragment.createInstance();
        getFragmentManager().beginTransaction().disallowAddToBackStack().add(R.id.container_filter_list, topAdsFilterListFragment, TopAdsFilterListFragment.class.getSimpleName()).commit();
        changeContent(filterContentFragmentList.get(0));
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

    private void changeContent(TopAdsFilterContentFragment filterContentFragment) {
        getFragmentManager().beginTransaction().disallowAddToBackStack().add(R.id.container_filter_content, filterContentFragment, TopAdsFilterListFragment.class.getSimpleName()).commit();
    }

    @Override
    public String getScreenName() {
        return null;
    }
}
