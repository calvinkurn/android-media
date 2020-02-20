package com.tokopedia.discovery.newdiscovery.search.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.tokopedia.core.analytics.HotlistPageTracking;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.share.DefaultShare;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.analytics.DiscoveryTracking;
import com.tokopedia.discovery.newdiscovery.base.BottomNavigationListener;
import com.tokopedia.discovery.newdiscovery.base.RedirectionListener;
import com.tokopedia.discovery.newdiscovery.hotlist.view.activity.HotlistActivity;
import com.tokopedia.filter.common.data.DynamicFilterModel;
import com.tokopedia.filter.common.data.Filter;
import com.tokopedia.filter.common.data.Option;
import com.tokopedia.filter.common.data.Sort;
import com.tokopedia.filter.common.manager.FilterSortManager;
import com.tokopedia.filter.newdynamicfilter.analytics.FilterEventTracking;
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTracking;
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData;
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper;
import com.tokopedia.linker.model.LinkerData;
import com.tokopedia.topads.sdk.domain.TopAdsParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by henrypriyono on 10/16/17.
 */

public abstract class BrowseSectionFragment extends BaseDaggerFragment
        implements BrowseSectionFragmentView {

    public static final int LANDSCAPE_COLUMN_MAIN = 3;
    public static final int PORTRAIT_COLUMN_MAIN = 2;

    public static final int REQUEST_CODE_GOTO_PRODUCT_DETAIL = 4;

    protected static final int START_ROW_FIRST_TIME_LOAD = 0;

    private static final String EXTRA_SPAN_COUNT = "EXTRA_SPAN_COUNT";
    private static final String EXTRA_FILTER = "EXTRA_FILTER";
    private static final String EXTRA_SORT = "EXTRA_SORT";
    private static final String EXTRA_SHOW_BOTTOM_BAR = "EXTRA_SHOW_BOTTOM_BAR";
    private static final String EXTRA_IS_GETTING_DYNNAMIC_FILTER = "EXTRA_IS_GETTING_DYNNAMIC_FILTER";
    private static final String EXTRA_FLAG_FILTER_HELPER = "EXTRA_FLAG_FILTER_HELPER";
    private static final String DEFAULT_GRID = "default";
    private static final String INSTAGRAM_GRID = "instagram grid";
    private static final String LIST_GRID = "list";

    private BottomNavigationListener bottomNavigationListener;
    private RedirectionListener redirectionListener;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout refreshLayout;
    private boolean showBottomBar;
    public int spanCount;
    private FilterTrackingData filterTrackingData;

    private ArrayList<Sort> sort;
    private ArrayList<Filter> filters;
    private HashMap<String, String> selectedSort;
    protected HashMap<String, String> selectedFilter;
    private boolean isGettingDynamicFilter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint()) {
            setupBottomNavigation();
        }

        if (savedInstanceState == null) {
            refreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    onFirstTimeLaunch();
                }
            });
        } else {
            onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSpan();
        initLayoutManager();
        initSwipeToRefresh(view);
    }

    private void initSwipeToRefresh(View view) {
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onSwipeToRefresh();
            }
        });
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
        if (context instanceof BottomNavigationListener) {
            this.bottomNavigationListener = (BottomNavigationListener) context;
        }
        if (context instanceof RedirectionListener) {
            this.redirectionListener = (RedirectionListener) context;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && getView() != null) {
            setupBottomNavigation();
            showBottomBarNavigation(showBottomBar);
            screenTrack();
        }
    }

    protected void screenTrack() {
        if (getUserVisibleHint()) {
            ScreenTracking.screen(MainApplication.getAppContext(), getScreenName());
        }
    }

    private void setupBottomNavigation() {
        bottomNavigationListener
                .setupBottomNavigation(getBottomNavigationItems(), getBottomNavClickListener());
    }

    public void showBottomBarNavigation(boolean show) {
        if (bottomNavigationListener == null) {
            return;
        }

        this.showBottomBar = show;

        if (!getUserVisibleHint()) {
            return;
        }

        if (this.showBottomBar) {
            bottomNavigationListener.showBottomNavigation();
        } else {
            bottomNavigationListener.hideBottomNavigation();
        }
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
                DiscoveryTracking.eventSearchResultChangeGrid(getActivity(),"grid 2", getScreenName());
                break;
            case GRID_2:
                setSpanCount(1);
                gridLayoutManager.setSpanCount(spanCount);
                getAdapter().changeSingleGridView();
                DiscoveryTracking.eventSearchResultChangeGrid(getActivity(), "grid 1", getScreenName());
                break;
            case GRID_3:
                setSpanCount(1);
                getAdapter().changeListView();
                DiscoveryTracking.eventSearchResultChangeGrid(getActivity(),"list", getScreenName());
                break;
        }
        refreshBottomBarGridIcon();
    }

    public void refreshBottomBarGridIcon() {
        bottomNavigationListener.refreshBottomNavigationIcon(getBottomNavigationItems());
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

        DiscoveryTracking.eventSearchResultShare(getActivity(), getScreenName());

        LinkerData shareData = LinkerData.Builder.getLinkerBuilder()
                .setType(LinkerData.DISCOVERY_TYPE)
                .setName(getString(R.string.message_share_catalog))
                .setTextContent(getString(R.string.message_share_category))
                .setUri(shareUrl)
                .build();

        if(getActivity() instanceof HotlistActivity){
            shareData.setType(LinkerData.HOTLIST_TYPE);
        } else {
            DiscoveryTracking.eventSearchResultShare(getActivity(), getScreenName());
        }
        new DefaultShare(getActivity(), shareData).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FilterSortManager.handleOnActivityResult(requestCode, resultCode, data, new FilterSortManager.Callback() {
            @Override
            public void onFilterResult(Map<String, String> queryParams, Map<String, String> selectedFilters,
                                       List<Option> selectedOptions) {
                handleFilterResult(queryParams, selectedFilters, selectedOptions);
            }

            @Override
            public void onSortResult(Map<String, String> selectedSort, String selectedSortName, String autoApplyFilter) {
                handleSortResult(selectedSort, selectedSortName);
            }
        });
    }

    private void handleSortResult(Map<String, String> selectedSort, String selectedSortName) {
        setSelectedSort(new HashMap<>(selectedSort));
        sendSortTracking(selectedSortName);
        clearDataFilterSort();
        showBottomBarNavigation(false);
        reloadData();
    }

    protected void sendSortTracking(String selectedSortName) {
        UnifyTracking.eventSearchResultSort(getActivity(),getScreenName(), selectedSortName);
    }

    protected void handleFilterResult(Map<String, String> queryParams, Map<String, String> selectedFilters,
                                      List<Option> selectedOptions) {
        setSelectedFilter(new HashMap<>(selectedFilters));
        clearDataFilterSort();
        if (getActivity() instanceof HotlistActivity) {
            HotlistPageTracking.eventHotlistFilter(getActivity(),getSelectedFilter());
        } else {
            FilterTracking.eventApplyFilter(getFilterTrackingData(),
                    "category page - " + getCategoryId(), getSelectedFilter());
        }
        showBottomBarNavigation(false);
        reloadData();
    }

    private void setFilterData(List<Filter> filters) {
        this.filters = new ArrayList<>();
        if (filters == null) {
            return;
        }
        for (Filter filter : filters) {
            this.filters.add(filter);
        }
    }

    protected ArrayList<Filter> getFilters() {
        return filters;
    }

    private void setSortData(List<Sort> sorts) {
        this.sort = new ArrayList<>();
        if (sorts == null) {
            return;
        }
        for (Sort pojo : sorts) {
            this.sort.add(pojo);
        }
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
        return selectedFilter;
    }

    protected boolean isFilterActive() {
        return selectedFilter != null && !selectedFilter.isEmpty();
    }

    @Override
    public HashMap<String, String> getExtraFilter() {
        return null;
    }

    @Override
    public void setSelectedFilter(HashMap<String, String> selectedFilter) {
        this.selectedFilter = selectedFilter;
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
        openFilterPage();
    }

    protected void openFilterPage() {
        if(getSelectedFilter() == null) {
            setSelectedFilter(new HashMap<>());
        }
        FilterSortManager.openFilterPage(getFilterTrackingData(),this, getScreenName(), getSelectedFilter());
    }

    protected boolean isFilterDataAvailable() {
        return filters != null && !filters.isEmpty();
    }

    protected void openSortActivity() {
        if (!FilterSortManager.openSortActivity(this, sort, getSelectedSort())) {
            NetworkErrorHelper.showSnackbar(getActivity(), getActivity().getString(R.string.error_sort_data_not_ready));
        }
    }

    @Override
    public void getDynamicFilter() {
        if (!isFilterDataAvailable() && !isGettingDynamicFilter) {
            isGettingDynamicFilter = true;
            requestDynamicFilter();
        }
    }

    protected void requestDynamicFilter() {
        getPresenter().requestDynamicFilter();
    }

    @Override
    public void renderDynamicFilter(DynamicFilterModel pojo) {
        isGettingDynamicFilter = false;
        setFilterData(pojo.getData().getFilter());
        setSortData(pojo.getData().getSort());
        showBottomBarNavigation(true);
    }

    @Override
    public void renderFailGetDynamicFilter() {
        isGettingDynamicFilter = false;
        NetworkErrorHelper.showSnackbar(getActivity(), getActivity().getString(R.string.error_get_dynamic_filter));
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_SPAN_COUNT, getSpanCount());
        outState.putParcelableArrayList(EXTRA_FILTER, getFilters());
        outState.putParcelableArrayList(EXTRA_SORT, getSort());
        outState.putBoolean(EXTRA_SHOW_BOTTOM_BAR, showBottomBar);
        outState.putBoolean(EXTRA_IS_GETTING_DYNNAMIC_FILTER, isGettingDynamicFilter);
    }

    public abstract void reloadData();

    protected abstract int getFilterRequestCode();

    protected abstract int getSortRequestCode();

    protected abstract List<AHBottomNavigationItem> getBottomNavigationItems();

    protected abstract AHBottomNavigation.OnTabSelectedListener getBottomNavClickListener();

    protected abstract BrowseSectionGeneralAdapter getAdapter();

    protected abstract BrowseSectionFragmentPresenter getPresenter();

    protected abstract GridLayoutManager.SpanSizeLookup onSpanSizeLookup();

    protected void onFirstTimeLaunch() {

    }

    protected void disableSwipeRefresh() {
        refreshLayout.setEnabled(false);
        refreshLayout.setRefreshing(false);
    }

    protected void onSwipeToRefresh() {

    }

    protected void updateDepartmentId(String deptId) {

    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        setSpanCount(savedInstanceState.getInt(EXTRA_SPAN_COUNT));
        setFilterData(savedInstanceState.<Filter>getParcelableArrayList(EXTRA_FILTER));
        setSortData(savedInstanceState.<Sort>getParcelableArrayList(EXTRA_SORT));
        showBottomBar = savedInstanceState.getBoolean(EXTRA_SHOW_BOTTOM_BAR);
        isGettingDynamicFilter = savedInstanceState.getBoolean(EXTRA_IS_GETTING_DYNNAMIC_FILTER);
    }

    protected void removeSelectedFilter(String uniqueId) {

        String optionKey = OptionHelper.parseKeyFromUniqueId(uniqueId);
        String optionValue = OptionHelper.parseValueFromUniqueId(uniqueId);

        if (Option.KEY_CATEGORY.equals(optionKey)) {
            getSelectedFilter().remove(Option.KEY_CATEGORY);
        } else if (Option.KEY_PRICE_MIN.equals(optionKey) ||
                Option.KEY_PRICE_MAX.equals(optionKey)) {
            getSelectedFilter().remove(Option.KEY_PRICE_MIN);
            getSelectedFilter().remove(Option.KEY_PRICE_MAX);
        } else {
            String mapValue = getSelectedFilter().get(optionKey);
            mapValue = removeValue(mapValue, optionValue);

            if (!TextUtils.isEmpty(mapValue)) {
                getSelectedFilter().put(optionKey, mapValue);
            } else {
                getSelectedFilter().remove(optionKey);
            }
        }

        clearDataFilterSort();
        showBottomBarNavigation(false);
        reloadData();
    }

    protected String removeValue(String mapValue, String removedValue) {
        return mapValue.replace(removedValue, "").replace(",,", ",");
    }

    @Override
    public void setOfficialSelected(Boolean officialSelectedFlag) {

    }

    private FilterTrackingData getFilterTrackingData() {
        if (filterTrackingData == null) {
            filterTrackingData = new FilterTrackingData(FilterEventTracking.Event.CLICK_CATEGORY,
                    FilterEventTracking.Category.FILTER_CATEGORY,
                    getCategoryId(),
                    FilterEventTracking.Category.PREFIX_CATEGORY_PAGE
            );
        }
        return filterTrackingData;
    }

    protected abstract String getCategoryId();
}
