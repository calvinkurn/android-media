package com.tokopedia.seller.topads.keyword.view.fragment;

import com.tokopedia.seller.topads.keyword.view.presenter.TopAdsKeywordListPresenter;

/**
 * @author normansyahputa on 5/17/17.
 */
public abstract class TopAdsBaseKeywordListFragment<T extends TopAdsKeywordListPresenter> extends TopAdsAdListFragment<T> {

    protected static final int REQUEST_CODE_CREATE_KEYWORD = 20;

    protected String keyword;

    public void onSearchChanged(String query) {
        keyword = query;
        searchAd(START_PAGE);
        updateEmptyViewNoResult();
    }

    public abstract void onFilterChanged(Object someObject);

    public abstract void onCreateKeyword();
}
