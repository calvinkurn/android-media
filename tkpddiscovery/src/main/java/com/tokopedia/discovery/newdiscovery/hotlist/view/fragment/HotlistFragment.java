package com.tokopedia.discovery.newdiscovery.hotlist.view.fragment;

import android.app.Activity;
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
import android.widget.ImageView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.gms.tagmanager.DataLayer;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.abstraction.common.utils.FindAndReplaceHelper;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.HotlistPageTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.model.Hotlist;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.share.DefaultShare;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.design.quickfilter.custom.CustomViewRoundedQuickFilterItem;
import com.tokopedia.discovery.DiscoveryRouter;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.intermediary.view.IntermediaryActivity;
import com.tokopedia.discovery.newdiscovery.analytics.SearchTracking;
import com.tokopedia.discovery.newdiscovery.base.BottomNavigationListener;
import com.tokopedia.discovery.newdiscovery.hotlist.di.component.DaggerHotlistComponent;
import com.tokopedia.discovery.newdiscovery.hotlist.di.component.HotlistComponent;
import com.tokopedia.discovery.newdiscovery.hotlist.domain.model.HotlistQueryModel;
import com.tokopedia.discovery.newdiscovery.hotlist.view.activity.HotlistActivity;
import com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.HotlistAdapter;
import com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.HotlistListener;
import com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.factory.HotlistAdapterTypeFactory;
import com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.factory.HotlistTypeFactory;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.HotlistHeaderViewModel;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.HotlistProductViewModel;
import com.tokopedia.discovery.newdiscovery.hotlist.view.presenter.HotlistFragmentContract;
import com.tokopedia.discovery.newdiscovery.hotlist.view.presenter.HotlistFragmentPresenter;
import com.tokopedia.discovery.newdiscovery.search.fragment.BrowseSectionFragment;
import com.tokopedia.discovery.newdiscovery.search.fragment.BrowseSectionFragmentPresenter;
import com.tokopedia.discovery.newdiscovery.search.fragment.BrowseSectionGeneralAdapter;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.itemdecoration.ProductItemDecoration;
import com.tokopedia.discovery.newdiscovery.util.HotlistParameter;;
import com.tokopedia.filter.common.data.DynamicFilterModel;
import com.tokopedia.filter.common.data.Filter;
import com.tokopedia.filter.common.data.Option;
import com.tokopedia.filter.common.manager.FilterSortManager;
import com.tokopedia.filter.newdynamicfilter.RevampedDynamicFilterActivity;
import com.tokopedia.filter.newdynamicfilter.SortProductActivity;
import com.tokopedia.linker.model.LinkerData;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.view.adapter.TopAdsRecyclerAdapter;
import com.tokopedia.track.TrackApp;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static com.tokopedia.core.router.productdetail.ProductDetailRouter.EXTRA_PRODUCT_ID;
import static com.tokopedia.core.router.productdetail.ProductDetailRouter.WIHSLIST_STATUS_IS_WISHLIST;
import static com.tokopedia.remoteconfig.RemoteConfigKey.HOTLIST_SHARE_MSG;

/**
 * Created by hangnadi on 10/6/17.
 */

public class HotlistFragment extends BrowseSectionFragment
        implements
        HotlistFragmentContract.View,
        RefreshHandler.OnRefreshHandlerListener, BrowseSectionGeneralAdapter.OnItemChangeView,
        HotlistListener, TopAdsListener, TopAdsItemClickListener,
        HotlistActivity.FragmentListener {

    private static final String TAG = HotlistFragment.class.getSimpleName();
    private static final String HOTLIST_DETAIL_ENHANCE_ANALYTIC = "HOTLIST_DETAIL_ENHANCE_ANALYTIC";
    private static final String LAST_POSITION_ENHANCE_PRODUCT = "LAST_POSITION_ENHANCE_PRODUCT";

    private static final String EXTRA_TRACKER_ATTRIBUTION = "EXTRA_TRACKER_ATTRIBUTION";
    private static final String EXTRA_URL = "extra_url";
    private static final String EXTRA_SEARCH_QUERY = "extra_search_query";
    private static final String EXTRA_ALIAS = "extra_alias";
    private static final String EXTRA_QUERY_HOTLIST = "EXTRA_QUERY_HOTLIST";
    private static final String EXTRA_DISABLE_TOPADS = "EXTRA_DISABLE_TOPADS";
    private static final String EXTRA_HEADER_URL = "EXTRA_HEADER_URL";
    private static final String EXTRA_DESC = "EXTRA_DESC";
    private static final String TOKO = "TOKO";
    private static final String POWER_BADGE = "Power Merchant";
    private static final String OFFICIAL_STORE = "OFFICIAL STORE";
    private static final String PENGIRIMAN = "Dukungan Pengiriman";
    private static final String INSTANT_COURIER = "INSTANT COURIER";

    private static final int REQUEST_ACTIVITY_SORT_HOTLIST = 2021;
    private static final int REQUEST_ACTIVITY_FILTER_HOTLIST = 1202;
    private static final int REQUEST_CODE_GOTO_PRODUCT_DETAIL = 1111;
    private static final String PERFORMANCE_TRACE_HOTLIST = "mp_hotlist";

    private static final String KEY_OFFICIAL = "official";

    protected BottomNavigationListener bottomNavigationListener;
    protected RefreshHandler refreshHandler;
    protected RecyclerView recyclerView;

    protected HotlistAdapter hotlistAdapter;
    protected TopAdsRecyclerAdapter topAdsRecyclerAdapter;

    private String aliasHotlist;
    private String shareUrl;
    private String descTxt;
    private String headerUrl;
    private HotlistQueryModel queryModel;
    private boolean disableTopads;

    private Config topAdsConfig;

    private LocalCacheHandler trackerProductCache;

    private String HOTLIST_NAME_PLACEHOLDER = "{{hotlist__name_plchldr}}";


    @Inject
    HotlistFragmentPresenter presenter;
    @Inject
    UserSessionInterface userSession;

    private String trackerAttribution;
    private PerformanceMonitoring performanceMonitoring;
    private boolean isTraceStopped;
    private RemoteConfig remoteConfig;

    private List<QuickFilterItem> quickFilterItems = new ArrayList<>();

    public static Fragment createInstanceUsingAlias(String alias, String trackerAttribution) {
        HotlistFragment fragment = new HotlistFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_ALIAS, alias);
        bundle.putString(EXTRA_TRACKER_ATTRIBUTION, trackerAttribution);
        fragment.setArguments(bundle);
        return fragment;
    }

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

    @Override
    public String getHotlistAlias() {
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
    public void loadImageHeader(String bannerImageUrl) {
        this.headerUrl = bannerImageUrl;
        ImageView imageHeader = getActivity().findViewById(R.id.hotlist_background);
        ImageHandler.loadImageSourceSize(getContext(), imageHeader, bannerImageUrl);
    }

    @Override
    public void setTitleHeader(String title) {
        getActivity().setTitle(title);
    }

    @Override
    public void setDescription(String description) {
        this.descTxt = description;
        ((HotlistActivity) getActivity()).renderHotlistDescription(description);
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
        performanceMonitoring = PerformanceMonitoring.start(PERFORMANCE_TRACE_HOTLIST);
        trackerProductCache = new LocalCacheHandler(getActivity(), HOTLIST_DETAIL_ENHANCE_ANALYTIC);
        if (getArguments() != null) {
            setTrackerAttribution(getArguments().getString(EXTRA_TRACKER_ATTRIBUTION, ""));
            if (getArguments().getString(EXTRA_URL, "").isEmpty()) {
                setHotlistAlias(getArguments().getString(EXTRA_ALIAS));
            } else {
                setHotlistAlias(generateAliasUsingURL(getArguments().getString(EXTRA_URL, "")));
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        screenTrack();
    }

    public void setTrackerAttribution(String trackerAttribution) {
        this.trackerAttribution = trackerAttribution;
    }

    @Override
    public String getHomeAttribution() {
        return trackerAttribution;
    }

    @Override
    public void trackImpressionProduct(Map<String, Object> dataLayer) {
        HotlistPageTracking.eventEnhance(getActivity(),dataLayer);
    }

    @Override
    public void setLastPositionProductTracker(int lastPositionProductTracker) {
        trackerProductCache.putInt(LAST_POSITION_ENHANCE_PRODUCT, lastPositionProductTracker);
        trackerProductCache.applyEditor();
    }

    @Override
    public int getLastPositionProductTracker() {
        return trackerProductCache.getInt(LAST_POSITION_ENHANCE_PRODUCT, 0);
    }

    @Override
    public void clearLastProductTracker(boolean clear) {
        if (clear) {
            LocalCacheHandler.clearCache(getActivity(), HOTLIST_DETAIL_ENHANCE_ANALYTIC);
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
        outState.putString(EXTRA_HEADER_URL, headerUrl);
        outState.putString(EXTRA_DESC, descTxt);
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
        loadImageHeader(savedInstanceState.getString(EXTRA_HEADER_URL));
        setDescription(savedInstanceState.getString(EXTRA_DESC));
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
        topAdsRecyclerAdapter.setAdsImpressionListener(new TopAdsItemImpressionListener() {
            @Override
            public void onImpressionProductAdsItem(int position, Product product) {
                TopAdsGtmTracker.eventHotlistProductView(getContext(), getQueryModel().getQueryKey(),
                        product, position);
            }
        });
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
    protected BrowseSectionGeneralAdapter getAdapter() {
        return hotlistAdapter;
    }

    @Override
    protected BrowseSectionFragmentPresenter getPresenter() {
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
    protected void switchLayoutType() {
        if (!getUserVisibleHint()) {
            return;
        }

        switch (getAdapter().getCurrentLayoutType()) {
            case GRID_1:
                setSpanCount(2);
                getGridLayoutManager().setSpanCount(spanCount);
                getAdapter().changeDoubleGridView();
                HotlistPageTracking.eventHotlistDisplay(getActivity(),"grid");
                break;
            case GRID_2:
                setSpanCount(1);
                getGridLayoutManager().setSpanCount(spanCount);
                getAdapter().changeSingleGridView();
                HotlistPageTracking.eventHotlistDisplay(getActivity(),"full");
                break;
            case GRID_3:
                setSpanCount(1);
                getAdapter().changeListView();
                HotlistPageTracking.eventHotlistDisplay(getActivity(),"list");
                break;
        }
        refreshBottomBarGridIcon();
    }

    @Override
    protected void startShareActivity(String shareUrl) {
        if (TextUtils.isEmpty(shareUrl)) {
            return;
        }
        remoteConfig = new FirebaseRemoteConfigImpl(getActivity());
        String hotlistShareMsg = remoteConfig.getString(HOTLIST_SHARE_MSG);
        if(!TextUtils.isEmpty(hotlistShareMsg)){
            String hotlistTitle = "";
            if(hotlistAdapter != null && hotlistAdapter.getItemList() != null && hotlistAdapter.getItemList().size() > 0
                    && hotlistAdapter.getItemList().get(0) instanceof HotlistHeaderViewModel){
                if(!TextUtils.isEmpty(((HotlistHeaderViewModel) hotlistAdapter.getItemList().get(0)).getHotlistTitle())){
                    hotlistTitle = ((HotlistHeaderViewModel) hotlistAdapter.getItemList().get(0)).getHotlistTitle();
                }
            }
            hotlistShareMsg = FindAndReplaceHelper.findAndReplacePlaceHolders(hotlistShareMsg, HOTLIST_NAME_PLACEHOLDER, hotlistTitle);
        }
        else {
            hotlistShareMsg = getString(R.string.message_share_category);
        }

        LinkerData shareData = LinkerData.Builder.getLinkerBuilder()
                .setType(LinkerData.DISCOVERY_TYPE)
                .setName(getString(R.string.message_share_catalog))
                .setTextContent(hotlistShareMsg)
                .setCustMsg(hotlistShareMsg)
                .setUri(shareUrl)
                .setId(aliasHotlist)
                .build();

        shareData.setType(LinkerData.HOTLIST_TYPE);
        new DefaultShare(getActivity(), shareData).show();
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
        String searchQuery = getArguments().getString(EXTRA_SEARCH_QUERY, getHotlistAlias());
        HotlistTypeFactory typeFactory = new HotlistAdapterTypeFactory(this, searchQuery, getHotlistAlias());
        hotlistAdapter = new HotlistAdapter(this, typeFactory);

        topAdsRecyclerAdapter = new TopAdsRecyclerAdapter(getActivity(), hotlistAdapter);
        topAdsConfig = new Config.Builder()
                .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                .setUserId(userSession.getUserId())
                .setEndpoint(Endpoint.PRODUCT)
                .build();
        topAdsRecyclerAdapter.setConfig(topAdsConfig);
        topAdsRecyclerAdapter.setHasHeader(true);
        topAdsRecyclerAdapter.setSpanSizeLookup(onSpanSizeLookup());
        recyclerView.setAdapter(topAdsRecyclerAdapter);
        recyclerView.addItemDecoration(new ProductItemDecoration(
                getContext().getResources().getDimensionPixelSize(R.dimen.dp_16),
                getContext().getResources().getColor(R.color.white)
        ));
        topAdsRecyclerAdapter.setLayoutManager(getGridLayoutManager());
    }


    @Override
    public void onTopAdsLoaded(List<Item> list) {

    }

    @Override
    public void onTopAdsFailToLoad(int errorCode, String message) {
        topAdsRecyclerAdapter.hideLoading();
    }

    @Override
    public void onProductItemClicked(int position, Product product) {
        Intent intent = getProductIntent(product.getId());
        startActivity(intent);
        TopAdsGtmTracker.eventHotlistProductClick(getContext(), getQueryModel().getQueryKey(),
                product, position);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((HotlistActivity) getActivity()).setFragmentListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onHandlingDataFromPDP(requestCode, resultCode, data);
    }

    @Override
    protected void sendSortTracking(String selectedSortName) {
        HotlistPageTracking.eventHotlistSort(getActivity(),selectedSortName);
    }

    @Override
    protected void handleFilterResult(Map<String, String> queryParams, Map<String, String> selectedFilters,
                                      List<Option> selectedOptions) {
        super.handleFilterResult(queryParams, selectedFilters, selectedOptions);
        showSelectedFilters(getSelectedFilter());
    }

    private void showSelectedFilters(HashMap<String, String> selectedFilter) {
        //pass viewHolder
        if (selectedFilter == null) {
            return;
        }
         for (QuickFilterItem quickFilterItem: this.quickFilterItems) {
            String[] str = quickFilterItem.getType().split("=");
            if (selectedFilter.containsKey(str[0])) {
                quickFilterItem.setSelected(true);
            } else {
                quickFilterItem.setSelected(false);
            }
        }
        hotlistAdapter.notifyDataSetChanged();
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
    public void onShopItemClicked(int position, Shop shop) {
        Intent intent = ((DiscoveryRouter) getActivity().getApplication()).getShopPageIntent(getActivity(), shop.getId());
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
                userSession.isLoggedIn() ?
                        userSession.getUserId() :
                        GCMHandler.getRegistrationId(getActivity())
        );
        parameter.setUserID(
                userSession.isLoggedIn() ?
                        userSession.getUserId() :
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
        showSelectedFilters(getSelectedFilter());
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
        HotlistPageTracking.eventClickHastag(getActivity(),url);
    }

    @Override
    public void onHotlistDescClicked(String messageClickAble) {

    }

    @Override
    public void onProductClicked(HotlistProductViewModel product, int adapterPosition) {
        trackingClicker(product, adapterPosition);
        trackingClickEnhance(product);
        gotoProductDetail(mappingIntoProductItem(product), adapterPosition);
    }

    private void trackingClickEnhance(HotlistProductViewModel product) {
        Map<String, Object> map = DataLayer.mapOf("event", "productClick",
                "eventCategory", "hotlist page",
                "eventAction", "click product list",
                "eventLabel", product.getProductUrl(),
                "ecommerce", DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "click", DataLayer.mapOf(
                                "actionField", DataLayer.mapOf("list", product.getTrackerName()),
                                "products", DataLayer.listOf(
                                        product.generateClickDataLayer()
                                )
                        )
                )
        );
        HotlistPageTracking.eventEnhance(getActivity(),map);
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

    private ProductItem mappingIntoProductItem(HotlistProductViewModel product) {
        ProductItem data = new ProductItem();
        data.setId(product.getProductID());
        data.setName(product.getProductName());
        data.setPrice(product.getPrice());
        data.setImgUri(product.getImageUrl());
        data.setTrackerAttribution(product.getHomeAttribution());
        data.setTrackerListName(product.getTrackerName());
        data.setCountCourier(!TextUtils.isEmpty(product.getCountCourier()) && product.getCountCourier().matches("\\d+") ?
                Integer.parseInt(product.getCountCourier()) : 0);
        data.setOfficial(product.isOfficial());
        data.setOriginalPrice(product.getOriginalPrice());
        data.setDiscountPercentage(product.getDiscountPercentage());
        data.setIsWishlist(product.isWishlist());
        data.setRating(product.getRating());
        data.setShop(product.getShopName());
        data.setShopLocation(product.getShopCity());
        data.setReviewCount(product.getCountReview());

        if (product.getLabelList() != null) {
            for (int i = 0; i < product.getLabelList().size(); i++) {
                if (product.getLabelList().get(i).getTitle().toLowerCase()
                        .contains(ProductItem.CASHBACK)) {
                    data.setCashback(product.getLabelList().get(i).getTitle());
                    break;
                }
            }
        }

        return data;
    }

    private void gotoProductDetail(ProductItem productItem, int adapterPosition) {
        Intent intent = getProductIntent(productItem.id);
        intent.putExtra(ProductDetailRouter.WISHLIST_STATUS_UPDATED_POSITION, adapterPosition);
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
    public void onWishlistClicked(int position, String productName, String productID, boolean wishlist) {
        if (userSession.isLoggedIn()) {
            hotlistAdapter.disableWishlistButton(productID);
            if (wishlist) {
                presenter.removeWishlist(productID);
            } else {
                presenter.addWishlist(productID);
            }
        } else {
            openLoginActivity(productID);
        }
        HotlistPageTracking.eventAddWishlist(getActivity(),position, productName, productID);
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
        if (performanceMonitoring != null && !isTraceStopped) {
            performanceMonitoring.stopTrace();
            isTraceStopped = true;
        }
    }

    @Override
    public HotlistQueryModel getQueryModel() {
        return queryModel;
    }

    protected boolean isFilterAvailable() {
        return (selectedFilter != null && !selectedFilter.isEmpty());
    }

    @Override
    public void setOfficialSelected(Boolean officialSelectedFlag) {
        super.setOfficialSelected(officialSelectedFlag);
        if(officialSelectedFlag){
            onQuickFilterSelected(KEY_OFFICIAL, officialSelectedFlag.toString());
        }
    }

    @Override
    public void renderDynamicFilter(DynamicFilterModel pojo) {
        super.renderDynamicFilter(pojo);
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(getActivity());
        if (remoteConfig.getBoolean(RemoteConfigKey.APP_SHOW_CATEGORY_QUICK_FILTERS, false)) {
            List<Option> optionList = new ArrayList<>();
            for (Filter filter : pojo.getData().getFilter()) {
                if (filter.getTitle().equalsIgnoreCase(TOKO)) {
                    for (Option option : filter.getOptions()) {
                        if (option.getName().equalsIgnoreCase(POWER_BADGE) || option.getName().equalsIgnoreCase(OFFICIAL_STORE)) {
                            optionList.add(0, option);
                        }
                    }
                } else if (filter.getTitle().equalsIgnoreCase(PENGIRIMAN)) {
                    for (Option option : filter.getOptions()) {
                        if (option.getName().equalsIgnoreCase(INSTANT_COURIER)) {
                            optionList.add(0, option);
                        }
                    }
                }
            }

            if (this.quickFilterItems != null && this.quickFilterItems.isEmpty()) {
                for (int i = 0; i < optionList.size(); i++) {
                    CustomViewRoundedQuickFilterItem quickFilterItem = new CustomViewRoundedQuickFilterItem();
                    quickFilterItem.setName(optionList.get(i).getName());
                    quickFilterItem.setType(optionList.get(i).getKey() + "=" + optionList.get(i).getValue());
                    if (optionList.get(i).getKey().equals(KEY_OFFICIAL) && pojo.getIsOfficialSelectedFlag()) {
                        quickFilterItem.setSelected(true);
                    }
                    this.quickFilterItems.add(quickFilterItem);
                }
            }

            if (!hotlistAdapter.getItemList().isEmpty()) {
                HotlistHeaderViewModel headerViewModel = (HotlistHeaderViewModel) hotlistAdapter.getItemList().get(0);
                headerViewModel.setQuickFilterList(this.quickFilterItems);
                hotlistAdapter.notifyDataSetChanged();
            }
        }
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
            eventClickHotlistProductFeatured(hotlist);
        }
    }

    public void eventClickHotlistProductFeatured(Hotlist hotlist) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf("event", AppEventTracking.Event.EVENT_INTERNAL_PROMO_MULTI,
                        "eventCategory", AppEventTracking.Category.CATEGORY_HOTLIST,
                        "eventAction", String.format("feature product hotlist %s - click product %s", hotlist.getHotlistAlias(), hotlist.getProductList().get(0).getProductName()),
                        "eventLabel", String.format("%s - %s", hotlist.getScreenName(), hotlist.getPosition(),
                                "ecommerce", DataLayer.mapOf(
                                        "click", DataLayer.mapOf(
                                                "actionField", DataLayer.mapOf(
                                                        "list", "hotlist"),
                                                "products", hotlist.getProduct().toArray(new Object[hotlist.getProduct().size()])
                                        )
                                )
                        )
                ));
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

            eventImpressionHotlistProductFeatured(hotlist);
        }

    }

    public static void eventImpressionHotlistProductFeatured(Hotlist hotlist) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf("event", AppEventTracking.Event.EVENT_INTERNAL_PROMO_MULTI,
                        "ecommerce", DataLayer.mapOf(
                                "actionField", DataLayer.mapOf("list", "hotlist"),
                                "impressions",
                                DataLayer.listOf(
                                        hotlist.getProduct().toArray(new Object[hotlist.getProduct().size()]))
                        )
                )
        );
    }

    @Override
    protected String getScreenName() {
        return getScreenNameId();
    }

    @Override
    public void stopScroll() {
        recyclerView.stopScroll();
    }

    @Override
    public String getUserId() {
        return userSession.getUserId();
    }

    @Override
    public boolean isLoggedIn() {
        return userSession.isLoggedIn();
    }

    @Override
    public void onQuickFilterSelected(String filterKey, String filterValue) {
        String eventLabel;
        if (this.selectedFilter == null) {
            this.selectedFilter = new HashMap<>();
        }
        if (this.selectedFilter.containsKey(filterKey)) {
            this.selectedFilter.remove(filterKey);
            eventLabel = "false";
        } else {
            this.selectedFilter.put(filterKey, filterValue);
            eventLabel = "true";
        }
        eventLabel = filterKey + " - " + filterValue + " - " + eventLabel;
        String id = "";
        if (queryModel != null && !TextUtils.isEmpty(queryModel.getCategoryID())) {
            id = queryModel.getCategoryID();
        }

        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.HOTLIST,
                AppEventTracking.Event.HOTLIST_PAGE,
                "quick filter" + " - " + getScreenName() + " - " + id,
                eventLabel
        ).getEvent());
        reloadData();
    }
}
