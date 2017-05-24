package com.tokopedia.seller.topads.keyword.view.fragment;

/**
 * Created by hendry on 5/18/2017.
 */

public class TopAdsKeywordAddNegativeSummaryFragment extends AbsTopAdsKeywordAddSummaryFragment {

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    boolean isPositiveKeyword() {
        return false;
    }

    public static TopAdsKeywordAddNegativeSummaryFragment newInstance(int groupId, String groupName) {
        TopAdsKeywordAddNegativeSummaryFragment fragment = new TopAdsKeywordAddNegativeSummaryFragment();
        fragment.setArguments(createBundle(groupId,groupName));
        return fragment;
    }

}
