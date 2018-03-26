package com.tokopedia.discovery.newdiscovery.search.fragment.catalog;

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
import android.widget.ProgressBar;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.router.discovery.DetailProductRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.di.component.DaggerSearchComponent;
import com.tokopedia.discovery.newdiscovery.di.component.SearchComponent;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionFragment;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionFragmentPresenter;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionGeneralAdapter;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.adapter.CatalogAdapter;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.adapter.factory.CatalogAdapterTypeFactory;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.adapter.factory.CatalogTypeFactory;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.adapter.factory.ItemClickListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.presenter.CatalogFragmentContract;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.presenter.CatalogPresenter;
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

/**
 * Created by hangnadi on 10/12/17.
 */

@SuppressWarnings("ParameterCanBeLocal")
public class CatalogFragment extends SearchSectionFragment implements
        CatalogFragmentContract.View, ItemClickListener, TopAdsItemClickListener,
        TopAdsListener, SearchSectionGeneralAdapter.OnItemChangeView {

    public static final String SOURCE = BrowseApi.DEFAULT_VALUE_SOURCE_CATALOG;

    private static final String EXTRA_DEPARTMENT_ID = "EXTRA_DEPARTMENT_ID";
    private static final String EXTRA_QUERY = "EXTRA_QUERY";
    private static final String EXTRA_SHARE_URL = "EXTRA_SHARE_URL";
    private static final int REQUEST_CODE_GOTO_CATALOG_DETAIL = 124;

    private static final int REQUEST_ACTIVITY_SORT_CATALOG = 1234;
    private static final int REQUEST_ACTIVITY_FILTER_CATALOG = 4321;

    protected RecyclerView recyclerView;
    protected ProgressBar loadingView;

    protected CatalogAdapter catalogAdapter;
    protected TopAdsRecyclerAdapter topAdsRecyclerAdapter;

    private String departmentId;
    private String query;
    private Config topAdsConfig;
    private String shareUrl;

    @Inject
    CatalogPresenter presenter;

    public static CatalogFragment createInstanceByQuery(String query) {
        CatalogFragment fragment = new CatalogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_QUERY, query);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static CatalogFragment createInstanceByCategoryID(String departmentId) {
        CatalogFragment fragment = new CatalogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_DEPARTMENT_ID, departmentId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public String getDepartmentId() {
        return departmentId;
    }

    @Override
    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String getQueryKey() {
        return query;
    }

    @Override
    public void setQueryKey(String queryKey) {
        this.query = queryKey;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    @Override
    public String getScreenNameId() {
        return AppScreen.SCREEN_SEARCH_PAGE_CATALOG_TAB;
    }

    @Override
    protected void initInjector() {
        SearchComponent component = DaggerSearchComponent.builder()
                .appComponent(getComponent(AppComponent.class))
                .build();
        component.inject(this);
        component.inject(presenter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            setQueryKey(getArguments().getString(EXTRA_QUERY, ""));
            setDepartmentId(getArguments().getString(EXTRA_DEPARTMENT_ID, ""));
            setShareUrl(getArguments().getString(EXTRA_SHARE_URL));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
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
        catalogAdapter.onSaveInstanceState(outState);
        outState.putString(EXTRA_QUERY, getQueryKey());
        outState.putString(EXTRA_DEPARTMENT_ID, getDepartmentId());
        outState.putString(EXTRA_SHARE_URL, getShareUrl());
    }

    @Override
    protected int getFilterRequestCode() {
        return REQUEST_ACTIVITY_FILTER_CATALOG;
    }

    @Override
    protected int getSortRequestCode() {
        return REQUEST_ACTIVITY_SORT_CATALOG;
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        loadingView = (ProgressBar) view.findViewById(R.id.loading);
    }

    protected void prepareView() {
        setupAdapter();
        setupListener();
    }

    private void setupListener() {
        topAdsRecyclerAdapter.setOnLoadListener(new TopAdsRecyclerAdapter.OnLoadListener() {
            @Override
            public void onLoad(int page, int totalCount) {
                if (isAllowLoadMore()) {
                    onLoadMoreCatalog();
                }
            }
        });
    }

    private boolean isAllowLoadMore() {
        return getUserVisibleHint()
                && catalogAdapter.hasNextPage()
                && !isRefreshing();
    }

    protected void onLoadMoreCatalog() {
        showBottomBarNavigation(false);
        if (getDepartmentId() != null && !getDepartmentId().isEmpty()) {
            presenter.requestCatalogLoadMore(getDepartmentId());
        } else {
            presenter.requestCatalogLoadMore();
        }
    }

    @Override
    protected void onSwipeToRefresh() {
        reloadData();
    }

    @Override
    public void setOnCatalogClicked(String catalogID) {
        Intent intent = DetailProductRouter.getCatalogDetailActivity(getActivity(), catalogID);
        startActivityForResult(intent, REQUEST_CODE_GOTO_CATALOG_DETAIL);
    }

    @Override
    public void onBannerAdsClicked(String appLink) {
        if (!TextUtils.isEmpty(appLink)) {
            ((TkpdCoreRouter) getActivity().getApplication()).actionApplink(getActivity(), appLink);
        }
    }

    @Override
    public void onEmptyButtonClicked() {
        showSearchInputView();
    }

    protected void setupAdapter() {

        topAdsConfig = new Config.Builder()
                .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                .setUserId(SessionHandler.getLoginID(getActivity()))
                .setEndpoint(Endpoint.PRODUCT)
                .build();

        CatalogTypeFactory typeFactory = new CatalogAdapterTypeFactory(this, topAdsConfig);
        catalogAdapter = new CatalogAdapter(this, typeFactory);

        topAdsRecyclerAdapter = new TopAdsRecyclerAdapter(getActivity(), catalogAdapter);
        topAdsRecyclerAdapter.setConfig(topAdsConfig);
        topAdsRecyclerAdapter.setSpanSizeLookup(onSpanSizeLookup());
        topAdsRecyclerAdapter.setAdsItemClickListener(this);
        topAdsRecyclerAdapter.setTopAdsListener(this);
        topAdsRecyclerAdapter.setHasHeader(true);
        recyclerView.setAdapter(topAdsRecyclerAdapter);
        topAdsRecyclerAdapter.setLayoutManager(getGridLayoutManager());
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
    protected GridLayoutManager.SpanSizeLookup onSpanSizeLookup() {
        return new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (catalogAdapter.isEmptyItem(position) ||
                        catalogAdapter.isCatalogHeader(position) ||
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void onFirstTimeLaunch() {
        super.onFirstTimeLaunch();
        if (getDepartmentId() != null && !getDepartmentId().isEmpty()) {
            presenter.requestCatalogList(getDepartmentId());
        } else {
            presenter.requestCatalogList();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        catalogAdapter.onRestoreInstanceState(savedInstanceState);
        setQueryKey(savedInstanceState.getString(EXTRA_QUERY));
        setDepartmentId(savedInstanceState.getString(EXTRA_DEPARTMENT_ID));
        setShareUrl(savedInstanceState.getString(EXTRA_SHARE_URL));

    }

    @Override
    public void renderListView(List<Visitable> catalogViewModels) {
        topAdsRecyclerAdapter.hideLoading();
        catalogAdapter.incrementStart();
        topAdsRecyclerAdapter.reset();
        catalogAdapter.clearData();
        catalogAdapter.addElements(catalogViewModels);
    }

    @Override
    public void renderNextListView(List<Visitable> catalogViewModels) {
        topAdsRecyclerAdapter.hideLoading();
        catalogAdapter.incrementStart();
        catalogAdapter.addElements(catalogViewModels);
    }

    @Override
    public void successRefreshCatalog(List<Visitable> visitables) {
        topAdsRecyclerAdapter.hideLoading();
        if (!visitables.isEmpty()) {
            catalogAdapter.setElement(visitables);
        } else {
            topAdsRecyclerAdapter.shouldLoadAds(false);
            String message = String.format(getString(R.string.empty_search_content_template), query);
            catalogAdapter.showEmptyState(message);
        }
    }

    @Override
    public void renderErrorView(String message) {
        if (catalogAdapter.isEmpty()) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), null);
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), message);
        }
    }

    @Override
    public void renderRetryInit() {
        topAdsRecyclerAdapter.hideLoading();
        if (catalogAdapter.isEmpty()) {
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
                    getActivity(), new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            onLoadMoreCatalog();
                        }
                    }).showRetrySnackbar();
        }
    }

    @Override
    public void renderRetryRefresh() {
        if (catalogAdapter.isEmpty()) {
            NetworkErrorHelper.showEmptyState(
                    getActivity(),
                    getView(),
                    new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            presenter.refreshSort();
                        }
                    });
        } else {
            NetworkErrorHelper.createSnackbarWithAction(
                    getActivity(), new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            presenter.refreshSort();
                        }
                    });
        }
    }

    @Override
    public void renderUnknown() {

    }

    @Override
    public void renderShareURL(String shareURL) {
        setShareUrl(shareURL);
    }

    @Override
    public void setHasNextPage(boolean hasNextPage) {
        catalogAdapter.setNextPage(hasNextPage);
    }

    @Override
    public int getStartFrom() {
        return catalogAdapter.getStartFrom();
    }

    @Override
    protected List<AHBottomNavigationItem> getBottomNavigationItems() {
        List<AHBottomNavigationItem> items = new ArrayList<>();
        items.add(new AHBottomNavigationItem(getString(R.string.sort), R.drawable.ic_sort_black));
        items.add(new AHBottomNavigationItem(getString(R.string.filter), R.drawable.ic_filter_list_black));
        items.add(new AHBottomNavigationItem(getString(catalogAdapter.getTitleTypeRecyclerView()), catalogAdapter.getIconTypeRecyclerView()));
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
                        startShareActivity(getShareUrl());
                        return true;
                    default:
                        return false;
                }
            }
        };
    }

    @Override
    public void initTopAdsParamsByQuery(RequestParams requestParams) {
        TopAdsParams adsParams = new TopAdsParams();
        adsParams.getParam().put(TopAdsParams.KEY_SRC, BrowseApi.DEFAULT_VALUE_SOURCE_SEARCH);
        adsParams.getParam().put(TopAdsParams.KEY_QUERY, getQueryKey());

        enrichWithFilterAndSortParams(adsParams);
        topAdsConfig.setTopAdsParams(adsParams);
        topAdsRecyclerAdapter.setConfig(topAdsConfig);
    }

    @Override
    public void initTopAdsParamsByCategory(RequestParams requestParams) {
        TopAdsParams adsParams = new TopAdsParams();
        adsParams.getParam().put(TopAdsParams.KEY_SRC, BrowseApi.DEFAULT_VALUE_SOURCE_DIRECTORY);
        adsParams.getParam().put(TopAdsParams.KEY_DEPARTEMENT_ID, getDepartmentId());

        enrichWithFilterAndSortParams(adsParams);

        topAdsConfig.setTopAdsParams(adsParams);
        topAdsRecyclerAdapter.setConfig(topAdsConfig);
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
    protected SearchSectionGeneralAdapter getAdapter() {
        return catalogAdapter;
    }

    @Override
    protected SearchSectionFragmentPresenter getPresenter() {
        return presenter;
    }

    @Override
    public String getSource() {
        return SOURCE;
    }

    @Override
    protected void reloadData() {
        catalogAdapter.resetStartFrom();
        catalogAdapter.clearData();
        topAdsRecyclerAdapter.reset();
        presenter.refreshSort();
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
        if (recyclerView != null) {
            recyclerView.smoothScrollToPosition(0);
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
