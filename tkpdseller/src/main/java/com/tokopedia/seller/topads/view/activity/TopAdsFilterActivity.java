package com.tokopedia.seller.topads.view.activity;

import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.model.other.FilterTitleItem;
import com.tokopedia.seller.topads.view.fragment.TopAdsFilterContentFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsFilterListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 1/27/2017.
 */

public abstract class TopAdsFilterActivity extends BasePresenterActivity implements TopAdsFilterListFragment.Callback {

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
        int selectedPosition = 0;
        filterContentFragmentList = getFilterContentList();
        topAdsFilterListFragment = TopAdsFilterListFragment.createInstance(getFilterTitleItemList(), selectedPosition);
        topAdsFilterListFragment.setCallback(this);
        TopAdsFilterContentFragment contentFragment = filterContentFragmentList.get(selectedPosition);
        getFragmentManager().beginTransaction().add(R.id.container_filter_list, topAdsFilterListFragment, TopAdsFilterListFragment.class.getSimpleName()).commit();
        getFragmentManager().beginTransaction().add(R.id.container_filter_content, contentFragment, TopAdsFilterListFragment.class.getSimpleName()).commit();
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
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_filter_content, filterContentFragment);
        fragmentTransaction.commit();
    }

    private ArrayList<FilterTitleItem> getFilterTitleItemList() {
        ArrayList<FilterTitleItem> filterTitleItemList = new ArrayList<>();
        for (TopAdsFilterContentFragment filterContentFragment : filterContentFragmentList) {
            FilterTitleItem filterTitleItem = new FilterTitleItem();
            filterTitleItem.setTitle(filterContentFragment.getTitle(this));
            filterTitleItemList.add(filterTitleItem);
        }
        return filterTitleItemList;
    }

    @Override
    public void onItemSelected(int position) {
        changeContent(filterContentFragmentList.get(position));
    }

    @Override
    public String getScreenName() {
        return null;
    }
}