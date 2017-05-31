package com.tokopedia.seller.topads.keyword.view.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.data.model.data.Ad;
import com.tokopedia.seller.topads.data.model.data.GroupAd;
import com.tokopedia.seller.topads.keyword.constant.KeywordStatusTypeDef;
import com.tokopedia.seller.topads.keyword.di.component.DaggerTopAdsKeywordComponent;
import com.tokopedia.seller.topads.keyword.di.module.TopAdsModule;
import com.tokopedia.seller.topads.keyword.domain.model.Datum;
import com.tokopedia.seller.topads.keyword.view.activity.TopAdsKeywordDetailNegativeActivity;
import com.tokopedia.seller.topads.keyword.view.activity.TopAdsKeywordFilterActivity;
import com.tokopedia.seller.topads.keyword.view.activity.TopAdsKeywordNewChooseGroupActivity;
import com.tokopedia.seller.topads.keyword.view.adapter.TopAdsKeywordAdapter;
import com.tokopedia.seller.topads.keyword.view.model.BaseKeywordParam;
import com.tokopedia.seller.topads.keyword.view.model.KeywordAd;
import com.tokopedia.seller.topads.keyword.view.presenter.TopAdsKeywordListPresenterImpl;
import com.tokopedia.seller.topads.view.adapter.TopAdsBaseListAdapter;
import com.tokopedia.seller.topads.view.adapter.viewholder.TopAdsEmptyAdDataBinder;

import java.util.Map;

import javax.inject.Inject;

/**
 * @author normansyahputa on 5/19/17.
 */

public class TopAdsKeywordNegativeListFragment extends TopAdsBaseKeywordListFragment<TopAdsKeywordListPresenterImpl> {

    @Inject
    TopAdsKeywordListPresenterImpl topAdsKeywordListPresenter;

    @KeywordStatusTypeDef
    int filterStatus = KeywordStatusTypeDef.KEYWORD_STATUS_ALL;

    GroupAd groupAd;

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
                = topAdsKeywordListPresenter.generateParam(keyword, page, isPositive(),
                startDate.getTime(), endDate.getTime());

        String s = filters.get(TopAdsExtraConstant.EXTRA_FILTER_CURRECT_GROUP_SELECTION);
        if (s != null && !s.isEmpty()) {
            baseKeywordParam.groupId = Integer.parseInt(s);
        }

        s = filters.get(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS);
        if (s != null && !s.isEmpty()) {
            @KeywordStatusTypeDef int i = Integer.parseInt(s);
            baseKeywordParam.keywordStatus = i;
        }

        searchAd(baseKeywordParam);
    }

    protected void searchAd(BaseKeywordParam baseKeywordParam) {
        topAdsKeywordListPresenter.fetchNegativeKeyword(
                baseKeywordParam
        );
    }

    protected boolean isPositive() {
        return false;
    }

    @Override
    public void onFilterChanged(Object someObject) {
        Intent intent = new Intent(getActivity(), TopAdsKeywordFilterActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_CURRECT_GROUP_SELECTION, groupAd);
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS, filterStatus);
        startActivityForResult(intent, REQUEST_CODE_FILTER_KEYWORD);
    }

    @Override
    public void onCreateKeyword() {
        TopAdsKeywordNewChooseGroupActivity.start(
                TopAdsKeywordNegativeListFragment.this,
                getActivity(), REQUEST_CODE_CREATE_KEYWORD, isPositive());
    }

    @Override
    protected TopAdsBaseListAdapter<Datum> initializeTopAdsAdapter() {
        return new TopAdsKeywordAdapter(new TopAdsBaseListAdapter.Callback<Datum>() {
            @Override
            public void onClicked(Datum datum) {
                if (datum != null) {
                    KeywordAd keywordAd = getKeywordAd(datum);

                    goToDetail(keywordAd);
                }
            }
        });
    }

    protected void goToDetail(KeywordAd keywordAd) {
        startActivity(TopAdsKeywordDetailNegativeActivity.createInstance(
                getActivity(), keywordAd, ""
        ));
    }

    @NonNull
    protected KeywordAd getKeywordAd(Datum datum) {
        KeywordAd keywordAd = new KeywordAd();
        keywordAd.setId(Integer.toString(datum.getKeywordId()));
        keywordAd.setGroupId(Integer.toString(datum.getGroupId()));
        keywordAd.setKeywordTypeId(datum.getKeywordTypeId());

        keywordAd.setKeywordTag(datum.getKeywordTag());
        keywordAd.setStatus(datum.getKeywordStatus());
        keywordAd.setStatusDesc(datum.getKeywordStatusDesc());
        keywordAd.setStatAvgClick(datum.getStatAvgClick());
        keywordAd.setStatTotalSpent(datum.getStatTotalSpent());
        keywordAd.setStatTotalImpression(datum.getStatTotalImpression());
        keywordAd.setStatTotalClick(datum.getStatTotalClick());
        keywordAd.setStatTotalCtr(datum.getStatTotalCtr());
        keywordAd.setStatTotalConversion(datum.getStatTotalConversion());
        keywordAd.setPriceBidFmt(datum.getKeywordPriceBidFmt());
        keywordAd.setLabelPerClick(datum.getLabelPerClick());
        keywordAd.setKeywordTypeDesc(datum.getKeywordTypeDesc());
        return keywordAd;
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
    public Map<String, String> parseFilter(int resultCode, Intent intent) {
        Map<String, String> filters = super.parseFilter(resultCode, intent);
        groupAd = intent.getParcelableExtra(TopAdsExtraConstant.EXTRA_FILTER_CURRECT_GROUP_SELECTION);
        filterStatus = intent.getIntExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS, KeywordStatusTypeDef.KEYWORD_STATUS_ALL);
        if (groupAd != null)
            filters.put(TopAdsExtraConstant.EXTRA_FILTER_CURRECT_GROUP_SELECTION, groupAd.getId());
        filters.put(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS, Integer.toString(status));
        return filters;
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
