package com.tokopedia.seller.selling.view.fragment;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Spinner;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.session.baseFragment.BaseFragment;
import com.tokopedia.core.util.AppWidgetUtil;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.seller.selling.model.orderShipping.OrderShippingList;
import com.tokopedia.seller.selling.presenter.NewOrder;
import com.tokopedia.seller.selling.presenter.NewOrderImpl;
import com.tokopedia.seller.selling.presenter.NewOrderView;
import com.tokopedia.seller.selling.presenter.adapter.BaseSellingAdapter;
import com.tokopedia.seller.selling.view.viewHolder.BaseSellingViewHolder;
import com.tokopedia.seller.selling.view.viewHolder.OrderViewHolder;

import java.util.List;

/**
 * Created by Toped10 on 7/28/2016.
 */
public class FragmentSellingNewOrder extends BaseFragment<NewOrder> implements NewOrderView {

    public static final int PROCESS_ORDER = 1;
    private boolean isRefresh = false;
    private boolean inhibit_spinner_deadline = true;
    private boolean shouldRefreshList = false;

    public static FragmentSellingNewOrder createInstance() {
        return new FragmentSellingNewOrder();
    }

    RecyclerView list;
    FloatingActionButton fab;
    View mainView;
    SearchView search;
    Spinner deadline;
    View filterLayout;

    @SuppressWarnings("all")
    private BottomSheetDialog bottomSheetDialog;
    private RefreshHandler refresh;
    private PagingHandler page;
    private BaseSellingAdapter adapter;

    public FragmentSellingNewOrder() {
    }

    @Override
    public int getFragmentId() {
        return 0;
    }

    @Override
    public void ariseRetry(int type, Object... data) {

    }

    @Override
    public void setData(int type, Bundle data) {

    }

    @Override
    public void onNetworkError(int type, Object... data) {

    }

    @Override
    public void onMessageError(int type, Object... data) {

    }

    @Override
    public void initHandlerAndAdapter() {
        setRetainInstance(true);
        page = new PagingHandler();
        adapter = new BaseSellingAdapter<OrderShippingList, OrderViewHolder>(OrderShippingList.class, getActivity(), R.layout.selling_order_list_item, OrderViewHolder.class) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, OrderShippingList model, int position) {
                viewHolder.bindDataModel(getActivity(), model);
                viewHolder.setOnItemClickListener(new BaseSellingViewHolder.OnItemClickListener() {
                    @Override
                    public void onItemClicked(int position) {
                        UnifyTracking.eventNewOrderDetail();
                        if (adapter.isLoading()) {
                            getPaging().setPage(getPaging().getPage() - 1);
                            presenter.finishConnection();
                        }
                        presenter.moveToDetail(position);
                    }

                    @Override
                    public void onLongClicked(int position) {

                    }
                });
            }

            @Override
            protected OrderViewHolder getViewHolder(int mModelLayout, ViewGroup parent) {
                ViewGroup view = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(mModelLayout, parent, false);
                return new OrderViewHolder(view);
            }
        };
    }

    @Override
    public void onPause() {
        presenter.finishConnection();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (shouldRefreshList) {
            shouldRefreshList = false;
            refresh.setRefreshing(true);
            refresh.setIsRefreshing(true);
            presenter.onRefreshView();
        }
    }

    @Override
    public void moveToDetailResult(Intent intent, int code) {
        startActivityForResult(intent, code);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case PROCESS_ORDER:
                    AppWidgetUtil.sendBroadcastToAppWidget(getActivity());
                    presenter.onRefreshView();
                    break;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        list = (RecyclerView) view.findViewById(R.id.order_list);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        mainView = view.findViewById(R.id.root);

        initView();
        return view;
    }

    public void initView() {
        refresh = new RefreshHandler(getActivity(), mainView, onRefreshListener());
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setAdapter(adapter);
        filterLayout = getActivity().getLayoutInflater().inflate(R.layout.filter_layout_selling_order, null);
        search = (SearchView) filterLayout.findViewById(R.id.search);
        int searchPlateId = search.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlate = search.findViewById(searchPlateId);
        searchPlate.setBackgroundColor(Color.TRANSPARENT);
        deadline = (Spinner) filterLayout.findViewById(R.id.deadline_spinner);
        bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(filterLayout);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.show();
            }
        });
    }

    @Override
    public void initListener() {
        adapter.setOnRetryListener(new BaseSellingAdapter.OnRetryListener() {
            @Override
            public void onRetryCliked() {
                presenter.getOrderList(getUserVisibleHint());
            }
        });
        search.setOnQueryTextListener(onSearchQuery());
        deadline.setOnItemSelectedListener(onDeadlineSelectedListener());
        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                presenter.onScrollList(((LinearLayoutManager) list.getLayoutManager()).findLastVisibleItemPosition() == list.getLayoutManager().getItemCount() - 1);
            }
        });
    }

    @Override
    public boolean getUserVisible() {
        return getUserVisibleHint();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        initPresenter();
        presenter.getOrderList(isVisibleToUser);
        ScreenTracking.screen(AppScreen.SCREEN_TX_SHOP_NEW_ORDER);
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void disableFilter() {
        deadline.setEnabled(false);
    }

    @Override
    public void setRefreshPullEnable(boolean b) {
        refresh.setPullEnabled(b);
    }

    @Override
    public String getQuery() {
        return search.getQuery().toString();
    }

    @Override
    public int getDeadlineSelectionPos() {
        return deadline.getSelectedItemPosition();
    }

    @Override
    public void notifyDataSetChanged(List<OrderShippingList> listDatas) {
        adapter.clearData();
        adapter.setListModel(listDatas);
    }

    @Override
    public void finishRefresh() {
        refresh.finishRefresh();
    }

    @Override
    public void removeRetry() {
        adapter.setIsRetry(false);
    }

    @Override
    public void removeLoading() {
        adapter.setIsLoading(false);
    }

    @Override
    public void enableFilter() {
        deadline.setEnabled(true);
    }

    @Override
    public View getRootView() {
        return getView();
    }


    @Override
    public void setRefreshing(boolean b) {
        refresh.setRefreshing(b);
    }

    @Override
    public void addLoadingFooter() {
        adapter.setIsLoading(true);
    }

    @Override
    public void addRetry() {
        adapter.setIsRetry(true);
    }

    @Override
    public void addEmptyView() {
        adapter.setIsDataEmpty(true);
    }

    @Override
    public void removeEmpty() {
        adapter.setIsDataEmpty(false);
    }

    @Override
    protected void initPresenter() {
        if (presenter == null) {
            presenter = new NewOrderImpl(this);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shop_new_order;
    }

    @Override
    public void hideFilterView() {
        bottomSheetDialog.hide();
    }

    @Override
    public boolean getRefreshing() {
        return refresh.isRefreshing();
    }

    @Override
    public void resetPage() {
        page.resetPage();
    }

    @Override
    public PagingHandler getPaging() {
        return page;
    }

    @Override
    public void hideFab() {
        fab.hide();
    }

    @Override
    public void showFab() {
        fab.show();
    }

    private SearchView.OnQueryTextListener onSearchQuery() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                presenter.onQuerySubmit(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                presenter.onQueryChange(newText);
                return false;
            }
        };
    }

    private AdapterView.OnItemSelectedListener onDeadlineSelectedListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //to avoid called itemselected when oncreate
                if (inhibit_spinner_deadline) {
                    inhibit_spinner_deadline = false;
                } else {
                    presenter.onDeadlineSelected();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    private RefreshHandler.OnRefreshHandlerListener onRefreshListener() {
        return new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                presenter.onRefreshView();
            }
        };
    }
}
