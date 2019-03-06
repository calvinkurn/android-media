package com.tokopedia.transaction.orders.orderlist.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.RefreshHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.design.component.FloatingButton;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.design.quickfilter.QuickSingleFilterView;
import com.tokopedia.design.quickfilter.custom.CustomViewRounderCornerFilterView;
import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.orders.UnifiedOrderListRouter;
import com.tokopedia.transaction.orders.orderdetails.view.OrderListAnalytics;
import com.tokopedia.transaction.orders.orderlist.common.OrderListContants;
import com.tokopedia.transaction.orders.orderlist.common.SaveDateBottomSheetActivity;
import com.tokopedia.transaction.orders.orderlist.common.SurveyBottomSheet;
import com.tokopedia.transaction.orders.orderlist.data.Order;
import com.tokopedia.transaction.orders.orderlist.data.OrderCategory;
import com.tokopedia.transaction.orders.orderlist.data.OrderLabelList;
import com.tokopedia.transaction.orders.orderlist.di.DaggerOrderListComponent;
import com.tokopedia.transaction.orders.orderlist.di.OrderListComponent;
import com.tokopedia.transaction.orders.orderlist.view.adapter.OrderListAdapter;
import com.tokopedia.transaction.orders.orderlist.view.presenter.OrderListContract;
import com.tokopedia.transaction.orders.orderlist.view.presenter.OrderListPresenterImpl;
import com.tokopedia.transaction.purchase.interactor.TxOrderNetInteractor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class OrderListFragment extends BaseDaggerFragment implements
        RefreshHandler.OnRefreshHandlerListener, OrderListContract.View, QuickSingleFilterView.ActionListener, SearchInputView.Listener, SearchInputView.ResetListener, OrderListAdapter.OnMenuItemListener, View.OnClickListener {

    private static final String ORDER_CATEGORY = "orderCategory";
    private static final String ORDER_TAB_LIST = "TAB_LIST";
    private static final int MINIMUM_CHARATERS_HIT_API = 3;
    private static final int FILTER_DATE_REQUEST = 1;
    private static final int SUBMIT_SURVEY_REQUEST = 2;
    OrderListComponent orderListComponent;
    RecyclerView recyclerView;
    SwipeToRefresh swipeToRefresh;
    LinearLayout emptyLayoutOrderList, emptyLayoutMarketPlace;
    OrderListAdapter orderListAdapter;
    LinearLayout filterDate;
    RelativeLayout mainContent;
    TextView tryAgain;
    private RefreshHandler refreshHandler;
    private boolean isLoading = false;
    private int page_num = 1;
    int totalItemCount;
    private int visibleThreshold = 2;
    private int lastVisibleItem;
    private Bundle savedState;
    private String startDate = "";
    private String endDate = "";
    private int orderId = 1;
    @Inject
    OrderListAnalytics orderListAnalytics;

    private String selectedFilter = "0";

    private CustomViewRounderCornerFilterView quickSingleFilterView;

    private SearchInputView simpleSearchView;

    private ImageView surveyBtn;

    private String searchedString = "";

    @Inject
    OrderListPresenterImpl presenter;

    private boolean hasRecyclerListener = false;

    private ArrayList<Order> mOrderDataList;
    private String mOrderCategory;
    private OrderLabelList orderLabelList;
    private boolean isSurveyBtnVisible = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(isRetainInstance());
        if (getArguments() != null) {
            setupArguments(getArguments());
        }
        initialPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transaction_list_order_module, container, false);
    }

    protected int getFragmentLayout() {
        return R.layout.fragment_transaction_list_order_module;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initialVar();
        setViewListener();
        setActionVar();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setHasOptionsMenu(getOptionsMenuEnable());
        initialListener(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!restoreStateFromArguments()) {
            onFirstTimeLaunched();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveStateToArguments();
    }

    private void saveStateToArguments() {
        if (getView() != null)
            savedState = saveState();
        if (savedState != null) {
            Bundle b = getArguments();
            if (b == null) b = new Bundle();
            b.putBundle("internalSavedViewState8954201239547", savedState);
        }
    }

    private Bundle saveState() {
        Bundle state = new Bundle();
        onSaveState(state);
        return state;
    }

    private boolean restoreStateFromArguments() {
        Bundle b = getArguments();
        if (b == null) b = new Bundle();
        savedState = b.getBundle("internalSavedViewState8954201239547");
        if (savedState != null) {
            restoreState();
            return true;
        }
        return false;
    }

    private void restoreState() {
        if (savedState != null) {
            onRestoreState(savedState);
        }
    }

    @Override
    public void onDestroyView() {
        saveStateToArguments();
        presenter.detachView();
        super.onDestroyView();
    }

    protected boolean isRetainInstance() {
        return false;
    }


    protected void onFirstTimeLaunched() {

    }


    public void onSaveState(Bundle state) {

    }


    public void onRestoreState(Bundle savedState) {

    }

    protected boolean getOptionsMenuEnable() {
        return false;
    }


    protected void initialPresenter() {
        presenter.attachView(this);
    }

    protected void initInjector() {
        orderListComponent = DaggerOrderListComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build();
        orderListComponent.inject(this);
    }

    protected void initialListener(Activity activity) {
    }

    protected void setupArguments(Bundle arguments) {
        mOrderCategory = arguments.getString(ORDER_CATEGORY);
        orderLabelList = arguments.getParcelable(ORDER_TAB_LIST);
        if (arguments.getString(OrderListContants.ORDER_FILTER_ID) != null) {
            selectedFilter = arguments.getString(OrderListContants.ORDER_FILTER_ID);
        }
    }

    protected void initView(View view) {
        recyclerView = view.findViewById(R.id.order_list_rv);
        swipeToRefresh = view.findViewById(R.id.swipe_refresh_layout);
        emptyLayoutOrderList = view.findViewById(R.id.empty_state_order_list);
        emptyLayoutMarketPlace = view.findViewById(R.id.empty_state_marketplace);
        quickSingleFilterView = view.findViewById(R.id.quick_filter);
        simpleSearchView = view.findViewById(R.id.simpleSearchView);
        simpleSearchView.setSearchHint(getContext().getResources().getString(R.string.search_hint_text));
        filterDate = view.findViewById(R.id.filterDate);
        surveyBtn = view.findViewById(R.id.survey_bom);
        surveyBtn.setOnClickListener(this);
        mainContent = view.findViewById(R.id.mainContent);
        tryAgain = emptyLayoutMarketPlace.findViewById(R.id.tryAgain);
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RouteManager.route(getContext(), ApplinkConst.HOME);
            }
        });
        if (orderLabelList != null && orderLabelList.getFilterStatusList() != null && orderLabelList.getFilterStatusList().size() > 0) {
            presenter.buildAndRenderFilterList(orderLabelList.getFilterStatusList());
        }

        filterDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(SaveDateBottomSheetActivity.getDateInstance(getContext(), startDate, endDate), FILTER_DATE_REQUEST);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == FILTER_DATE_REQUEST) {
                startDate = data.getStringExtra(SaveDateBottomSheetActivity.START_DATE);
                endDate = data.getStringExtra(SaveDateBottomSheetActivity.END_DATE);
                refreshHandler.startRefresh();
                orderListAnalytics.sendDateFilterClickEvent();
            } else if (requestCode == SUBMIT_SURVEY_REQUEST) {
                presenter.insertSurveyRequest(data.getIntExtra(SaveDateBottomSheetActivity.SURVEY_RATING, 3), data.getStringExtra(SaveDateBottomSheetActivity.SURVEY_COMMENT));
            }
        }
    }

    protected void setViewListener() {
        refreshHandler = new RefreshHandler(getActivity(), getView(), this);
        refreshHandler.setPullEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(orderListAdapter);
        quickSingleFilterView.setListener(this);
        simpleSearchView.setListener(this);
        simpleSearchView.setResetListener(this);
    }

    private void addRecyclerListener() {
        hasRecyclerListener = true;
        recyclerView.addOnScrollListener(scrollListener);
    }

    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy > 0 || dy < 0 && (mOrderCategory.equalsIgnoreCase(OrderCategory.MARKETPLACE) || mOrderCategory.equalsIgnoreCase("belanja")))
                setVisibilitySurveyBtn(false);
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

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE && (mOrderCategory.equalsIgnoreCase(OrderCategory.MARKETPLACE) || mOrderCategory.equalsIgnoreCase("belanja"))) {
                setVisibilitySurveyBtn(true);
            }
            super.onScrollStateChanged(recyclerView, newState);
        }
    };

    @Override
    public void onRefresh(View view) {
        page_num = 1;
        isLoading = true;
        emptyLayoutOrderList.setVisibility(View.GONE);
        emptyLayoutMarketPlace.setVisibility(View.GONE);
        if (orderListAdapter.getItemCount() != 0) {
            mOrderDataList.clear();
            orderListAdapter.clearItemList();
        }
        if (mOrderCategory.equalsIgnoreCase(OrderListContants.BELANJA) || mOrderCategory.equalsIgnoreCase(OrderListContants.MARKETPLACE)) {
            quickSingleFilterView.setVisibility(View.VISIBLE);
            simpleSearchView.setVisibility(View.VISIBLE);
        }
        presenter.getAllOrderData(getActivity(), mOrderCategory, TxOrderNetInteractor.TypeRequest.INITIAL, page_num, 1);
    }

    void onLoadMore() {
        page_num++;
        mOrderDataList.add(null);
        orderListAdapter.addItemAtLast(null);
        if (!isLoading) {
            isLoading = true;
            presenter.getAllOrderData(getActivity(), mOrderCategory, TxOrderNetInteractor.TypeRequest.LOAD_MORE, page_num, orderId);
        }
        orderListAnalytics.sendLoadMoreEvent("load-" + page_num);
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
        if (typeRequest == TxOrderNetInteractor.TypeRequest.INITIAL) {
            swipeToRefresh.setVisibility(View.VISIBLE);
            if (mOrderCategory.equalsIgnoreCase(OrderListContants.BELANJA) || mOrderCategory.equalsIgnoreCase(OrderListContants.MARKETPLACE)) {
                emptyLayoutMarketPlace.setVisibility(View.VISIBLE);
            } else {
                emptyLayoutOrderList.setVisibility(View.VISIBLE);
            }
            filterDate.setVisibility(View.GONE);
            surveyBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public Context getAppContext() {
        if (getActivity() != null)
            return getActivity().getApplicationContext();
        else
            return null;
    }

    @Override
    public void setLastOrderId(int orderid) {
        this.orderId = orderid;
    }

    private NetworkErrorHelper.RetryClickedListener getEditShipmentRetryListener() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.getAllOrderData(getActivity(), mOrderCategory, TxOrderNetInteractor.TypeRequest.INITIAL, page_num, 1);
            }
        };
    }

    protected void initialVar() {
        orderListAdapter = new OrderListAdapter(getActivity(), this);
    }

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
    public void showProcessGetData() {
        if (!refreshHandler.isRefreshing()) {
            refreshHandler.setRefreshing(true);
            refreshHandler.setPullEnabled(false);
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
        emptyLayoutOrderList.setVisibility(View.GONE);
        emptyLayoutMarketPlace.setVisibility(View.GONE);
        if (mOrderCategory.equalsIgnoreCase(OrderListContants.BELANJA) || mOrderCategory.equalsIgnoreCase(OrderListContants.MARKETPLACE)) {
            filterDate.setVisibility(View.VISIBLE);
        }
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
            try {
                startActivity(((UnifiedOrderListRouter) getActivity()
                        .getApplication()).getWebviewActivityWithIntent(getContext(),
                        URLEncoder.encode(uri, "UTF-8")));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void selectFilter(String typeFilter) {
        selectedFilter = typeFilter;
        refreshHandler.startRefresh();
        orderListAnalytics.sendQuickFilterClickEvent(typeFilter);
    }


    @Override
    public void renderOrderStatus(List<QuickFilterItem> filterItems, int selctedIndex) {
        quickSingleFilterView.setDefaultItem(null);
        quickSingleFilterView.renderFilter(filterItems, selctedIndex);
    }

    @Override
    public void showSurveyButton(boolean isEligible) {
        if (isEligible && (mOrderCategory.equalsIgnoreCase(OrderCategory.MARKETPLACE) || mOrderCategory.equalsIgnoreCase("belanja"))) {
            surveyBtn.setVisibility(View.VISIBLE);
        } else {
            surveyBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public String getSearchedString() {
        return searchedString;
    }

    @Override
    public String getStartDate() {
        return startDate;
    }

    @Override
    public String getEndDate() {
        return endDate;
    }

    @Override
    public void showSuccessMessage(String message) {
        ToasterNormal.showClose(getActivity(), message);
    }

    @Override
    public void showFailureMessage(String message) {
        ToasterError.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public String getSelectedFilter() {
        return String.valueOf(selectedFilter);
    }

    @Override
    public void onSearchSubmitted(String text) {
            searchedString = text;
    }

    @Override
    public void onSearchTextChanged(String text) {
        if (text.length() >= MINIMUM_CHARATERS_HIT_API || text.length() == 0) {
            searchedString = text;
            orderListAnalytics.sendSearchFilterClickEvent();
            filterDate.setVisibility(View.GONE);
            refreshHandler.startRefresh();
        }
    }

    @Override
    public void onSearchReset() {
        searchedString = "";
        refreshHandler.startRefresh();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.survey_bom) {
            startActivityForResult(SaveDateBottomSheetActivity.getSurveyInstance(getContext(), "2"), SUBMIT_SURVEY_REQUEST);
        }
    }

    private void setVisibilitySurveyBtn(boolean isVisible) {
        if (isVisible && !isSurveyBtnVisible) {
           surveyBtn.animate().translationY(0).setDuration(500).start();
            isSurveyBtnVisible=true;
        } else if(!isVisible && isSurveyBtnVisible){
            surveyBtn.animate().translationY(surveyBtn.getHeight() + getResources().getDimensionPixelSize(R.dimen.dp_10)).setDuration(500).start();
            isSurveyBtnVisible=false;
        }
    }
}

