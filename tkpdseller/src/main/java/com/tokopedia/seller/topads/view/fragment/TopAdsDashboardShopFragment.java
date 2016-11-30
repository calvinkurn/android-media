package com.tokopedia.seller.topads.view.fragment;

import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.presenter.TopAdsDashboardShopPresenterImpl;
import com.tokopedia.seller.topads.view.listener.TopAdsDashboardFragmentListener;

import java.util.Calendar;

public class TopAdsDashboardShopFragment extends TopAdsDashboardFragment<TopAdsDashboardShopPresenterImpl> implements TopAdsDashboardFragmentListener {

    public static TopAdsDashboardShopFragment createInstance() {
        TopAdsDashboardShopFragment fragment = new TopAdsDashboardShopFragment();
        return fragment;
    }

    @Override
    protected void initialPresenter() {
        presenter = new TopAdsDashboardShopPresenterImpl(getActivity());
        presenter.setTopAdsDashboardFragmentListener(this);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_store;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.add(Calendar.DAY_OF_YEAR, 3);
        presenter.populateSummary(startCalendar.getTime(), endCalendar.getTime());
        presenter.populateDeposit();
        presenter.populateShopInfo();
    }
}