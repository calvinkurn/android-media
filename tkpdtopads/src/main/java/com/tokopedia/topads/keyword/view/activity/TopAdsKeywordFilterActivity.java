package com.tokopedia.topads.keyword.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.base.view.activity.BaseFilterActivity;
import com.tokopedia.seller.base.view.fragment.TopAdsFilterListFragment;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsFilterStatusFragment;
import com.tokopedia.topads.keyword.constant.KeywordStatusTypeDef;
import com.tokopedia.topads.keyword.view.fragment.TopAdsKeywordFilterStatusFragment;
import com.tokopedia.topads.keyword.view.fragment.TopAdsKeywordGroupsFragment;
import com.tokopedia.topads.keyword.view.listener.TopAdsKeywordGroupListListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 1/27/2017.
 */
public class TopAdsKeywordFilterActivity extends BaseFilterActivity
        implements HasComponent<AppComponent>, TopAdsKeywordGroupListListener {

    @KeywordStatusTypeDef
    private int selectedFilterStatus;

    private GroupAd currentGroupAd;

    private boolean showStatus;

    @Override
    protected void setupBundlePass(Bundle extras) {
        super.setupBundlePass(extras);
        selectedFilterStatus = extras.getInt(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS,
                KeywordStatusTypeDef.KEYWORD_STATUS_ALL);
        currentGroupAd = extras.getParcelable(TopAdsExtraConstant.EXTRA_FILTER_CURRECT_GROUP_SELECTION);
        selectedPosition = extras.getInt(TopAdsFilterListFragment.EXTRA_ITEM_SELECTED_POSITION, 0);
        showStatus = extras.getBoolean(TopAdsExtraConstant.EXTRA_FILTER_SHOW_STATUS, true);
    }

    @Override
    protected List<Fragment> getFilterContentList() {
        List<Fragment> filterContentFragmentList = new ArrayList<>();
        if (showStatus) {
            TopAdsFilterStatusFragment topAdsFilterStatusFragment = TopAdsKeywordFilterStatusFragment.createInstance(selectedFilterStatus);
            topAdsFilterStatusFragment.setActive(true);
            filterContentFragmentList.add(topAdsFilterStatusFragment);
        }
        TopAdsKeywordGroupsFragment topAdsFilterGroupNameFragment = TopAdsKeywordGroupsFragment.createInstance(currentGroupAd, showStatus ? 1 : 0);
        topAdsFilterGroupNameFragment.setActive(currentGroupAd != null);
        filterContentFragmentList.add(topAdsFilterGroupNameFragment);
        return filterContentFragmentList;
    }

    @Override
    protected Intent getDefaultIntentResult() {
        Intent intent = new Intent();
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS, selectedFilterStatus);
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_GROUP_ID, currentGroupAd);
        intent.putExtra(TopAdsFilterListFragment.EXTRA_ITEM_SELECTED_POSITION, selectedPosition);
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SHOW_STATUS, showStatus);
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
    public void notifySelect(GroupAd groupAd, int position) {
        getCurrentFragment(position, TopAdsKeywordGroupsFragment.class).setActive(true);
        topAdsFilterListFragment.setActive(position, true);
    }

    @Override
    public void resetSelection(int position) {
        getCurrentFragment(position, TopAdsKeywordGroupsFragment.class).setActive(false);
        topAdsFilterListFragment.setActive(position, false);
    }

}