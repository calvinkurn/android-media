package com.tokopedia.topads.dashboard.view.fragment;

import android.os.Bundle;

import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;

/**
 * Created by Nathaniel on 1/31/2017.
 */

public class TopAdsProductFilterStatusFragment extends TopAdsFilterStatusFragment {

    public static TopAdsProductFilterStatusFragment createInstance(int status) {
        TopAdsProductFilterStatusFragment fragment = new TopAdsProductFilterStatusFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS, status);
        fragment.setArguments(bundle);
        return fragment;
    }

    public String[] getStatusValueList() {
        return getResources().getStringArray(R.array.top_ads_filter_status_list_values);
    }

    public String[] getStatusNameList() {
        return getResources().getStringArray(R.array.top_ads_filter_status_list_entries);
    }
}