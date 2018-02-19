package com.tokopedia.discovery.newdiscovery.search.fragment.product;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.SearchTracking;
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
import com.tokopedia.discovery.newdiscovery.di.component.DaggerSearchComponent;
import com.tokopedia.discovery.newdiscovery.di.component.SearchComponent;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionFragment;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionFragmentPresenter;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionGeneralAdapter;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.ProductListAdapter;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.listener.ItemClickListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.typefactory.ProductListTypeFactory;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.typefactory.ProductListTypeFactoryImpl;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.helper.NetworkParamHelper;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.listener.WishlistActionListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.GuidedSearchViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.HeaderViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductViewModel;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;
import com.tokopedia.discovery.newdynamicfilter.RevampedDynamicFilterActivity;
import com.tokopedia.discovery.newdynamicfilter.helper.FilterFlagSelectedModel;
import com.tokopedia.discovery.newdynamicfilter.helper.PreFilterHelper;
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
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by henrypriyono on 10/6/17.
 */

public class ProductListFragment extends SearchSectionFragment
        implements SearchSectionGeneralAdapter.OnItemChangeView, ProductListFragmentView,
        ItemClickListener, WishlistActionListener, TopAdsItemClickListener, TopAdsListener {

    public static final int REQUEST_CODE_LOGIN = 561;
    private static final int REQUEST_CODE_GOTO_PRODUCT_DETAIL = 123;
    private static final int REQUEST_ACTIVITY_SORT_PRODUCT = 1233;
    private static final int REQUEST_ACTIVITY_FILTER_PRODUCT = 4320;

    private static final String ARG_VIEW_MODEL = "ARG_VIEW_MODEL";
    private static final String EXTRA_PRODUCT_LIST = "EXTRA_PRODUCT_LIST";
    private static final String EXTRA_SEARCH_PARAMETER = "EXTRA_SEARCH_PARAMETER";
    private static final String EXTRA_FORCE_SEARCH = "EXTRA_FORCE_SEARCH";

    protected RecyclerView recyclerView;
    @Inject
    ProductListPresenter presenter;

    private SessionHandler sessionHandler;
    private GCMHandler gcmHandler;
    private Config topAdsConfig;
    private ProductListAdapter adapter;
    protected TopAdsRecyclerAdapter topAdsRecyclerAdapter;
    private ProductViewModel productViewModel;
    private ProductListTypeFactory productListTypeFactory;
    private SearchParameter searchParameter;
    private boolean forceSearch;

    public static ProductListFragment newInstance(ProductViewModel productViewModel) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_VIEW_MODEL, productViewModel);
        ProductListFragment productListFragment = new ProductListFragment();
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
    }

    private void loadDataFromSavedState(Bundle savedInstanceState) {
        productViewModel = savedInstanceState.getParcelable(EXTRA_PRODUCT_LIST);
        setSearchParameter((SearchParameter) savedInstanceState.getParcelable(EXTRA_SEARCH_PARAMETER));
        setForceSearch(savedInstanceState.getBoolean(EXTRA_FORCE_SEARCH));
    }

    private void loadDataFromArguments() {
        productViewModel = getArguments().getParcelable(ARG_VIEW_MODEL);
        if (productViewModel != null) {
            setSearchParameter(productViewModel.getSearchParameter());
            setForceSearch(productViewModel.isForceSearch());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState!=null) {
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
                .setUserId(SessionHandler.getLoginID(getActivity()))
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
        productListTypeFactory = new ProductListTypeFactoryImpl(this, topAdsConfig);
        adapter = new ProductListAdapter(getActivity(), this, productListTypeFactory);
        topAdsRecyclerAdapter = new TopAdsRecyclerAdapter(getActivity(), adapter);
        topAdsRecyclerAdapter.setConfig(topAdsConfig);
        topAdsRecyclerAdapter.setSpanSizeLookup(onSpanSizeLookup());
        recyclerView.setAdapter(topAdsRecyclerAdapter);
        topAdsRecyclerAdapter.setLayoutManager(getGridLayoutManager());
        topAdsRecyclerAdapter.setOnLoadListener(new TopAdsRecyclerAdapter.OnLoadListener() {
            @Override
            public void onLoad(int page, int totalCount) {
                if (isAllowLoadMore()) {
                    loadMoreProduct(adapter.getStartFrom());
                } else {
                    topAdsRecyclerAdapter.hideLoading();
                }
            }
        });

        if (productViewModel.getProductList().isEmpty()) {
            setEmptyProduct();
            setHeaderTopAds(false);
            showBottomBarNavigation(false);
        } else {
            setProductList(initMappingProduct());
            setHeaderTopAds(true);
            showBottomBarNavigation(true);
        }

        adapter.setTotalData(productViewModel.getTotalData());
    }

    private List<Visitable> initMappingProduct() {
        List<Visitable> list = new ArrayList<>();
        HeaderViewModel headerViewModel = new HeaderViewModel();
        headerViewModel.setSuggestionModel(productViewModel.getSuggestionModel());

        if (headerViewModel.hasHeader()) {
            list.add(headerViewModel);
        }

        list.addAll(productViewModel.getProductList());

        return list;
    }

    private void setupListener() {
        topAdsRecyclerAdapter.setAdsItemClickListener(this);
        topAdsRecyclerAdapter.setTopAdsListener(this);
    }

    @Override
    public void initTopAdsParams() {
        TopAdsParams adsParams = new TopAdsParams();
        adsParams.getParam().put(TopAdsParams.KEY_SRC, BrowseApi.DEFAULT_VALUE_SOURCE_SEARCH); //[TODO replace source with source from parameters
        adsParams.getParam().put(TopAdsParams.KEY_QUERY, getQueryKey());

        if (getFlagFilterHelper() != null &&
                !TextUtils.isEmpty(getFlagFilterHelper().getCategoryId())) {
            adsParams.getParam().put(TopAdsParams.KEY_DEPARTEMENT_ID, getFlagFilterHelper().getCategoryId());
        } else if (getSearchParameter().getDepartmentId() != null &&
                !getSearchParameter().getDepartmentId().isEmpty() &&
                !getSearchParameter().getDepartmentId().equals("0")) {
            adsParams.getParam().put(TopAdsParams.KEY_DEPARTEMENT_ID, getSearchParameter().getDepartmentId());
        }
        enrichWithFilterAndSortParams(adsParams);
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
        topAdsRecyclerAdapter.setHasHeader(hasHeader);
    }

    @Override
    public void setProductList(List<Visitable> list) {
        sendProductImpressionTrackingEvent(list);
        adapter.appendItems(list);
    }

    private void sendProductImpressionTrackingEvent(List<Visitable> list) {
        String userId = SessionHandler.isV4Login(getContext()) ? SessionHandler.getLoginID(getContext()) : "";
        List<Object> dataLayerList = new ArrayList<>();
        for(Visitable object : list) {
            if (object instanceof ProductItem) {
                dataLayerList.add(((ProductItem) object).getProductAsObjectDataLayer(userId));
            }
        }
        SearchTracking.eventImpressionSearchResultProduct(dataLayerList, getQueryKey());
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

    private void loadMoreProduct(final int startRow) {
        SearchParameter searchParameter
                = generateLoadMoreParameter(startRow, productViewModel.getQuery());
        HashMap<String, String> additionalParams
                = NetworkParamHelper.getParamMap(productViewModel.getAdditionalParams());
        presenter.loadMoreData(searchParameter,  additionalParams);
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

    private SearchParameter generateLoadMoreParameter(int startRow, String query) {
        SearchParameter searchParameter = getSearchParameter();
        searchParameter.setUniqueID(generateUniqueId());
        searchParameter.setUserID(generateUserId());
        searchParameter.setQueryKey(query);
        searchParameter.setStartRow(startRow);
        searchParameter.setDepartmentId(getSearchParameter().getDepartmentId());
        return searchParameter;
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
    public void showBottomBarNavigation(boolean show) {
        super.showBottomBarNavigation(show);
    }

    @Override
    public String getScreenNameId() {
        return AppScreen.SCREEN_SEARCH_PAGE_PRODUCT_TAB;
    }

    @Override
    protected List<AHBottomNavigationItem> getBottomNavigationItems() {
        List<AHBottomNavigationItem> items = new ArrayList<>();
        items.add(new AHBottomNavigationItem(getString(R.string.sort), R.drawable.ic_sort_black));
        items.add(new AHBottomNavigationItem(getString(R.string.filter), R.drawable.ic_filter_list_black));
        items.add(new AHBottomNavigationItem(getString(adapter.getTitleTypeRecyclerView()), adapter.getIconTypeRecyclerView()));
        items.add(new AHBottomNavigationItem(getString(R.string.share), R.drawable.ic_share_black));
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
                        startShareActivity(productViewModel.getShareUrl());
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
                if (adapter.isEmptyItem(position) ||
                        adapter.isHeaderBanner(position) ||
                        adapter.isGuidedSearch(topAdsRecyclerAdapter.getOriginalPosition(position)) ||
                        topAdsRecyclerAdapter.isLoading(position) ||
                        topAdsRecyclerAdapter.isTopAdsViewHolder(position)) {
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
        outState.putParcelable(EXTRA_SEARCH_PARAMETER, getSearchParameter());
        outState.putBoolean(EXTRA_FORCE_SEARCH, isForceSearch());
    }

    @Override
    protected void onFirstTimeLaunch() {
        super.onFirstTimeLaunch();
        getDynamicFilter();
        getGuidedSearch();
    }

    private void getGuidedSearch() {
        if (!TextUtils.isEmpty(productViewModel.getQuery())) {
            presenter.loadGuidedSearch(productViewModel.getQuery());
        }
    }

    @Override
    protected void requestDynamicFilter() {
        presenter.requestDynamicFilter(NetworkParamHelper.getParamMap(productViewModel.getAdditionalParams()));
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
        Bundle bundle = new Bundle();
        Intent intent = ProductDetailRouter.createInstanceProductDetailInfoActivity(getActivity());
        bundle.putParcelable(ProductDetailRouter.EXTRA_PRODUCT_ITEM, data);
        intent.putExtras(bundle);
        intent.putExtra(ProductDetailRouter.WISHLIST_STATUS_UPDATED_POSITION, adapterPosition);
        sendItemClickTrackingEvent(item);
        startActivityForResult(intent, REQUEST_CODE_GOTO_PRODUCT_DETAIL);
    }

    private void sendItemClickTrackingEvent(ProductItem item) {
        String userId = SessionHandler.isV4Login(getContext()) ?
                SessionHandler.getLoginID(getContext()) : "";

        SearchTracking.trackEventClickSearchResultProduct(
                item.getProductAsObjectDataLayer(userId),
                productViewModel.getQuery()
        );
    }

    @Override
    public void onWishlistButtonClicked(ProductItem productItem, int adapterPosition) {
        presenter.handleWishlistButtonClicked(productItem, topAdsRecyclerAdapter.getOriginalPosition(adapterPosition));
    }

    @Override
    public void onSuggestionClicked(String suggestedQuery) {
        performNewProductSearch(suggestedQuery, true);
    }

    @Override
    public void onBannerAdsClicked(String appLink) {
        if (!TextUtils.isEmpty(appLink)) {
            ((TkpdCoreRouter) getActivity().getApplication()).actionApplink(getActivity(), appLink);
        }
    }

    @Override
    public void onSearchGuideClicked(String keyword) {
        performNewProductSearch(keyword, true);
    }

    @Override
    public void onEmptyButtonClicked() {
        showSearchInputView();
    }

    @Override
    public void onErrorAddWishList(String errorMessage, int adapterPosition) {
        enableWishlistButton(adapterPosition);
        adapter.notifyItemChanged(adapterPosition);
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessAddWishlist(int adapterPosition) {
        UnifyTracking.eventSearchResultProductWishlistClick(true, getQueryKey());
        adapter.updateWishlistStatus(adapterPosition, true);
        enableWishlistButton(adapterPosition);
        adapter.notifyItemChanged(adapterPosition);
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_add_wishlist));
    }

    @Override
    public void onErrorRemoveWishlist(String errorMessage, int adapterPosition) {
        enableWishlistButton(adapterPosition);
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessRemoveWishlist(int adapterPosition) {
        UnifyTracking.eventSearchResultProductWishlistClick(false, getQueryKey());
        adapter.updateWishlistStatus(adapterPosition, false);
        enableWishlistButton(adapterPosition);
        adapter.notifyItemChanged(adapterPosition);
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_remove_wishlist));
    }

    @Override
    public void launchLoginActivity(Bundle extras) {
        Intent intent = ((DiscoveryRouter)MainApplication.getAppContext()).getLoginIntent
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
    public String getQueryKey() {
        return productViewModel.getQuery();
    }

    @Override
    public void setEmptyProduct() {
        topAdsRecyclerAdapter.shouldLoadAds(false);
        adapter.showEmpty(productViewModel.getQuery());
    }

    @Override
    protected void updateDepartmentId(String deptId) {
        getSearchParameter().setDepartmentId(deptId);
    }

    @Override
    protected void reloadData() {
        if (!adapter.hasGuidedSearch()) {
            getGuidedSearch();
        }
        adapter.clearData();
        initTopAdsParams();
        topAdsRecyclerAdapter.setConfig(topAdsConfig);
        topAdsRecyclerAdapter.reset();
        showBottomBarNavigation(false);
        SearchParameter searchParameter
                = generateLoadMoreParameter(0, productViewModel.getQuery());
        presenter.loadData(searchParameter, isForceSearch(), getAdditionalParams());
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
    public void addGuidedSearch() {
        adapter.addGuidedSearch();
    }

    @Override
    public void onGetGuidedSearchComplete(GuidedSearchViewModel guidedSearchViewModel) {
        adapter.setGuidedSearch(guidedSearchViewModel);
        Toast.makeText(getContext(), "Retrieve Guided Search Complete", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetGuidedSearchFailed() {
        Toast.makeText(getContext(), "Retrieve Guided Search Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void openFilterActivity() {
        if (isFilterDataAvailable()) {
            String preFilteredSc = getSearchParameter().getDepartmentId();
            if (!TextUtils.isEmpty(preFilteredSc)) {
                addPreFilteredCategory(preFilteredSc);
            }
            Intent intent = RevampedDynamicFilterActivity.createInstance(
                    getActivity(), getScreenNameId(), getFlagFilterHelper()
            );
            startActivityForResult(intent, getFilterRequestCode());
            getActivity().overridePendingTransition(R.anim.pull_up, android.R.anim.fade_out);
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), getActivity().getString(R.string.error_filter_data_not_ready));
        }
    }

    private void addPreFilteredCategory(String categoryId) {
        if (getFlagFilterHelper() == null) {
            setFlagFilterHelper(new FilterFlagSelectedModel());
            getFlagFilterHelper().setSavedCheckedState(new HashMap<String, Boolean>());
            getFlagFilterHelper().setSavedTextInput(new HashMap<String, String>());
            PreFilterHelper.addPreFilteredCategory(getFilters(), getFlagFilterHelper(), categoryId);
        }
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
}
