package com.tokopedia.seller.topads.view.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.model.data.TotalAd;
import com.tokopedia.seller.topads.presenter.TopAdsDashboardProductPresenterImpl;
import com.tokopedia.seller.topads.view.activity.TopAdsGroupAdListActivity;
import com.tokopedia.seller.topads.view.activity.TopAdsProductAdListActivity;
import com.tokopedia.seller.topads.view.activity.TopAdsStatisticProductActivity;
import com.tokopedia.seller.topads.view.listener.TopAdsDashboardProductFragmentListener;
import com.tokopedia.seller.topads.view.widget.TopAdsLabelView;

public class TopAdsDashboardProductFragment extends TopAdsDashboardFragment<TopAdsDashboardProductPresenterImpl> implements TopAdsDashboardProductFragmentListener {

    TopAdsLabelView groupSummaryLabelView;
    TopAdsLabelView itemSummaryLabelView;

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
        groupSummaryLabelView = (TopAdsLabelView) view.findViewById(R.id.label_view_group_summary);
        itemSummaryLabelView = (TopAdsLabelView) view.findViewById(R.id.label_view_item_summary);
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
        groupSummaryLabelView.setVisibleArrow(true);
        itemSummaryLabelView.setVisibleArrow(true);
        totalProductAd = totalAd.getTotalProductAd();
        onLoadDataSuccess();
    }

    @Override
    public void onLoadTotalAdError(@NonNull Throwable throwable) {
        showNetworkError();
        hideLoading();
    }

    void onProductGroupClicked() {
        Intent intent = new Intent(getActivity(), TopAdsGroupAdListActivity.class);
        if (totalProductAd >= 0) {
            intent.putExtra(TopAdsExtraConstant.EXTRA_TOTAL_PRODUCT_ADS, totalProductAd);
        }
        startActivity(intent);
    }

    void onProductItemClicked() {
        Intent intent = new Intent(getActivity(), TopAdsProductAdListActivity.class);
        startActivity(intent);
    }

    @Override
    protected Class<?> getClassIntentStatistic() {
        return TopAdsStatisticProductActivity.class;
    }
}