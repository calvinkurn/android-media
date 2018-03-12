package com.tokopedia.discovery.newdiscovery.hotlist.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.model.Hotlist;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.discovery.DiscoveryRouter;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.intermediary.view.IntermediaryActivity;
import com.tokopedia.discovery.newdiscovery.base.BottomNavigationListener;
import com.tokopedia.discovery.newdiscovery.hotlist.di.component.DaggerHotlistComponent;
import com.tokopedia.discovery.newdiscovery.hotlist.di.component.HotlistComponent;
import com.tokopedia.discovery.newdiscovery.hotlist.domain.model.HotlistQueryModel;
import com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.HotlistAdapter;
import com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.ItemClickListener;
import com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.factory.HotlistAdapterTypeFactory;
import com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.factory.HotlistTypeFactory;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.HotlistHeaderViewModel;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.HotlistProductViewModel;
import com.tokopedia.discovery.newdiscovery.hotlist.view.presenter.HotlistFragmentContract;
import com.tokopedia.discovery.newdiscovery.hotlist.view.presenter.HotlistFragmentPresenter;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionFragment;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionFragmentPresenter;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionGeneralAdapter;
import com.tokopedia.discovery.newdiscovery.util.HotlistParameter;
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

import javax.inject.Inject;

import static com.tokopedia.core.router.productdetail.ProductDetailRouter.EXTRA_PRODUCT_ID;
import static com.tokopedia.core.router.productdetail.ProductDetailRouter.WIHSLIST_STATUS_IS_WISHLIST;

/**
 * Created by hangnadi on 10/6/17.
 */

public class HotlistFragment extends SearchSectionFragment
        implements
        HotlistFragmentContract.View,
        RefreshHandler.OnRefreshHandlerListener, SearchSectionGeneralAdapter.OnItemChangeView,
        ItemClickListener, TopAdsListener, TopAdsItemClickListener {

    private static final String EXTRA_URL = "extra_url";
    private static final String EXTRA_SEARCH_QUERY = "extra_search_query";
    private static final String EXTRA_ALIAS = "extra_alias";
    private static final String EXTRA_QUERY_HOTLIST = "EXTRA_QUERY_HOTLIST";
    private static final String EXTRA_DISABLE_TOPADS = "EXTRA_DISABLE_TOPADS";

    private static final int REQUEST_ACTIVITY_SORT_HOTLIST = 2021;
    private static final int REQUEST_ACTIVITY_FILTER_HOTLIST = 1202;
    private static final int REQUEST_CODE_GOTO_PRODUCT_DETAIL = 1111;

    protected BottomNavigationListener bottomNavigationListener;
    protected RefreshHandler refreshHandler;
    protected RecyclerView recyclerView;

    protected HotlistAdapter hotlistAdapter;
    protected TopAdsRecyclerAdapter topAdsRecyclerAdapter;

    private String aliasHotlist;
    private String shareUrl;
    private HotlistQueryModel queryModel;
    private boolean disableTopads;

    private Config topAdsConfig;

    @Inject
    HotlistFragmentPresenter presenter;

    public static Fragment createInstanceUsingAlias(String alias) {
        HotlistFragment fragment = new HotlistFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_ALIAS, alias);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Fragment createInstanceUsingURL(String url, String searchQuery) {
        Bundle args = new Bundle();
        args.putString(EXTRA_URL, url);
        args.putString(EXTRA_SEARCH_QUERY, searchQuery);
        HotlistFragment fragment = new HotlistFragment();
        fragment.setArguments(args);
        return fragment;
    }

    protected String getHotlistAlias() {
        return aliasHotlist;
    }

    protected void setHotlistAlias(String aliasHotlist) {
        this.aliasHotlist = aliasHotlist;
    }

    protected String generateAliasUsingURL(String url) {
        Uri uri = Uri.parse(url);
        return uri.getPathSegments().get(1);
    }

    @Override
    public String getShareUrl() {
        return shareUrl;
    }

    @Override
    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    @Override
    public void setDisableTopads(boolean disableTopads) {
        this.disableTopads = disableTopads;
    }

    @Override
    public boolean isDisableTopads() {
        return disableTopads;
    }


    @Override
    public String getScreenNameId() {
        return AppScreen.SCREEN_BROWSE_HOT;
    }

    @Override
    protected void initInjector() {
        HotlistComponent component = DaggerHotlistComponent.builder()
                .appComponent(getComponent(AppComponent.class))
                .build();
        component.inject(this);
        component.inject(presenter);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BottomNavigationListener) {
            this.bottomNavigationListener = (BottomNavigationListener) context;
        }
    }

    @Override
    protected List<AHBottomNavigationItem> getBottomNavigationItems() {
        List<AHBottomNavigationItem> items = new ArrayList<>();
        items.add(new AHBottomNavigationItem(getString(R.string.sort), R.drawable.ic_sort_black));
        items.add(new AHBottomNavigationItem(getString(R.string.filter), R.drawable.ic_filter_list_black));
        items.add(new AHBottomNavigationItem(getString(hotlistAdapter.getTitleTypeRecyclerView()), hotlistAdapter.getIconTypeRecyclerView()));
        items.add(new AHBottomNavigationItem(getString(R.string.share), R.drawable.ic_share_black));
        return items;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
        if (getArguments() != null) {
            if (getArguments().getString(EXTRA_URL, "").isEmpty()) {
                setHotlistAlias(getArguments().getString(EXTRA_ALIAS));
            } else {
                setHotlistAlias(generateAliasUsingURL(getArguments().getString(EXTRA_URL, "")));
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        presenter.attachView(this);
        return inflater.inflate(R.layout.fragment_base_discovery, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        prepareView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        hotlistAdapter.onSaveInstanceState(outState);
        outState.putString(EXTRA_ALIAS, getHotlistAlias());
        outState.putParcelable(EXTRA_QUERY_HOTLIST, getQueryModel());
        outState.putBoolean(EXTRA_DISABLE_TOPADS, isDisableTopads());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        setHotlistAlias(savedInstanceState.getString(EXTRA_ALIAS));
        setQueryModel((HotlistQueryModel) savedInstanceState.getParcelable(EXTRA_QUERY_HOTLIST));
        setDisableTopads(savedInstanceState.getBoolean(EXTRA_DISABLE_TOPADS));
        switchLayoutType();
        initTopAdsParams();
        hotlistAdapter.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onRefresh(View view) {
        showBottomBarNavigation(false);
        reloadData();
    }

    @Override
    protected void onFirstTimeLaunch() {
        super.onFirstTimeLaunch();
        showBottomBarNavigation(false);
        presenter.requestDataForTheFirstTime(getHotlistInitParam());
    }

    protected void onLoadMoreProduct() {
        showBottomBarNavigation(false);
        presenter.requestLoadMore();
    }

    private void initView(View view) {
        refreshHandler = new RefreshHandler(getActivity(), view, this);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
    }

    protected void prepareView() {
        setupAdapter();
        setupListener();
    }

    private void setupListener() {
        topAdsRecyclerAdapter.setAdsItemClickListener(this);
        topAdsRecyclerAdapter.setTopAdsListener(this);
        topAdsRecyclerAdapter.setOnLoadListener(new TopAdsRecyclerAdapter.OnScrollListener() {
            @Override
            public void onLoad(int page, int totalCount) {
                if (isAllowLoadMore()) {
                    onLoadMoreProduct();
                }
            }

            @Override
            public void onScroll(int lastVisiblePosition) {
                //TODO implement onscroll lastvisible
            }
        });
    }

    @Override
    protected SearchSectionGeneralAdapter getAdapter() {
        return hotlistAdapter;
    }

    @Override
    protected SearchSectionFragmentPresenter getPresenter() {
        return presenter;
    }

    private boolean isAllowLoadMore() {
        return getUserVisibleHint()
                && hotlistAdapter.hasNextPage()
                && !refreshHandler.isRefreshing();
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
                        if (!refreshHandler.isRefreshing() && !topAdsRecyclerAdapter.isLoading()) {
                            switchLayoutType();
                        }
                        return true;
                    case 3:
                        startShareActivity(getShareUrl());
                        return true;
                    default:
                        return false;
                }
            }
        };
    }

    @Override
    protected int getFilterRequestCode() {
        return REQUEST_ACTIVITY_FILTER_HOTLIST;
    }

    @Override
    protected int getSortRequestCode() {
        return REQUEST_ACTIVITY_SORT_HOTLIST;
    }

    protected void setupAdapter() {
        String searchQuery = getArguments().getString(EXTRA_SEARCH_QUERY, "");
        HotlistTypeFactory typeFactory = new HotlistAdapterTypeFactory(this, searchQuery);
        hotlistAdapter = new HotlistAdapter(this, typeFactory);

        topAdsRecyclerAdapter = new TopAdsRecyclerAdapter(getActivity(), hotlistAdapter);
        topAdsConfig = new Config.Builder()
                .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                .setUserId(SessionHandler.getLoginID(getActivity()))
                .setEndpoint(Endpoint.PRODUCT)
                .build();
        topAdsRecyclerAdapter.setConfig(topAdsConfig);
        topAdsRecyclerAdapter.setHasHeader(true);
        topAdsRecyclerAdapter.setSpanSizeLookup(onSpanSizeLookup());
        recyclerView.setAdapter(topAdsRecyclerAdapter);
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
        ProductItem data = new ProductItem();
        data.setId(product.getId());
        data.setName(product.getName());
        data.setPrice(product.getPriceFormat());
        data.setImgUri(product.getImage().getM_url());
        Bundle bundle = new Bundle();
        Intent intent = ProductDetailRouter.createInstanceProductDetailInfoActivity(getActivity());
        bundle.putParcelable(ProductDetailRouter.EXTRA_PRODUCT_ITEM, data);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onHandlingDataFromPDP(requestCode, resultCode, data);
    }

    @Override
    public void reloadData() {
        if (!hotlistAdapter.getItemList().isEmpty()) {
            HotlistHeaderViewModel headerViewModel = (HotlistHeaderViewModel) hotlistAdapter.getItemList().get(0);
            presenter.refreshSort(headerViewModel);
        } else {
            showBottomBarNavigation(false);
            presenter.requestDataForTheFirstTime(getHotlistInitParam());
        }
    }

    private void onHandlingDataFromPDP(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_GOTO_PRODUCT_DETAIL
                && data != null
                && data.getExtras() != null) {

            String productID = data.getExtras().getString(EXTRA_PRODUCT_ID, "");
            boolean isWishlist = data.getExtras().getBoolean(WIHSLIST_STATUS_IS_WISHLIST, false);

            if (!productID.isEmpty()) {
                hotlistAdapter.updateWishlistStatus(productID, isWishlist);
            }
        }
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
    protected GridLayoutManager.SpanSizeLookup onSpanSizeLookup() {
        return new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (hotlistAdapter.isHotListBanner(position) ||
                        hotlistAdapter.isEmptyHotlist(position) ||
                        topAdsRecyclerAdapter.isLoading(position) ||
                        topAdsRecyclerAdapter.isTopAdsViewHolder(position)) {
                    return spanCount;
                } else {
                    return 1;
                }
            }
        };
    }

    protected TopAdsParams getTopAdsParam() {
        TopAdsParams params = new TopAdsParams();
        params.getParam().put(TopAdsParams.KEY_SRC, BrowseApi.DEFAULT_VALUE_SOURCE_HOTLIST);
        params.getParam().put(TopAdsParams.KEY_HOTLIST_ID,
                getQueryModel() != null ? getQueryModel().getHotlistID() : "");
        params.getParam().put(TopAdsParams.KEY_DEPARTEMENT_ID,
                getQueryModel() != null ? getQueryModel().getCategoryID() : "");
        enrichWithFilterAndSortParams(params);
        return params;
    }

    protected HotlistParameter getHotlistInitParam() {
        HotlistParameter parameter = new HotlistParameter();
        parameter.setHotlistAlias(getHotlistAlias());
        parameter.setUniqueID(
                SessionHandler.isV4Login(getActivity()) ?
                        SessionHandler.getLoginID(getActivity()) :
                        GCMHandler.getRegistrationId(getActivity())
        );
        parameter.setUserID(
                SessionHandler.isV4Login(getActivity()) ?
                        SessionHandler.getLoginID(getActivity()) :
                        null
        );
        parameter.setSource(HotlistParameter.SOURCE_HOTLIST);
        parameter.setStartRow(hotlistAdapter.getStartFrom());
        return parameter;
    }

    @Override
    public void storeTotalData(int totalData) {
        hotlistAdapter.setTotalData(totalData);
    }

    @Override
    public void renderErrorView(String message) {
        topAdsRecyclerAdapter.hideLoading();
        if (hotlistAdapter.isEmpty()) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), message, null);
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), message);
        }
    }

    @Override
    public void renderRetryInit() {
        topAdsRecyclerAdapter.hideLoading();
        if (hotlistAdapter.isEmpty()) {
            NetworkErrorHelper.showEmptyState(
                    getActivity(),
                    getView(),
                    new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            onFirstTimeLaunch();
                        }
                    });
        } else {
            NetworkErrorHelper.createSnackbarWithAction(
                    getActivity(),
                    new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            presenter.requestLoadMore();
                        }
                    }).showRetrySnackbar();
        }
    }

    @Override
    public void renderRetryRefresh() {
        topAdsRecyclerAdapter.hideLoading();
        if (hotlistAdapter.isEmpty()) {
            NetworkErrorHelper.showEmptyState(
                    getActivity(),
                    getView(),
                    new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            reloadData();
                        }
                    });
        } else {
            NetworkErrorHelper.createSnackbarWithAction(
                    getActivity(), new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            reloadData();
                        }
                    });
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
    public void renderListView(List<Visitable> visitables) {
        trackingImpression(visitables);
        topAdsRecyclerAdapter.shouldLoadAds(!isDisableTopads());
        hotlistAdapter.incrementStart();
        hotlistAdapter.addElements(visitables);
    }

    @Override
    public void renderEmptyHotlist(List<Visitable> visitables) {
        topAdsRecyclerAdapter.shouldLoadAds(false);
        hotlistAdapter.addElements(visitables);
    }

    @Override
    public void renderNextListView(List<Visitable> visitables) {
        trackingImpression(visitables);
        topAdsRecyclerAdapter.hideLoading();
        topAdsRecyclerAdapter.shouldLoadAds(!isDisableTopads());
        hotlistAdapter.incrementStart();
        hotlistAdapter.addElements(visitables);
    }

    @Override
    public void initTopAdsParams() {
        if (!isDisableTopads()) {
            topAdsConfig.setTopAdsParams(getTopAdsParam());
            topAdsRecyclerAdapter.setConfig(topAdsConfig);
        }
    }

    @Override
    public void onHashTagClicked(String name, String url, String departmentID) {
        IntermediaryActivity.moveTo(getActivity(), departmentID, name);
    }

    @Override
    public void onHotlistDescClicked(String messageClickAble) {

    }

    @Override
    public void onProductClicked(HotlistProductViewModel product, int adapterPosition) {
        trackingClicker(product, adapterPosition);
        gotoProductDetail(mappingIntoProductItem(product), adapterPosition);
    }

    @Override
    public void onBannerAdsClicked(String appLink) {
        if (!TextUtils.isEmpty(appLink)) {
            ((TkpdCoreRouter) getActivity().getApplication()).actionApplink(getActivity(), appLink);
        }
    }

    private ProductItem mappingIntoProductItem(HotlistProductViewModel product) {
        ProductItem data = new ProductItem();
        data.setId(product.getProductID());
        data.setName(product.getProductName());
        data.setPrice(product.getPrice());
        data.setImgUri(product.getImageUrl());
        return data;
    }

    private void gotoProductDetail(ProductItem productItem, int adapterPosition) {
        Bundle bundle = new Bundle();
        Intent intent = ProductDetailRouter.createInstanceProductDetailInfoActivity(getActivity());
        bundle.putParcelable(ProductDetailRouter.EXTRA_PRODUCT_ITEM, productItem);
        intent.putExtras(bundle);
        intent.putExtra(ProductDetailRouter.WISHLIST_STATUS_UPDATED_POSITION, adapterPosition);
        startActivityForResult(intent, REQUEST_CODE_GOTO_PRODUCT_DETAIL);
    }

    @Override
    public void onWishlistClicked(String productID, boolean wishlist) {
        if (SessionHandler.isV4Login(getActivity())) {
            hotlistAdapter.disableWishlistButton(productID);
            if (wishlist) {
                presenter.removeWishlist(productID);
            } else {
                presenter.addWishlist(productID);
            }
        } else {
            openLoginActivity(productID);
        }
    }

    @Override
    public void onEditWishlistError(String errorMessage, String productID) {
        hotlistAdapter.enableWishlistButton(productID);
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onEditWishlistSuccess(String successMessage, String productID) {
        hotlistAdapter.enableWishlistButton(productID);
        hotlistAdapter.updateWishlistStatus(productID);
        NetworkErrorHelper.showSnackbar(getActivity(), successMessage);
    }

    private void openLoginActivity(String productID) {
        Bundle extras = new Bundle();
        extras.putString("product_id", productID);
        Intent intent = ((DiscoveryRouter) MainApplication.getAppContext()).getLoginIntent
                (getActivity());
        intent.putExtras(extras);
        startActivity(intent);
    }

    @Override
    public void setQueryModel(HotlistQueryModel queryModel) {
        this.queryModel = queryModel;
    }

    @Override
    public int getStartFrom() {
        return hotlistAdapter.getStartFrom();
    }

    @Override
    public void showRefresh() {
        refreshHandler.setRefreshing(true);
    }

    @Override
    public void hideRefresh() {
        refreshHandler.setRefreshing(false);
    }

    @Override
    public HotlistQueryModel getQueryModel() {
        return queryModel;
    }

    @Override
    public void getDynamicFilter() {
        presenter.requestDynamicFilter();
    }

    @Override
    public void resetData() {
        hotlistAdapter.resetStartFrom();
        hotlistAdapter.clearData();
        topAdsRecyclerAdapter.reset();
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
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    private void trackingClicker(HotlistProductViewModel element, int adapterPosition) {
        if (element.isFeatured()) {
            Hotlist hotlist = new Hotlist();
            hotlist.setHotlistAlias(getHotlistAlias());
            hotlist.setScreenName(getScreenNameId());
            hotlist.setPosition(adapterPosition);

            List<Hotlist.Product> list = new ArrayList<>();
            Hotlist.Product product = new Hotlist.Product();
            product.setProductID(element.getProductID());
            product.setProductName(element.getProductName());
            product.setCategoryName(getHotlistAlias());
            list.add(product);

            hotlist.setProductList(list);
            TrackingUtils.eventClickHotlistProductFeatured(hotlist);
        }
    }

    private void trackingImpression(List<Visitable> visitables) {
        List<HotlistProductViewModel> extractList = new ArrayList<>();
        for (Visitable visitable : visitables) {
            if (visitable instanceof HotlistProductViewModel) {
                HotlistProductViewModel model = (HotlistProductViewModel) visitable;
                if (model.isFeatured()) {
                    extractList.add(model);
                }
            }
        }

        if (!extractList.isEmpty()) {
            Hotlist hotlist = new Hotlist();
            hotlist.setHotlistAlias(getHotlistAlias());
            hotlist.setScreenName(getScreenNameId());

            List<Hotlist.Product> list = new ArrayList<>();
            for (HotlistProductViewModel model : extractList) {
                Hotlist.Product product = new Hotlist.Product();
                product.setProductID(model.getProductID());
                product.setProductName(model.getProductName());
                product.setCategoryName(getHotlistAlias());
                list.add(product);
            }
            hotlist.setProductList(list);

            TrackingUtils.eventImpressionHotlistProductFeatured(hotlist);
        }

    }

    @Override
    protected String getScreenName() {
        return getScreenNameId();
    }
}
