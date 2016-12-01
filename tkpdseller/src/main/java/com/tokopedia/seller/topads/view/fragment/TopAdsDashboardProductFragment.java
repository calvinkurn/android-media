package com.tokopedia.seller.topads.view.fragment;

import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.topads.presenter.TopAdsDashboardProductPresenterImpl;
import com.tokopedia.seller.topads.view.listener.TopAdsDashboardFragmentListener;

import java.util.Calendar;

import butterknife.Bind;

public class TopAdsDashboardProductFragment extends TopAdsDashboardFragment<TopAdsDashboardProductPresenterImpl> implements TopAdsDashboardFragmentListener {

    @Bind(R2.id.layout_top_ads_product_group_summary)
    View productGroupSummaryLayout;

    @Bind(R2.id.layout_top_ads_product_item_summary)
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
        presenter.populateSummary(startDate, endDate);
        presenter.populateDeposit();
        presenter.populateShopInfo();
    }
}