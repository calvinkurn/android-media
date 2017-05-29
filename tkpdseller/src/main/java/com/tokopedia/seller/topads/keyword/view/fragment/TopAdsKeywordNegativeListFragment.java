package com.tokopedia.seller.topads.keyword.view.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.topads.data.model.data.Ad;
import com.tokopedia.seller.topads.keyword.di.component.DaggerTopAdsKeywordComponent;
import com.tokopedia.seller.topads.keyword.di.module.TopAdsModule;
import com.tokopedia.seller.topads.keyword.view.activity.TopAdsKeywordFilterActivity;
import com.tokopedia.seller.topads.keyword.view.activity.TopAdsKeywordNewChooseGroupActivity;
import com.tokopedia.seller.topads.keyword.view.adapter.TopAdsKeywordAdapter;
import com.tokopedia.seller.topads.keyword.view.model.BaseKeywordParam;
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
    protected void searchAd() {
        super.searchAd();
        BaseKeywordParam baseKeywordParam
                = topAdsKeywordListPresenter.generateParam(keyword, page, false,
                startDate.getTime(), endDate.getTime());
        topAdsKeywordListPresenter.fetchNegativeKeyword(
                baseKeywordParam
        );
    }

    @Override
    public void onFilterChanged(Object someObject) {
        Intent intent = new Intent(getActivity(), TopAdsKeywordFilterActivity.class);
        startActivityForResult(intent, REQUEST_CODE_FILTER_KEYWORD);
    }

    @Override
    public void onCreateKeyword() {
        Intent intent = new Intent(getActivity(), TopAdsKeywordNewChooseGroupActivity.class);
        startActivityForResult(intent, REQUEST_CODE_CREATE_KEYWORD);
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
