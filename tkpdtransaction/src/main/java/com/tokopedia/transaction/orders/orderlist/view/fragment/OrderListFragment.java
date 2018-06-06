package com.tokopedia.transaction.orders.orderlist.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.orders.orderlist.data.Order;
import com.tokopedia.transaction.orders.orderlist.data.OrderCategory;
import com.tokopedia.transaction.orders.orderlist.di.OrderListComponent;
import com.tokopedia.transaction.orders.orderlist.view.adapter.OrderListAdapter;
import com.tokopedia.transaction.orders.orderlist.view.presenter.OrderListContract;
import com.tokopedia.transaction.orders.orderlist.view.presenter.OrderListPresenterImpl;
import com.tokopedia.transaction.orders.orderlist.di.DaggerOrderListComponent;
import com.tokopedia.transaction.purchase.interactor.TxOrderNetInteractor;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;



public class OrderListFragment extends BasePresenterFragment<OrderListContract.Presenter> implements
        RefreshHandler.OnRefreshHandlerListener, OrderListContract.View, OrderListAdapter.OnMenuItemListener {

    private static final java.lang.String ORDER_CATEGORY = "orderCategory";
    OrderListComponent orderListComponent;
    RecyclerView recyclerView;
    SwipeToRefresh swipeToRefresh;
    LinearLayout emptyLayout;
    OrderListAdapter orderListAdapter;
    private RefreshHandler refreshHandler;
    private boolean isLoading = false;
    private int page_num = 1;
    int totalItemCount;
    private int visibleThreshold = 2;
    private int lastVisibleItem;

    @Inject
    OrderListPresenterImpl presenter;

    private boolean hasRecyclerListener = false;

    private ArrayList<Order> mOrderDataList;
    private OrderCategory mOrderCategory;

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }


    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        initInjector();
        presenter.attachView(this);
    }

    protected void initInjector() {
        orderListComponent = DaggerOrderListComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build();
        orderListComponent.inject(this);
    }

    @Override
    protected void initialListener(Activity activity) {
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        int category = arguments.getInt(ORDER_CATEGORY);
        Log.e("sandeep","category ="+category);
        switch (category) {
            case 0:
                mOrderCategory = OrderCategory.ALL;
                break;
            case 1:
                mOrderCategory = OrderCategory.GOLD;
                break;
            case 2:
                mOrderCategory = OrderCategory.DIGITAL;
                break;
            case 3:
                mOrderCategory = OrderCategory.MARKETPLACE;
                break;
            case 4:
                mOrderCategory = OrderCategory.RIDE;
                break;
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_transaction_list_order_module;
    }

    @Override
    protected void initView(View view) {
        recyclerView = view.findViewById(R.id.order_list_rv);
        swipeToRefresh = view.findViewById(R.id.swipe_refresh_layout);
        emptyLayout = view.findViewById(R.id.empty_view);
    }

    @Override
    protected void setViewListener() {
        refreshHandler = new RefreshHandler(getActivity(), getView(), this);
        refreshHandler.setPullEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(orderListAdapter);
    }

    private void addRecyclerListener() {
        hasRecyclerListener = true;
        recyclerView.addOnScrollListener(scrollListener);
    }

    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (!isLoading && hasRecyclerListener) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (linearLayoutManager != null) {
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        onLoadMore();
                    }
                }
            }
        }
    };

    @Override
    public void onRefresh(View view) {
        page_num = 1;
        isLoading = true;
        if (orderListAdapter.getItemCount() != 0) {
            mOrderDataList.clear();

        }
        presenter.getAllOrderData(getActivity(), mOrderCategory, TxOrderNetInteractor.TypeRequest.INITIAL, page_num);
    }

    void onLoadMore() {
        page_num++;
        mOrderDataList.add(null);
        orderListAdapter.addItemAtLast(null);
        if (!isLoading) {
            isLoading = true;
            presenter.getAllOrderData(getActivity(), mOrderCategory, TxOrderNetInteractor.TypeRequest.LOAD_MORE, page_num);
        }
    }

    @Override
    public void removeProgressBarView() {
        isLoading = false;
        refreshHandler.finishRefresh();
        refreshHandler.setPullEnabled(true);
        if (mOrderDataList != null && mOrderDataList.size() > 0) {
            mOrderDataList.remove(mOrderDataList.size() - 1);
            orderListAdapter.removeItemAtLast(mOrderDataList.size());
        }
    }

    @Override
    public void unregisterScrollListener() {
        hasRecyclerListener = false;
    }

    @Override
    public void showErrorNetwork(String errorMessage) {
        NetworkErrorHelper.showEmptyState(
                getActivity(), getView(),
                getString(R.string.label_title_error_no_connection_initial_cart_data),
                getString(R.string.label_transaction_error_message_try_again),
                getString(R.string.label_title_button_retry), 0,
                getEditShipmentRetryListener()
        );
    }

    @Override
    public void renderEmptyList(int typeRequest) {
        if(typeRequest == TxOrderNetInteractor.TypeRequest.INITIAL) {
            swipeToRefresh.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        }
    }

    private NetworkErrorHelper.RetryClickedListener getEditShipmentRetryListener() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.getAllOrderData(getActivity(), mOrderCategory, TxOrderNetInteractor.TypeRequest.INITIAL, page_num);
            }
        };
    }

    @Override
    protected void initialVar() {
        orderListAdapter = new OrderListAdapter(getActivity(), this);
    }

    @Override
    protected void setActionVar() {
        initialData();
    }

    private void initialData() {
        if (!isLoading && getActivity() != null
                && (orderListAdapter == null || orderListAdapter.getItemCount() == 0)) {
            refreshHandler.startRefresh();
        }

    }

    @Override
    public void showProcessGetData(OrderCategory orderCategory) {
        switch (orderCategory) {
            case DIGITAL:
                if (!refreshHandler.isRefreshing()) {
                    refreshHandler.setRefreshing(true);
                    refreshHandler.setPullEnabled(false);
                }
                break;
            case ALL:
                break;
            case MARKETPLACE:
                if (!refreshHandler.isRefreshing()) {
                    refreshHandler.setRefreshing(true);
                    refreshHandler.setPullEnabled(false);
                }
                break;
        }
    }

    @Override
    public void renderDataList(List<Order> orderDataList) {
        refreshHandler.finishRefresh();
        refreshHandler.setPullEnabled(true);
        if (mOrderDataList == null) {
            mOrderDataList = new ArrayList<>(orderDataList);
            orderListAdapter.addAll(mOrderDataList);
            orderListAdapter.notifyDataSetChanged();
        } else if (mOrderDataList.size() == 0) {
            mOrderDataList.addAll(orderDataList);
            orderListAdapter.addAll(orderDataList);
            orderListAdapter.notifyDataSetChanged();
        } else {
            int prevSize = mOrderDataList.size();
            mOrderDataList.addAll(orderDataList);
            orderListAdapter.addAll(mOrderDataList);
            orderListAdapter.notifyItemRangeInserted(prevSize, mOrderDataList.size());
        }
        if (!hasRecyclerListener) {
            addRecyclerListener();
        }

        swipeToRefresh.setVisibility(View.VISIBLE);
        emptyLayout.setVisibility(View.GONE);
    }

    @Override
    public void showFailedResetData(String message) {

    }

    @Override
    public void showNoConnectionResetData(String message) {

    }

    @Override
    public void showEmptyData(int typeRequest) {

    }


    @Override
    public void startUri(String uri) {
        if (!uri.equals(""))
            TransactionPurchaseRouter.startWebViewActivity(getActivity(), uri);
    }
}

