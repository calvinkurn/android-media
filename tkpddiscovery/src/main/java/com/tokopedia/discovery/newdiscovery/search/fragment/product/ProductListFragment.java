package com.tokopedia.discovery.newdiscovery.search.fragment.product;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.core.analytics.AppScreen;
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
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.HeaderViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.TopAdsViewModel;
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter;
import com.tokopedia.discovery.newdynamicfilter.helper.FilterFlagSelectedModel;
import com.tokopedia.discovery.newdynamicfilter.helper.FilterHelper;
import com.tokopedia.discovery.newdynamicfilter.helper.OptionHelper;
import com.tokopedia.discovery.similarsearch.SimilarSearchManager;
import com.tokopedia.showcase.ShowCaseBuilder;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
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

    public static final int REQUEST_CODE_LOGIN = 561;
    private static final int REQUEST_CODE_GOTO_PRODUCT_DETAIL = 123;
    private static final int REQUEST_ACTIVITY_SORT_PRODUCT = 1233;
    private static final int REQUEST_ACTIVITY_FILTER_PRODUCT = 4320;

    private static final String ARG_VIEW_MODEL = "ARG_VIEW_MODEL";
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
    private ProductViewModel productViewModel;
    private ProductListTypeFactory productListTypeFactory;
    private boolean forceSearch;

    private SimilarSearchManager similarSearchManager;
    private ShowCaseDialog showCaseDialog;
    private PerformanceMonitoring performanceMonitoring;

    public static ProductListFragment newInstance(ProductViewModel productViewModel) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_VIEW_MODEL, productViewModel);
        args.putParcelable(EXTRA_SEARCH_PARAMETER, productViewModel.getSearchParameter());
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
    }

    private void loadDataFromArguments() {
        if(getArguments() != null) {
            productViewModel = getArguments().getParcelable(ARG_VIEW_MODEL);
            if (productViewModel != null) {
                setForceSearch(productViewModel.isForceSearch());
                renderDynamicFilter(productViewModel.getDynamicFilterModel());
            }

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


    private boolean isAllowLoadMore() {
        return getUserVisibleHint()
                && adapter.hasNextPage();
    }

    private void initTopAdsConfig() {
        topAdsConfig = new Config.Builder()
                .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                .setUserId(userSession.getUserId())
                .setEndpoint(Endpoint.PRODUCT)
                .build();
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
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
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
        if (productViewModel.getProductList().isEmpty()) {
            setEmptyProduct();
        } else {
            setProductList(initMappingProduct());
        }

        if (productViewModel.getTotalData() > Integer.parseInt(BrowseApi.DEFAULT_VALUE_OF_PARAMETER_ROWS)) {
            adapter.addLoading();
        }
        adapter.setTotalData(productViewModel.getTotalData());
    }

    private List<Visitable> initMappingProduct() {
        List<Visitable> list = new ArrayList<>();
        HeaderViewModel headerViewModel = new HeaderViewModel();
        headerViewModel.setSuggestionModel(productViewModel.getSuggestionModel());
        if (productViewModel.getGuidedSearchViewModel() != null) {
            headerViewModel.setGuidedSearch(productViewModel.getGuidedSearchViewModel());
        }
        if (productViewModel.getQuickFilterModel() != null
                && productViewModel.getQuickFilterModel().getFilter() != null) {
            headerViewModel.setQuickFilterList(getQuickFilterOptions(productViewModel.getQuickFilterModel()));
        }
        if (productViewModel.getCpmModel() != null) {
            headerViewModel.setCpmModel(productViewModel.getCpmModel());
        }
        list.add(headerViewModel);
        if (!productViewModel.getAdsModel().getData().isEmpty()) {
            list.add(new TopAdsViewModel(productViewModel.getAdsModel(), productViewModel.getQuery()));
        }
        list.addAll(productViewModel.getProductList());
        if (productViewModel.getRelatedSearchModel() != null) {
            list.add(productViewModel.getRelatedSearchModel());
        }
        return list;
    }

    private void setupListener() {
        recyclerView.addOnScrollListener(getRecyclerViewBottomSheetScrollListener());
        gridLayoutLoadMoreTriggerListener = new EndlessRecyclerviewListener(getGridLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (isAllowLoadMore()) {
                    loadMoreProduct(adapter.getStartFrom());
                } else {
                    adapter.removeLoading();
                }
            }
        };

        linearLayoutLoadMoreTriggerListener = new EndlessRecyclerviewListener(getLinearLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (isAllowLoadMore()) {
                    loadMoreProduct(adapter.getStartFrom());
                } else {
                    adapter.removeLoading();
                }
            }
        };
        if (productViewModel.getTotalData() > Integer.parseInt(BrowseApi.DEFAULT_VALUE_OF_PARAMETER_ROWS)) {
            recyclerView.addOnScrollListener(gridLayoutLoadMoreTriggerListener);
        }
    }

    @Override
    public void initTopAdsParams() {
        TopAdsParams adsParams = new TopAdsParams();
        adsParams.getParam().put(TopAdsParams.KEY_SRC, BrowseApi.DEFAULT_VALUE_SOURCE_SEARCH); //[TODO replace source with source from parameters
        adsParams.getParam().put(TopAdsParams.KEY_QUERY, getQueryKey());
        adsParams.getParam().put(TopAdsParams.KEY_USER_ID, userSession.getUserId());
        adsParams.getParam().putAll(getAdditionalParams());

        if (getFlagFilterHelper() != null &&
                !TextUtils.isEmpty(getFlagFilterHelper().getCategoryId())) {
            adsParams.getParam().put(TopAdsParams.KEY_DEPARTEMENT_ID, getFlagFilterHelper().getCategoryId());
        } else if (canGetDepartmentIdFromSearchParameter()) {
            adsParams.getParam().put(TopAdsParams.KEY_DEPARTEMENT_ID, getSearchParameter().get(SearchApiConst.SC));
        }
        enrichWithFilterAndSortParams(adsParams);
        topAdsConfig.setTopAdsParams(adsParams);
    }

    private boolean canGetDepartmentIdFromSearchParameter() {
        String departmentId = getSearchParameter().get(SearchApiConst.SC);
        return !departmentId.isEmpty() && !departmentId.equals("0");
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
        sendProductImpressionTrackingEvent(list);

        adapter.appendItems(list);
    }

    private void sendProductImpressionTrackingEvent(List<Visitable> list) {
        String userId = userSession.isLoggedIn() ? userSession.getUserId() : "";
        List<Object> dataLayerList = new ArrayList<>();
        for (Visitable object : list) {
            if (object instanceof ProductItem) {
                dataLayerList.add(((ProductItem) object).getProductAsObjectDataLayer(userId));
            }
        }
        SearchTracking.eventImpressionSearchResultProduct(getActivity(), dataLayerList, getQueryKey());
    }

    private void loadMoreProduct(final int startRow) {
        generateLoadMoreParameter(startRow);

        HashMap<String, String> additionalParams
                = NetworkParamHelper.getParamMap(productViewModel.getAdditionalParams());

        presenter.loadMoreData(getSearchParameter(), additionalParams);
    }

    @Override
    public void showNetworkError(final int startRow) {
        if (adapter.isListEmpty()) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    reloadData();
                }
            });
        } else {
            NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    adapter.setStartFrom(startRow);
                    loadMoreProduct(startRow);
                }
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
        return AppScreen.SCREEN_SEARCH_PAGE_PRODUCT_TAB;
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
                        adapter.isTopAds(position) ||
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
    public void onItemClicked(ProductItem item, int adapterPosition) {
        com.tokopedia.core.var.ProductItem data = new com.tokopedia.core.var.ProductItem();
        data.setId(item.getProductID());
        data.setName(item.getProductName());
        data.setPrice(item.getPrice());
        data.setImgUri(item.getImageUrl());
        data.setIsWishlist(item.isWishlisted());
        data.setRating(Integer.toString(item.getRating()));
        data.setReviewCount(Integer.toString(item.getCountReview()));
        data.setCountCourier(item.getCountCourier());
        data.setDiscountPercentage(item.getDiscountPercentage());
        data.setOriginalPrice(item.getOriginalPrice());
        data.setShop(item.getShopName());
        data.setShopLocation(item.getShopCity());
        data.setOfficial(item.isOfficial());

        if (item.getLabelList() != null) {
            for (int i = 0; i < item.getLabelList().size(); i++) {
                if (item.getLabelList().get(i).getTitle().toLowerCase()
                        .contains(com.tokopedia.core.var.ProductItem.CASHBACK)) {
                    data.setCashback(item.getLabelList().get(i).getTitle());
                    break;
                }
            }
        }

        Bundle bundle = new Bundle();
        Intent intent = ProductDetailRouter.createInstanceProductDetailInfoActivity(getActivity());
        bundle.putParcelable(ProductDetailRouter.EXTRA_PRODUCT_ITEM, data);
        intent.putExtras(bundle);
        intent.putExtra(ProductDetailRouter.WISHLIST_STATUS_UPDATED_POSITION, adapterPosition);
        sendItemClickTrackingEvent(item);
        startActivityForResult(intent, REQUEST_CODE_GOTO_PRODUCT_DETAIL);
    }

    @Override
    public void onLongClick(ProductItem item, int adapterPosition) {
        similarSearchManager.startSimilarSearchIfEnable(getSearchParameter().getSearchQuery(), item);
    }

    private void sendItemClickTrackingEvent(ProductItem item) {
        String userId = userSession.isLoggedIn() ?
                userSession.getUserId() : "";

        SearchTracking.trackEventClickSearchResultProduct(
                getActivity(),
                item.getProductAsObjectDataLayer(userId),
                item.getPageNumber(),
                productViewModel.getQuery(),
                getSelectedFilter(),
                getSelectedSort()
        );
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
    public void onQuickFilterSelected(Option option) {
        boolean isQuickFilterSelected = !Boolean.parseBoolean(option.getInputState());
        if (getFlagFilterHelper() == null) {
            setFlagFilterHelper(new FilterFlagSelectedModel());
            getFlagFilterHelper().setSavedCheckedState(new HashMap<String, Boolean>());
            getFlagFilterHelper().setSavedTextInput(new HashMap<String, String>());
        }

        if (isQuickFilterSelected) {
            getFlagFilterHelper().getSavedCheckedState().put(option.getUniqueId(), true);
        } else {
            getFlagFilterHelper().getSavedCheckedState().remove(option.getUniqueId());
        }

        if (getSelectedFilter() == null) {
            setSelectedFilter(new HashMap<String, String>());
        }
        String mapValue = getSelectedFilter().get(option.getKey());
        if (TextUtils.isEmpty(mapValue)) {
            mapValue = option.getValue();
        } else if (isQuickFilterSelected) {
            mapValue += "," + option.getValue();
        } else {
            mapValue = removeValue(mapValue, option.getValue());
        }
        getSelectedFilter().put(option.getKey(), mapValue);
        clearDataFilterSort();
        if (option.isCategoryOption()) {
            if (isQuickFilterSelected) {
                getSearchParameter().set(SearchApiConst.SC, option.getValue());
            } else {
                getSearchParameter().remove(SearchApiConst.SC);
            }
        }
        if (option.isOfficialOption()) {
            getSearchParameter().set(SearchApiConst.OFFICIAL, String.valueOf(isQuickFilterSelected));
        }
        reloadData();
        UnifyTracking.eventSearchResultQuickFilter(getActivity(),option.getKey(), option.getValue(), isQuickFilterSelected);
    }

    @Override
    public void setSelectedFilter(HashMap<String, String> selectedFilter) {
        super.setSelectedFilter(selectedFilter);
        if (selectedFilter == null) {
            return;
        }

        getSearchParameter().getSearchParameterHashMap().putAll(selectedFilter);
    }

    @Override
    public void onSelectedFilterRemoved(String uniqueId) {
        removeSelectedFilter(uniqueId);
    }

    @Override
    protected void removeSelectedFilter(String uniqueId) {
        String optionKey = OptionHelper.parseKeyFromUniqueId(uniqueId);

        searchParameter.remove(optionKey);

        super.removeSelectedFilter(uniqueId);
    }

    @Override
    public void onEmptyButtonClicked() {
        showSearchInputView();
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
        return productViewModel.getQuery();
    }

    @Override
    public void setEmptyProduct() {
        adapter.showEmptyState(getActivity(), productViewModel.getQuery(), isFilterActive(), getFlagFilterHelper(), getString(R.string.product_tab_title).toLowerCase());
        SearchTracking.eventSearchNoResult(getActivity(), productViewModel.getQuery(), getScreenName(), getSelectedFilter());
    }

    @Override
    protected void updateDepartmentId(String deptId) {
        getSearchParameter().set(SearchApiConst.SC, deptId);
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
        presenter.loadData(getSearchParameter(), isForceSearch(), getAdditionalParams());
    }

    private HashMap<String, String> getAdditionalParams() {
        if (isFilterActive()) {
            return NetworkParamHelper.getParamMap(productViewModel.getAdditionalParams());
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

    public boolean isForceSearch() {
        return forceSearch;
    }

    public void setForceSearch(boolean forceSearch) {
        this.forceSearch = forceSearch;
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
            for (Option option : filter.getOptions()) {
                optionList.add(option);
            }
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

    @Override
    public void addLoading() {
        adapter.addLoading();
    }

    @Override
    public void removeLoading() {
        adapter.removeLoading();
    }
}
