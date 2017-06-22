package com.tokopedia.seller.topads.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.base.view.activity.BaseFilterActivity;
import com.tokopedia.seller.topads.view.fragment.TopAdsFilterStatusFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsProductFilterStatusFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 1/27/2017.
 */
public class TopAdsFilterGroupActivity extends BaseFilterActivity {

    private int selectedFilterStatus;

    @Override
    protected void setupBundlePass(Bundle extras) {
        super.setupBundlePass(extras);
        selectedFilterStatus = extras.getInt(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS);
    }

    @Override
    protected List<Fragment> getFilterContentList() {
        List<Fragment> filterContentFragmentList = new ArrayList<>();
        TopAdsFilterStatusFragment topAdsFilterStatusFragment = TopAdsProductFilterStatusFragment.createInstance(selectedFilterStatus);
        topAdsFilterStatusFragment.setActive(true);
        filterContentFragmentList.add(topAdsFilterStatusFragment);
        return filterContentFragmentList;
    }

    @Override
    protected Intent getDefaultIntentResult() {
        Intent intent = new Intent();
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS, selectedFilterStatus);
        return intent;
    }

    @Override
    public String getScreenName() {
        return null;
    }
}