package com.tokopedia.seller.topads.keyword.view.fragment;

import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.keyword.view.model.TopAdsKeywordEditDetailViewModel;

/**
 * @author sebastianuskh on 5/30/17.
 */

public class TopAdsKeywordEditDetailNegativeFragment extends TopAdsKeywordEditDetailFragment {

    public static TopAdsKeywordEditDetailNegativeFragment createInstance(TopAdsKeywordEditDetailViewModel model) {
        TopAdsKeywordEditDetailNegativeFragment fragment = new TopAdsKeywordEditDetailNegativeFragment();
        fragment.setArguments(createArguments(model));
        return fragment;
    }

    @Override
    protected void settingTopAdsKeywordType(View view) {
        super.settingTopAdsKeywordType(view);
        topAdsKeywordType.setEntries(getResources().getStringArray(R.array.top_ads_keyword_type_list_names));
        topAdsKeywordType.setValues(getResources().getStringArray(R.array.top_ads_keyword_type_negative_list_values));
    }

    @Override
    protected void settingTopAdsCostPerClick(View view) {
        super.settingTopAdsCostPerClick(view);
        topAdsCostPerClick.setVisibility(View.GONE);
        topAdsMaxPriceInstruction.setVisibility(View.GONE);
    }
}
