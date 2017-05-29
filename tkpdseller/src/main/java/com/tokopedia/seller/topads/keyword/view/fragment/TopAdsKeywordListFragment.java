package com.tokopedia.seller.topads.keyword.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.utils.DateHeaderFormatter;
import com.tokopedia.seller.lib.datepicker.constant.DatePickerConstant;
import com.tokopedia.seller.lib.widget.DateLabelView;
import com.tokopedia.seller.reputation.view.helper.ReputationHeaderViewHelper;
import com.tokopedia.seller.topads.data.model.data.Ad;
import com.tokopedia.seller.topads.keyword.view.activity.TopAdsKeywordNewChooseGroupActivity;
import com.tokopedia.seller.topads.keyword.view.adapter.TopAdsKeywordAdapter;
import com.tokopedia.seller.topads.keyword.di.component.DaggerTopAdsKeywordComponent;
import com.tokopedia.seller.topads.keyword.di.module.TopAdsModule;
import com.tokopedia.seller.topads.keyword.view.activity.TopAdsKeywordFilterActivity;
import com.tokopedia.seller.topads.keyword.view.activity.TopAdsKeywordNewChooseGroupActivity;
import com.tokopedia.seller.topads.keyword.view.adapter.TopAdsKeywordAdapter;
import com.tokopedia.seller.topads.keyword.view.listener.TopAdsDashboardListener;
import com.tokopedia.seller.topads.keyword.view.model.BaseKeywordParam;
import com.tokopedia.seller.topads.keyword.view.presenter.TopAdsKeywordListPresenterImpl;
import com.tokopedia.seller.topads.view.adapter.TopAdsAdListAdapter;
import com.tokopedia.seller.topads.view.adapter.viewholder.TopAdsEmptyAdDataBinder;

import javax.inject.Inject;

/**
 * Created by normansyahputa on 5/17/17.
 */

public class TopAdsKeywordListFragment extends TopAdsBaseKeywordListFragment<TopAdsKeywordListPresenterImpl> {

    @Inject
    TopAdsKeywordListPresenterImpl topAdsKeywordListPresenter;
    private DateLabelView dateLabelView;
    private TopAdsDashboardListener keywordListListener;

    public static Fragment createInstance() {
        return new TopAdsKeywordListFragment();
    }

    @Override
    protected void initialPresenter() {
        super.initialPresenter();
        topAdsKeywordListPresenter.attachView(this);

        if (getActivity() != null && getActivity() instanceof TopAdsDashboardListener) {
            keywordListListener = (TopAdsDashboardListener) getActivity();
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        dateLabelView = (DateLabelView) view.findViewById(R.id.date_label_view);
        dateLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });
        return view;
    }

    @Override
    protected void fetchData() {
        bindDate(); // set ui after date changed.

        // reset searchview and filter
        if (keywordListListener != null) {
            keywordListListener.resetSearchView();
        }

        BaseKeywordParam baseKeywordParam
                = topAdsKeywordListPresenter.generateParam(keyword, page, true,
                startDate.getTime(), endDate.getTime());
        topAdsKeywordListPresenter.fetchNegativeKeyword(
                baseKeywordParam
        );
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    void loadDateFromPresenter() {
        super.loadDateFromPresenter();
        bindDate();
    }

    private void bindDate() {
        dateLabelView.setDate(startDate, endDate);
    }


    @Override
    protected void searchAd() {
        super.searchAd();
        fetchData();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_keyword_positive_list;
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
