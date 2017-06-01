package com.tokopedia.seller.topads.keyword.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.lib.widget.DateLabelView;
import com.tokopedia.seller.topads.keyword.view.activity.TopAdsKeywordDetailActivity;
import com.tokopedia.seller.topads.keyword.view.model.BaseKeywordParam;
import com.tokopedia.seller.topads.keyword.view.model.KeywordAd;

/**
 * @author normansyahputa on 5/17/17.
 */

public class TopAdsKeywordListFragment extends TopAdsKeywordNegativeListFragment {

    private DateLabelView dateLabelView;

    public static Fragment createInstance() {
        return new TopAdsKeywordListFragment();
    }

    @Override
    protected String getScreenName() {
        return null;
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
    protected void loadData() {
        super.loadData();
        bindDate();
    }

    private void bindDate() {
        dateLabelView.setDate(startDate, endDate);
    }


    @Override
    protected void searchAd() {
        bindDate(); // set ui after date changed.
        super.searchAd();
    }

    @Override
    protected void searchAd(BaseKeywordParam baseKeywordParam) {
        topAdsKeywordListPresenter.fetchPositiveKeyword(
                baseKeywordParam
        );
    }

    protected boolean isPositive() {
        return true;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_keyword_positive_list;
    }

    @Override
    protected void goToDetail(KeywordAd keywordAd) {
        startActivity(TopAdsKeywordDetailActivity.createInstance(
                getActivity(), keywordAd, ""
        ));
    }
}
