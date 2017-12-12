package com.tokopedia.flight.orderlist.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.design.quickfilter.QuickFilterAdapter;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.flight.R;
import com.tokopedia.flight.orderlist.contract.FlightOrderListContract;
import com.tokopedia.flight.orderlist.di.FlightOrderComponent;
import com.tokopedia.flight.orderlist.presenter.FlightOrderListPresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * @author by zulfikarrahman on 11/28/17.
 */

public class FlightOrderListFragment extends BaseDaggerFragment implements FlightOrderListContract.View , QuickFilterAdapter.ActionListener {
    @Inject
    FlightOrderListPresenter presenter;
    private RecyclerView ordersRecyclerView;
    private RecyclerView filtersRecyclerView;
    private SwipeToRefresh swipeToRefresh;
    private QuickFilterAdapter filterAdapter;

    public static FlightOrderListFragment createInstance() {
        return new FlightOrderListFragment();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(FlightOrderComponent.class)
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_order_list, container, false);
        swipeToRefresh = view.findViewById(R.id.swipe_refresh_layout);
        filtersRecyclerView = view.findViewById(R.id.rv_filters);
        ordersRecyclerView = view.findViewById(R.id.rv_orders);
        filtersRecyclerView.setHasFixedSize(true);
        filtersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        filterAdapter = new QuickFilterAdapter();
        filtersRecyclerView.setAdapter(filterAdapter);
        filterAdapter.setListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        presenter.getInitialOrderData();
    }

    @Override
    public void showGetInitialOrderDataLoading() {
        swipeToRefresh.setRefreshing(true);
    }

    @Override
    public void hideGetInitialOrderDataLoading() {
        swipeToRefresh.setRefreshing(false);
    }

    @Override
    public void renderOrderStatus(List<QuickFilterItem> filterItems) {
        filterAdapter.addQuickFilterItems(filterItems);
    }

    @Override
    public void clearFilter() {

    }

    @Override
    public void selectFilter(String typeFilter) {

    }
}
