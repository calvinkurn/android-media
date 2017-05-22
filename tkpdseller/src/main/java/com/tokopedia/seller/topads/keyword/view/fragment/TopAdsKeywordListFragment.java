package com.tokopedia.seller.topads.keyword.view.fragment;

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
import com.tokopedia.seller.reputation.view.helper.ReputationHeaderViewHelper;
import com.tokopedia.seller.topads.data.model.data.Ad;
import com.tokopedia.seller.topads.keyword.view.adapter.TopAdsKeywordAdapter;
import com.tokopedia.seller.topads.keyword.view.di.component.DaggerTopAdsKeywordComponent;
import com.tokopedia.seller.topads.keyword.view.di.module.TopAdsModule;
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
    private DateHeaderFormatter dateHeaderFormatter;
    private ReputationHeaderViewHelper reputationViewHelper;
    private RelativeLayout widgetHeaderReputaitonContainer;

    public static Fragment createInstance() {
        return new TopAdsKeywordListFragment();
    }

    @Override
    protected void initialPresenter() {
        super.initialPresenter();
        topAdsKeywordListPresenter.attachView(this);

        dateHeaderFormatter = new DateHeaderFormatter(
                getResources().getStringArray(R.array.month_names_abrev)
        );

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        widgetHeaderReputaitonContainer = (RelativeLayout) view.findViewById(R.id.widget_header_reputation_container);
        reputationViewHelper = new ReputationHeaderViewHelper(view);
        widgetHeaderReputaitonContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reputationViewHelper.onClick(TopAdsKeywordListFragment.this, false);
            }
        });
        return view;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    void loadDateFromPresenter() {
        super.loadDateFromPresenter();
        reputationViewHelper.bindDate(
                dateHeaderFormatter,
                startDate.getTime(),
                endDate.getTime(),
                DatePickerConstant.SELECTION_TYPE_CUSTOM_DATE,
                DatePickerConstant.SELECTION_TYPE_PERIOD_DATE
        );
    }

    @Override
    protected void searchAd() {
        super.searchAd();
        topAdsKeywordListPresenter.fetchPositiveKeyword(
                topAdsKeywordListPresenter.generateParam(getActivity(), keyword, page, true)
        );
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_list_with_date;
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
