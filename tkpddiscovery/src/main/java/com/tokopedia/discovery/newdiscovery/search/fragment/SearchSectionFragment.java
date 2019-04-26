package com.tokopedia.discovery.newdiscovery.search.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.discovery.model.DynamicFilterModel;
import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.core.discovery.model.Sort;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.share.DefaultShare;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.activity.SortProductActivity;
import com.tokopedia.discovery.newdiscovery.analytics.SearchTracking;
import com.tokopedia.discovery.newdiscovery.base.BottomSheetListener;
import com.tokopedia.discovery.newdiscovery.base.RedirectionListener;
import com.tokopedia.discovery.newdiscovery.hotlist.view.activity.HotlistActivity;
import com.tokopedia.discovery.newdiscovery.search.SearchNavigationListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.ProductListFragment;
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter;
import com.tokopedia.discovery.newdynamicfilter.RevampedDynamicFilterActivity;
import com.tokopedia.discovery.newdynamicfilter.controller.FilterController;
import com.tokopedia.discovery.newdynamicfilter.helper.FilterHelper;
import com.tokopedia.discovery.newdynamicfilter.helper.OptionHelper;
import com.tokopedia.discovery.newdynamicfilter.helper.SortHelper;
import com.tokopedia.linker.model.LinkerData;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.topads.sdk.domain.TopAdsParams;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tokopedia.core.home.helper.ProductFeedHelper.LANDSCAPE_COLUMN_MAIN;
import static com.tokopedia.core.home.helper.ProductFeedHelper.PORTRAIT_COLUMN_MAIN;

/**
 * Created by henrypriyono on 10/16/17.
 */

public abstract class SearchSectionFragment extends BaseDaggerFragment
        implements SearchSectionFragmentView {

    public static final int REQUEST_CODE_GOTO_PRODUCT_DETAIL = 4;

    protected static final int START_ROW_FIRST_TIME_LOAD = 0;

    private static final String EXTRA_SPAN_COUNT = "EXTRA_SPAN_COUNT";
    private static final String EXTRA_FILTER = "EXTRA_FILTER";
    private static final String EXTRA_SORT = "EXTRA_SORT";
    private static final String EXTRA_SELECTED_FILTER = "EXTRA_SELECTED_FILTER";
    private static final String EXTRA_SELECTED_SORT = "EXTRA_SELECTED_SORT";
    private static final String EXTRA_SHOW_BOTTOM_BAR = "EXTRA_SHOW_BOTTOM_BAR";
    private static final String EXTRA_IS_GETTING_DYNNAMIC_FILTER = "EXTRA_IS_GETTING_DYNNAMIC_FILTER";
    private static final String DEFAULT_GRID = "default";
    private static final String INSTAGRAM_GRID = "instagram grid";
    private static final String LIST_GRID = "list";
    protected static final String EXTRA_SEARCH_PARAMETER = "EXTRA_SEARCH_PARAMETER";

    private SearchNavigationListener searchNavigationListener;
    private BottomSheetListener bottomSheetListener;
    private RedirectionListener redirectionListener;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout refreshLayout;
    private boolean showBottomBar;
    public int spanCount;

    private ArrayList<Sort> sort;
    private ArrayList<Filter> filters;
    private HashMap<String, String> selectedSort;
    private boolean isGettingDynamicFilter;
    private boolean isUsingBottomSheetFilter;
    protected boolean isListEmpty = false;

    protected SearchParameter searchParameter;
    protected FilterController filterController = new FilterController();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint()) {
            setupSearchNavigation();
        }

        if (savedInstanceState == null) {
            refreshLayout.post(this::onFirstTimeLaunch);
        } else {
            onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSpan();
        initLayoutManager();
        initSwipeToRefresh(view);
    }

    private void initSwipeToRefresh(View view) {
        refreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        refreshLayout.setOnRefreshListener(this::onSwipeToRefresh);
    }

    @Override
    public void showRefreshLayout() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void hideRefreshLayout() {
        refreshLayout.setRefreshing(false);
    }

    private void initSpan() {
        setSpanCount(calcColumnSize(getResources().getConfiguration().orientation));
    }

    private int calcColumnSize(int orientation) {
        int defaultColumnNumber = 1;
        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                defaultColumnNumber = PORTRAIT_COLUMN_MAIN;
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                defaultColumnNumber = LANDSCAPE_COLUMN_MAIN;
                break;
        }
        return defaultColumnNumber;
    }

    private void initLayoutManager() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        gridLayoutManager = new GridLayoutManager(getActivity(), getSpanCount());
        gridLayoutManager.setSpanSizeLookup(onSpanSizeLookup());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchNavigationListener) {
            this.searchNavigationListener = (SearchNavigationListener) context;
        }
        if (context instanceof BottomSheetListener) {
            this.bottomSheetListener = (BottomSheetListener) context;
        }
        if (context instanceof RedirectionListener) {
            this.redirectionListener = (RedirectionListener) context;
        }
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(context);
        isUsingBottomSheetFilter = remoteConfig.getBoolean(
                RemoteConfigKey.ENABLE_BOTTOM_SHEET_FILTER,
                true) && (this instanceof ProductListFragment);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && getView() != null) {
            setupSearchNavigation();
            screenTrack();
        }
    }

    protected void screenTrack() {
        if (getUserVisibleHint()) {
            ScreenTracking.screen(MainApplication.getAppContext(), getScreenName());
        }
    }

    private void setupSearchNavigation() {
        searchNavigationListener
                .setupSearchNavigation(new SearchNavigationListener.ClickListener() {
                    @Override
                    public void onFilterClick() {
                        openFilterActivity();
                    }

                    @Override
                    public void onSortClick() {
                        openSortActivity();
                    }

                    @Override
                    public void onChangeGridClick() {
                        switchLayoutType();
                    }
                }, isSortEnabled());
        refreshMenuItemGridIcon();
    }

    protected GridLayoutManager getGridLayoutManager() {
        return gridLayoutManager;
    }

    protected LinearLayoutManager getLinearLayoutManager() {
        return linearLayoutManager;
    }

    protected void switchLayoutType() {
        if (!getUserVisibleHint()) {
            return;
        }

        switch (getAdapter().getCurrentLayoutType()) {
            case GRID_1:
                setSpanCount(2);
                gridLayoutManager.setSpanCount(spanCount);
                getAdapter().changeDoubleGridView();
                SearchTracking.eventSearchResultChangeGrid(getActivity(),"grid 2", getScreenName());
                break;
            case GRID_2:
                setSpanCount(1);
                gridLayoutManager.setSpanCount(spanCount);
                getAdapter().changeSingleGridView();
                SearchTracking.eventSearchResultChangeGrid(getActivity(), "grid 1", getScreenName());
                break;
            case GRID_3:
                setSpanCount(1);
                getAdapter().changeListView();
                SearchTracking.eventSearchResultChangeGrid(getActivity(),"list", getScreenName());
                break;
        }
        refreshMenuItemGridIcon();
    }

    public void refreshMenuItemGridIcon() {
        if(searchNavigationListener == null) return;

        searchNavigationListener.refreshMenuItemGridIcon(getAdapter().getTitleTypeRecyclerView(), getAdapter().getIconTypeRecyclerView());
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
    }

    private int getSpanCount() {
        return spanCount;
    }

    protected void startShareActivity(String shareUrl) {

        if (TextUtils.isEmpty(shareUrl)) {
            return;
        }

        SearchTracking.eventSearchResultShare(getActivity(), getScreenName());

        LinkerData shareData = LinkerData.Builder.getLinkerBuilder()
                .setType(LinkerData.DISCOVERY_TYPE)
                .setName(getString(R.string.message_share_catalog))
                .setTextContent(getString(R.string.message_share_category))
                .setUri(shareUrl)
                .build();

        if(getActivity() instanceof HotlistActivity){
            shareData.setType(LinkerData.HOTLIST_TYPE);
        } else {
            SearchTracking.eventSearchResultShare(getActivity(), getScreenName());
        }
        new DefaultShare(getActivity(), shareData).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == getSortRequestCode()) {
                setSelectedSort(new HashMap<>(getMapFromIntent(data, SortProductActivity.EXTRA_SELECTED_SORT)));
                String selectedSortName = data.getStringExtra(SortProductActivity.EXTRA_SELECTED_NAME);
                UnifyTracking.eventSearchResultSort(getActivity(),getScreenName(), selectedSortName);

                if(searchParameter != null) {
                    searchParameter.getSearchParameterHashMap().putAll(getSelectedSort());
                }

                clearDataFilterSort();
                reloadData();
            } else if (requestCode == getFilterRequestCode()) {
                Map<String, String> filterParameter = getMapFromIntent(data, RevampedDynamicFilterActivity.EXTRA_FILTER_PARAMETER);
                Map<String, String> activeFilterParameter = getMapFromIntent(data, RevampedDynamicFilterActivity.EXTRA_SELECTED_FILTERS);

                SearchTracking.eventSearchResultFilter(getActivity(), getScreenName(), activeFilterParameter);

                applyFilterToSearchParameter(filterParameter);
                setSelectedFilter(new HashMap<>(filterParameter));
                clearDataFilterSort();
                reloadData();
            }
        }
    }

    private Map<String, String> getMapFromIntent(Intent data, String extraName) {
        Serializable serializableExtra = data.getSerializableExtra(extraName);

        if(serializableExtra == null) return new HashMap<>();

        Map<?, ?> filterParameterMapIntent = (Map<?, ?>)data.getSerializableExtra(extraName);
        Map<String, String> filterParameter = new HashMap<>(filterParameterMapIntent.size());

        for(Map.Entry<?, ?> entry: filterParameterMapIntent.entrySet()) {
            filterParameter.put(entry.getKey().toString(), entry.getValue().toString());
        }

        return filterParameter;
    }

    private void setFilterData(List<Filter> filters) {
        this.filters = new ArrayList<>();
        if (filters == null) {
            return;
        }

        this.filters.addAll(filters);
    }

    protected ArrayList<Filter> getFilters() {
        return filters;
    }

    protected void setSortData(List<Sort> sorts) {
        this.sort = new ArrayList<>();
        if (sorts == null) {
            return;
        }

        this.sort.addAll(sorts);
    }

    private ArrayList<Sort> getSort() {
        return sort;
    }

    @Override
    public HashMap<String, String> getSelectedSort() {
        return selectedSort;
    }

    @Override
    public void setSelectedSort(HashMap<String, String> selectedSort) {
        this.selectedSort = selectedSort;
    }

    @Override
    public HashMap<String, String> getSelectedFilter() {
        if(filterController == null) return new HashMap<>();

        return new HashMap<>(filterController.getActiveFilterMap());
    }

    protected boolean isFilterActive() {
        if(filterController == null) return false;

        return filterController.isFilterActive();
    }

    @Override
    public HashMap<String, String> getExtraFilter() {
        return null;
    }

    @Override
    public void setSelectedFilter(HashMap<String, String> selectedFilter) {
        if(filterController == null || getFilters() == null) return;

        List<Filter> initializedFilterList = FilterHelper.initializeFilterList(getFilters());
        filterController.initFilterController(selectedFilter, initializedFilterList);
    }

    public void clearDataFilterSort() {
        if (filters != null) {
            this.filters.clear();
        }
        if (sort != null) {
            this.sort.clear();
        }
    }

    protected void openFilterActivity() {
        if (!isFilterDataAvailable()) {
            NetworkErrorHelper.showSnackbar(getActivity(), getActivity().getString(R.string.error_filter_data_not_ready));
            return;
        }

        if (bottomSheetListener != null && isUsingBottomSheetFilter) {
            openBottomSheetFilter();
        } else {
            openFilterPage();
        }
    }

    protected void openBottomSheetFilter() {
        if(searchParameter == null || getFilters() == null) return;

        bottomSheetListener.loadFilterItems(getFilters(), searchParameter.getSearchParameterHashMap());
        bottomSheetListener.launchFilterBottomSheet();
    }

    protected void openFilterPage() {
        if (searchParameter == null) return;

        Intent intent = RevampedDynamicFilterActivity.createInstance(
                getActivity(), getScreenName(), searchParameter.getSearchParameterHashMap(), null
        );
        startActivityForResult(intent, getFilterRequestCode());

        if (getActivity() != null) {
            getActivity().overridePendingTransition(R.anim.pull_up, android.R.anim.fade_out);
        }
    }

    protected boolean isFilterDataAvailable() {
        return filters != null && !filters.isEmpty();
    }

    protected void openSortActivity() {
        if (isSortDataAvailable()) {
            Intent intent = SortProductActivity.createInstance(
                    getActivity(), getSort(), getSelectedSort()
            );
            startActivityForResult(intent, getSortRequestCode());

            if(getActivity() != null) {
                getActivity().overridePendingTransition(R.anim.pull_up, android.R.anim.fade_out);
            }
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), getActivity().getString(R.string.error_sort_data_not_ready));
        }
    }

    private boolean isSortDataAvailable() {
        return sort != null && !sort.isEmpty();
    }

    @Override
    public void getDynamicFilter() {
        if (canRequestDynamicFilter()) {
            isGettingDynamicFilter = true;
            requestDynamicFilter();
        }
    }

    private boolean canRequestDynamicFilter() {
        return !isFilterDataAvailable()
                && !isGettingDynamicFilter;
    }

    //TODO will be removed after catalog and shop already migrated also to Gql
    protected void requestDynamicFilter() {
        getPresenter().requestDynamicFilter();
    }

    @Override
    public void renderDynamicFilter(DynamicFilterModel pojo) {
        isGettingDynamicFilter = false;
        setFilterData(pojo.getData().getFilter());
        setSortData(pojo.getData().getSort());

        if(filterController == null || searchParameter == null
                || getFilters() == null || getSort() == null) return;

        List<Filter> initializedFilterList = FilterHelper.initializeFilterList(getFilters());
        filterController.initFilterController(searchParameter.getSearchParameterHashMap(), initializedFilterList);
        initSelectedSort();

        if(isListEmpty) {
            refreshAdapterForEmptySearch();
        }
    }

    private void initSelectedSort() {
        if(getSort() == null) return;

        HashMap<String, String> selectedSort = new HashMap<>(
                SortHelper.Companion.getSelectedSortFromSearchParameter(searchParameter.getSearchParameterHashMap(), getSort())
        );

        setSelectedSort(selectedSort);
    }

    protected abstract void refreshAdapterForEmptySearch();

    @Override
    public void renderFailGetDynamicFilter() {
        isGettingDynamicFilter = false;
        NetworkErrorHelper.showSnackbar(getActivity(), getActivity().getString(R.string.error_get_dynamic_filter));
    }

    public void performNewProductSearch(String query, boolean forceSearch) {
        redirectionListener.performNewProductSearch(query, forceSearch);
    }

    public void showSearchInputView() {
        redirectionListener.showSearchInputView();
    }

    protected boolean isRefreshing() {
        return refreshLayout.isRefreshing();
    }

    protected TopAdsParams enrichWithFilterAndSortParams(TopAdsParams topAdsParams) {
        if (getSelectedSort() != null) {
            topAdsParams.getParam().putAll(getSelectedSort());
        }
        if (getSelectedFilter() != null) {
            topAdsParams.getParam().putAll(getSelectedFilter());
        }
        if (getExtraFilter() != null) {
            topAdsParams.getParam().putAll(getExtraFilter());
        }
        return topAdsParams;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_SPAN_COUNT, getSpanCount());
        outState.putParcelable(EXTRA_SEARCH_PARAMETER, searchParameter);
        outState.putParcelableArrayList(EXTRA_FILTER, getFilters());
        outState.putParcelableArrayList(EXTRA_SORT, getSort());
        outState.putSerializable(EXTRA_SELECTED_FILTER, getSelectedFilter());
        outState.putSerializable(EXTRA_SELECTED_SORT, getSelectedSort());
        outState.putBoolean(EXTRA_SHOW_BOTTOM_BAR, showBottomBar);
        outState.putBoolean(EXTRA_IS_GETTING_DYNNAMIC_FILTER, isGettingDynamicFilter);
    }

    public abstract void reloadData();

    protected abstract int getFilterRequestCode();

    protected abstract int getSortRequestCode();

    protected abstract SearchSectionGeneralAdapter getAdapter();

    protected abstract SearchSectionFragmentPresenter getPresenter();

    protected abstract GridLayoutManager.SpanSizeLookup onSpanSizeLookup();

    protected abstract boolean isSortEnabled();

    protected void onFirstTimeLaunch() {

    }

    protected void disableSwipeRefresh() {
        refreshLayout.setEnabled(false);
        refreshLayout.setRefreshing(false);
    }

    protected void onSwipeToRefresh() {

    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        setSpanCount(savedInstanceState.getInt(EXTRA_SPAN_COUNT));
        copySearchParameter(savedInstanceState.getParcelable(EXTRA_SEARCH_PARAMETER));
        setFilterData(savedInstanceState.getParcelableArrayList(EXTRA_FILTER));
        setSortData(savedInstanceState.getParcelableArrayList(EXTRA_SORT));
        setSelectedFilter((HashMap<String, String>) savedInstanceState.getSerializable(EXTRA_SELECTED_FILTER));
        setSelectedSort((HashMap<String, String>) savedInstanceState.getSerializable(EXTRA_SELECTED_SORT));
        showBottomBar = savedInstanceState.getBoolean(EXTRA_SHOW_BOTTOM_BAR);
        isGettingDynamicFilter = savedInstanceState.getBoolean(EXTRA_IS_GETTING_DYNNAMIC_FILTER);
    }

    @Override
    public void setTotalSearchResultCount(String formattedResultCount) {
        if (bottomSheetListener != null) {
            bottomSheetListener.setFilterResultCount(formattedResultCount);
        }
    }

    protected RecyclerView.OnScrollListener getRecyclerViewBottomSheetScrollListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING && bottomSheetListener != null) {
                    bottomSheetListener.closeFilterBottomSheet();
                }
            }
        };
    }

    public void onBottomSheetHide() {
        SearchTracking.eventSearchResultCloseBottomSheetFilter(getActivity(), getScreenName(), getSelectedFilter());
    }

    protected boolean isUsingBottomSheetFilter() {
        return isUsingBottomSheetFilter;
    }

    protected void removeSelectedFilter(String uniqueId) {
        if(filterController == null) return;

        Option option = OptionHelper.generateOptionFromUniqueId(uniqueId);

        removeFilterFromFilterController(option);
        applyFilterToSearchParameter(filterController.getFilterParameter());
        clearDataFilterSort();
        reloadData();
    }

    public void removeFilterFromFilterController(Option option) {
        if(filterController == null) return;

        String optionKey = option.getKey();

        if (Option.KEY_CATEGORY.equals(optionKey)) {
            filterController.setFilter(option, false, true);
        } else if (Option.KEY_PRICE_MIN.equals(optionKey) ||
                Option.KEY_PRICE_MAX.equals(optionKey)) {
            filterController.setFilter(generatePriceOption(Option.KEY_PRICE_MIN), false, true);
            filterController.setFilter(generatePriceOption(Option.KEY_PRICE_MAX), false, true);
        } else {
            filterController.setFilter(option, false);
        }
    }

    private Option generatePriceOption(String priceOptionKey) {
        Option option = new Option();
        option.setKey(priceOptionKey);
        return option;
    }

    protected void copySearchParameter(@Nullable SearchParameter searchParameterToCopy) {
        if (searchParameterToCopy != null) {
            this.searchParameter = new SearchParameter(searchParameterToCopy);
        }
    }

    public void applyFilterToSearchParameter(Map<String, String> filterParameter) {
        if(searchParameter == null) return;

        this.searchParameter.getSearchParameterHashMap().clear();
        this.searchParameter.getSearchParameterHashMap().putAll(filterParameter);
    }

    protected List<Option> getOptionListFromFilterController() {
        if(filterController == null) return new ArrayList<>();

        return OptionHelper.combinePriceFilterIfExists(filterController.getActiveFilterOptionList(),
                getResources().getString(R.string.empty_state_selected_filter_price_name));
    }
}
