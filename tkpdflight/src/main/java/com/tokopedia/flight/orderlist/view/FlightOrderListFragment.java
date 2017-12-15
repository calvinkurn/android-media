package com.tokopedia.flight.orderlist.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerviewListener;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.utils.snackbar.NetworkErrorHelper;
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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

;

/**
 * @author by zulfikarrahman on 11/28/17.
 */

public class FlightOrderListFragment extends BaseDaggerFragment implements FlightOrderListContract.View, QuickFilterAdapter.ActionListener, FlightOrderAdapter.OnAdapterInteractionListener {
    @Inject
    FlightOrderListPresenter presenter;
    private RecyclerView ordersRecyclerView;
    private RecyclerView filtersRecyclerView;
    private SwipeToRefresh swipeToRefresh;
    private QuickFilterAdapter filterAdapter;
    private FlightOrderAdapter flightOrderAdapter;

    private EndlessRecyclerviewListener endlessRecyclerviewListener;
    private boolean isLoadMore;
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
        swipeToRefresh = view.findViewById(R.id.swipe_refresh_layout);
        filtersRecyclerView = view.findViewById(R.id.rv_filters);
        ordersRecyclerView = view.findViewById(R.id.rv_orders);
        ordersRecyclerView.setHasFixedSize(true);
        ordersRecyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager orderLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        ordersRecyclerView.setLayoutManager(orderLayoutManager);
        endlessRecyclerviewListener = new EndlessRecyclerviewListener(orderLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (!isLoadMore) {
                    isLoadMore = true;
                    presenter.onOrderLoadMore(selectedFilter, page);
                }
            }
        };
        ordersRecyclerView.addOnScrollListener(endlessRecyclerviewListener);
        FlightOrderTypeFactory flightOrderTypeFactory = new FlightOrderAdapterTypeFactory(this);
        flightOrderAdapter = new FlightOrderAdapter(flightOrderTypeFactory, new ArrayList<Visitable>());
        ordersRecyclerView.setAdapter(flightOrderAdapter);

        filtersRecyclerView.setHasFixedSize(true);
        filtersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        filterAdapter = new QuickFilterAdapter();
        filtersRecyclerView.setNestedScrollingEnabled(false);
        filtersRecyclerView.setAdapter(filterAdapter);
        filterAdapter.setListener(this);
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.onSwipeRefresh();
            }
        });
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
    public void renderOrders(List<Visitable> visitables) {
        flightOrderAdapter.clearData();
        flightOrderAdapter.addElement(visitables);
    }

    @Override
    public void showLoadMoreLoading() {
        flightOrderAdapter.showLoading();
    }

    @Override
    public void hideLoadMoreLoading() {
        flightOrderAdapter.hideLoading();
    }

    @Override
    public void renderAddMoreData(List<Visitable> visitables) {
        flightOrderAdapter.addMoreData(visitables);
    }

    @Override
    public void setLoadMoreStatusToFalse() {
        isLoadMore = false;
    }

    @Override
    public void showEmptyView() {
        flightOrderAdapter.clearData();
        flightOrderAdapter.addElement(new EmptyModel());
    }

    @Override
    public String getSelectedFilter() {
        return String.valueOf(selectedFilter);
    }

    @Override
    public void showErrorGetOrderOnFilterChanged(String message) {
        NetworkErrorHelper.showEmptyState(
                getActivity(), getView(), message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.onFilterSelected();
                    }
                }
        );
    }

    @Override
    public void showErrorGetInitialOrders(String message) {
        NetworkErrorHelper.showEmptyState(
                getActivity(), getView(), message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.getInitialOrderData();
                    }
                }
        );
    }

    @Override
    public void disableSwipeRefresh() {
        swipeToRefresh.setEnabled(false);
    }

    @Override
    public void enableSwipeRefresh() {
        swipeToRefresh.setEnabled(true);
    }

    @Override
    public void clearFilter() {
        selectedFilter = "";
        presenter.getInitialOrderData();
    }

    @Override
    public void selectFilter(String typeFilter) {
        selectedFilter = typeFilter;
        presenter.onFilterSelected();
        endlessRecyclerviewListener.resetState();
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
}
