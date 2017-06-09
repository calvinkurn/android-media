package com.tokopedia.seller.topads.keyword.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.data.model.data.GroupAd;
import com.tokopedia.seller.topads.keyword.constant.KeywordStatusTypeDef;
import com.tokopedia.seller.topads.keyword.di.component.DaggerTopAdsKeywordComponent;
import com.tokopedia.seller.topads.keyword.di.module.TopAdsModule;
import com.tokopedia.seller.topads.keyword.view.activity.TopAdsKeywordDetailActivity;
import com.tokopedia.seller.topads.keyword.view.activity.TopAdsKeywordFilterActivity;
import com.tokopedia.seller.topads.keyword.view.activity.TopAdsKeywordNewChooseGroupActivity;
import com.tokopedia.seller.topads.keyword.view.adapter.TopAdsKeywordAdapter;
import com.tokopedia.seller.topads.keyword.view.listener.KeywordListListener;
import com.tokopedia.seller.topads.keyword.view.model.BaseKeywordParam;
import com.tokopedia.seller.topads.keyword.view.model.KeywordAd;
import com.tokopedia.seller.topads.keyword.view.presenter.TopAdsKeywordListPresenterImpl;
import com.tokopedia.seller.topads.view.adapter.TopAdsBaseListAdapter;
import com.tokopedia.seller.topads.view.adapter.viewholder.TopAdsEmptyAdDataBinder;
import com.tokopedia.seller.topads.view.fragment.TopAdsAdListFragment;
import com.tokopedia.seller.topads.view.model.Ad;

import java.util.List;

import javax.inject.Inject;

/**
 * @author normansyahputa on 5/17/17.
 */
public class TopAdsKeywordListFragment extends TopAdsAdListFragment<TopAdsKeywordListPresenterImpl>
        implements TopAdsEmptyAdDataBinder.Callback {

    protected int filterStatus;
    protected GroupAd groupAd;
    protected int selectedPosition;
    private boolean hasData;

    @Inject
    TopAdsKeywordListPresenterImpl topAdsKeywordListPresenter;
    private KeywordListListener.Listener keywordAdListener;

    public static Fragment createInstance() {
        return new TopAdsKeywordListFragment();
    }

    @Override
    protected void initialListener(Activity activity) {
        super.initialListener(activity);
        if (activity != null && activity instanceof KeywordListListener.Listener) {
            keywordAdListener = (KeywordListListener.Listener) activity;
        }
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
    protected TopAdsEmptyAdDataBinder getEmptyViewDefaultBinder() {
        TopAdsEmptyAdDataBinder emptyGroupAdsDataBinder = new TopAdsEmptyAdDataBinder(adapter) {
            @Override
            protected int getEmptyLayout() {
                return R.layout.listview_top_ads_empty_keyword_list;
            }
        };
        emptyGroupAdsDataBinder.setEmptyTitleText(getString(R.string.top_ads_keyword_your_keyword_empty));
        emptyGroupAdsDataBinder.setEmptyContentText(getString(R.string.top_ads_keyword_please_use));
        emptyGroupAdsDataBinder.setEmptyButtonItemText(getString(R.string.top_ads_keyword_add_keyword));
        emptyGroupAdsDataBinder.setCallback(this);
        return emptyGroupAdsDataBinder;
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
        topAdsKeywordListPresenter.fetchKeyword(baseKeywordParam);
    }

    @Override
    protected TopAdsBaseListAdapter<KeywordAd> getNewAdapter() {
        return new TopAdsKeywordAdapter();
    }

    protected boolean isPositive() {
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE_AD_FILTER && intent != null) {
            groupAd = intent.getParcelableExtra(TopAdsExtraConstant.EXTRA_FILTER_CURRECT_GROUP_SELECTION);
            filterStatus = intent.getIntExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS, KeywordStatusTypeDef.KEYWORD_STATUS_ALL);
            selectedPosition = intent.getIntExtra(TopAdsExtraConstant.EXTRA_ITEM_SELECTED_POSITION, 0);
            searchAd(START_PAGE);
        }
    }

    protected boolean isStatusShown() {
        return true;
    }

    @Override
    public void goToFilter() {
        Intent intent = new Intent(getActivity(), TopAdsKeywordFilterActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_CURRECT_GROUP_SELECTION, groupAd);
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS, filterStatus);
        intent.putExtra(TopAdsExtraConstant.EXTRA_ITEM_SELECTED_POSITION, selectedPosition);
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SHOW_STATUS, isStatusShown());
        startActivityForResult(intent, REQUEST_CODE_AD_FILTER);
    }

    @Override
    public void onCreateAd() {
        TopAdsKeywordNewChooseGroupActivity.start(this, getActivity(), REQUEST_CODE_AD_ADD, isPositive());
    }

    @Override
    public void onItemClicked(Ad ad) {
        startActivityForResult(TopAdsKeywordDetailActivity.createInstance(getActivity(), (KeywordAd) ad, ""), REQUEST_CODE_AD_CHANGE);
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

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }
  
    public void onEmptyContentItemTextClicked() {
        // Do nothing
    }

    @Override
    public void onEmptyButtonClicked() {
        onCreateAd();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(TopAdsExtraConstant.EXTRA_FILTER_CURRECT_GROUP_SELECTION, groupAd);
        outState.putInt(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS, filterStatus);
    }

    @Override
    protected void showViewList(@NonNull List list) {
        super.showViewList(list);
        hasData = true;
        if (keywordAdListener != null) {
            keywordAdListener.validateMenuItem();
        }
    }

    @Override
    public void onLoadSearchError() {
        super.onLoadSearchError();
        hasData = false;
        if (keywordAdListener != null) {
            keywordAdListener.validateMenuItem();
        }
    }

    @Override
    protected void showViewEmptyList() {
        super.showViewEmptyList();
        if (keywordAdListener != null) {
            keywordAdListener.validateMenuItem();
        }
    }

    public boolean hasDataFromServer() {
        return hasData;
    }

}