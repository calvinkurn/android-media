package com.tokopedia.seller.topads.keyword.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.data.model.data.GroupAd;
import com.tokopedia.seller.topads.keyword.constant.KeywordStatusTypeDef;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsKeywordFilterStatusFragment;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsKeywordGroupsFragment;
import com.tokopedia.seller.topads.keyword.view.listener.TopAdsKeywordGroupListListener;
import com.tokopedia.seller.topads.view.fragment.TopAdsFilterStatusFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 1/27/2017.
 */
public class TopAdsKeywordFilterActivity extends TopAdsFilterActivity
        implements HasComponent<AppComponent>, TopAdsKeywordGroupListListener {

    @KeywordStatusTypeDef
    private int selectedFilterStatus;

    private GroupAd currentGroupAd;

    @Override
    protected void setupBundlePass(Bundle extras) {
        super.setupBundlePass(extras);
        selectedFilterStatus = extras.getInt(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS, KeywordStatusTypeDef.KEYWORD_STATUS_ALL);
        currentGroupAd = extras.getParcelable(TopAdsExtraConstant.EXTRA_FILTER_CURRECT_GROUP_SELECTION);
    }

    @Override
    protected List<Fragment> getFilterContentList() {
        List<Fragment> filterContentFragmentList = new ArrayList<>();
        TopAdsFilterStatusFragment topAdsFilterStatusFragment = TopAdsKeywordFilterStatusFragment.createInstance(selectedFilterStatus);
        topAdsFilterStatusFragment.setActive(true);
        filterContentFragmentList.add(topAdsFilterStatusFragment);
        TopAdsKeywordGroupsFragment topAdsFilterGroupNameFragment = TopAdsKeywordGroupsFragment.createInstance(currentGroupAd);
        topAdsFilterGroupNameFragment.setActive(currentGroupAd != null);
        filterContentFragmentList.add(topAdsFilterGroupNameFragment);
        return filterContentFragmentList;
    }

    @Override
    protected Intent getDefaultIntentResult() {
        Intent intent = new Intent();
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS, selectedFilterStatus);
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_GROUP_ID, currentGroupAd);
        return intent;
    }

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }

    @Override
    public void notifySelect(GroupAd groupAd) {
        getCurrentFragment(1, TopAdsKeywordGroupsFragment.class).setActive(true);
        topAdsFilterListFragment.setActive(1, true);
    }

    @Override
    public void resetSelection() {
        getCurrentFragment(1, TopAdsKeywordGroupsFragment.class).setActive(false);
        topAdsFilterListFragment.setActive(1, false);
    }
}