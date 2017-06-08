package com.tokopedia.seller.topads.keyword.view.fragment;

import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.keyword.view.activity.TopAdsKeywordDetailNegativeActivity;
import com.tokopedia.seller.topads.keyword.view.activity.TopAdsKeywordNewChooseGroupActivity;
import com.tokopedia.seller.topads.keyword.view.model.KeywordAd;
import com.tokopedia.seller.topads.view.adapter.viewholder.TopAdsEmptyAdDataBinder;
import com.tokopedia.seller.topads.view.model.Ad;

/**
 * @author normansyahputa on 5/19/17.
 */

public class TopAdsKeywordNegativeListFragment extends TopAdsKeywordListFragment {

    public static Fragment createInstance() {
        return new TopAdsKeywordNegativeListFragment();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_keyword_negative_list;
    }

    @Override
    protected void initDateLabelView(View view) {
        // Do nothing
    }

    @Override
    protected void updateDateLabelViewText() {
        // Do nothing
    }

    @Override
    protected boolean isStatusShown() {
        return false;
    }

    @Override
    protected TopAdsEmptyAdDataBinder getEmptyViewDefaultBinder() {
        TopAdsEmptyAdDataBinder emptyViewDefaultBinder = super.getEmptyViewDefaultBinder();
        emptyViewDefaultBinder.setEmptyTitleText(getString(R.string.top_ads_keyword_your_keyword_negative_empty));
        emptyViewDefaultBinder.setEmptyContentText(getString(R.string.top_ads_keyword_please_use_negative));
        return emptyViewDefaultBinder;
    }

    protected boolean isPositive() {
        return false;
    }

    @Override
    public void onCreateAd() {
        TopAdsKeywordNewChooseGroupActivity.start(this, getActivity(), REQUEST_CODE_CREATE_KEYWORD, isPositive());
    }

    @Override
    public void onItemClicked(Ad ad) {
        startActivityForResult(TopAdsKeywordDetailNegativeActivity.createInstance(getActivity(), (KeywordAd) ad, ""), REQUEST_CODE_AD_STATUS);
    }
}