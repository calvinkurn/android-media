package com.tokopedia.seller.topads.keyword.view.fragment;

import android.os.Bundle;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.view.fragment.TopAdsFilterStatusFragment;

/**
 * Created by Nathaniel on 1/31/2017.
 */

public class TopAdsKeywordFilterStatusFragment extends TopAdsFilterStatusFragment {

    public static TopAdsKeywordFilterStatusFragment createInstance(int status) {
        TopAdsKeywordFilterStatusFragment fragment = new TopAdsKeywordFilterStatusFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS, status);
        fragment.setArguments(bundle);
        return fragment;
    }

    public String[] getStatusValueList() {
        return getResources().getStringArray(R.array.top_ads_keyword_filter_status_list_values);
    }

    public String[] getStatusNameList() {
        return getResources().getStringArray(R.array.top_ads_keyword_filter_status_list_names);
    }
}