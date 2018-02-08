package com.tokopedia.topads.keyword.view.fragment;

import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.view.adapter.viewholder.TopAdsEmptyAdDataBinder;
import com.tokopedia.topads.keyword.view.activity.TopAdsKeywordDetailNegativeActivity;
import com.tokopedia.topads.keyword.view.activity.TopAdsKeywordNewChooseGroupActivity;
import com.tokopedia.topads.keyword.view.model.KeywordAd;

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
        searchInputView = (SearchInputView) view.findViewById(R.id.search_input_view);
        searchInputView.setDelayTextChanged(DEFAULT_DELAY_TEXT_CHANGED);
        searchInputView.setListener(this);
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
        UnifyTracking.eventTopAdsProductNewPromoKeywordNegatif();
        TopAdsKeywordNewChooseGroupActivity.start(this, getActivity(), REQUEST_CODE_AD_ADD, isPositive());
    }

    @Override
    public void onItemClicked(KeywordAd ad) {
        startActivityForResult(TopAdsKeywordDetailNegativeActivity.createInstance(getActivity(), ad, ""), REQUEST_CODE_AD_CHANGE);
    }
}