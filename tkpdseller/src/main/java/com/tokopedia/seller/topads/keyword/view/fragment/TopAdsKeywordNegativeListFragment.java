package com.tokopedia.seller.topads.keyword.view.fragment;

import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.topads.data.model.data.Ad;
import com.tokopedia.seller.topads.keyword.view.adapter.TopAdsKeywordAdapter;
import com.tokopedia.seller.topads.keyword.view.di.component.DaggerTopAdsKeywordComponent;
import com.tokopedia.seller.topads.keyword.view.di.module.TopAdsModule;
import com.tokopedia.seller.topads.keyword.view.presenter.TopAdsKeywordListPresenterImpl;
import com.tokopedia.seller.topads.view.adapter.TopAdsAdListAdapter;
import com.tokopedia.seller.topads.view.adapter.viewholder.TopAdsEmptyAdDataBinder;

import javax.inject.Inject;

/**
 * Created by normansyahputa on 5/19/17.
 */

public class TopAdsKeywordNegativeListFragment extends TopAdsBaseKeywordListFragment<TopAdsKeywordListPresenterImpl> {

    @Inject
    TopAdsKeywordListPresenterImpl topAdsKeywordListPresenter;

    public static Fragment createInstance() {
        return new TopAdsKeywordNegativeListFragment();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initialPresenter() {
        super.initialPresenter();
        topAdsKeywordListPresenter.attachView(this);
    }

    @Override
    protected void fetchData() {
        TopAdsKeywordListPresenterImpl.BaseKeywordParam baseKeywordParam
                = topAdsKeywordListPresenter.generateParam(getActivity(), keyword, page, false,
                startDate.getTime(), endDate.getTime());
        topAdsKeywordListPresenter.fetchNegativeKeyword(
                baseKeywordParam
        );
    }

    @Override
    protected void searchAd() {
        super.searchAd();
        fetchData();
    }

    @Override
    public void onFilterChanged(Object someObject) {

    }

    @Override
    protected TopAdsAdListAdapter initializeTopAdsAdapter() {
        return new TopAdsKeywordAdapter();
    }

    @Override
    protected void initInjector() {
        DaggerTopAdsKeywordComponent.builder()
                .topAdsModule(new TopAdsModule())
                .appComponent(getComponent(AppComponent.class))
                .build()
                .inject(this);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        topAdsKeywordListPresenter.detachView();
    }
}
