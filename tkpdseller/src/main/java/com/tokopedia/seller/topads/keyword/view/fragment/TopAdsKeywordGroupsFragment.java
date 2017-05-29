package com.tokopedia.seller.topads.keyword.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.data.model.data.Ad;
import com.tokopedia.seller.topads.data.model.data.GroupAd;
import com.tokopedia.seller.topads.keyword.di.component.DaggerTopAdsKeywordNewChooseGroupComponent;
import com.tokopedia.seller.topads.keyword.di.module.TopAdsKeywordNewChooseGroupModule;
import com.tokopedia.seller.topads.keyword.view.adapter.TopAdsKeywordGroupListAdapter;
import com.tokopedia.seller.topads.keyword.view.listener.TopAdsKeywordGroupListView;
import com.tokopedia.seller.topads.keyword.view.presenter.TopAdsKeywordNewChooseGroupPresenter;
import com.tokopedia.seller.topads.view.adapter.TopAdsAdListAdapter;
import com.tokopedia.seller.topads.view.adapter.viewholder.TopAdsEmptyAdDataBinder;
import com.tokopedia.seller.topads.view.listener.TopAdsFilterContentFragmentListener;

import java.util.List;

import javax.inject.Inject;

/**
 * @author normansyahputa on 5/26/17.
 */

public class TopAdsKeywordGroupsFragment extends TopAdsBaseKeywordListFragment<TopAdsKeywordNewChooseGroupPresenter> implements TopAdsKeywordGroupListView, TopAdsFilterContentFragmentListener {

    protected TopAdsFilterContentFragment.Callback callback;
    @Inject
    TopAdsKeywordNewChooseGroupPresenter topAdsKeywordNewChooseGroupPresenter;
    private EditText groupFilterSearch;
    private RecyclerView groupFilterRecyclerView;
    /**
     * Sign for title filter list
     */
    private boolean active;

    public static TopAdsKeywordGroupsFragment createInstance(long selectedGroupId, long currentGroupId, String currentGroupName) {
        return new TopAdsKeywordGroupsFragment();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onFilterChanged(Object someObject) {

    }

    @Override
    public void onCreateKeyword() {

    }

    @Override
    protected void initInjector() {
        DaggerTopAdsKeywordNewChooseGroupComponent.builder()
                .topAdsKeywordNewChooseGroupModule(new TopAdsKeywordNewChooseGroupModule())
                .appComponent(getComponent(AppComponent.class))
                .build()
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        topAdsKeywordNewChooseGroupPresenter.attachView(this);
        View view = super.onCreateView(inflater, container, savedInstanceState);
        groupFilterSearch = (EditText) view.findViewById(R.id.group_filter_search);
        groupFilterSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                topAdsKeywordNewChooseGroupPresenter.searchGroupName(editable.toString());
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        topAdsKeywordNewChooseGroupPresenter.searchGroupName("");
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_keyword_filter_group_name;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        topAdsKeywordNewChooseGroupPresenter.detachView();
    }

    @Override
    public void onClicked(Ad ad) {

    }

    @Override
    protected TopAdsEmptyAdDataBinder getEmptyViewBinder() {
        return null;
    }

    @Override
    protected void goToFilter() {

    }

    @Override
    protected void fetchData() {

    }

    @Override
    public void onGetGroupAdList(List<GroupAd> groupAds) {
        onSearchAdLoaded(groupAds, groupAds.size());
    }

    @Override
    public void onGetGroupAdListError() {

    }

    @Override
    protected TopAdsAdListAdapter initializeTopAdsAdapter() {
        return new TopAdsKeywordGroupListAdapter();
    }

    @Override
    public String getTitle(Context context) {
        return "Filter Group";
    }

    @Override
    public Intent addResult(Intent intent) {
        // TODO return selection.
        return intent;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void setCallback(TopAdsFilterContentFragment.Callback callback) {
        this.callback = callback;
    }
}
