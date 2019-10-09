package com.tokopedia.transaction.orders.orderlist.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.RefreshHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.design.quickfilter.QuickSingleFilterView;
import com.tokopedia.design.quickfilter.custom.CustomViewRounderCornerFilterView;
import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.trackingoptimizer.TrackingQueue;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.orders.UnifiedOrderListRouter;
import com.tokopedia.transaction.orders.orderdetails.data.ShopInfo;
import com.tokopedia.transaction.orders.orderdetails.data.Status;
import com.tokopedia.transaction.orders.orderdetails.view.OrderListAnalytics;
import com.tokopedia.transaction.orders.orderdetails.view.activity.RequestCancelActivity;
import com.tokopedia.transaction.orders.orderlist.common.OrderListContants;
import com.tokopedia.transaction.orders.orderlist.common.SaveDateBottomSheetActivity;
import com.tokopedia.transaction.orders.orderlist.data.ActionButton;
import com.tokopedia.transaction.orders.orderlist.data.Order;
import com.tokopedia.transaction.orders.orderlist.data.OrderCategory;
import com.tokopedia.transaction.orders.orderlist.data.OrderLabelList;
import com.tokopedia.transaction.orders.orderlist.di.DaggerOrderListComponent;
import com.tokopedia.transaction.orders.orderlist.di.OrderListComponent;
import com.tokopedia.transaction.orders.orderlist.di.OrderListUseCaseModule;
import com.tokopedia.transaction.orders.orderlist.view.adapter.OrderListAdapter;
import com.tokopedia.transaction.orders.orderlist.view.adapter.WishListResponseListener;
import com.tokopedia.transaction.orders.orderlist.view.adapter.factory.OrderListAdapterFactory;
import com.tokopedia.transaction.orders.orderlist.view.adapter.viewHolder.OrderListRecomListViewHolder;
import com.tokopedia.transaction.orders.orderlist.view.adapter.viewHolder.OrderListViewHolder;
import com.tokopedia.transaction.orders.orderlist.view.adapter.viewModel.OrderListRecomViewModel;
import com.tokopedia.transaction.orders.orderlist.view.presenter.OrderListContract;
import com.tokopedia.transaction.orders.orderlist.view.presenter.OrderListPresenterImpl;
import com.tokopedia.transaction.purchase.interactor.TxOrderNetInteractor;
import com.tokopedia.unifycomponents.Toaster;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class OrderListFragment extends BaseDaggerFragment implements
        OrderListRecomListViewHolder.ActionListener, RefreshHandler.OnRefreshHandlerListener,
        OrderListContract.View, QuickSingleFilterView.ActionListener, SearchInputView.Listener,
        SearchInputView.ResetListener, OrderListViewHolder.OnMenuItemListener, View.OnClickListener,
        OrderListViewHolder.OnActionButtonListener {

    private static final String ORDER_CATEGORY = "orderCategory";
    private static final String ORDER_TAB_LIST = "TAB_LIST";
    private static final int MINIMUM_CHARATERS_HIT_API = 3;
    private static final int FILTER_DATE_REQUEST = 1;
    private static final int ANIMATION_DURATION = 500;
    private static final int SUBMIT_SURVEY_REQUEST = 2;
    public static final String OPEN_SURVEY_PAGE = "2";
    private static final String KEY_URI = "tokopedia";
    private static final String KEY_URI_PARAMETER = "idem_potency_key";
    private static final String KEY_URI_PARAMETER_EQUAL = "idem_potency_key=";
    private static final String INVOICE_URL = "invoiceUrl";
    private static final String TX_ASK_SELLER = "tx_ask_seller";
    public static final String STATUS_CODE_220 = "220";
    public static final String STATUS_CODE_400 = "400";
    public static final String STATUS_CODE_11 = "11";
    public static final int REQUEST_CANCEL_ORDER = 101;
    public static final int REJECT_BUYER_REQUEST = 102;
    public static final int CANCEL_BUYER_REQUEST = 103;
    private static final long KEYBOARD_SEARCH_WAITING_TIME = 300;
    private static final String ACTION_BUY_AGAIN = "beli lagi";
    private static final String ACTION_ASK_SELLER = "tanya penjual";
    private static final String ACTION_TRACK_IT = "lacak";
    private static final String ACTION_SUBMIT_CANCELLATION = "ajukan pembatalan";
    private static final String ACTION_DONE = "selesai";
    OrderListComponent orderListComponent;
    RecyclerView recyclerView;
    SwipeToRefresh swipeToRefresh;
    LinearLayout filterDate;
    RelativeLayout mainContent;
    private RefreshHandler refreshHandler;
    private boolean isLoading = false;
    private int page_num = 1;
    private Bundle savedState;
    private String startDate = "";
    private String endDate = "";
    private int orderId = 1;
    private String selectedOrderId = "0";
    private String actionButtonUri = "";
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

    private String mOrderCategory;
    private OrderLabelList orderLabelList;
    private boolean isSurveyBtnVisible = true;
    private OrderListAdapter orderListAdapter;
    private Boolean isRecommendation = false;
    private GridLayoutManager layoutManager;
    EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    private TrackingQueue trackingQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trackingQueue = new TrackingQueue(getAppContext());
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
                .orderListUseCaseModule(new OrderListUseCaseModule())
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
        quickSingleFilterView = view.findViewById(R.id.quick_filter);
        simpleSearchView = view.findViewById(R.id.simpleSearchView);
        simpleSearchView.setSearchHint(getContext().getResources().getString(R.string.search_hint_text));
        filterDate = view.findViewById(R.id.filterDate);
        surveyBtn = view.findViewById(R.id.survey_bom);
        surveyBtn.setOnClickListener(this);
        mainContent = view.findViewById(R.id.mainContent);
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
        } else
        if (requestCode == REQUEST_CANCEL_ORDER) {
            String reason = "";
            int reasonCode = 1;
            if (resultCode == REJECT_BUYER_REQUEST) {
                reason = data.getStringExtra(OrderListContants.REASON);
                reasonCode = data.getIntExtra(OrderListContants.REASON_CODE, 1);
                presenter.updateOrderCancelReason(reason, selectedOrderId, reasonCode, actionButtonUri);
            } else if (resultCode == CANCEL_BUYER_REQUEST) {
                reason = data.getStringExtra(OrderListContants.REASON);
                reasonCode = data.getIntExtra(OrderListContants.REASON_CODE, 1);
                presenter.updateOrderCancelReason(reason, selectedOrderId, reasonCode, actionButtonUri);
            }
        }
    }

    protected void setViewListener() {
        refreshHandler = new RefreshHandler(getActivity(), getView(), this);
        refreshHandler.setPullEnabled(true);
        layoutManager = new GridLayoutManager(getContext(), 2);
        layoutManager.setSpanSizeLookup(onSpanSizeLookup());
        orderListAdapter = new OrderListAdapter(new OrderListAdapterFactory(orderListAnalytics, this, this, this));
        orderListAdapter.setVisitables(new ArrayList<>());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(orderListAdapter);
        recyclerView.setHasFixedSize(false);
        quickSingleFilterView.setListener(this);
        simpleSearchView.setListener(this);
        simpleSearchView.setResetListener(this);
    }

    private void addRecyclerListener() {
        hasRecyclerListener = true;
        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (isRecommendation) {
                    presenter.processGetRecommendationData(endlessRecyclerViewScrollListener.getCurrentPage(), false);
                } else {
                    page_num++;
                    if (!isLoading) {
                        isLoading = true;
                        presenter.getAllOrderData(getActivity(), mOrderCategory, TxOrderNetInteractor.TypeRequest.LOAD_MORE, page_num, orderId);
                    }
                    orderListAnalytics.sendLoadMoreEvent("load-" + page_num);
                }
            }

            @Override
            public void onScrolled(RecyclerView view, int dx, int dy) {
                super.onScrolled(view, dx, dy);
                if (dy > 0 || dy < 0 && (mOrderCategory.equalsIgnoreCase(OrderCategory.MARKETPLACE) || mOrderCategory.equalsIgnoreCase(OrderListContants.BELANJA)))
                    setVisibilitySurveyBtn(false);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && (mOrderCategory.equalsIgnoreCase(OrderCategory.MARKETPLACE) || mOrderCategory.equalsIgnoreCase(OrderListContants.BELANJA))) {
                    setVisibilitySurveyBtn(true);
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        };
        recyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);
    }

    @Override
    public void onRefresh(View view) {
        page_num = 1;
        isLoading = true;
        presenter.onRefresh();
        if (orderListAdapter != null) {
            orderListAdapter.clearAllElements();
        }
        if (mOrderCategory.equalsIgnoreCase(OrderListContants.BELANJA) || mOrderCategory.equalsIgnoreCase(OrderListContants.MARKETPLACE)) {
            quickSingleFilterView.setVisibility(View.VISIBLE);
            simpleSearchView.setVisibility(View.VISIBLE);
        }
        presenter.getAllOrderData(getActivity(), mOrderCategory, TxOrderNetInteractor.TypeRequest.INITIAL, page_num, 1);
    }

    @Override
    public void removeProgressBarView() {
        isLoading = false;
        refreshHandler.finishRefresh();
        refreshHandler.setPullEnabled(true);
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
            if (!hasRecyclerListener) {
                addRecyclerListener();
            }
            if (mOrderCategory.equalsIgnoreCase(OrderListContants.BELANJA) || mOrderCategory.equalsIgnoreCase(OrderListContants.MARKETPLACE)) {
                orderListAdapter.setEmptyMarketplace();
                presenter.processGetRecommendationData(endlessRecyclerViewScrollListener.getCurrentPage(), true);
            } else {
                orderListAdapter.setEmptyOrderList();
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
                if (orderListAdapter != null) {
                    orderListAdapter.clearAllElements();
                }
                presenter.getAllOrderData(getActivity(), mOrderCategory, TxOrderNetInteractor.TypeRequest.INITIAL, page_num, 1);
            }
        };
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
        quickSingleFilterView.setDefaultItem(filterItems.get(selctedIndex));
        quickSingleFilterView.renderFilter(filterItems, selctedIndex);
    }

    @Override
    public void showSurveyButton(boolean isEligible) {
        if (isEligible && (mOrderCategory.equalsIgnoreCase(OrderCategory.MARKETPLACE) || mOrderCategory.equalsIgnoreCase(OrderListContants.BELANJA))) {
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
    public void showSuccessMessageWithAction(String message) {
        Toaster.INSTANCE.showNormalWithAction(mainContent, message, Snackbar.LENGTH_LONG, getString(R.string.bom_check_cart), v -> RouteManager.route(getContext(), ApplinkConst.CART));
    }

    @Override
    public void addData(List<Visitable> data, Boolean isRecommendation) {
        this.isRecommendation = isRecommendation;
        if (!hasRecyclerListener) {
            addRecyclerListener();
        }
        refreshHandler.finishRefresh();
        refreshHandler.setPullEnabled(true);
        orderListAdapter.addElement(data);
        endlessRecyclerViewScrollListener.updateStateAfterGetData();
        swipeToRefresh.setVisibility(View.VISIBLE);
        if ((mOrderCategory.equalsIgnoreCase(OrderListContants.BELANJA) || mOrderCategory.equalsIgnoreCase(OrderListContants.MARKETPLACE)) && !isRecommendation) {
            filterDate.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void displayLoadMore(boolean isLoadMore) {
        if (orderListAdapter != null) {
            if (isLoadMore) {
                orderListAdapter.showLoading();
            } else {
                orderListAdapter.hideLoading();
            }
        }
    }

    @Override
    public void triggerSendEnhancedEcommerceAddToCartSuccess(AddToCartDataModel addToCartDataResponseModel, Object productModel) {
        if (productModel instanceof OrderListRecomViewModel) {
            OrderListRecomViewModel orderListRecomViewModel = (OrderListRecomViewModel) productModel;
            orderListAnalytics.eventRecommendationAddToCart(orderListRecomViewModel, addToCartDataResponseModel);
        }
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
            Handler handler = new Handler();
            handler.postDelayed(() -> refreshHandler.startRefresh(), KEYBOARD_SEARCH_WAITING_TIME);
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
            startActivityForResult(SaveDateBottomSheetActivity.getSurveyInstance(getContext(), OPEN_SURVEY_PAGE), SUBMIT_SURVEY_REQUEST);
        }
    }

    private void setVisibilitySurveyBtn(boolean isVisible) {
        if (isVisible && !isSurveyBtnVisible) {
            surveyBtn.animate().translationY(0).setDuration(ANIMATION_DURATION).start();
            isSurveyBtnVisible = true;
        } else if (!isVisible && isSurveyBtnVisible) {
            surveyBtn.animate().translationY(surveyBtn.getHeight() + getResources().getDimensionPixelSize(R.dimen.dp_10)).setDuration(ANIMATION_DURATION).start();
            isSurveyBtnVisible = false;
        }
    }

    public GridLayoutManager.SpanSizeLookup onSpanSizeLookup() {
        return new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (orderListAdapter.getItemViewType(position) == OrderListRecomListViewHolder.LAYOUT) {
                    return 1;
                } else {
                    return 2;
                }
            }
        };
    }

    @Override
    public void onCartClicked(@NotNull Object productModel) {
        presenter.processAddToCart(productModel);
    }

    @Override
    public void onPause() {
        super.onPause();
        orderListAnalytics.sendEmptyWishlistProductImpression(trackingQueue);
        trackingQueue.sendAll();
    }

    @Override
    public void onWishListClicked(@NotNull Object productModel, boolean isSelected, @NotNull WishListResponseListener wishListResponseListener) {
        if (productModel instanceof OrderListRecomViewModel) {
            if (isSelected) {
                presenter.addWishlist(((OrderListRecomViewModel) productModel).getRecommendationItem(), wishListResponseListener);
            } else {
                presenter.removeWishlist(((OrderListRecomViewModel) productModel).getRecommendationItem(), wishListResponseListener);
            }
        }
    }

    @Override
    public void handleActionButtonClick(@NotNull Order order, @Nullable ActionButton actionButton) {
        if (actionButton != null)
            this.actionButtonUri = actionButton.uri();
        this.selectedOrderId = order.id();

        switch (actionButton.label().toLowerCase()) {
            case ACTION_BUY_AGAIN:
            case ACTION_SUBMIT_CANCELLATION:
            case ACTION_ASK_SELLER:
                presenter.setOrderDetails(selectedOrderId, mOrderCategory, actionButton.label().toLowerCase());
                break;
            case ACTION_TRACK_IT:
                trackOrder();
                break;
            case ACTION_DONE:
                presenter.finishOrder(selectedOrderId, actionButtonUri);
                break;
            default:
                String newUri = actionButton.uri();
                if (newUri.startsWith(KEY_URI)) {
                    if (newUri.contains(KEY_URI_PARAMETER)) {
                        Uri url = Uri.parse(newUri);
                        String queryParameter = url.getQueryParameter(KEY_URI_PARAMETER) != null ? url.getQueryParameter(KEY_URI_PARAMETER):"";
                        newUri = newUri.replace(queryParameter, "");
                        newUri = newUri.replace(KEY_URI_PARAMETER_EQUAL, "");
                    }
                    RouteManager.route(getActivity(), newUri);
                } else if (!TextUtils.isEmpty(newUri)) {
                    try {
                        startActivity(((UnifiedOrderListRouter) getActivity()
                                .getApplication()).getWebviewActivityWithIntent(getContext(),
                                URLEncoder.encode(newUri, "UTF-8")));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }

    }

    @Override
    public void requestCancelOrder(Status status) {
        Intent intent = new Intent(getContext(), RequestCancelActivity.class);
        intent.putExtra("OrderId", selectedOrderId);
        intent.putExtra("action_button_url", actionButtonUri);
        if (status.status().equals(STATUS_CODE_220) || status.status().equals(STATUS_CODE_400)) {
            if (presenter.shouldShowTimeForCancellation()) {
                Toaster.INSTANCE.showErrorWithAction(mainContent,
                        presenter.getCancelTime(),
                        Snackbar.LENGTH_LONG,
                        getResources().getString(R.string.title_ok), v -> {});
            } else
                startActivityForResult(RequestCancelActivity.getInstance(getContext(), selectedOrderId, actionButtonUri, 1), REQUEST_CANCEL_ORDER);
        } else if (status.status().equals(STATUS_CODE_11)) {
            startActivityForResult(RequestCancelActivity.getInstance(getContext(), selectedOrderId, actionButtonUri, 0), REQUEST_CANCEL_ORDER);
        }
    }

    private void trackOrder() {
        String routingAppLink;
        routingAppLink = ApplinkConst.ORDER_TRACKING.replace("{order_id}", selectedOrderId);
        String trackingUrl;
        Uri uri = Uri.parse(actionButtonUri);
        trackingUrl = uri.getQueryParameter("url");
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.appendQueryParameter(ApplinkConst.Query.ORDER_TRACKING_URL_LIVE_TRACKING, trackingUrl);
        routingAppLink += uriBuilder.toString();
        RouteManager.route(getContext(), routingAppLink);
    }

    @Override
    public void startSellerAndAddInvoice() {
        ShopInfo shopInfo = presenter.getShopInfo();
        if (shopInfo != null) {
            String shopId = String.valueOf(shopInfo.getShopId());
            String shopName = shopInfo.getShopName();
            String shopLogo = shopInfo.getShopLogo();
            String shopUrl = shopInfo.getShopUrl();
            String invoiceUrl;
            Uri uri = Uri.parse(actionButtonUri);
            invoiceUrl = uri.getQueryParameter(INVOICE_URL);
            String applink = "tokopedia://topchat/askseller/" + shopId;
            Intent intent = RouteManager.getIntent(getContext(), applink);
            presenter.assignInvoiceDataTo(intent);
            intent.putExtra(ApplinkConst.Chat.SOURCE, TX_ASK_SELLER);
            startActivity(intent);
        }
    }

    @Override
    public void finishOrderDetail() {
        refreshHandler.startRefresh();
    }
}

