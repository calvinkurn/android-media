package com.tokopedia.seller.topads.view.activity;

import com.tokopedia.seller.topads.view.fragment.TopAdsFilterContentFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsFilterGroupNameFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsFilterStatusFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 1/27/2017.
 */

public class TopAdsFilterProductActivity extends TopAdsFilterActivity {

    @Override
    protected List<TopAdsFilterContentFragment> getFilterContentList() {
        List<TopAdsFilterContentFragment> filterContentFragmentList = new ArrayList<>();
        filterContentFragmentList.add(new TopAdsFilterStatusFragment());
        filterContentFragmentList.add(new TopAdsFilterGroupNameFragment());
        return filterContentFragmentList;
    }

    @Override
    public String getScreenName() {
        return null;
    }
}
