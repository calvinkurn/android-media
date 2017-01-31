package com.tokopedia.seller.topads.view.fragment;

import com.tokopedia.seller.R;

/**
 * Created by Nathaniel on 1/31/2017.
 */

public class TopAdsFilterStatusFragment extends TopAdsFilterContentFragment {

    @Override
    public String getTitle() {
        return getString(R.string.label_top_ads_status);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_filter_status;
    }
}
