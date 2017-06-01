package com.tokopedia.seller.topads.keyword.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.data.model.data.GroupAd;
import com.tokopedia.seller.topads.keyword.constant.KeywordStatusTypeDef;
import com.tokopedia.seller.topads.keyword.di.component.DaggerTopAdsKeywordComponent;
import com.tokopedia.seller.topads.keyword.di.module.TopAdsModule;
import com.tokopedia.seller.topads.keyword.domain.model.Datum;
import com.tokopedia.seller.topads.keyword.view.activity.TopAdsKeywordDetailActivity;
import com.tokopedia.seller.topads.keyword.view.activity.TopAdsKeywordFilterActivity;
import com.tokopedia.seller.topads.keyword.view.activity.TopAdsKeywordNewChooseGroupActivity;
import com.tokopedia.seller.topads.keyword.view.adapter.TopAdsKeywordAdapter;
import com.tokopedia.seller.topads.keyword.view.listener.AdListMenuListener;
import com.tokopedia.seller.topads.keyword.view.model.BaseKeywordParam;
import com.tokopedia.seller.topads.keyword.view.model.KeywordAd;
import com.tokopedia.seller.topads.keyword.view.presenter.TopAdsKeywordListPresenterImpl;
import com.tokopedia.seller.topads.view.adapter.TopAdsBaseListAdapter;
import com.tokopedia.seller.topads.view.adapter.viewholder.TopAdsEmptyAdDataBinder;
import com.tokopedia.seller.topads.view.fragment.TopAdsAdListFragment;
import com.tokopedia.seller.topads.view.model.Ad;

import javax.inject.Inject;

/**
 * @author normansyahputa on 5/17/17.
 */
public class TopAdsKeywordListFragment extends TopAdsAdListFragment<TopAdsKeywordListPresenterImpl> implements AdListMenuListener {

    public static Fragment createInstance() {
        return new TopAdsKeywordListFragment();
    }

    protected static final int REQUEST_CODE_CREATE_KEYWORD = 20;

    @KeywordStatusTypeDef
    protected int filterStatus;
    protected GroupAd groupAd;

    @Inject
    TopAdsKeywordListPresenterImpl topAdsKeywordListPresenter;

    @Override
    protected void initInjector() {
        DaggerTopAdsKeywordComponent.builder()
                .topAdsModule(new TopAdsModule())
                .appComponent(getComponent(AppComponent.class))
                .build()
                .inject(this);
    }

    @Override
    protected void initialVar() {
        super.initialVar();
        filterStatus = KeywordStatusTypeDef.KEYWORD_STATUS_ALL;
    }

    @Override
    protected void initialPresenter() {
        super.initialPresenter();
        topAdsKeywordListPresenter.attachView(this);
    }

    @Override
    protected TopAdsEmptyAdDataBinder getEmptyViewBinder() {
        return null;
    }

    @Override
    protected void searchAd() {
        super.searchAd();
        BaseKeywordParam baseKeywordParam = topAdsKeywordListPresenter.generateParam(keyword, page,
                isPositive(), startDate.getTime(), endDate.getTime());
        if (groupAd != null) {
            baseKeywordParam.groupId = Integer.parseInt(groupAd.getId());
        }
        baseKeywordParam.keywordStatus = filterStatus;
        searchAd(baseKeywordParam);
    }

    protected void searchAd(BaseKeywordParam baseKeywordParam) {
        topAdsKeywordListPresenter.fetchPositiveKeyword(baseKeywordParam);
    }

    @Override
    protected TopAdsBaseListAdapter<Datum> getNewAdapter() {
        return new TopAdsKeywordAdapter(new TopAdsBaseListAdapter.Callback<Datum>() {
            @Override
            public void onItemClicked(Datum datum) {
                if (datum != null) {
                    KeywordAd keywordAd = getKeywordAd(datum);
                    goToDetail(keywordAd);
                }
            }
        });
    }

    protected void goToDetail(KeywordAd keywordAd) {
        onItemClicked(keywordAd);
    }

    protected boolean isPositive() {
        return true;
    }

    @NonNull
    protected KeywordAd getKeywordAd(Datum datum) {
        KeywordAd keywordAd = new KeywordAd();
        keywordAd.setId(Integer.toString(datum.getKeywordId()));
        keywordAd.setGroupId(Integer.toString(datum.getGroupId()));
        keywordAd.setKeywordTypeId(datum.getKeywordTypeId());
        keywordAd.setGroupName(datum.getGroupName());
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
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE_AD_FILTER && intent != null) {
            groupAd = intent.getParcelableExtra(TopAdsExtraConstant.EXTRA_FILTER_CURRECT_GROUP_SELECTION);
            filterStatus = intent.getIntExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS, KeywordStatusTypeDef.KEYWORD_STATUS_ALL);
            searchAd();
            updateEmptyViewNoResult();
        } else if (requestCode == REQUEST_CODE_CREATE_KEYWORD) {
            if (resultCode == Activity.RESULT_OK) {
                onSearch(null);
            }
        }
    }

    @Override
    public void onSearch(String keyword) {
        onQueryTextSubmit(keyword);
    }

    @Override
    public void goToFilter() {
        Intent intent = new Intent(getActivity(), TopAdsKeywordFilterActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_CURRECT_GROUP_SELECTION, groupAd);
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS, filterStatus);
        startActivityForResult(intent, REQUEST_CODE_AD_FILTER);
    }

    @Override
    public void onCreateAd() {
        TopAdsKeywordNewChooseGroupActivity.start(this, getActivity(), REQUEST_CODE_CREATE_KEYWORD, isPositive());
    }

    @Override
    public void onItemClicked(Ad ad) {
        startActivity(TopAdsKeywordDetailActivity.createInstance(getActivity(), (KeywordAd) ad, ""));
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        topAdsKeywordListPresenter.detachView();
    }
}