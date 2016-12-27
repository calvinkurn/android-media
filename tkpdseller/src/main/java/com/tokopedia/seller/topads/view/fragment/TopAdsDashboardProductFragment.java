package com.tokopedia.seller.topads.view.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.topads.model.data.TotalAd;
import com.tokopedia.seller.topads.presenter.TopAdsDashboardProductPresenterImpl;
import com.tokopedia.seller.topads.view.activity.TopAdsGroupListActivity;
import com.tokopedia.seller.topads.view.listener.TopAdsDashboardProductFragmentListener;

import butterknife.BindView;
import butterknife.OnClick;

public class TopAdsDashboardProductFragment extends TopAdsDashboardFragment<TopAdsDashboardProductPresenterImpl> implements TopAdsDashboardProductFragmentListener {

    @BindView(R2.id.layout_top_ads_product_group_summary)
    View productGroupSummaryLayout;

    @BindView(R2.id.layout_top_ads_product_item_summary)
    View productItemSummaryLayout;

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
        updateInfoText(productGroupSummaryLayout, R.id.text_view_title, String.valueOf(getString(R.string.label_top_ads_groups)));
        updateInfoText(productItemSummaryLayout, R.id.text_view_title, String.valueOf(getString(R.string.label_top_ads_items)));
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        presenter.populateSummary(startDate, endDate);
        presenter.populateDeposit();
        presenter.populateShopInfo();
        presenter.populateTotalAd();
    }

    @Override
    public void onTotalAdLoaded(@NonNull TotalAd totalAd) {
        updateInfoText(productGroupSummaryLayout, R.id.text_view_content, String.valueOf(totalAd.getTotalProductGroupAd()));
        updateInfoText(productItemSummaryLayout, R.id.text_view_content, String.valueOf(totalAd.getTotalProductAd()));
    }

    @Override
    public void onLoadTotalAdError(@NonNull Throwable throwable) {

    }

    @OnClick(R2.id.layout_top_ads_product_group_summary)
    void onProductGroupClicked() {
        Intent intent = new Intent(getActivity(), TopAdsGroupListActivity.class);
        startActivity(intent);
    }
}