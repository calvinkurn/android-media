package com.tokopedia.seller.topads.keyword.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.keyword.constant.KeywordTypeDef;
import com.tokopedia.seller.topads.keyword.view.activity.TopAdsKeywordEditDetailNegativeActivity;
import com.tokopedia.seller.topads.keyword.view.model.KeywordAd;
import com.tokopedia.seller.topads.view.model.Ad;

/**
 * Created by zulfikarrahman on 5/30/17.
 */

public class TopAdsKeywordDetailNegativeFragment extends TopAdsKeywordDetailFragment {

    public static Fragment createInstance(KeywordAd ad, String adId) {
        Fragment fragment = new TopAdsKeywordDetailNegativeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TopAdsExtraConstant.EXTRA_AD, ad);
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_keyword_negative_detail;
    }

    @Override
    protected void initDateLabelView() {
        // Do nothing
    }

    @Override
    protected void updateDateLabelViewText() {
        // Do nothing
    }

    @Override
    protected void updateCostView(Ad ad) {
        // Do nothing
    }

    @Override
    protected void updateDailyBudgetView(Ad ad) {
        // Do nothing
    }

    @Override
    protected void updateStatisticView(Ad ad) {
        // Do nothing
    }

    @Override
    protected int getKeywordTypeValue() {
        return TopAdsConstant.KEYWORD_TYPE_NEGATIVE_VALUE;
    }

    @Override
    protected void editAd() {
        TopAdsKeywordEditDetailNegativeActivity.start(getActivity(), keywordAd);
    }
}
