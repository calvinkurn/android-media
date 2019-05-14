package com.tokopedia.discovery.newdiscovery.search.fragment.product;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.presentation.EndlessRecyclerviewListener;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.discovery.DiscoveryRouter;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.common.data.DataValue;
import com.tokopedia.discovery.common.data.Filter;
import com.tokopedia.discovery.common.data.Option;
import com.tokopedia.discovery.newdiscovery.analytics.SearchTracking;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.discovery.newdiscovery.constant.SearchEventTracking;
import com.tokopedia.discovery.newdiscovery.di.component.DaggerSearchComponent;
import com.tokopedia.discovery.newdiscovery.di.component.SearchComponent;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionFragment;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionFragmentPresenter;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionGeneralAdapter;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.ProductListAdapter;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.itemdecoration.ProductItemDecoration;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.listener.ProductListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.typefactory.ProductListTypeFactory;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.typefactory.ProductListTypeFactoryImpl;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.helper.NetworkParamHelper;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.GlobalNavViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter;
import com.tokopedia.discovery.newdynamicfilter.controller.FilterController;
import com.tokopedia.discovery.similarsearch.SimilarSearchManager;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.showcase.ShowCaseBuilder;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Category;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.utils.ImpresionTask;
import com.tokopedia.track.TrackApp;
import com.tokopedia.trackingoptimizer.TrackingQueue;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.wishlist.common.listener.WishListActionListener;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by henrypriyono on 10/6/17.
 */

public class ProductListFragment extends SearchSectionFragment
        implements SearchSectionGeneralAdapter.OnItemChangeView, ProductListFragmentView,
        ProductListener, WishListActionListener {

    public static final String SCREEN_SEARCH_PAGE_PRODUCT_TAB = "Search result - Product tab";
    public static final int REQUEST_CODE_LOGIN = 561;
    private static final int REQUEST_CODE_GOTO_PRODUCT_DETAIL = 123;
    private static final int REQUEST_ACTIVITY_SORT_PRODUCT = 1233;
    private static final int REQUEST_ACTIVITY_FILTER_PRODUCT = 4320;
    public static final Locale DEFAULT_LOCALE = new Locale("in", "ID");

    private static final String ARG_VIEW_MODEL = "ARG_VIEW_MODEL";
    private static final String EXTRA_ADDITIONAL_PARAMS = "EXTRA_ADDITIONAL_PARAMS";
    private static final String SEARCH_PRODUCT_TRACE = "search_product_trace";
    private static int PRODUCT_POSITION = 2;
    protected RecyclerView recyclerView;

    @Inject
    ProductListPresenter presenter;
    @Inject
    SearchTracking searchTracking;
    @Inject
    UserSessionInterface userSession;

    private EndlessRecyclerviewListener linearLayoutLoadMoreTriggerListener;
    private EndlessRecyclerviewListener gridLayoutLoadMoreTriggerListener;

    private GCMHandler gcmHandler;
    private Config topAdsConfig;
    private ProductListAdapter adapter;
    private ProductListTypeFactory productListTypeFactory;
    private String additionalParams = "";
    private boolean isFirstTimeLoad;

    private SimilarSearchManager similarSearchManager;
    private PerformanceMonitoring performanceMonitoring;
    private TrackingQueue trackingQueue;
    private ShowCaseDialog showCaseDialog;

    private FilterController quickFilterController = new FilterController();

    public static ProductListFragment newInstance(SearchParameter searchParameter) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_SEARCH_PARAMETER, searchParameter);
        ProductListFragment productListFragment = new ProductListFragment();
        productListFragment.setArguments(args);
        return productListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadDataFromArguments();

        if(getContext() == null) return;

        similarSearchManager = SimilarSearchManager.getInstance(getContext());
        gcmHandler = new GCMHandler(getContext());
        trackingQueue = new TrackingQueue(getContext());
    }

    private void loadDataFromArguments() {
        if(getArguments() != null) {
            copySearchParameter(getArguments().getParcelable(EXTRA_SEARCH_PARAMETER));
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            switchLayoutType();
        }
    }

    @Override
    protected void initInjector() {
        SearchComponent component = DaggerSearchComponent.builder()
                // getAppComponent from tkpdcore. Temporary solution until this Fragment moved to search module
                .appComponent(getAppComponent())
                .build();
        component.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        presenter.attachView(this, this);
        return inflater.inflate(R.layout.fragment_base_discovery, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);
        initTopAdsConfig();
        initTopAdsParams();
        setupAdapter();
        setupListener();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (adapter.getItemCount() > 0) {
            adapter.notifyDataSetChanged();
        }
        incrementStart();
    }

    private void bindView(View rootView) {
        recyclerView = rootView.findViewById(R.id.recyclerview);
    }

    private void initTopAdsConfig() {
        topAdsConfig = new Config.Builder()
                .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                .setUserId(userSession.getUserId())
                .setEndpoint(Endpoint.PRODUCT)
                .build();
    }

    private void setupAdapter() {
        productListTypeFactory = new ProductListTypeFactoryImpl(this, topAdsConfig, getQueryKey());
        adapter = new ProductListAdapter(getActivity(), this, productListTypeFactory);
        recyclerView.setLayoutManager(getGridLayoutManager());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new ProductItemDecoration(
                getContext().getResources().getDimensionPixelSize(R.dimen.dp_16),
                getContext().getResources().getColor(R.color.white)
        ));
        setHeaderTopAds(true);
    }

    private void setupListener() {
        recyclerView.addOnScrollListener(getRecyclerViewBottomSheetScrollListener());

        gridLayoutLoadMoreTriggerListener = getEndlessRecyclerViewListener(getGridLayoutManager());

        linearLayoutLoadMoreTriggerListener = getEndlessRecyclerViewListener(getLinearLayoutManager());

        recyclerView.addOnScrollListener(gridLayoutLoadMoreTriggerListener);
    }

    private EndlessRecyclerviewListener getEndlessRecyclerViewListener(LinearLayoutManager linearLayoutManager) {
        return new EndlessRecyclerviewListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (isAllowLoadMore()) {
                    loadMoreProduct(adapter.getStartFrom());
                } else {
                    adapter.removeLoading();
                }
            }
        };
    }

    private boolean isAllowLoadMore() {
        return getUserVisibleHint()
                && adapter.hasNextPage();
    }

    @Override
    public void initTopAdsParams() {
        TopAdsParams adsParams = new TopAdsParams();
        adsParams.getParam().put(TopAdsParams.KEY_SRC, SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH);
        adsParams.getParam().put(TopAdsParams.KEY_QUERY, getQueryKey());
        adsParams.getParam().put(TopAdsParams.KEY_USER_ID, userSession.getUserId());

        if(getSearchParameter() != null) {
            adsParams.getParam().putAll(getSearchParameter().getSearchParameterHashMap());
        }

        topAdsConfig.setTopAdsParams(adsParams);
    }

    @Override
    public void storeTotalData(int totalData) {
        adapter.setTotalData(totalData);
    }

    @Override
    public void incrementStart() {
        adapter.incrementStart();
    }

    @Override
    public boolean isEvenPage() {
        return adapter.isEvenPage();
    }

    @Override
    public int getStartFrom() {
        return adapter.getStartFrom();
    }

    @Override
    public void setHeaderTopAds(boolean hasHeader) {

    }

    @Override
    public void addProductList(List<Visitable> list) {
        isListEmpty = false;

        sendProductImpressionTrackingEvent(list);

        adapter.appendItems(list);
    }

    public void setProductList(List<Visitable> list) {
        adapter.clear();

        addProductList(list);
        if (similarSearchManager.isSimilarSearchEnable()){
            startShowCase();
        }
    }

    private void sendProductImpressionTrackingEvent(List<Visitable> list) {
        String userId = userSession.isLoggedIn() ? userSession.getUserId() : "0";
        List<Object> dataLayerList = new ArrayList<>();
        for (Visitable object : list) {
            if (object instanceof ProductItem) {
                ProductItem item = (ProductItem) object;
                if (!item.isTopAds()) {
                    dataLayerList.add(item.getProductAsObjectDataLayer(userId));
                }
            }
        }
        SearchTracking.eventImpressionSearchResultProduct(trackingQueue, dataLayerList, getQueryKey());
    }

    private void loadMoreProduct(final int startRow) {
        generateLoadMoreParameter(startRow);

        presenter.loadMoreData(getSearchParameter(), getAdditionalParamsMap());
    }

    @Override
    public void showNetworkError(final int startRow) {
        if (adapter.isListEmpty()) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), this::reloadData);
        } else {
            NetworkErrorHelper.createSnackbarWithAction(getActivity(), () -> {
                adapter.setStartFrom(startRow);
                loadMoreProduct(startRow);
            }).showRetrySnackbar();
        }
    }

    private void generateLoadMoreParameter(int startRow) {
        if(getSearchParameter() == null) setSearchParameter(new SearchParameter());

        getSearchParameter().set(SearchApiConst.UNIQUE_ID, generateUniqueId());
        getSearchParameter().set(SearchApiConst.USER_ID, generateUserId());
        getSearchParameter().set(SearchApiConst.START, String.valueOf(startRow));
    }

    private String generateUserId() {
        return userSession.isLoggedIn() ? userSession.getUserId() : "0";
    }

    private String generateUniqueId() {
        return userSession.isLoggedIn() ?
                AuthUtil.md5(userSession.getUserId()) :
                AuthUtil.md5(gcmHandler.getRegistrationId());
    }

    @Override
    public String getScreenNameId() {
        return SCREEN_SEARCH_PAGE_PRODUCT_TAB;
    }

    @Override
    protected void switchLayoutType() {
        super.switchLayoutType();

        if (!getUserVisibleHint()) {
            return;
        }
        recyclerView.clearOnScrollListeners();

        recyclerView.addOnScrollListener(getRecyclerViewBottomSheetScrollListener());

        switch (getAdapter().getCurrentLayoutType()) {
            case GRID_1: // List
                recyclerView.addOnScrollListener(linearLayoutLoadMoreTriggerListener);
                break;
            case GRID_2: // Grid 2x2
            case GRID_3: // Grid 1x1
                recyclerView.addOnScrollListener(gridLayoutLoadMoreTriggerListener);
                break;
        }
    }

    @Override
    protected GridLayoutManager.SpanSizeLookup onSpanSizeLookup() {
        return new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.isEmptyItem(position) ||
                        adapter.isRelatedSearch(position) ||
                        adapter.isHeaderBanner(position) ||
                        adapter.isLoading(position)) {
                    return spanCount;
                } else {
                    return 1;
                }
            }
        };
    }

    @Override
    protected boolean isSortEnabled() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunch() {
        super.onFirstTimeLaunch();

        isFirstTimeLoad = true;
        reloadData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GOTO_PRODUCT_DETAIL
                && data != null
                && data.getExtras() != null
                && data.getExtras().getInt(ProductDetailRouter
                .WISHLIST_STATUS_UPDATED_POSITION, -1) != -1) {
            int position = data.getExtras().getInt(ProductDetailRouter
                    .WISHLIST_STATUS_UPDATED_POSITION, -1);
            boolean isWishlist = data.getExtras().getBoolean(ProductDetailRouter
                    .WIHSLIST_STATUS_IS_WISHLIST, false);

            updateWishlistFromPDP(position, isWishlist);
        }
    }

    private void updateWishlistFromPDP(int position, boolean isWishlist) {
        if (adapter != null && adapter.isProductItem(position)) {
            adapter.updateWishlistStatus(position, isWishlist);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        TopAdsGtmTracker.getInstance().eventSearchResultProductView(trackingQueue, getQueryKey());
        trackingQueue.sendAll();
    }

    @Override
    public void onProductImpressed(ProductItem item, int adapterPosition) {
        if (item.isTopAds()) {
            new ImpresionTask().execute(item.getTopadsImpressionUrl());
            Product product = new Product();
            product.setId(item.getProductID());
            product.setName(item.getProductName());
            product.setPriceFormat(item.getPrice());
            product.setCategory(new Category(item.getCategoryID()));
            TopAdsGtmTracker.getInstance().addSearchResultProductViewImpressions(product, adapterPosition);
        }
    }

    @Override
    public void onGlobalNavWidgetClicked(GlobalNavViewModel.Item item, String keyword) {
        if (!TextUtils.isEmpty(item.getApplink())) {
            RouteManager.route(getActivity(), item.getApplink());
        } else {
            RouteManager.route(getActivity(), item.getUrl());
        }
        SearchTracking.trackEventClickGlobalNavWidgetItem(item.getGlobalNavItemAsObjectDataLayer(), keyword);
    }

    @Override
    public void onGlobalNavWidgetClickSeeAll(String applink, String url) {
        if (!TextUtils.isEmpty(applink)) {
            RouteManager.route(getActivity(), applink);
        } else {
            RouteManager.route(getActivity(), url);
        }
        SearchTracking.eventUserClickSeeAllGlobalNavWidget();
    }

    @Override
    public void onItemClicked(ProductItem item, int adapterPosition) {
        Intent intent = getProductIntent(item.getProductID());

        if(intent != null) {
            intent.putExtra(ProductDetailRouter.WISHLIST_STATUS_UPDATED_POSITION, adapterPosition);
            sendItemClickTrackingEvent(item, adapterPosition);
            startActivityForResult(intent, REQUEST_CODE_GOTO_PRODUCT_DETAIL);
        }
    }

    private Intent getProductIntent(String productId){
        if (getContext() != null) {
            return RouteManager.getIntent(getContext(),ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId);
        } else {
            return null;
        }
    }

    @Override
    public void onLongClick(ProductItem item, int adapterPosition) {
        if(similarSearchManager == null || getSearchParameter() == null) return;

        similarSearchManager.startSimilarSearchIfEnable(getSearchParameter().getSearchQuery(), item);
    }

    private void sendItemClickTrackingEvent(ProductItem item, int pos) {
        String userId = userSession.isLoggedIn() ? userSession.getUserId() : "0";
        if (item.isTopAds()) {
            sendItemClickTrackingEventForTopAdsItem(item, pos);
        } else {
            SearchTracking.trackEventClickSearchResultProduct(
                    getActivity(),
                    item.getProductAsObjectDataLayer(userId),
                    item.getPageNumber(),
                    getQueryKey(),
                    getSelectedFilter(),
                    getSelectedSort()
            );
        }
    }

    private void sendItemClickTrackingEventForTopAdsItem(ProductItem item, int pos) {
        new ImpresionTask().execute(item.getTopadsClickUrl());

        Product product = createTopAdsProductForTracking(item);

        TopAdsGtmTracker.eventSearchResultProductClick(getContext(), getQueryKey(), product, pos);
    }

    private Product createTopAdsProductForTracking(ProductItem item) {
        Product product  = new Product();
        product.setId(item.getProductID());
        product.setName(item.getProductName());
        product.setPriceFormat(item.getPrice());
        product.setCategory(new Category(item.getCategoryID()));

        return product;
    }

    @Override
    public void onWishlistButtonClicked(final ProductItem productItem) {
        presenter.handleWishlistButtonClicked(productItem);
    }

    @Override
    public void onSuggestionClicked(String queryParams) {
        performNewProductSearch(queryParams);
    }

    @Override
    public void onBannerAdsClicked(String appLink) {
        if(getActivity() == null) return;

        TkpdCoreRouter router = ((TkpdCoreRouter) getActivity().getApplicationContext());

        if (router.isSupportedDelegateDeepLink(appLink)) {
            router.actionApplink(getActivity(), appLink);
        } else if (!TextUtils.isEmpty(appLink)) {
            Intent intent = new Intent(getContext(), BannerWebView.class);
            intent.putExtra("url", appLink);
            startActivity(intent);
        }
    }

    @Override
    public void onSearchGuideClicked(String queryParams) {
        performNewProductSearch(queryParams);
    }

    @Override
    public void onRelatedSearchClicked(String queryParams, String keyword) {
        SearchTracking.eventClickRelatedSearch(getContext(), getQueryKey(), keyword);
        performNewProductSearch(queryParams);
    }

    @Override
    public boolean isQuickFilterSelected(Option option) {
        if(quickFilterController == null) return false;

        return quickFilterController.getFilterViewState(option.getUniqueId());
    }

    @Override
    public void onQuickFilterSelected(Option option) {
        if(quickFilterController == null) return;

        boolean isQuickFilterSelectedReversed = !isQuickFilterSelected(option);

        setFilterToQuickFilterController(option, isQuickFilterSelectedReversed);

        Map<String, String> parameterFromFilter = quickFilterController.getParameter();
        applyFilterToSearchParameter(parameterFromFilter);
        setSelectedFilter(new HashMap<>(parameterFromFilter));

        clearDataFilterSort();
        reloadData();

        eventSearchResultQuickFilter(option.getKey(), option.getValue(), isQuickFilterSelectedReversed);
    }

    private void setFilterToQuickFilterController(Option option, boolean isQuickFilterSelected) {
        if(quickFilterController == null) return;

        if (option.isCategoryOption()) {
            quickFilterController.setFilter(option, isQuickFilterSelected, true);
        } else {
            quickFilterController.setFilter(option, isQuickFilterSelected);
        }
    }

    public void eventSearchResultQuickFilter(String filterName, String filterValue, boolean isSelected) {
        searchTracking.sendGeneralEventWithUserId(
                SearchEventTracking.Event.SEARCH_RESULT,
                SearchEventTracking.Category.FILTER_PRODUCT,
                SearchEventTracking.Action.QUICK_FILTER,
                filterName + " - " + filterValue + " - " + Boolean.toString(isSelected)
        );
    }

    @Override
    public void onSelectedFilterRemoved(String uniqueId) {
        removeSelectedFilter(uniqueId);
    }

    @Override
    public void onEmptyButtonClicked() {
        SearchTracking.eventUserClickNewSearchOnEmptySearch(getContext(), getScreenName());
        showSearchInputView();
    }

    @Override
    public List<Option> getSelectedFilterAsOptionList() {
        return getOptionListFromFilterController();
    }

    @Override
    public void onErrorAddWishList(String errorMessage, String productId) {
        enableWishlistButton(productId);
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessAddWishlist(String productId) {
        searchTracking.sendGeneralEventWithUserId(SearchEventTracking.Event.PRODUCT_VIEW,
                SearchEventTracking.Category.SEARCH_RESULT.toLowerCase(),
                SearchEventTracking.Action.CLICK_WISHLIST,
                generateWishlistClickEventLabel(true));
        adapter.updateWishlistStatus(productId, true);
        enableWishlistButton(productId);
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_add_wishlist));
    }

    private String generateWishlistClickEventLabel(boolean isWishlisted) {
        String action = isWishlisted ? "add" : "remove";
        return action + " - " + getQueryKey() + " - " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", DEFAULT_LOCALE).format(new Date());
    }

    @Override
    public void onErrorRemoveWishlist(String errorMessage, String productId) {
        enableWishlistButton(productId);
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessRemoveWishlist(String productId) {
        searchTracking.sendGeneralEventWithUserId(
                SearchEventTracking.Event.PRODUCT_VIEW,
                SearchEventTracking.Category.SEARCH_RESULT.toLowerCase(),
                SearchEventTracking.Action.CLICK_WISHLIST,
                generateWishlistClickEventLabel(false));
        adapter.updateWishlistStatus(productId, false);
        enableWishlistButton(productId);
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_remove_wishlist));
    }

    @Override
    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void stopTracePerformanceMonitoring() {
        if (performanceMonitoring != null) {
            performanceMonitoring.stopTrace();
        }
    }

    @Override
    public void launchLoginActivity(Bundle extras) {
        Intent intent = ((DiscoveryRouter) MainApplication.getAppContext()).getLoginIntent
                (getActivity());
        intent.putExtras(extras);
        startActivityForResult(intent, REQUEST_CODE_LOGIN);
    }

    @Override
    public boolean isUserHasLogin() {
        return userSession.isLoggedIn();
    }

    @Override
    public String getUserId() {
        return userSession.getUserId();
    }

    @Override
    public void disableWishlistButton(String productId) {
        adapter.setWishlistButtonEnabled(productId, false);
    }

    @Override
    public void enableWishlistButton(String productId) {
        adapter.setWishlistButtonEnabled(productId, true);
    }

    @Override
    public String getQueryKey() {
        return searchParameter == null ? "" : searchParameter.getSearchQuery();
    }

    @Override
    public void setEmptyProduct() {
        isListEmpty = true;
        adapter.showEmptyState(getActivity(), getQueryKey(), isFilterActive(), null, getString(R.string.product_tab_title).toLowerCase());
        SearchTracking.eventSearchNoResult(getActivity(), getQueryKey(), getScreenName(), getSelectedFilter());
    }

    @Override
    protected void refreshAdapterForEmptySearch() {
        if (adapter != null) {
            adapter.showEmptyState(getActivity(), getQueryKey(), isFilterActive(), null, getString(R.string.product_tab_title).toLowerCase());
        }
    }

    @Override
    public void reloadData() {
        if (adapter == null || getSearchParameter() == null) {
            return;
        }

        showRefreshLayout();
        adapter.clearData();
        adapter.notifyDataSetChanged();
        initTopAdsParams();
        generateLoadMoreParameter(0);
        performanceMonitoring = PerformanceMonitoring.start(SEARCH_PRODUCT_TRACE);
        presenter.loadData(getSearchParameter(), getAdditionalParamsMap(), isFirstTimeLoad);
        TopAdsGtmTracker.getInstance().clearDataLayerList();
    }

    @Override
    public HashMap<String, String> getAdditionalParamsMap() {
        return NetworkParamHelper.getParamMap(additionalParams);
    }

    @Override
    protected void onSwipeToRefresh() {
        reloadData();
    }

    @Override
    protected int getFilterRequestCode() {
        return REQUEST_ACTIVITY_FILTER_PRODUCT;
    }

    @Override
    protected int getSortRequestCode() {
        return REQUEST_ACTIVITY_SORT_PRODUCT;
    }

    @Override
    protected SearchSectionGeneralAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected SearchSectionFragmentPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onChangeList() {
        recyclerView.setLayoutManager(getLinearLayoutManager());
    }

    @Override
    public void onChangeDoubleGrid() {
        recyclerView.setLayoutManager(getGridLayoutManager());
    }

    @Override
    public void onChangeSingleGrid() {
        recyclerView.setLayoutManager(getGridLayoutManager());
    }

    @Override
    public SearchParameter getSearchParameter() {
        return searchParameter;
    }

    @Override
    public void setSearchParameter(SearchParameter searchParameter) {
        this.searchParameter = searchParameter;
    }

    @Override
    public void backToTop() {
        if (recyclerView != null) {
            recyclerView.smoothScrollToPosition(0);
        }
    }

    @Override
    public List<Option> getQuickFilterOptions(DataValue dynamicFilterModel) {
        ArrayList<Option> optionList = new ArrayList<>();

        for (Filter filter : dynamicFilterModel.getFilter()) {
            optionList.addAll(filter.getOptions());
        }

        return optionList;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    protected String getScreenName() {
        return getScreenNameId();
    }

    @Override
    public void addLoading() {
        adapter.addLoading();
    }

    @Override
    public void removeLoading() {
        adapter.removeLoading();
    }

    @Override
    public void initQuickFilter(List<Filter> quickFilterList) {
        if(quickFilterController == null) return;

        quickFilterController.initFilterController(getSearchParameter().getSearchParameterHashMap(), quickFilterList);
    }

    @Override
    public void setAdditionalParams(String additionalParams) {
        if (!TextUtils.isEmpty(additionalParams)) {
            this.additionalParams = additionalParams;
        }
    }

    @Override
    public void sendTrackingEventAppsFlyerViewListingSearch(JSONArray afProdIds, String query, ArrayList<String> prodIdArray) {
        TrackingUtils.eventAppsFlyerViewListingSearch(getActivity(), afProdIds, query, prodIdArray);
    }

    @Override
    public void sendTrackingEventMoEngageSearchAttempt(String query, boolean hasProductList, HashMap<String, String> category) {
        sendMoEngageSearchAttempt(getActivity(), query, hasProductList, category);
    }

    @Override
    public void setFirstTimeLoad(boolean isFirstTimeLoad) {
        this.isFirstTimeLoad = isFirstTimeLoad;
    }

    @Override
    public void sendImpressionGlobalNav(GlobalNavViewModel globalNavViewModel) {
        List<Object> dataLayerList = new ArrayList<>();
        for (GlobalNavViewModel.Item item : globalNavViewModel.getItemList()) {
            dataLayerList.add(item.getGlobalNavItemAsObjectDataLayer());
        }
        SearchTracking.trackEventImpressionGlobalNavWidgetItem(trackingQueue, dataLayerList, globalNavViewModel.getKeyword());
    }

    @Override
    public boolean isAnyFilterActive() {
        return isFilterActive();
    }

    public void sendMoEngageSearchAttempt(Context context, String keyword, boolean isResultFound, HashMap<String, String> category) {
        Map<String, Object> value = DataLayer.mapOf(
                SearchEventTracking.MOENGAGE.KEYWORD, keyword,
                SearchEventTracking.MOENGAGE.IS_RESULT_FOUND, isResultFound
        );
        if (category != null) {
            value.put(SearchEventTracking.MOENGAGE.CATEGORY_ID_MAPPING, new JSONArray(Arrays.asList(category.keySet().toArray())));
            value.put(SearchEventTracking.MOENGAGE.CATEGORY_NAME_MAPPING, new JSONArray((category.values())));
        }
        TrackApp.getInstance().getMoEngage().sendTrackEvent(value, SearchEventTracking.EventMoEngage.SEARCH_ATTEMPT);
    }



    private boolean isShowCaseAllowed(String tag) {
        if (getActivity() == null) {
            return false;
        }
        return similarSearchManager.isSimilarSearchEnable() && !ShowCasePreference.hasShown(getActivity(), tag);
    }


    public void startShowCase() {
        final String showCaseTag = ProductListFragment.class.getName();
        if (!isShowCaseAllowed(showCaseTag)) {
            return;
        }
        if (showCaseDialog != null) {
            return;
        }

        final ArrayList<ShowCaseObject> showCaseList = new ArrayList<>();

        if (recyclerView == null)
            return;

        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getView() == null) {
                    return;
                }

                View itemView = scrollToShowCaseItem();
                if (itemView != null) {
                    showCaseList.add(
                            new ShowCaseObject(
                                    itemView.findViewById(R.id.container),
                                    getString(R.string.view_similar_item),
                                    getString(R.string.press_to_see_similar),
                                    ShowCaseContentPosition.BOTTOM));
                }

                if (showCaseList.isEmpty())
                    return;

                showCaseDialog = createShowCaseDialog();
                showCaseDialog.show(getActivity(), showCaseTag, showCaseList);
            }
        }, 300);
    }

    private ShowCaseDialog createShowCaseDialog() {
        return new ShowCaseBuilder()
                .customView(R.layout.item_top_ads_show_case)
                .titleTextColorRes(R.color.white)
                .spacingRes(R.dimen.spacing_show_case)
                .arrowWidth(R.dimen.arrow_width_show_case)
                .textColorRes(R.color.grey_400)
                .shadowColorRes(R.color.shadow)
                .backgroundContentColorRes(R.color.black)
                .textSizeRes(R.dimen.fontvs)
                .finishStringRes(R.string.megerti)
                .useCircleIndicator(true)
                .clickable(true)
                .useArrow(true)
                .useSkipWord(false)
                .build();
    }


    public View scrollToShowCaseItem() {
        if (recyclerView.getAdapter().getItemCount() >= PRODUCT_POSITION) {
            recyclerView.stopScroll();
            recyclerView.getLayoutManager().scrollToPosition(PRODUCT_POSITION + PRODUCT_POSITION);
            return ((GridLayoutManager) recyclerView.getLayoutManager()).findViewByPosition(PRODUCT_POSITION);
        }
        return null;
    }
}