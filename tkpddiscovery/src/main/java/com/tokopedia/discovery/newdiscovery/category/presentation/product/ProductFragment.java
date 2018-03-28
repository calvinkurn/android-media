package com.tokopedia.discovery.newdiscovery.category.presentation.product;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.gms.tagmanager.DataLayer;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.URLParser;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.CategoryPageTracking;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.discovery.DiscoveryRouter;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.categorynav.view.CategoryNavigationActivity;
import com.tokopedia.discovery.newdiscovery.category.di.component.CategoryComponent;
import com.tokopedia.discovery.newdiscovery.category.di.component.DaggerCategoryComponent;
import com.tokopedia.discovery.newdiscovery.category.presentation.CategoryActivity;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter.CategoryProductListAdapter;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter.DefaultCategoryAdapter;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter.RevampCategoryAdapter;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter.listener.ItemClickListener;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter.typefactory.CategoryProductListTypeFactoryImpl;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.listener.WishlistActionListener;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.ChildCategoryModel;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.ProductItem;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.ProductViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionFragment;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionFragmentPresenter;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionGeneralAdapter;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.view.adapter.TopAdsRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

/**
 * @author by alifa on 10/26/17.
 */

public class ProductFragment extends SearchSectionFragment
        implements SearchSectionGeneralAdapter.OnItemChangeView, ProductContract.View,
        ItemClickListener, WishlistActionListener, TopAdsItemClickListener, TopAdsListener,
        DefaultCategoryAdapter.CategoryListener, RevampCategoryAdapter.CategoryListener {

    public static final int REQUEST_CODE_LOGIN = 1;
    private static final int REQUEST_CODE_GOTO_PRODUCT_DETAIL = 2;
    private static final int REQUEST_ACTIVITY_SORT_PRODUCT = 3;
    private static final int REQUEST_ACTIVITY_FILTER_PRODUCT = 4;

    private static final String ARG_VIEW_MODEL = "ARG_VIEW_MODEL";
    private static final String ARGS_TRACKER_ATTRIBUTION = "ARGS_TRACKER_ATTRIBUTION";
    private static final String ARG_URL = "ARG_URL";
    private static final String EXTRA_PRODUCT_LIST = "EXTRA_PRODUCT_LIST";
    private static final String LAST_POSITION_ENHANCE_PRODUCT = "LAST_POSITION_ENHANCE_PRODUCT";
    private static final String CATEGORY_ENHANCE_ANALYTIC = "CATEGORY_ENHANCE_ANALYTIC";

    protected RecyclerView recyclerView;
    protected SwipeRefreshLayout refreshLayout;
    @Inject
    ProductPresenter presenter;

    private SessionHandler sessionHandler;
    private GCMHandler gcmHandler;
    private Config topAdsConfig;
    private CategoryProductListAdapter adapter;
    protected TopAdsRecyclerAdapter topAdsRecyclerAdapter;
    private ProductViewModel productViewModel;
    private String trackerAttribution;

    private boolean isLoadingData;

    private LocalCacheHandler trackerProductCache;

    public static ProductFragment newInstance(ProductViewModel productViewModel, String trackerAttribution) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_VIEW_MODEL, productViewModel);
        args.putString(ARGS_TRACKER_ATTRIBUTION, trackerAttribution);
        ProductFragment productListFragment = new ProductFragment();
        productListFragment.setArguments(args);
        return productListFragment;
    }

    public static ProductFragment newInstance(ProductViewModel productViewModel, String categoryUrl, String trackerAttribution) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_VIEW_MODEL, productViewModel);
        args.putString(ARG_URL,categoryUrl);
        args.putString(ARGS_TRACKER_ATTRIBUTION, trackerAttribution);
        ProductFragment productListFragment = new ProductFragment();
        productListFragment.setArguments(args);
        return productListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            loadDataFromSavedState(savedInstanceState);
        } else {
            loadDataFromArguments();
        }
        sessionHandler = new SessionHandler(getContext());
        gcmHandler = new GCMHandler(getContext());
        trackerProductCache = new LocalCacheHandler(getActivity(), CATEGORY_ENHANCE_ANALYTIC);
        clearLastProductTracker(true);
    }

    private void loadDataFromSavedState(Bundle savedInstanceState) {
        productViewModel = savedInstanceState.getParcelable(EXTRA_PRODUCT_LIST);
    }

    private void loadDataFromArguments() {
        productViewModel = getArguments().getParcelable(ARG_VIEW_MODEL);
        trackerAttribution = getArguments().getString(ARGS_TRACKER_ATTRIBUTION, "none / other");
        if (!TextUtils.isEmpty(getArguments().getString(ARG_URL))) {
            URLParser urlParser = new URLParser(getArguments().getString(ARG_URL));
            setSelectedFilter(urlParser.getParamKeyValueMap());
        }
    }

    @Override
    protected void initInjector() {
        CategoryComponent component = DaggerCategoryComponent.builder()
                .appComponent(getComponent(AppComponent.class))
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);
        initTopAdsConfig();
        initTopAdsParams();
        setupAdapter();
        setupListener();
    }

    private void initTopAdsConfig() {
        topAdsConfig = new Config.Builder()
                .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                .setUserId(SessionHandler.getLoginID(getActivity()))
                .setEndpoint(Endpoint.PRODUCT)
                .build();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState!=null) {
            switchLayoutType();
        }
    }

    private void bindView(View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadData();
            }
        });
    }

    private void setupAdapter() {
        if (productViewModel.getProductList() != null && !productViewModel.getProductList().isEmpty()) {
            productViewModel.setProductList(mappingTrackerProduct(productViewModel.getProductList(), 1));
            trackEnhanceProduct(createImpressionProductDataLayer(productViewModel.getProductList()));
        }
        adapter = new CategoryProductListAdapter(this,
                productViewModel.getCategoryHeaderModel(),
                productViewModel.getProductList(),
                new CategoryProductListTypeFactoryImpl(this,this,this, topAdsConfig)
        );
        topAdsRecyclerAdapter = new TopAdsRecyclerAdapter(getActivity(), adapter);
        topAdsRecyclerAdapter.setHasHeader(true);
        topAdsRecyclerAdapter.setConfig(topAdsConfig);
        topAdsRecyclerAdapter.setSpanSizeLookup(onSpanSizeLookup());
        recyclerView.setAdapter(topAdsRecyclerAdapter);
        topAdsRecyclerAdapter.setLayoutManager(getGridLayoutManager());
        topAdsRecyclerAdapter.setOnLoadListener(new TopAdsRecyclerAdapter.OnLoadListener() {
            @Override
            public void onLoad(int page, int totalCount) {
                if (!isLoadingData) {
                    loadMoreProduct(adapter.getItemList().size() - 1);
                }
            }
        });
        if (adapter.getItemCount() == 0) {
            showEmptyProduct();
            showBottomBarNavigation(false);
        } else {
            showBottomBarNavigation(true);
        }
    }

    @Override
    public void trackEnhanceProduct(Map<String, Object> dataLayer) {
        CategoryPageTracking.eventEnhance(dataLayer);
    }

    @Override
    public List<ProductItem> mappingTrackerProduct(List<ProductItem> productList, int page) {
        int lastPositionProduct = getLastPositionProductTracker();
        for (int i = 0; i < productList.size(); i++) {
            lastPositionProduct++;
            ProductItem item = productList.get(i);
            item.setTrackerName(String.format(
                    Locale.getDefault(),
                    "/category/%s - product %d",
                    productViewModel.getCategoryHeaderModel().getHeaderModel().getCategoryName(),
                    page)
            );
            item.setTrackerPosition(String.valueOf(lastPositionProduct));
            item.setHomeAttribution(trackerAttribution);
            productList.set(i, item);
        }
        return productList;
    }

    @Override
    public void clearLastProductTracker(boolean clear) {
        if (clear) {
            LocalCacheHandler.clearCache(getActivity(), CATEGORY_ENHANCE_ANALYTIC);
        }
    }

    @Override
    public int getLastPositionProductTracker() {
        return trackerProductCache.getInt(LAST_POSITION_ENHANCE_PRODUCT, 0);
    }

    @Override
    public void setLastPositionProductTracker(int lastPositionProductTracker) {
        trackerProductCache.putInt(LAST_POSITION_ENHANCE_PRODUCT, lastPositionProductTracker);
        trackerProductCache.applyEditor();
    }

    @Override
    public Map<String, Object> createImpressionProductDataLayer(List<ProductItem> productList) {
        List<Map<String, Object>> productListDataLayer = new ArrayList<>();
        for (ProductItem model : productList) {
            productListDataLayer.add(model.generateImpressionDataLayer());
        }
        return DataLayer.mapOf(
                "event", "productView",
                "eventCategory", "category page",
                "eventAction", "product list impression",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "impressions", DataLayer.listOf(
                                productListDataLayer.toArray(new Object[productListDataLayer.size()])

                        ))
        );
    }

    public Map<String, Object> createClickProductDataLayer(Map<String, Object> productMap, String trackerName) {
        return DataLayer.mapOf("event", "productClick",
                "eventCategory", "hotlist page",
                "eventAction", "click product curation",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "click", DataLayer.mapOf(
                                "actionField", DataLayer.mapOf("list", trackerName),
                                "products", DataLayer.listOf(
                                        productMap
                                )
                        )
                )
        );
    }

    private void setupListener() {
        topAdsRecyclerAdapter.setAdsItemClickListener(this);
        topAdsRecyclerAdapter.setTopAdsListener(this);
    }

    @Override
    public void initTopAdsParams() {
        TopAdsParams adsParams = new TopAdsParams();
        adsParams.getParam().put(TopAdsParams.KEY_SRC, BrowseApi.DEFAULT_VALUE_SOURCE_DIRECTORY);
        adsParams.getParam().put(TopAdsParams.KEY_DEPARTEMENT_ID,
                productViewModel.getCategoryHeaderModel().getDepartementId());
        enrichWithFilterAndSortParams(adsParams);
        topAdsConfig.setTopAdsParams(adsParams);
    }

    private void loadDataProduct(final int startRow) {
        SearchParameter searchParameter = new SearchParameter();
        searchParameter.setUniqueID(generateUniqueId());
        searchParameter.setUserID(generateUserId());
        searchParameter.setDepartmentId(productViewModel.getCategoryHeaderModel().getDepartementId());
        searchParameter.setStartRow(startRow);
        searchParameter.setSource(BrowseApi.DEFAULT_VALUE_SOURCE_DIRECTORY);
        presenter.loadDataProduct(searchParameter, productViewModel.getCategoryHeaderModel());
    }

    private void loadMoreProduct(final int startRow) {
        SearchParameter searchParameter = new SearchParameter();
        searchParameter.setUniqueID(generateUniqueId());
        searchParameter.setUserID(generateUserId());
        searchParameter.setDepartmentId(productViewModel.getCategoryHeaderModel().getDepartementId());
        searchParameter.setStartRow(startRow);
        searchParameter.setSource(BrowseApi.DEFAULT_VALUE_SOURCE_DIRECTORY);
        isLoadingData = true;
        presenter.loadMore(searchParameter, new ProductPresenter.LoadMoreListener() {
            @Override
            public void onSuccess(List<ProductItem> productItemList) {
                int page = (startRow / 12) + 1;
                clearLastProductTracker(page == 1);
                if (productItemList.isEmpty()) {
                    unSetTopAdsEndlessListener();
                    showBottomBarNavigation(false);
                } else {
                    List<ProductItem> resultMapper = mappingTrackerProduct(productItemList, page);
                    trackEnhanceProduct(createImpressionProductDataLayer(resultMapper));
                    List<Visitable> list = new ArrayList<Visitable>();
                    list.addAll(resultMapper);
                    setProductList(list);
                    showBottomBarNavigation(true);
                }
                isLoadingData = false;
                hideRefreshLayout();
            }

            @Override
            public void onFailed() {
                showNetworkError(startRow);
                isLoadingData = false;
            }
        });
    }

    @Override
    public void showNetworkError(final int startRow) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                loadMoreProduct(startRow);
            }
        }).showRetrySnackbar();
    }

    private String generateUserId() {
        return sessionHandler.isV4Login() ? sessionHandler.getLoginID() : null;
    }

    private String generateUniqueId() {
        return sessionHandler.isV4Login() ?
                AuthUtil.md5(sessionHandler.getLoginID()) :
                AuthUtil.md5(gcmHandler.getRegistrationId());
    }

    @Override
    public String getScreenNameId() {
        return AppScreen.SCREEN_BROWSE_PRODUCT_FROM_CATEGORY;
    }

    @Override
    protected List<AHBottomNavigationItem> getBottomNavigationItems() {
        List<AHBottomNavigationItem> items = new ArrayList<>();
        items.add(new AHBottomNavigationItem(getString(R.string.sort), R.drawable.ic_sort_black));
        items.add(new AHBottomNavigationItem(getString(R.string.filter), R.drawable.ic_filter_list_black));
        items.add(new AHBottomNavigationItem(getString(adapter.getTitleTypeRecyclerView()), adapter.getIconTypeRecyclerView()));
        items.add(new AHBottomNavigationItem(getString(R.string.title_category), R.drawable.ic_category_black));
        return items;
    }

    @Override
    protected AHBottomNavigation.OnTabSelectedListener getBottomNavClickListener() {
        return new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(final int position, boolean wasSelected) {
                switch (position) {
                    case 0:
                        openSortActivity();
                        return true;
                    case 1:
                        openFilterActivity();
                        return true;
                    case 2:
                        switchLayoutType();
                        return true;
                    case 3:
                        String currentCategoryId = productViewModel.getCategoryHeaderModel().getDepartementId();
                        String currentRootCategoryId = productViewModel.getCategoryHeaderModel().getRootCategoryId();
                        UnifyTracking.eventBottomCategoryNavigation(currentRootCategoryId, currentCategoryId);
                        Intent intent = CategoryNavigationActivity.createInstance(getActivity(), productViewModel.getCategoryHeaderModel().getDepartementId());
                        startActivityForResult(intent, CategoryNavigationActivity.DESTROY_BROWSE_PARENT);
                        getActivity().overridePendingTransition(com.tokopedia.core.R.anim.pull_up, android.R.anim.fade_out);
                        return true;
                    default:
                        return false;
                }
            }
        };
    }

    @Override
    protected GridLayoutManager.SpanSizeLookup onSpanSizeLookup() {
        return new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.isEmptyItem(position) || topAdsRecyclerAdapter.isLoading(position) ||
                        topAdsRecyclerAdapter.isTopAdsViewHolder(position) || adapter.isCategoryHeader(position)) {
                    return spanCount;
                } else {
                    return 1;
                }
            }
        };
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_PRODUCT_LIST, productViewModel);
    }

    @Override
    protected void onFirstTimeLaunch() {
        super.onFirstTimeLaunch();
        getDynamicFilter();
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
        trackEnhanceProduct(createClickProductDataLayer(item.generateClickDataLayer(), item.getTrackerName()));
        com.tokopedia.core.var.ProductItem data = new com.tokopedia.core.var.ProductItem();
        data.setId(item.getProductID());
        data.setName(item.getProductName());
        data.setPrice(item.getPrice());
        data.setImgUri(item.getImageUrl());
        Bundle bundle = new Bundle();
        Intent intent = ProductDetailRouter.createInstanceProductDetailInfoActivity(getActivity());
        bundle.putParcelable(ProductDetailRouter.EXTRA_PRODUCT_ITEM, data);
        intent.putExtras(bundle);
        intent.putExtra(ProductDetailRouter.WISHLIST_STATUS_UPDATED_POSITION, adapterPosition);
        startActivityForResult(intent, REQUEST_CODE_GOTO_PRODUCT_DETAIL);
    }

    @Override
    public void onWishlistButtonClicked(ProductItem productItem, int adapterPosition) {
        presenter.handleWishlistButtonClicked(productItem, topAdsRecyclerAdapter.getOriginalPosition(adapterPosition));
    }

    @Override
    public void onErrorAddWishList(String errorMessage, int adapterPosition) {
        enableWishlistButton(adapterPosition);
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessAddWishlist(int adapterPosition) {
        adapter.updateWishlistStatus(adapterPosition, true);
        enableWishlistButton(adapterPosition);
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_add_wishlist));
    }

    @Override
    public void onErrorRemoveWishlist(String errorMessage, int adapterPosition) {
        enableWishlistButton(adapterPosition);
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessRemoveWishlist(int adapterPosition) {
        adapter.updateWishlistStatus(adapterPosition, false);
        enableWishlistButton(adapterPosition);
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_remove_wishlist));
    }

    @Override
    public String getDepartmentId() {
        if (productViewModel == null ||
                productViewModel.getCategoryHeaderModel() == null) {
            return "0";
        }
        return productViewModel.getCategoryHeaderModel().getDepartementId();
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
        return SessionHandler.isV4Login(getContext());
    }

    @Override
    public String getUserId() {
        return SessionHandler.getLoginID(getContext());
    }

    @Override
    public void disableWishlistButton(int adapterPosition) {
        adapter.setWishlistButtonEnabled(adapterPosition, false);
    }

    @Override
    public void enableWishlistButton(int adapterPosition) {
        adapter.setWishlistButtonEnabled(adapterPosition, true);
    }

    @Override
    protected void reloadData() {
        adapter.clearData();
        initTopAdsParams();
        topAdsRecyclerAdapter.reset();
        topAdsRecyclerAdapter.setConfig(topAdsConfig);
        showBottomBarNavigation(false);
        loadDataProduct(0);
    }

    @Override
    public void showRefreshLayout() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void hideRefreshLayout() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    protected void onSwipeToRefresh() {
        showRefreshLayout();
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
        topAdsRecyclerAdapter.setLayoutManager(getLinearLayoutManager());
    }

    @Override
    public void onChangeDoubleGrid() {
        topAdsRecyclerAdapter.setLayoutManager(getGridLayoutManager());
    }

    @Override
    public void onChangeSingleGrid() {
        topAdsRecyclerAdapter.setLayoutManager(getGridLayoutManager());
    }

    @Override
    public void onTopAdsLoaded() {

    }

    @Override
    public void onTopAdsFailToLoad(int errorCode, String message) {
        topAdsRecyclerAdapter.hideLoading();
    }

    @Override
    public void onProductItemClicked(Product product) {
        com.tokopedia.core.var.ProductItem data = new com.tokopedia.core.var.ProductItem();
        data.setId(product.getId());
        data.setName(product.getName());
        data.setPrice(product.getPriceFormat());
        data.setImgUri(product.getImage().getM_url());
        Bundle bundle = new Bundle();
        Intent intent = ProductDetailRouter.createInstanceProductDetailInfoActivity(getActivity());
        bundle.putParcelable(ProductDetailRouter.EXTRA_PRODUCT_ITEM, data);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_CODE_GOTO_PRODUCT_DETAIL);
    }

    @Override
    public void onShopItemClicked(Shop shop) {
        Bundle bundle = ShopInfoActivity.createBundle(shop.getId(), "");
        Intent intent = new Intent(getActivity(), ShopInfoActivity.class);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    @Override
    public void onAddFavorite(int position, Data data) {

    }

    @Override
    public void onCategoryClick(ChildCategoryModel child) {
        UnifyTracking.eventLevelCategory(productViewModel.getCategoryHeaderModel().getDepartementId()
                , child.getCategoryId());
        CategoryActivity.moveTo(
                getActivity(),
                child.getCategoryId(),
                child.getCategoryName(),
                true,
                ""
        );
    }

    @Override
    public void onCategoryRevampClick(ChildCategoryModel child) {
        UnifyTracking.eventLevelCategory(productViewModel.getCategoryHeaderModel().getDepartementId(),
                child.getCategoryId());
        CategoryActivity.moveTo(
                getActivity(),
                child.getCategoryId(),
                child.getCategoryName(),
                true,
                ""
        );
    }

    @Override
    public void onBannerAdsClicked(String appLink) {
        if (!TextUtils.isEmpty(appLink)) {
            ((TkpdCoreRouter) getActivity().getApplication()).actionApplink(getActivity(), appLink);
        }
    }

    @Override
    public void setTopAdsEndlessListener() {
        topAdsRecyclerAdapter.setEndlessScrollListener();
    }

    @Override
    public void unSetTopAdsEndlessListener() {
        topAdsRecyclerAdapter.unsetEndlessScrollListener();
        topAdsRecyclerAdapter.hideLoading();
    }

    @Override
    public void backToTop() {
        recyclerView.scrollToPosition(0);
    }

    public void setProductList(List<Visitable> productList) {
        adapter.appendItems(productList);
    }

    @Override
    public void showEmptyProduct() {
        topAdsRecyclerAdapter.shouldLoadAds(false);
        adapter.showEmpty();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    protected void screenTrack() {
        if (getDepartmentId() != null && !getDepartmentId().equals("0")) {
            ScreenTracking.eventDiscoveryScreenAuth(getDepartmentId());
        }
    }

    @Override
    protected String getScreenName() {
        return getScreenNameId();
    }
}

