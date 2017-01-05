package com.tokopedia.seller.topads.view.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.model.data.TotalAd;
import com.tokopedia.seller.topads.presenter.TopAdsDashboardProductPresenterImpl;
import com.tokopedia.seller.topads.view.activity.TopAdsGroupAdListActivity;
import com.tokopedia.seller.topads.view.activity.TopAdsProductAdListActivity;
import com.tokopedia.seller.topads.view.listener.TopAdsDashboardProductFragmentListener;
import com.tokopedia.seller.topads.view.widget.TopAdsLabelView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class TopAdsDashboardProductFragment extends TopAdsDashboardFragment<TopAdsDashboardProductPresenterImpl> implements TopAdsDashboardProductFragmentListener {

    @BindView(R2.id.label_view_group_summary)
    TopAdsLabelView groupSummaryLabelView;

    @BindView(R2.id.label_view_item_summary)
    TopAdsLabelView itemSummaryLabelView;

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

    protected void loadData() {
        super.loadData();
        presenter.populateTotalAd();
    }

    @Override
    public void onTotalAdLoaded(@NonNull TotalAd totalAd) {
        groupSummaryLabelView.setValue(String.valueOf(totalAd.getTotalProductGroupAd()));
        itemSummaryLabelView.setValue(String.valueOf(totalAd.getTotalProductAd()));
    }

    @Override
    public void onLoadTotalAdError(@NonNull Throwable throwable) {

    }

    @OnClick(R2.id.label_view_group_summary)
    void onProductGroupClicked() {
        Intent intent = new Intent(getActivity(), TopAdsGroupAdListActivity.class);
        intent.putExtra(TopAdsNetworkConstant.PARAM_START_DATE, new SimpleDateFormat(TopAdsConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(startDate));
        intent.putExtra(TopAdsNetworkConstant.PARAM_END_DATE, new SimpleDateFormat(TopAdsConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(endDate));
        startActivity(intent);
    }

    @OnClick(R2.id.label_view_item_summary)
    void onProductItemClicked() {
        Intent intent = new Intent(getActivity(), TopAdsProductAdListActivity.class);
        intent.putExtra(TopAdsNetworkConstant.PARAM_START_DATE, new SimpleDateFormat(TopAdsConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(startDate));
        intent.putExtra(TopAdsNetworkConstant.PARAM_END_DATE, new SimpleDateFormat(TopAdsConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(endDate));
        startActivity(intent);
    }
}