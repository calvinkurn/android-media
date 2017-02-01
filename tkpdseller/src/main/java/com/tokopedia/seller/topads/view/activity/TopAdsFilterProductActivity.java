package com.tokopedia.seller.topads.view.activity;

import android.os.Bundle;

import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.view.fragment.TopAdsFilterContentFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsFilterGroupNameFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsFilterStatusFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 1/27/2017.
 */

public class TopAdsFilterProductActivity extends TopAdsFilterActivity {

    private int selectedFilterStatus;

    @Override
    protected void setupBundlePass(Bundle extras) {
        super.setupBundlePass(extras);
        selectedFilterStatus = extras.getInt(TopAdsExtraConstant.EXTRA_FILTER_STATUS_VALUE);
    }

    @Override
    protected List<TopAdsFilterContentFragment> getFilterContentList() {
        List<TopAdsFilterContentFragment> filterContentFragmentList = new ArrayList<>();
        filterContentFragmentList.add(TopAdsFilterStatusFragment.createInstance(selectedFilterStatus));
        filterContentFragmentList.add(new TopAdsFilterGroupNameFragment());
        return filterContentFragmentList;
    }

    @Override
    public String getScreenName() {
        return null;
    }
}
