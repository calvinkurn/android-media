package com.tokopedia.seller.topads.view.activity;

import com.tokopedia.seller.topads.view.fragment.TopAdsFilterContentFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsFilterStatusFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 1/27/2017.
 */

public class TopAdsFilterGroupActivity extends TopAdsFilterActivity {

    public static final String FILTER_CONTENT_LIST = "FILTER_CONTENT_LIST";

    @Override
    protected List<TopAdsFilterContentFragment> getFilterContentList() {
        List<TopAdsFilterContentFragment> filterContentFragmentList = new ArrayList<>();
        filterContentFragmentList.add(new TopAdsFilterStatusFragment());
        return filterContentFragmentList;
    }

    @Override
    public String getScreenName() {
        return null;
    }
}
