package com.tokopedia.seller.topads.view.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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

public abstract class TopAdsFilterActivity extends BasePresenterActivity implements TopAdsFilterListFragment.Callback, TopAdsFilterContentFragment.Callback {

    private TopAdsFilterListFragment topAdsFilterListFragment;
    private List<TopAdsFilterContentFragment> filterContentFragmentList;
    private Button submitButton;

    protected abstract List<TopAdsFilterContentFragment> getFilterContentList();

    protected abstract Intent getDefaultIntentResult();

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
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
        int selectedPosition = 0;
        submitButton = (Button) findViewById(R.id.button_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFilterChangedResult();
                finish();
            }
        });
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
        filterContentFragment.setCallback(this);
    }

    private ArrayList<FilterTitleItem> getFilterTitleItemList() {
        ArrayList<FilterTitleItem> filterTitleItemList = new ArrayList<>();
        for (TopAdsFilterContentFragment filterContentFragment : filterContentFragmentList) {
            FilterTitleItem filterTitleItem = new FilterTitleItem();
            filterTitleItem.setTitle(filterContentFragment.getTitle(this));
            filterTitleItem.setActive(filterContentFragment.isActive());
            filterTitleItemList.add(filterTitleItem);
        }
        return filterTitleItemList;
    }

    @Override
    public void onStatusChanged(boolean active) {
        int position = topAdsFilterListFragment.getCurrentPosition();
        topAdsFilterListFragment.setActive(position, active);
    }

    @Override
    public void onItemSelected(int position) {
        changeContent(filterContentFragmentList.get(position));
    }

    private void setFilterChangedResult() {
        Intent intent = getDefaultIntentResult();
        for (TopAdsFilterContentFragment topAdsFilterContentFragment : filterContentFragmentList) {
            topAdsFilterContentFragment.addResult(intent);
        }
        setResult(Activity.RESULT_OK, intent);
    }

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top_ads_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_close) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}