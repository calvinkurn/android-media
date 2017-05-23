package com.tokopedia.seller.topads.view.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.lib.widget.LabelView;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.data.model.data.TotalAd;
import com.tokopedia.seller.topads.keyword.view.activity.TopAdsKeywordListActivity;
import com.tokopedia.seller.topads.view.activity.TopAdsGroupAdListActivity;
import com.tokopedia.seller.topads.view.activity.TopAdsProductAdListActivity;
import com.tokopedia.seller.topads.view.activity.TopAdsStatisticProductActivity;
import com.tokopedia.seller.topads.view.listener.TopAdsDashboardProductFragmentListener;
import com.tokopedia.seller.topads.view.presenter.TopAdsDashboardProductPresenterImpl;

public class TopAdsDashboardProductFragment extends TopAdsDashboardFragment<TopAdsDashboardProductPresenterImpl> implements TopAdsDashboardProductFragmentListener {

    public static final int REQUEST_CODE_AD_STATUS = 2;
    private LabelView groupSummaryLabelView;
    private LabelView itemSummaryLabelView;
    private LabelView keywordLabelView;

    int totalProductAd;

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
        onLoadDataSuccess();
        showShowCase();
    }

    // use for show case in activity
    public View getDepositView() {
        return getView().findViewById(R.id.view_group_deposit);
    }
    public View getCalendarView() {
        return getView().findViewById(R.id.layout_date);
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
        startActivityForResult(intent, REQUEST_CODE_AD_STATUS);
    }

    boolean adStatusChanged = false;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_CODE_AD_STATUS && intent != null) {
            adStatusChanged = intent.getBooleanExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, false);
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adStatusChanged) {
            loadData();
            adStatusChanged = false;
        }
    }

    @Override
    protected Class<?> getClassIntentStatistic() {
        return TopAdsStatisticProductActivity.class;
    }
}