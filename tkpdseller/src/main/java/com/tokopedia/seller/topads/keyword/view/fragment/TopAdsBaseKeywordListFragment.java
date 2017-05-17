package com.tokopedia.seller.topads.keyword.view.fragment;

import com.tokopedia.seller.topads.view.presenter.TopAdsAdListPresenter;

/**
 * @author normansyahputa on 5/17/17.
 */
public abstract class TopAdsBaseKeywordListFragment<T extends TopAdsAdListPresenter> extends TopAdsAdListFragment<T> {
    public abstract void onSearchChanged(String query);

    public abstract void onFilterChanged(Object someObject);
}
