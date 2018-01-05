package com.tokopedia.flight.orderlist.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.design.quickfilter.QuickFilterAdapter;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.flight.R;
import com.tokopedia.flight.detail.view.activity.FlightDetailOrderActivity;
import com.tokopedia.flight.orderlist.contract.FlightOrderListContract;
import com.tokopedia.flight.orderlist.di.FlightOrderComponent;
import com.tokopedia.flight.orderlist.presenter.FlightOrderListPresenter;
import com.tokopedia.flight.orderlist.view.adapter.FlightOrderAdapter;
import com.tokopedia.flight.orderlist.view.adapter.FlightOrderAdapterTypeFactory;
import com.tokopedia.flight.orderlist.view.adapter.FlightOrderTypeFactory;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderBaseViewModel;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderDetailPassData;

import java.util.List;

import javax.inject.Inject;

;

/**
 * @author by zulfikarrahman on 11/28/17.
 */

public class FlightOrderListFragment extends BaseListFragment<Visitable, FlightOrderTypeFactory>
        implements FlightOrderListContract.View,
        QuickFilterAdapter.ActionListener,
        FlightOrderAdapter.OnAdapterInteractionListener {
    public static final int PER_PAGE = 10;
    @Inject
    FlightOrderListPresenter presenter;
    private QuickFilterAdapter filterAdapter;

    private String selectedFilter;

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
        RecyclerView filtersRecyclerView = view.findViewById(R.id.rv_filters);

        filtersRecyclerView.setHasFixedSize(true);
        filtersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        filterAdapter = new QuickFilterAdapter();
        filtersRecyclerView.setAdapter(filterAdapter);
        filterAdapter.setListener(this);
        return view;
    }

    @Nullable
    @Override
    public SwipeRefreshLayout getSwipeRefreshLayout(View view) {
        return view.findViewById(R.id.swipe_refresh_layout);
    }

    @Override
    public RecyclerView getRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.rv_orders);
        recyclerView.setHasFixedSize(true);
        return recyclerView;
    }

    @NonNull
    @Override
    protected BaseListAdapter<Visitable, FlightOrderTypeFactory> createAdapterInstance() {
        FlightOrderTypeFactory flightOrderTypeFactory = new FlightOrderAdapterTypeFactory(this);
        return new FlightOrderAdapter(flightOrderTypeFactory);
    }

    @Override
    public void loadData(int page) {
        presenter.attachView(this);
        presenter.loadData(selectedFilter, page, PER_PAGE);
    }

    @Override
    protected FlightOrderTypeFactory getAdapterTypeFactory() {
        return new FlightOrderAdapterTypeFactory(this);
    }

    @Override
    public void renderOrderStatus(List<QuickFilterItem> filterItems) {
        filterAdapter.addQuickFilterItems(filterItems);
    }

    @Override
    public String getSelectedFilter() {
        return String.valueOf(selectedFilter);
    }

    @Override
    public void clearFilter() {
        selectedFilter = "";
        showSwipeLoading();
        loadInitialData();
    }

    @Override
    public void selectFilter(String typeFilter) {
        selectedFilter = typeFilter;
        showSwipeLoading();
        loadInitialData();
    }

    @Override
    public void onDetailOrderClicked(FlightOrderDetailPassData viewModel) {
        startActivity(FlightDetailOrderActivity.createIntent(getActivity(), viewModel));
    }

    @Override
    public void onDetailOrderClicked(String orderId) {
        FlightOrderDetailPassData passData = new FlightOrderDetailPassData();
        passData.setOrderId(orderId);
        startActivity(FlightDetailOrderActivity.createIntent(getActivity(), passData));
    }

    @Override
    public void onHelpOptionClicked(String orderId) {

    }

    @Override
    public void onDestroyView() {
        presenter.onDestroyView();
        super.onDestroyView();
    }

    @Override
    public void onReBookingClicked(FlightOrderBaseViewModel item) {
        getActivity().finish();
    }

    @Override
    public void onItemClicked(Visitable visitable) {

    }
}
