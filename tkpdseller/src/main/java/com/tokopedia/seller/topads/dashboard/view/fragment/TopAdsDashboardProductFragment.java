package com.tokopedia.seller.topads.dashboard.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.seller.R;
import com.tokopedia.seller.lib.widget.LabelView;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.dashboard.data.model.data.TotalAd;
import com.tokopedia.seller.topads.keyword.view.activity.TopAdsKeywordListActivity;
import com.tokopedia.seller.topads.dashboard.view.activity.TopAdsGroupAdListActivity;
import com.tokopedia.seller.topads.dashboard.view.activity.TopAdsProductAdListActivity;
import com.tokopedia.seller.topads.dashboard.view.activity.TopAdsStatisticProductActivity;
import com.tokopedia.seller.topads.dashboard.view.listener.TopAdsDashboardProductFragmentListener;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDashboardProductPresenterImpl;

public class TopAdsDashboardProductFragment extends TopAdsDashboardFragment<TopAdsDashboardProductPresenterImpl> implements TopAdsDashboardProductFragmentListener {

    public static final int REQUEST_CODE_AD_STATUS = 2;
    public static final String GTM_KEYWORD = "GTM_KEYWORD";
    private LabelView groupSummaryLabelView;
    private LabelView itemSummaryLabelView;
    private LabelView keywordLabelView;

    private int totalProductAd;
    private int totalGroupAd;
    private boolean showKeyword;

    public static TopAdsDashboardProductFragment createInstance() {
        TopAdsDashboardProductFragment fragment = new TopAdsDashboardProductFragment();
        return fragment;
    }

    @Override
    protected void initialPresenter() {
        super.initialPresenter();
        presenter = new TopAdsDashboardProductPresenterImpl(getActivity());
        presenter.setTopAdsDashboardFragmentListener(this);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_product;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        groupSummaryLabelView = (LabelView) view.findViewById(R.id.label_view_group_summary);
        itemSummaryLabelView = (LabelView) view.findViewById(R.id.label_view_item_summary);
        keywordLabelView = (LabelView) view.findViewById(R.id.label_view_keyword);
        groupSummaryLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onProductGroupClicked();
            }
        });
        itemSummaryLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onProductItemClicked();
            }
        });
        keywordLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onKeywordLabelClicked();
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {
            showKeyword = TrackingUtils.getBoolean(AppEventTracking.GTM.SELLER_TOP_ADS_SHOW_KEYWORD);
        } else if (savedInstanceState.containsKey(GTM_KEYWORD)) {
            showKeyword = savedInstanceState.getBoolean(GTM_KEYWORD);
        }
        if (showKeyword) {
            keywordLabelView.setVisibility(View.VISIBLE);
        } else {
            keywordLabelView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initialVar() {
        super.initialVar();
        totalProductAd = Integer.MIN_VALUE;
    }

    @Override
    protected void setViewListener() {
        super.setViewListener();
    }

    public void loadData() {
        super.loadData();
        presenter.populateTotalAd();
    }

    @Override
    public void onTotalAdLoaded(@NonNull TotalAd totalAd) {
        groupSummaryLabelView.setContent(String.valueOf(totalAd.getTotalProductGroupAd()));
        itemSummaryLabelView.setContent(String.valueOf(totalAd.getTotalProductAd()));
        keywordLabelView.setContent(String.valueOf(totalAd.getTotalKeyword()));
        groupSummaryLabelView.setVisibleArrow(true);
        itemSummaryLabelView.setVisibleArrow(true);
        keywordLabelView.setVisibleArrow(true);
        totalProductAd = totalAd.getTotalProductAd();
        totalGroupAd = totalAd.getTotalProductGroupAd();
        onLoadDataSuccess();
        showShowCase();
    }

    // use for show case in activity
    public View getDepositView() {
        return getView().findViewById(R.id.view_group_deposit);
    }
    public View getCalendarView() {
        return getView().findViewById(R.id.date_label_view);
    }
    public View getStatisticView() {
        return getView().findViewById(R.id.view_group_statistic);
    }
    public ViewGroup getScrollView() {
        return (ViewGroup) getView().findViewById(R.id.scrollView);
    }

    @Override
    public void onLoadTotalAdError(@NonNull Throwable throwable) {
        showNetworkError();
        hideLoading();
    }

    private void onProductGroupClicked() {
        Intent intent = new Intent(getActivity(), TopAdsGroupAdListActivity.class);
        if (totalProductAd >= 0) {
            intent.putExtra(TopAdsExtraConstant.EXTRA_TOTAL_PRODUCT_ADS, totalProductAd);
        }
        startActivityForResult(intent, REQUEST_CODE_AD_STATUS);
    }

    private void onProductItemClicked() {
        Intent intent = new Intent(getActivity(), TopAdsProductAdListActivity.class);
        startActivityForResult(intent, REQUEST_CODE_AD_STATUS);
    }

    private void onKeywordLabelClicked() {
        Intent intent = new Intent(getActivity(), TopAdsKeywordListActivity.class);
        if (totalGroupAd >= 0) {
            intent.putExtra(TopAdsExtraConstant.EXTRA_TOTAL_GROUP_ADS, totalGroupAd);
        }
        startActivityForResult(intent, REQUEST_CODE_AD_STATUS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE_AD_STATUS && intent != null) {
            if (startDate == null || endDate == null) {
                return;
            }
            boolean adStatusChanged = intent.getBooleanExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, false);
            if (adStatusChanged) {
                loadData();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(GTM_KEYWORD, showKeyword);
    }

    @Override
    protected Class<?> getClassIntentStatistic() {
        return TopAdsStatisticProductActivity.class;
    }
}