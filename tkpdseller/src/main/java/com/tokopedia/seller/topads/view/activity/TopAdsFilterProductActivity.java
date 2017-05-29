package com.tokopedia.seller.topads.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.keyword.view.activity.TopAdsFilterActivity;
import com.tokopedia.seller.topads.view.fragment.TopAdsFilterGroupNameFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsFilterStatusFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsProductFilterStatusFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 1/27/2017.
 */

public class TopAdsFilterProductActivity extends TopAdsFilterActivity {

    private int selectedFilterStatus;
    private long selectedGroupId;
    private long currentGroupId;
    private String currentGroupName;

    @Override
    protected void setupBundlePass(Bundle extras) {
        super.setupBundlePass(extras);
        selectedFilterStatus = extras.getInt(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS);
        selectedGroupId = extras.getLong(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_GROUP_ID);
        currentGroupId = extras.getLong(TopAdsExtraConstant.EXTRA_FILTER_CURRENT_GROUP_ID);
        currentGroupName = extras.getString(TopAdsExtraConstant.EXTRA_FILTER_CURRENT_GROUP_NAME);
    }

    @Override
    protected List<Fragment> getFilterContentList() {
        List<Fragment> filterContentFragmentList = new ArrayList<>();
        TopAdsFilterStatusFragment topAdsFilterStatusFragment = TopAdsProductFilterStatusFragment.createInstance(selectedFilterStatus);
        topAdsFilterStatusFragment.setActive(true);
        filterContentFragmentList.add(topAdsFilterStatusFragment);
        TopAdsFilterGroupNameFragment topAdsFilterGroupNameFragment = TopAdsFilterGroupNameFragment.createInstance(selectedGroupId, currentGroupId, currentGroupName);
        topAdsFilterGroupNameFragment.setActive(true);
        filterContentFragmentList.add(topAdsFilterGroupNameFragment);
        return filterContentFragmentList;
    }

    @Override
    protected Intent getDefaultIntentResult() {
        Intent intent = new Intent();
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS, selectedFilterStatus);
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_GROUP_ID, selectedGroupId);
        return intent;
    }

    @Override
    public String getScreenName() {
        return null;
    }
}