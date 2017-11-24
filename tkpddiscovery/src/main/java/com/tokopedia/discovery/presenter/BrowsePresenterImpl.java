package com.tokopedia.discovery.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.core.discovery.model.DataValue;
import com.tokopedia.core.discovery.model.DynamicFilterModel;
import com.tokopedia.core.discovery.model.HotListBannerModel;
import com.tokopedia.core.discovery.model.ObjContainer;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.topads.api.TopAdsApi;
import com.tokopedia.core.network.entity.intermediary.Data;
import com.tokopedia.core.network.entity.intermediary.SimpleCategory;
import com.tokopedia.core.network.entity.discovery.BrowseProductActivityModel;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.util.Pair;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.activity.BrowseProductAtribut;
import com.tokopedia.discovery.activity.FilterMapAtribut;
import com.tokopedia.discovery.fragment.BrowseParentFragment;
import com.tokopedia.discovery.helper.OfficialStoreQueryHelper;
import com.tokopedia.discovery.interactor.DiscoveryInteractor;
import com.tokopedia.discovery.interactor.DiscoveryInteractorImpl;
import com.tokopedia.discovery.interfaces.DiscoveryListener;
import com.tokopedia.discovery.newdynamicfilter.RevampedDynamicFilterActivity;
import com.tokopedia.discovery.search.view.fragment.SearchMainFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.tokopedia.core.router.CustomerRouter.IS_DEEP_LINK_SEARCH;
import static com.tokopedia.core.router.discovery.BrowseProductRouter.AD_SRC;
import static com.tokopedia.core.router.discovery.BrowseProductRouter.EXTRAS_SEARCH_TERM;
import static com.tokopedia.core.router.discovery.BrowseProductRouter.EXTRA_TITLE;
import static com.tokopedia.core.router.discovery.BrowseProductRouter.FRAGMENT_ID;
import static com.tokopedia.core.router.discovery.BrowseProductRouter.GridType.GRID_1;
import static com.tokopedia.core.router.discovery.BrowseProductRouter.GridType.GRID_2;
import static com.tokopedia.core.router.discovery.BrowseProductRouter.GridType.GRID_3;
import static com.tokopedia.core.router.discovery.BrowseProductRouter.VALUES_INVALID_FRAGMENT_ID;
import static com.tokopedia.discovery.activity.BrowseProductActivity.REQUEST_SORT;
import static com.tokopedia.discovery.presenter.BrowsePresenterImpl.FDest.FILTER;
import static com.tokopedia.discovery.presenter.BrowsePresenterImpl.FDest.SORT;

/**
 * Created by HenryPri on 29/03/17.
 */
public class BrowsePresenterImpl implements BrowsePresenter {

    public static final String EXTRA_BROWSE_MODEL = "EXTRA_BROWSE_MODEL";
    private static final String EXTRA_FIRST_TIME = "EXTRA_FIRST_TIME";
    private static final String EXTRA_FILTER_MAP = "EXTRA_FILTER_MAP";
    private static final String EXTRA_BROWSE_ATRIBUT = "EXTRA_BROWSE_ATRIBUT";
    private static final String LAYOUT_GRID_DEFAULT = "1";
    private static final String LAYOUT_GRID_BOX = "2";
    private static final String LAYOUT_LIST = "3";
    private static final String KEY_GTM = "GTMFilterData";
    private static final String LABEL_TRACKING = "label";

    enum FDest {
        SORT, FILTER
    }

    private BrowseView browseView;
    private DiscoveryInteractor discoveryInteractor;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();
    private BrowseProductActivityModel browseModel;
    private BrowseProductAtribut mBrowseProductAtribut;
    private FilterMapAtribut mFilterMapAtribut;
    private SharedPreferences preferences;
    private Stack<SimpleCategory> categoryLevel = new Stack<>();
    private boolean isCustomGridType = false;
    private LocalCacheHandler cacheGTM;
    private LocalCacheHandler cacheGridType;
    private BrowseProductRouter.GridType gridType = BrowseProductRouter.GridType.GRID_2;
    private boolean isBottomBarFirstTimeChange = true;
    private boolean isFetchingDynamicAttribute = false;
    private String searchQuery;
    private Context context;

    public BrowsePresenterImpl(BrowseView browseView,
                               DiscoveryInteractor discoveryInteractor,
                               SharedPreferences preferences) {

        this.browseView = browseView;
        this.discoveryInteractor = discoveryInteractor;
        this.preferences = preferences;
        this.context = browseView.getContext();
    }

    @Override
    public void initPresenterData(Bundle savedInstanceState, Intent intent) {
        if (savedInstanceState == null) {
            browseModel = new BrowseProductActivityModel();
            mBrowseProductAtribut = new BrowseProductAtribut();
            mFilterMapAtribut = new FilterMapAtribut();
            fetchIntent(intent);
            deleteFilterAndSortCache();
            setFilterOptionsFromIntent(intent);
            if (intent.hasExtra(EXTRA_BROWSE_MODEL) && intent.getParcelableExtra(EXTRA_BROWSE_MODEL) != null) {
                BrowseProductActivityModel temporary = intent.getParcelableExtra(EXTRA_BROWSE_MODEL);
                if (temporary.getFilterOptions() != null && temporary.getFilterOptions().size() > 0) {
                    browseModel.setFilterOptions(temporary.getFilterOptions());
                    browseModel.setSource(temporary.getSource());
                }
            }
        } else {
            isBottomBarFirstTimeChange = savedInstanceState.getBoolean(EXTRA_FIRST_TIME);
            browseModel = savedInstanceState.getParcelable(EXTRA_BROWSE_MODEL);
            mBrowseProductAtribut = savedInstanceState.getParcelable(EXTRA_BROWSE_ATRIBUT);
            if (mBrowseProductAtribut == null) mBrowseProductAtribut = new BrowseProductAtribut();

            mFilterMapAtribut = savedInstanceState.getParcelable(EXTRA_FILTER_MAP);

            if (mFilterMapAtribut != null && mFilterMapAtribut.getFiltersMap() != null) {
                FilterMapAtribut.FilterMapValue filterMapAtribut
                        = mFilterMapAtribut.getFiltersMap().get(browseModel.getActiveTab());

                if (filterMapAtribut != null) {
                    browseModel.setFilterOptions(filterMapAtribut.getValue());
                } else {
                    browseModel.setFilterOptions(new HashMap<String, String>());
                }
            }
        }
        cacheGridType = new LocalCacheHandler(context, TkpdCache.DEFAULT_GRID_SETTINGS);
        if (isFromCategory()) {
            retrieveLastGridConfig(browseModel.getDepartmentId());
        }
        if (SessionHandler.isV4Login(context)) {
            String userId = SessionHandler.getLoginID(context);
            browseModel.setUnique_id(AuthUtil.md5(userId));
        } else {
            browseModel.setUnique_id(AuthUtil.md5(GCMHandler.getRegistrationId(context)));
        }

        String toolbarTitle = "";
        boolean isToolbarClickable = false;

        switch (browseModel.getSource()) {
            case BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_PRODUCT:
            case BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_CATALOG:
            case BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_SHOP:
                toolbarTitle = browseModel.getQ();
                searchQuery = toolbarTitle;
                isToolbarClickable = true;
                break;
            case BrowseProductRouter.VALUES_DYNAMIC_FILTER_DIRECTORY:
                toolbarTitle = context.getString(R.string.title_activity_browse_category);
                break;
            case BrowseProductRouter.VALUES_DYNAMIC_FILTER_HOT_PRODUCT:
                toolbarTitle = context.getString(R.string.title_activity_browse_hot_detail);
                break;
        }
        String extraTitle = intent.getStringExtra(EXTRA_TITLE);
        if (extraTitle != null) {
            toolbarTitle = extraTitle;
        }

        browseView.initToolbar(toolbarTitle, isToolbarClickable);
        browseView.initDiscoverySearchView(searchQuery);

        cacheGTM = new LocalCacheHandler(context, KEY_GTM);

        if (browseModel.alias != null && browseModel.getHotListBannerModel() == null) {
            fetchHotListHeader(browseModel.alias);
        } else if (browseModel.isSearchDeeplink()) {
            sendQuery(browseModel.getQ(), "");
        } else {
            switch (browseModel.getFragmentId()) {
                case BrowseProductRouter.VALUES_PRODUCT_FRAGMENT_ID:
                    int keepActivitySettings = Settings.System.getInt(context.getContentResolver(), Settings.Global.ALWAYS_FINISH_ACTIVITIES, 0);
                    if (!browseView.isFragmentCreated(BrowseParentFragment.FRAGMENT_TAG) || keepActivitySettings == 1) {
                        browseView.showBrowseParentFragment(browseModel);
                    }
                    break;
                case BrowseProductRouter.VALUES_HISTORY_FRAGMENT_ID:
                    browseView.showSearchPage();
                    break;
            }
        }
    }

    private void setFilterOptionsFromIntent(Intent intent) {
        Serializable filterOptions = intent.getSerializableExtra(BrowseProductRouter.EXTRA_FILTER);
        if (filterOptions != null) {
            browseModel.setFilterOptions((HashMap<String, String>) filterOptions);
        }
    }

    @Override
    public void disposePresenterData() {
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(EXTRA_BROWSE_MODEL, browseModel);
        outState.putBoolean(EXTRA_FIRST_TIME, isBottomBarFirstTimeChange);
        outState.putParcelable(EXTRA_BROWSE_ATRIBUT, mBrowseProductAtribut);
        outState.putParcelable(EXTRA_FILTER_MAP, mFilterMapAtribut);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        browseModel = savedInstanceState.getParcelable(EXTRA_BROWSE_MODEL);
        mBrowseProductAtribut = savedInstanceState.getParcelable(EXTRA_BROWSE_ATRIBUT);
        mFilterMapAtribut = savedInstanceState.getParcelable(EXTRA_FILTER_MAP);
    }

    @Override
    public void handleResultData(int requestCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SORT:
                DataValue sortData = data.getParcelableExtra(BrowseParentFragment.SORT_EXTRA);
                mBrowseProductAtribut.getFilterAttributMap().put(browseModel.getActiveTab(), sortData);
                String newOb = sortData.getSelectedOb();
                if (browseModel.getActiveTab() == 1) {
                    browseModel.setObCatalog(newOb);
                } else {
                    browseModel.setOb(newOb);
                }

                if (browseModel.getHotListBannerModel() != null) {
                    browseModel.getHotListBannerModel().query.ob = browseModel.getOb();
                }
                sendSortGTM(browseModel.getOb());
                break;
            case RevampedDynamicFilterActivity.REQUEST_CODE:
                FilterMapAtribut.FilterMapValue filterMapValue =
                        data.getParcelableExtra(RevampedDynamicFilterActivity.EXTRA_SELECTED_FILTERS);
                mFilterMapAtribut.getFiltersMap()
                        .put(browseModel.getActiveTab(), filterMapValue);
                browseModel.setFilterOptions(filterMapValue.getValue());
                clearFilterAttributMap();
                sendFilterGTM(filterMapValue.getValue());
                break;
        }
    }

    @Override
    public void onBottomBarChanged(String source) {
        browseModel.setSource(source);

        switch (source) {
            case BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_SHOP:
                browseView.setupShopItemsBottomBar(source);
                break;
            default:
                browseView.setupAllItemsBottomBar(source);
        }

        if (isBottomBarFirstTimeChange) {
            if (!source.contains("shop")) {
                browseView.setFocusOnBottomBarFirstItem();
            }
            isBottomBarFirstTimeChange = false;
        }

    }

    @Override
    public boolean onBottomBarTabSelected(String source, int position, int activeTab, boolean isShopFragment) {
        DataValue filterAttribute
                = mBrowseProductAtribut.getFilterAttributMap().get(activeTab);
        switch (position) {
            case 0:
                if (isShopFragment) {
                    openFilterPageIfNeeded(filterAttribute, source, activeTab);
                } else {
                    openSortPageIfNeeded(filterAttribute, source, activeTab);
                }
                break;
            case 1:
                if (isShopFragment) {
                    switchGridType();
                } else {
                    openFilterPageIfNeeded(filterAttribute, source, activeTab);
                }
                break;
            case 2:
                switchGridType();
                break;
            case 3:
                if (isFromCategory()) {
                    browseView.openCategoryNavigation(browseModel.getDepartmentId());
                    UnifyTracking.eventBottomCategoryNavigation(browseModel.getParentDepartement(),browseModel.getDepartmentId());
                } else {
                    String shareUrl = browseView.getShareUrl();
                    if (StringUtils.isNotBlank(shareUrl)) {
                        ShareData shareData = ShareData.Builder.aShareData()
                                .setType(ShareData.DISCOVERY_TYPE)
                                .setName(context.getString(R.string.message_share_catalog))
                                .setTextContent(context.getString(R.string.message_share_category))
                                .setUri(shareUrl)
                                .build();
                        if (browseModel.getSource().equals(BrowseProductRouter.VALUES_DYNAMIC_FILTER_DIRECTORY)) {
                            shareData.setType(ShareData.CATEGORY_TYPE);
                            shareData.setDescription(browseModel.getParentDepartement() + ","
                                    + browseModel.getDepartmentId());
                        } else if (browseModel.getSource().equals(BrowseProductRouter.VALUES_DYNAMIC_FILTER_HOT_PRODUCT)) {
                            shareData.setType(ShareData.HOTLIST_TYPE);
                            shareData.setId(browseModel.getHotListBannerModel().info.aliasKey);

                        }
                        browseView.startShareActivity(shareData);
                    }
                }
                break;
        }
        return true;
    }

    private void switchGridType() {
        int gridIcon;
        int gridTitleResId;
        switch (gridType) {
            case GRID_1:
                gridType = GRID_2;
                gridIcon = R.drawable.ic_grid_default;
                gridTitleResId = R.string.grid;
                UnifyTracking.eventDisplayCategory(browseModel.getParentDepartement(),LAYOUT_GRID_DEFAULT);
                break;
            case GRID_2:
                gridType = GRID_3;
                gridIcon = R.drawable.ic_grid_box;
                gridTitleResId = R.string.grid;
                UnifyTracking.eventDisplayCategory(browseModel.getParentDepartement(),LAYOUT_GRID_BOX);
                break;
            case GRID_3:
                gridType = GRID_1;
                gridIcon = R.drawable.ic_list;
                gridTitleResId = R.string.list;
                UnifyTracking.eventDisplayCategory(browseModel.getParentDepartement(),LAYOUT_LIST);
                break;
            default:
                gridIcon = R.drawable.ic_grid_default;
                gridTitleResId = R.string.grid;
        }

        isCustomGridType = true;

                if (isFromCategory()) {
                    saveLastGridConfig(browseModel.getDepartmentId(), gridType.toString());
                }

        browseView.sendChangeGridBroadcast(gridType);
        browseView.changeBottomBarGridIcon(gridIcon, gridTitleResId);
    }

    private void fetchDynamicAttribute(final int activeTab,
                                      final String source,
                                      final FDest dest) {

        discoveryInteractor.setDiscoveryListener(new DiscoveryListener() {
            @Override
            public void onComplete(int type, Pair<String, ? extends ObjContainer> data) {
            }

            @Override
            public void onFailed(int type, Pair<String, ? extends ObjContainer> data) {
                browseView.showFailedFetchAttribute();
                isFetchingDynamicAttribute = false;
            }

            @Override
            public void onSuccess(int type, Pair<String, ? extends ObjContainer> data) {
                switch (type) {
                    case DYNAMIC_ATTRIBUTE:
                        DynamicFilterModel.DynamicFilterContainer dynamicFilterContainer
                                = (DynamicFilterModel.DynamicFilterContainer) data.getModel2();

                        DataValue filterAttribute = dynamicFilterContainer.body().getData();
                        if (filterAttribute.getSort() != null) {
                            filterAttribute.setSelected(filterAttribute.getSort().get(0).getName());
                        }
                        browseView.setFilterAttribute(filterAttribute, activeTab);
                        isFetchingDynamicAttribute = false;
                        switch (dest) {
                            case FILTER:
                                openFilterPageIfNeeded(filterAttribute, source, activeTab);
                                break;
                            case SORT:
                                openSortPageIfNeeded(filterAttribute, source, activeTab);
                                break;
                        }
                        break;
                }
            }
        });

        ((DiscoveryInteractorImpl) discoveryInteractor).setCompositeSubscription(compositeSubscription);

        String sourceKey;
        if (source.contains("catalog")) {
            sourceKey = BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_CATALOG;
        } else if (source.contains("shop")) {
            sourceKey = BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_SHOP;
        } else if (source.contains("directory") && activeTab == 0) {
            sourceKey = BrowseProductRouter.VALUES_DYNAMIC_FILTER_DIRECTORY;
        } else if (source.contains("directory") && activeTab == 1) {
            sourceKey = BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_CATALOG;
        } else {
            sourceKey = BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_PRODUCT;
        }

        discoveryInteractor.getDynamicAttribute(context, sourceKey,
                browseModel.getDepartmentId(), browseModel.getQ());
    }

    @Override
    public void fetchHotListHeader(final String alias) {
        HashMap<String, String> query = new HashMap<>();
        query.put("key", alias);
        browseView.showLoading(true);
        discoveryInteractor.setDiscoveryListener(new DiscoveryListener() {
            @Override
            public void onComplete(int type, Pair<String, ? extends ObjContainer> data) {

            }

            @Override
            public void onFailed(int type, Pair<String, ? extends ObjContainer> data) {
                browseView.showEmptyState(new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        fetchHotListHeader(alias);
                    }
                });
            }

            @Override
            public void onSuccess(int type, Pair<String, ? extends ObjContainer> data) {
                switch (type) {
                    case DiscoveryListener.HOTLIST_BANNER:
                        ObjContainer model2 = data.getModel2();
                        HotListBannerModel.HotListBannerContainer hotListBannerContainer = (HotListBannerModel.HotListBannerContainer) model2;
                        HotListBannerModel body = hotListBannerContainer.body();
                        if (browseModel.getOb() != null) {
                            body.query.ob = browseModel.getOb();
                        }
                        Map<String, String> filters;

                        if (browseModel != null) {
                            filters = browseModel.getFilterOptions();
                            for (Map.Entry<String, String> set : filters.entrySet()) {
                                if (set.getKey().equals("ob")) {
                                    body.query.ob = set.getValue();
                                }
                            }
                        } else {
                            filters = new HashMap<String, String>();
                            filters.put("sc", body.query.sc);
                            ArrayMap<String, Boolean> selectedPositions = new ArrayMap<>();
                            List<String> scList = new ArrayList<String>();
                            if (body.query.sc.contains(",")) {
                                for (String s : body.query.sc.split(",")) {
                                    scList.add(s);
                                }
                            } else {
                                scList.add(body.query.sc);
                            }
                            for (String s : scList) {
                                selectedPositions.put(s, true);
                            }
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString(RevampedDynamicFilterActivity.FILTER_CHECKED_STATE_PREF, new Gson().toJson(selectedPositions));
                            editor.apply();
                        }

                        FilterMapAtribut.FilterMapValue filterMapValue
                                = new FilterMapAtribut.FilterMapValue();
                        filterMapValue.setValue((HashMap<String, String>) filters);
                        mFilterMapAtribut.getFiltersMap()
                                .put(browseModel.getActiveTab(), filterMapValue);

                        browseModel.setFilterOptions(filters);
                        browseModel.setOb(body.query.ob);
                        browseModel.setHotListBannerModel(body);

                        browseView.showBrowseParentFragment(browseModel);
                        break;
                }
            }
        });
        ((DiscoveryInteractorImpl) discoveryInteractor).setCompositeSubscription(compositeSubscription);
        discoveryInteractor.getHotListBanner(query);
    }
    public boolean isFromCategory() {
        return !TextUtils.isEmpty(browseModel.getParentDepartement())
                && !("0").equals(browseModel.getParentDepartement());
    }

    @Override
    public void resetBrowseProductActivityModel() {
        deleteFilterAndSortCache();
        browseModel.setAdSrc(TopAdsApi.SRC_BROWSE_PRODUCT);
        browseModel.alias = null;
        browseModel.setHotListBannerModel(null);
        browseModel.removeBannerModel();
        browseModel.setDepartmentId("");
    }

    @Override
    public boolean sendQuery(String query, String depId) {

        if (OfficialStoreQueryHelper.isOfficialStoreSearchQuery(query)) {
            browseView.launchOfficialStorePage();
            return true;
        }

        searchQuery = query;
        resetBrowseProductActivityModel();
        browseModel.setQ(query);
        browseModel.setDepartmentId(depId);
        if (isBottomBarFirstTimeChange || browseModel.getSource().equals(BrowseProductRouter.VALUES_DYNAMIC_FILTER_HOT_PRODUCT)
                || browseModel.getSource().equals(BrowseProductRouter.VALUES_DYNAMIC_FILTER_DIRECTORY)) {
            browseModel.setSource(BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_PRODUCT);
            browseModel.setOb("23");
        }
        browseView.showBrowseParentFragment(browseModel);
        deleteFilterCache();
        browseView.sendQueryBroadcast(query);

        int currentSuggestionTab = browseView.getCurrentSuggestionTab();

        if (currentSuggestionTab == SearchMainFragment.PAGER_POSITION_PRODUCT) {
            browseModel.setSource(BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_PRODUCT);
        } else if (currentSuggestionTab == SearchMainFragment.PAGER_POSITION_SHOP) {
            browseModel.setSource(BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_SHOP);
        }
        return false;
    }

    @Override
    public void sendHotlist(String selected, String keyword) {
        fetchHotListHeader(selected);
        browseModel.setQ(keyword);
        browseModel.setSource(BrowseProductRouter.VALUES_DYNAMIC_FILTER_HOT_PRODUCT);
        browseModel.alias = selected;
    }

    @Override
    public BrowseProductActivityModel getBrowseProductActivityModel() {
        return browseModel;
    }

    @Override
    public void setFilterAttribute(DataValue filterAttribute, int activeTab) {
        if (checkHasFilterAttributeIsNull(activeTab))
            mBrowseProductAtribut.getFilterAttributMap().put(activeTab, filterAttribute);
    }

    @Override
    public boolean checkHasFilterAttributeIsNull(int activeTab) {
        return mBrowseProductAtribut.getFilterAttributMap().get(activeTab) == null;
    }

    @Override
    public void onSetFragment(int fragmentId) {
        browseModel.setFragmentId(BrowseProductRouter.VALUES_PRODUCT_FRAGMENT_ID);
    }

    @Override
    public void onBackPressed() {
        if (categoryLevel.size() > 0) {
            resetBrowseProductActivityModel();
            SimpleCategory simpleCategory = categoryLevel.pop();
            browseModel.setAdSrc(TopAdsApi.SRC_DIRECTORY);
            browseModel.setDepartmentId(simpleCategory.getId());
            browseModel.setSource(BrowseProductRouter.VALUES_DYNAMIC_FILTER_DIRECTORY);
            browseView.renderUpperCategoryLevel(simpleCategory);
        } else {
            browseView.close();
        }
    }

    @Override
    public void onRenderLowerCategoryLevel(String departmentId, String name, String title) {
        categoryLevel.push(new SimpleCategory(browseModel.getDepartmentId(), title));
        if (departmentId != null && name != null) {
            browseModel.setDepartmentId(departmentId);
        }
    }

    @Override
    public String getSearchQuery() {
        return searchQuery;
    }

    @Override
    public BrowseProductRouter.GridType getGridType() {
        return gridType;
    }

    @Override
    public void onRenderUpperCategoryLevel(String departmentId) {
        if (departmentId != null) {
            browseModel.setDepartmentId(departmentId);
        }
    }

    @Override
    public void retrieveLastGridConfig(final String departmentId) {
        isCustomGridType = false;
        Subscription subscription = Observable
                .create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        String config = cacheGridType.getString(departmentId);
                        subscriber.onNext(config);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String gridConfig) {
                        if (!TextUtils.isEmpty(gridConfig)) {
                            gridType = BrowseProductRouter.GridType.valueOf(gridConfig);
                            int gridIcon;
                            int gridTitleRes;
                            switch (gridType) {
                                case GRID_1:
                                    gridIcon = R.drawable.ic_list;
                                    gridTitleRes = R.string.list;
                                    break;
                                case GRID_2:
                                    gridIcon = R.drawable.ic_grid_default;
                                    gridTitleRes = R.string.grid;
                                    break;
                                case GRID_3:
                                    gridIcon = R.drawable.ic_grid_box;
                                    gridTitleRes = R.string.grid;
                                    break;
                                default:
                                    gridIcon = R.drawable.ic_grid_default;
                                    gridTitleRes = R.string.grid;
                            }
                            isCustomGridType = true;
                            browseView.sendChangeGridBroadcast(gridType);
                            browseView.changeBottomBarGridIcon(gridIcon, gridTitleRes);
                        }
                    }
                });
        getCompositeSubscription().add(subscription);
    }

    @Override
    public void setDefaultGridTypeFromNetwork(Integer viewType) {
        changeGridTypeIfNeeded(viewType);
    }

    private void changeGridTypeIfNeeded(Integer viewType) {
        if (!isFromCategory() || isCustomGridType) {
            return;
        }

        int gridIcon;
        int gridTitleRes;

        switch (viewType) {
            case Data.GRID_2_VIEW_TYPE:
                this.gridType = BrowseProductRouter.GridType.GRID_2;
                gridIcon = R.drawable.ic_grid_default;
                gridTitleRes = R.string.grid;
                break;
            case Data.GRID_1_VIEW_TYPE:
                this.gridType = BrowseProductRouter.GridType.GRID_3;
                gridIcon = R.drawable.ic_grid_box;
                gridTitleRes = R.string.grid;
                break;
            case Data.LIST_VIEW_TYPE:
                this.gridType = BrowseProductRouter.GridType.GRID_1;
                gridIcon = R.drawable.ic_list;
                gridTitleRes = R.string.list;
                break;
            default:
                this.gridType = BrowseProductRouter.GridType.GRID_2;
                gridIcon = R.drawable.ic_grid_default;
                gridTitleRes = R.string.grid;
        }

        browseView.sendChangeGridBroadcast(gridType);
        browseView.changeBottomBarGridIcon(gridIcon, gridTitleRes);
    }

    private CompositeSubscription getCompositeSubscription() {
        compositeSubscription = RxUtils.getNewCompositeSubIfUnsubscribed(compositeSubscription);
        return compositeSubscription;
    }

    private void saveLastGridConfig(String departmentId, String gridType) {
        cacheGridType.putString(departmentId, gridType);
        cacheGridType.applyEditor();
    }

    private void openFilterPageIfNeeded(DataValue filterAttribute, String source, int activeTab) {
        if (filterAttribute != null ) {
            Map<String, String> filters;
            if (mFilterMapAtribut != null && mFilterMapAtribut.getFiltersMap() != null) {
                if (mFilterMapAtribut.getFiltersMap().get(source) != null) {
                    filters = mFilterMapAtribut.getFiltersMap().get(source).getValue();
                } else {
                    filters = new HashMap<>();
                }
            } else {
                filters = new HashMap<>();
            }
            browseView.openFilter(filterAttribute, source,
                    browseModel.getParentDepartement(), browseModel.getDepartmentId(), filters);
        } else if (!isFetchingDynamicAttribute) {
            isFetchingDynamicAttribute = true;
            fetchDynamicAttribute(activeTab, source, FILTER);
        }
    }

    private void openSortPageIfNeeded(DataValue filterAttribute, String source, int activeTab) {
        if (filterAttribute != null) {
            if (browseModel.getOb() != null) {
                filterAttribute.setSelectedOb(browseModel.getOb());
            }
            browseView.openSort(filterAttribute, source);
        } else if (!isFetchingDynamicAttribute) {
            isFetchingDynamicAttribute = true;
            fetchDynamicAttribute(activeTab, source, SORT);
        }
    }

    private void fetchIntent(Intent intent) {
        if (intent != null) {
            //[START] check hot list param
            browseModel.alias
                    = intent.getStringExtra(BrowseProductRouter.EXTRAS_DISCOVERY_ALIAS);

            //[END] check hot list param
            String source = intent.getStringExtra(BrowseProductRouter.EXTRA_SOURCE);
            if (source != null) {
                browseModel.setSource(source);
            }
            if (browseModel.alias == null) {
                // get department and fragment id that would be shown.
                String departmentId = intent.getStringExtra(BrowseProductRouter.DEPARTMENT_ID);
                int fragmentId = intent.getIntExtra(FRAGMENT_ID, VALUES_INVALID_FRAGMENT_ID);
                String adSrc = intent.getStringExtra(AD_SRC);

                this.searchQuery = intent.getStringExtra(EXTRAS_SEARCH_TERM);
                browseModel.setQ(this.searchQuery);
                // set the value get from intent
                if (adSrc != null)
                    browseModel.setAdSrc(adSrc);
                if (departmentId != null) {
                    browseModel.setDepartmentId(departmentId);
                    browseModel.setParentDepartement(departmentId);
                }
                browseModel.setFragmentId(fragmentId);
            }
            browseModel.setSearchDeeplink(intent.getBooleanExtra(IS_DEEP_LINK_SEARCH, false));
        }
    }

    private void deleteFilterCache() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(RevampedDynamicFilterActivity.FILTER_CHECKED_STATE_PREF);
        editor.remove(RevampedDynamicFilterActivity.FILTER_TEXT_PREF);
        editor.remove(RevampedDynamicFilterActivity.FILTER_SELECTED_CATEGORY_ROOT_ID_PREF);
        editor.remove(RevampedDynamicFilterActivity.FILTER_SELECTED_CATEGORY_ID_PREF);
        editor.remove(RevampedDynamicFilterActivity.FILTER_SELECTED_CATEGORY_NAME_PREF);
        editor.apply();
        if (browseModel != null) {
            browseModel.setFilterOptions(new HashMap<String, String>());
        }

    }

    private void deleteFilterAndSortCache() {
        deleteFilterCache();
        browseModel.setOb("23");
        clearFilterAttributMap();
    }

    private void clearFilterAttributMap() {
        if (mBrowseProductAtribut != null && mBrowseProductAtribut.getFilterAttributMap() != null) {
            mBrowseProductAtribut.getFilterAttributMap().clear();
        }
    }

    private void sendFilterGTM(Map<String, String> maps) {
        String sortFilterData = "";
        if (TextUtils.isEmpty(cacheGTM.getString(KEY_GTM)) || cacheGTM.isExpired()) {
            sortFilterData = TrackingUtils.getGtmString(AppEventTracking.GTM.FILTER_SORT);
            if (TextUtils.isEmpty(sortFilterData))
                return;
            cacheGTM.putString(KEY_GTM, sortFilterData);
            cacheGTM.setExpire(86400);
            cacheGTM.applyEditor();
        } else {
            sortFilterData = cacheGTM.getString(KEY_GTM);
        }

        try {
            JSONObject jsonObject = new JSONObject(sortFilterData);
            JSONArray dynamicFilter = jsonObject.getJSONArray("dynamic_filter");
            String filteredKey = jsonObject.getString("dynamic_filter_key");
            for (Map.Entry<String, String> map : maps.entrySet()) {
                if (filteredKey.contains(map.getKey())) {
                    for (int i = 0; i < dynamicFilter.length(); i++) {
                        JSONObject item = (JSONObject) dynamicFilter.get(i);
                        if (item.getString("key").equalsIgnoreCase(map.getKey())) {
                            if (TextUtils.isEmpty(item.getString("value")) ||
                                    item.getString("value").equalsIgnoreCase(map.getValue())) {
                                UnifyTracking.eventDiscoveryFilter(item.getString(LABEL_TRACKING));
                                if (browseModel.getSource().equals(BrowseProductRouter.
                                        VALUES_DYNAMIC_FILTER_DIRECTORY)) {
                                    UnifyTracking.eventFilterCategory(browseModel.getParentDepartement(),
                                            item.getString(LABEL_TRACKING));
                                }
                            }
                            break;
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendSortGTM(String valueSort) {
        String sortFilterData = "";
        if (TextUtils.isEmpty(cacheGTM.getString(KEY_GTM)) || cacheGTM.isExpired()) {
            sortFilterData = TrackingUtils.getGtmString(AppEventTracking.GTM.FILTER_SORT);
            if (TextUtils.isEmpty(sortFilterData))
                return;
            cacheGTM.putString(KEY_GTM, sortFilterData);
            cacheGTM.setExpire(86400);
            cacheGTM.applyEditor();
        } else {
            sortFilterData = cacheGTM.getString(KEY_GTM);
        }

        try {
            JSONObject jsonObject = new JSONObject(sortFilterData);
            JSONArray dynamicSort = jsonObject.getJSONArray("dynamic_sort");
            for (int i = 0; i < dynamicSort.length(); i++) {
                JSONObject item = (JSONObject) dynamicSort.get(i);
                if (item.getString("value").equalsIgnoreCase(valueSort)) {
                    UnifyTracking.eventDiscoverySort(item.getString(LABEL_TRACKING));
                    if (browseModel.getSource().equals(BrowseProductRouter.VALUES_DYNAMIC_FILTER_DIRECTORY)) {
                        UnifyTracking.eventSortCategory(browseModel.getParentDepartement(),
                                item.getString(LABEL_TRACKING));
                    }
                    break;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
