package com.tokopedia.discovery.newdiscovery.search.fragment.product;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.EndlessRecyclerviewListener;
import com.tokopedia.core.discovery.model.DataValue;
import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.discovery.DiscoveryRouter;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.analytics.SearchTracking;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
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
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductViewModel;
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter;
import com.tokopedia.discovery.newdynamicfilter.controller.FilterController;
import com.tokopedia.discovery.newdynamicfilter.helper.OptionHelper;
import com.tokopedia.discovery.similarsearch.SimilarSearchManager;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Category;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.utils.ImpresionTask;
import com.tokopedia.trackingoptimizer.TrackingQueue;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.wishlist.common.listener.WishListActionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    private static final String ARG_VIEW_MODEL = "ARG_VIEW_MODEL";
    private static final String EXTRA_IS_FORCE_SEARCH = "EXTRA_IS_FORCE_SEARCH";
    private static final String EXTRA_ADDITIONAL_PARAMS = "EXTRA_ADDITIONAL_PARAMS";
    private static final String SEARCH_PRODUCT_TRACE = "search_product_trace";
    private static int PRODUCT_POSITION = 2;
    protected RecyclerView recyclerView;
    @Inject
    ProductListPresenter presenter;

    private EndlessRecyclerviewListener linearLayoutLoadMoreTriggerListener;
    private EndlessRecyclerviewListener gridLayoutLoadMoreTriggerListener;

    private UserSessionInterface userSession;
    private GCMHandler gcmHandler;
    private Config topAdsConfig;
    private ProductListAdapter adapter;
    private ProductListTypeFactory productListTypeFactory;
    private boolean isForceSearch;
    private String additionalParams = "";

    private SimilarSearchManager similarSearchManager;
    private ShowCaseDialog showCaseDialog;
    private PerformanceMonitoring performanceMonitoring;
    private TrackingQueue trackingQueue;

    private FilterController quickFilterController = new FilterController();

    @Deprecated
    public static ProductListFragment newInstance(ProductViewModel productViewModel) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_VIEW_MODEL, productViewModel);
        args.putParcelable(EXTRA_SEARCH_PARAMETER, productViewModel.getSearchParameter());
        ProductListFragment productListFragment = new ProductListFragment();
        productListFragment.setArguments(args);
        return productListFragment;
    }

    public static ProductListFragment newInstance(SearchParameter searchParameter, boolean isForceSearch) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_SEARCH_PARAMETER, searchParameter);
        args.putBoolean(EXTRA_IS_FORCE_SEARCH, isForceSearch);
        ProductListFragment productListFragment = new ProductListFragment();
        productListFragment.setArguments(args);
        return productListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        similarSearchManager = SimilarSearchManager.getInstance(getContext());
        loadDataFromArguments();
        userSession = new UserSession(getContext());
        gcmHandler = new GCMHandler(getContext());
        trackingQueue = new TrackingQueue(getContext());
    }

    private void loadDataFromArguments() {
        if(getArguments() != null) {
            copySearchParameter(getArguments().getParcelable(EXTRA_SEARCH_PARAMETER));
            isForceSearch = getArguments().getBoolean(EXTRA_IS_FORCE_SEARCH);
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
                .appComponent(getComponent(AppComponent.class))
                .build();
        component.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        presenter.attachView(this, this);
        presenter.setIsUsingFilterV4(isUsingBottomSheetFilter());
        return inflater.inflate(R.layout.fragment_base_discovery, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
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
        adsParams.getParam().put(TopAdsParams.KEY_SRC, BrowseApi.DEFAULT_VALUE_SOURCE_SEARCH); //[TODO replace source with source from parameters
        adsParams.getParam().put(TopAdsParams.KEY_QUERY, getQueryKey());
        adsParams.getParam().put(TopAdsParams.KEY_USER_ID, userSession.getUserId());

        adsParams.getParam().putAll(searchParameter.getSearchParameterHashMap());

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
    public void setProductList(List<Visitable> list) {
        isListEmpty = false;

        sendProductImpressionTrackingEvent(list);

        adapter.appendItems(list);
    }

    private void sendProductImpressionTrackingEvent(List<Visitable> list) {
        String userId = userSession.isLoggedIn() ? userSession.getUserId() : "";
        List<Object> dataLayerList = new ArrayList<>();
        for (Visitable object : list) {
            if (object instanceof ProductItem) {
                ProductItem item = (ProductItem) object;
                if (!item.isTopAds()) {
                    dataLayerList.add(item.getProductAsObjectDataLayer(userId));
                }
            }
        }
        SearchTracking.eventImpressionSearchResultProduct(getActivity(), dataLayerList, getQueryKey());
    }

    private void loadMoreProduct(final int startRow) {
        generateLoadMoreParameter(startRow);

        HashMap<String, String> additionalParamsMap
                = NetworkParamHelper.getParamMap(additionalParams);

        presenter.loadMoreData(getSearchParameter(), additionalParamsMap);
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
        getSearchParameter().set(SearchApiConst.UNIQUE_ID, generateUniqueId());
        getSearchParameter().set(SearchApiConst.USER_ID, generateUserId());
        getSearchParameter().set(SearchApiConst.START, String.valueOf(startRow));
    }

    private String generateUserId() {
        return userSession.isLoggedIn() ? userSession.getUserId() : "";
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
        reloadData();
    }

    @Override
    protected void requestDynamicFilter() {

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
    public void onItemClicked(ProductItem item, int adapterPosition) {
        Intent intent = getProductIntent(item.getProductID());
        intent.putExtra(ProductDetailRouter.WISHLIST_STATUS_UPDATED_POSITION, adapterPosition);
        sendItemClickTrackingEvent(item, adapterPosition);
        startActivityForResult(intent, REQUEST_CODE_GOTO_PRODUCT_DETAIL);
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
        similarSearchManager.startSimilarSearchIfEnable(getSearchParameter().getSearchQuery(), item);
    }

    private void sendItemClickTrackingEvent(ProductItem item, int pos) {
        String userId = userSession.isLoggedIn() ? userSession.getUserId() : "";
        if (item.isTopAds()) {
            new ImpresionTask().execute(item.getTopadsClickUrl());
            Product product = new Product();
            product.setId(item.getProductID());
            product.setName(item.getProductName());
            product.setPriceFormat(item.getPrice());
            product.setCategory(new Category(item.getCategoryID()));
            TopAdsGtmTracker.eventSearchResultProductClick(getContext(), getQueryKey(), product, pos);
        } else {
            SearchTracking.trackEventClickSearchResultProduct(
                    getActivity(),
                    item.getProductAsObjectDataLayer(userId),
                    item.getPageNumber(),
                    searchParameter.getSearchQuery(),
                    getSelectedFilter(),
                    getSelectedSort()
            );
        }
    }

    @Override
    public void onWishlistButtonClicked(final ProductItem productItem) {
        presenter.handleWishlistButtonClicked(productItem);
    }

    @Override
    public void onSuggestionClicked(String suggestedQuery) {
        performNewProductSearch(suggestedQuery, true);
    }

    @Override
    public void onBannerAdsClicked(String appLink) {
        TkpdCoreRouter router = ((TkpdCoreRouter) getActivity().getApplicationContext());
        if (router.isSupportedDelegateDeepLink(appLink)) {
            router.actionApplink(getActivity(), appLink);
        } else if (appLink != "") {
            Intent intent = new Intent(getContext(), BannerWebView.class);
            intent.putExtra("url", appLink);
            startActivity(intent);
        }
    }

    @Override
    public void onSearchGuideClicked(String keyword) {
        performNewProductSearch(keyword, true);
    }

    @Override
    public void onRelatedSearchClicked(String keyword) {
        SearchTracking.eventClickRelatedSearch(getContext(), getQueryKey(), keyword);
        performNewProductSearch(keyword, true);
    }

    @Override
    public boolean isQuickFilterSelected(Option option) {
        return quickFilterController.getFilterViewState(option.getUniqueId());
    }

    @Override
    public void onQuickFilterSelected(Option option) {
        boolean isQuickFilterSelectedReversed = !isQuickFilterSelected(option);

        setFilterToQuickFilterController(option, isQuickFilterSelectedReversed);
        applyFilterToSearchParameter(quickFilterController.getFilterParameter());
        setSelectedFilter(new HashMap<>(quickFilterController.getFilterParameter()));

        clearDataFilterSort();
        reloadData();

        UnifyTracking.eventSearchResultQuickFilter(getActivity(),option.getKey(), option.getValue(), isQuickFilterSelectedReversed);
    }

    private void setFilterToQuickFilterController(Option option, boolean isQuickFilterSelected) {
        if (option.isCategoryOption()) {
            quickFilterController.setFilter(option, isQuickFilterSelected, true);
        } else {
            quickFilterController.setFilter(option, isQuickFilterSelected);
        }
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
        UnifyTracking.eventSearchResultProductWishlistClick(getActivity(),true, getQueryKey());
        adapter.updateWishlistStatus(productId, true);
        enableWishlistButton(productId);
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_add_wishlist));
    }

    @Override
    public void onErrorRemoveWishlist(String errorMessage, String productId) {
        enableWishlistButton(productId);
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessRemoveWishlist(String productId) {
        UnifyTracking.eventSearchResultProductWishlistClick(getActivity(),false, getQueryKey());
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
        return searchParameter.getSearchQuery();
    }

    @Override
    public void setEmptyProduct() {
        isListEmpty = true;
        adapter.showEmptyState(getActivity(), getQueryKey(), isFilterActive(), getFlagFilterHelper(), getString(R.string.product_tab_title).toLowerCase());
        SearchTracking.eventSearchNoResult(getActivity(), getQueryKey(), getScreenName(), getSelectedFilter());
    }

    @Override
    protected void refreshAdapterForEmptySearch() {
        if (adapter != null) {
            adapter.showEmptyState(getActivity(), getQueryKey(), isFilterActive(), getFlagFilterHelper(), getString(R.string.product_tab_title).toLowerCase());
        }
    }

    @Override
    public void reloadData() {
        if (adapter == null) {
            return;
        }
        showRefreshLayout();
        adapter.clearData();
        adapter.notifyDataSetChanged();
        initTopAdsParams();
        generateLoadMoreParameter(0);
        performanceMonitoring = PerformanceMonitoring.start(SEARCH_PRODUCT_TRACE);
        presenter.loadData(getSearchParameter(), isForceSearch, getAdditionalParamsMap());
        TopAdsGtmTracker.getInstance().clearDataLayerList();
    }

    private HashMap<String, String> getAdditionalParamsMap() {
        if (isFilterActive()) {
            return NetworkParamHelper.getParamMap(additionalParams);
        } else {
            return new HashMap<>();
        }
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
        List<Option> quickFilterOptions = getOptionList(dynamicFilterModel);
        enrichWithInputState(quickFilterOptions);
        return quickFilterOptions;
    }

    private void enrichWithInputState(List<Option> optionList) {
        if (getFlagFilterHelper() == null) {
            return;
        }

        for (Option option : optionList) {
            option.setInputState(
                    OptionHelper.loadOptionInputState(option,
                            getFlagFilterHelper().getSavedCheckedState(),
                            getFlagFilterHelper().getSavedTextInput())
            );
        }
    }

    private ArrayList<Option> getOptionList(DataValue dynamicFilterModel) {
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
        quickFilterController.initFilterController(searchParameter.getSearchParameterHashMap(), quickFilterList);
    }

    @Override
    public void setAdditionalParams(String additionalParams) {
        this.additionalParams = additionalParams;
    }
}
