package com.tokopedia.topads.keyword.view.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.fragment.TopAdsFilterListFragment;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.view.activity.TopAdsGroupNewPromoActivity;
import com.tokopedia.topads.dashboard.view.adapter.viewholder.TopAdsEmptyAdDataBinder;
import com.tokopedia.topads.dashboard.view.adapter.viewholder.TopAdsEmptyKeywordAdDataBinder;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsAdListFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsGroupNewPromoFragment;
import com.tokopedia.topads.keyword.constant.KeywordStatusTypeDef;
import com.tokopedia.topads.keyword.di.component.DaggerTopAdsKeywordComponent;
import com.tokopedia.topads.keyword.di.module.TopAdsKeywordModule;
import com.tokopedia.topads.keyword.view.activity.TopAdsKeywordDetailActivity;
import com.tokopedia.topads.keyword.view.activity.TopAdsKeywordFilterActivity;
import com.tokopedia.topads.keyword.view.activity.TopAdsKeywordNewChooseGroupActivity;
import com.tokopedia.topads.keyword.view.adapter.TopAdsKeywordAdapter;
import com.tokopedia.topads.keyword.view.model.BaseKeywordParam;
import com.tokopedia.topads.keyword.view.model.KeywordAd;
import com.tokopedia.topads.keyword.view.presenter.TopAdsKeywordListPresenterImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @author normansyahputa on 5/17/17.
 */
public class TopAdsKeywordListFragment extends TopAdsAdListFragment<TopAdsKeywordListPresenterImpl, KeywordAd>
        implements TopAdsEmptyAdDataBinder.Callback {

    protected int filterStatus;
    protected GroupAd groupAd;
    protected int selectedPosition;
    @Inject
    TopAdsKeywordListPresenterImpl topAdsKeywordListPresenter;
    private boolean hasData;
    private GroupTopAdsListener groupTopAdsListener;

    public static Fragment createInstance() {
        return new TopAdsKeywordListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public void onAttachListener(Context context) {
        super.onAttachListener(context);
        if (context != null && context instanceof GroupTopAdsListener) {
            groupTopAdsListener = (GroupTopAdsListener) context;
        }
    }

    @Override
    protected void initInjector() {
        DaggerTopAdsKeywordComponent.builder()
                .topAdsKeywordModule(new TopAdsKeywordModule())
                .topAdsComponent(getTopAdsComponent())
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
        TopAdsEmptyAdDataBinder emptyGroupAdsDataBinder = new TopAdsEmptyKeywordAdDataBinder(adapter);
        emptyGroupAdsDataBinder.setEmptyTitleText(getString(R.string.top_ads_keyword_your_keyword_empty));
        emptyGroupAdsDataBinder.setEmptyContentText(getString(R.string.top_ads_keyword_please_use));
        emptyGroupAdsDataBinder.setEmptyButtonItemText(getString(R.string.top_ads_keyword_add_keyword));
        emptyGroupAdsDataBinder.setCallback(this);
        hasData = false;
        return emptyGroupAdsDataBinder;
    }

    @Override
    protected void searchForPage(int page) {
        BaseKeywordParam baseKeywordParam = topAdsKeywordListPresenter.generateParam(keyword, getCurrentPage(),
                isPositive(), startDate.getTime(), endDate.getTime());
        if (groupAd != null) {
            baseKeywordParam.groupId = Integer.parseInt(groupAd.getId());
        }
        baseKeywordParam.keywordStatus = filterStatus;
        searchData(baseKeywordParam);
    }

    protected void searchData(BaseKeywordParam baseKeywordParam) {
        topAdsKeywordListPresenter.fetchKeyword(baseKeywordParam);
    }

    @Override
    protected BaseListAdapter<KeywordAd> getNewAdapter() {
        return new TopAdsKeywordAdapter<>();
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
            selectedPosition = intent.getIntExtra(TopAdsFilterListFragment.EXTRA_ITEM_SELECTED_POSITION, 0);
            resetPageAndSearch();
        }

        if (requestCode == TopAdsGroupNewPromoFragment.REQUEST_CODE_AD_STATUS) {
            if (resultCode == Activity.RESULT_OK) {
                // top ads new groups/edit existing group/promo not in group has been success
                boolean adStatusChanged = intent.getBooleanExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, false);
                if (adStatusChanged) {
                    if (groupTopAdsListener != null) {
                        groupTopAdsListener.setGroupTopAdsSize(1);// force to notify that group already added
                    }
                }
            }
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
        intent.putExtra(TopAdsFilterListFragment.EXTRA_ITEM_SELECTED_POSITION, selectedPosition);
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SHOW_STATUS, isStatusShown());
        startActivityForResult(intent, REQUEST_CODE_AD_FILTER);
    }

    @Override
    public void onCreateAd() {
        UnifyTracking.eventTopAdsProductNewPromoKeywordPositif();
        TopAdsKeywordNewChooseGroupActivity.start(this, getActivity(), REQUEST_CODE_AD_ADD, isPositive());
    }

    @Override
    public void onItemClicked(KeywordAd ad) {
        startActivityForResult(TopAdsKeywordDetailActivity.createInstance(getActivity(), ad, ad.getId()), REQUEST_CODE_AD_CHANGE);
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
  
    public void onEmptyContentItemTextClicked() {
        // Do nothing
    }

    @Override
    public void onEmptyButtonClicked() {
        if (groupTopAdsListener != null && groupTopAdsListener.getGroupTopAdsSize() <= 0) {
            showExitDialog();
        } else {
            onCreateAd();
        }
    }

    private void showExitDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getActivity()
                , R.style.AppCompatAlertDialogStyle);
        myAlertDialog.setMessage(getString(R.string.top_ads_keyword_add_group_promo_desc));

        myAlertDialog.setPositiveButton(getString(R.string.top_ads_keyword_add_group_promo_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity(), TopAdsGroupNewPromoActivity.class);
                TopAdsKeywordListFragment.this.startActivityForResult(intent, TopAdsGroupNewPromoFragment.REQUEST_CODE_AD_STATUS);
            }
        });

        myAlertDialog.setNegativeButton(getString(R.string.top_ads_keyword_add_group_promo_no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        Dialog dialog = myAlertDialog.create();
        dialog.setTitle(R.string.top_ads_keyword_add_group_promo);
        dialog.show();
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
    }

    @Override
    public void onLoadSearchError(Throwable t) {
        super.onLoadSearchError(t);
        hasData = false;
    }

    public boolean hasDataFromServer() {
        return hasData;
    }

    public RecyclerView getRecyclerView(){
        return recyclerView;
    }

    public interface GroupTopAdsListener {
        int getGroupTopAdsSize();

        void setGroupTopAdsSize(int size);
    }

}