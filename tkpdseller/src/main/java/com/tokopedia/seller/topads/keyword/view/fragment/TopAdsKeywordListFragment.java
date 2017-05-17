package com.tokopedia.seller.topads.keyword.view.fragment;

import android.support.v4.app.Fragment;

import com.tokopedia.seller.topads.data.model.data.Ad;
import com.tokopedia.seller.topads.keyword.view.presenter.TopAdsKeywordListPresenterImpl;
import com.tokopedia.seller.topads.view.adapter.viewholder.TopAdsEmptyAdDataBinder;

/**
 * Created by normansyahputa on 5/17/17.
 */

public class TopAdsKeywordListFragment extends TopAdsBaseKeywordListFragment<TopAdsKeywordListPresenterImpl> {

    public static Fragment createInstance() {
        return new TopAdsKeywordListFragment();
    }

    @Override
    protected void initialPresenter() {
        super.initialPresenter();
        presenter = new TopAdsKeywordListPresenterImpl();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onSearchChanged(String query) {

    }

    @Override
    public void onFilterChanged(Object someObject) {

    }

    @Override
    protected void initInjector() {

    }

    @Override
    public void onClicked(Ad ad) {

    }

    @Override
    protected TopAdsEmptyAdDataBinder getEmptyViewBinder() {
        return null;
    }

    @Override
    protected void goToFilter() {

    }
}
