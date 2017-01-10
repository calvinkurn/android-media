package com.tokopedia.seller.topads.view.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.model.data.TotalAd;
import com.tokopedia.seller.topads.presenter.TopAdsDashboardProductPresenterImpl;
import com.tokopedia.seller.topads.view.activity.TopAdsGroupAdListActivity;
import com.tokopedia.seller.topads.view.activity.TopAdsProductAdListActivity;
import com.tokopedia.seller.topads.view.listener.TopAdsDashboardProductFragmentListener;
import com.tokopedia.seller.topads.view.widget.TopAdsLabelView;

import butterknife.BindView;
import butterknife.OnClick;

public class TopAdsDashboardProductFragment extends TopAdsDashboardFragment<TopAdsDashboardProductPresenterImpl> implements TopAdsDashboardProductFragmentListener {

    @BindView(R2.id.label_view_group_summary)
    TopAdsLabelView groupSummaryLabelView;

    @BindView(R2.id.label_view_item_summary)
    TopAdsLabelView itemSummaryLabelView;

    int totalProductAd;

    public static TopAdsDashboardProductFragment createInstance() {
        TopAdsDashboardProductFragment fragment = new TopAdsDashboardProductFragment();
        return fragment;
    }

    @Override
    protected void initialPresenter() {
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
    }

    @Override
    protected void initialVar() {
        super.initialVar();
        totalProductAd = Integer.MIN_VALUE;
    }

    protected void loadData() {
        super.loadData();
        presenter.populateTotalAd();
    }

    @Override
    public void onTotalAdLoaded(@NonNull TotalAd totalAd) {
        groupSummaryLabelView.setContent(String.valueOf(totalAd.getTotalProductGroupAd()));
        itemSummaryLabelView.setContent(String.valueOf(totalAd.getTotalProductAd()));
        totalProductAd = totalAd.getTotalProductAd();
    }

    @Override
    public void onLoadTotalAdError(@NonNull Throwable throwable) {
        showNetworkError();
        hideLoading();
    }

    @OnClick(R2.id.label_view_group_summary)
    void onProductGroupClicked() {
        Intent intent = new Intent(getActivity(), TopAdsGroupAdListActivity.class);
        if (totalProductAd >= 0) {
            intent.putExtra(TopAdsExtraConstant.EXTRA_TOTAL_PRODUCT_ADS, totalProductAd);
        }
        startActivity(intent);
    }

    @OnClick(R2.id.label_view_item_summary)
    void onProductItemClicked() {
        Intent intent = new Intent(getActivity(), TopAdsProductAdListActivity.class);
        startActivity(intent);
    }
}