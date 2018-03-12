package com.tokopedia.discovery.newdiscovery.search.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.discovery.model.DynamicFilterModel;
import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.core.discovery.model.Sort;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.share.ShareActivity;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.activity.SortProductActivity;
import com.tokopedia.discovery.newdiscovery.base.BottomNavigationListener;
import com.tokopedia.discovery.newdiscovery.base.BottomSheetListener;
import com.tokopedia.discovery.newdiscovery.base.RedirectionListener;
import com.tokopedia.discovery.newdiscovery.search.SearchActivity;
import com.tokopedia.discovery.newdynamicfilter.RevampedDynamicFilterActivity;
import com.tokopedia.discovery.newdynamicfilter.helper.FilterFlagSelectedModel;
import com.tokopedia.topads.sdk.domain.TopAdsParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private static final String EXTRA_FLAG_FILTER_HELPER = "EXTRA_FLAG_FILTER_HELPER";

    private BottomNavigationListener bottomNavigationListener;
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
    private HashMap<String, String> selectedFilter;
    private FilterFlagSelectedModel flagFilterHelper;
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
            this.bottomSheetListener = (BottomSheetListener) context;
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
            ScreenTracking.screen(getScreenName());
        }
    }

    private void setupBottomNavigation() {
        bottomNavigationListener
                .setupBottomNavigation(getBottomNavigationItems(), getBottomNavClickListener());
    }

    protected void showBottomBarNavigation(boolean show) {
        boolean isBottomSheetShown = bottomSheetListener.isBottomSheetShown();
        if (show && isBottomSheetShown) {
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
                break;
            case GRID_2:
                setSpanCount(1);
                gridLayoutManager.setSpanCount(spanCount);
                getAdapter().changeSingleGridView();
                break;
            case GRID_3:
                setSpanCount(1);
                getAdapter().changeListView();
                break;
        }
        refreshBottomBarGridIcon();
    }

    private void refreshBottomBarGridIcon() {
        bottomNavigationListener.refreshBottomNavigationIcon(getBottomNavigationItems());
    }

    private void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
    }

    private int getSpanCount() {
        return spanCount;
    }

    public void startShareActivity(String shareUrl) {

        if (TextUtils.isEmpty(shareUrl)) {
            return;
        }

        ShareData shareData = ShareData.Builder.aShareData()
                .setType(ShareData.DISCOVERY_TYPE)
                .setName(getString(R.string.message_share_catalog))
                .setTextContent(getString(R.string.message_share_category))
                .setUri(shareUrl)
                .build();

        Intent intent = new Intent(getActivity(), ShareActivity.class);
        intent.putExtra(ShareData.TAG, shareData);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == getSortRequestCode()) {
                setSelectedSort((HashMap<String, String>) data.getSerializableExtra(SortProductActivity.EXTRA_SELECTED_SORT));
                String selectedSortName = data.getStringExtra(SortProductActivity.EXTRA_SELECTED_NAME);
                UnifyTracking.eventSearchResultSort(getScreenName(), selectedSortName);
                clearDataFilterSort();
                showBottomBarNavigation(false);
                reloadData();
            } else if (requestCode == getFilterRequestCode()) {
                setFlagFilterHelper((FilterFlagSelectedModel) data.getParcelableExtra(RevampedDynamicFilterActivity.EXTRA_SELECTED_FLAG_FILTER));
                setSelectedFilter((HashMap<String, String>) data.getSerializableExtra(RevampedDynamicFilterActivity.EXTRA_SELECTED_FILTERS));
                UnifyTracking.eventSearchResultFilter(getScreenName(), getSelectedFilter());
                clearDataFilterSort();
                showBottomBarNavigation(false);
                updateDepartmentId(getFlagFilterHelper().getCategoryId());
                reloadData();
            }
        }
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

    protected void setFlagFilterHelper(FilterFlagSelectedModel flagFilterHelper) {
        this.flagFilterHelper = flagFilterHelper;
    }

    protected FilterFlagSelectedModel getFlagFilterHelper() {
        return flagFilterHelper;
    }

    public void clearDataFilterSort() {
        this.filters.clear();
        this.sort.clear();
    }

    protected void openFilterActivity() {
        if (isFilterDataAvailable()) {
            bottomSheetListener.launchFilterBottomSheet();
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), getActivity().getString(R.string.error_filter_data_not_ready));
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
            getActivity().overridePendingTransition(R.anim.pull_up, android.R.anim.fade_out);
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), getActivity().getString(R.string.error_sort_data_not_ready));
        }
    }

    private boolean isSortDataAvailable() {
        return sort != null && !sort.isEmpty();
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
        bottomSheetListener.loadFilterItems(getFilters());
    }

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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_SPAN_COUNT, getSpanCount());
        outState.putParcelableArrayList(EXTRA_FILTER, getFilters());
        outState.putParcelableArrayList(EXTRA_SORT, getSort());
        outState.putSerializable(EXTRA_SELECTED_FILTER, getSelectedFilter());
        outState.putSerializable(EXTRA_SELECTED_SORT, getSelectedSort());
        outState.putBoolean(EXTRA_SHOW_BOTTOM_BAR, showBottomBar);
        outState.putBoolean(EXTRA_IS_GETTING_DYNNAMIC_FILTER, isGettingDynamicFilter);
        outState.putParcelable(EXTRA_FLAG_FILTER_HELPER, getFlagFilterHelper());
    }

    public abstract void reloadData();

    protected abstract int getFilterRequestCode();

    protected abstract int getSortRequestCode();

    protected abstract List<AHBottomNavigationItem> getBottomNavigationItems();

    protected abstract AHBottomNavigation.OnTabSelectedListener getBottomNavClickListener();

    protected abstract SearchSectionGeneralAdapter getAdapter();

    protected abstract SearchSectionFragmentPresenter getPresenter();

    protected abstract GridLayoutManager.SpanSizeLookup onSpanSizeLookup();

    protected void onFirstTimeLaunch() {

    }

    protected void onSwipeToRefresh() {

    }

    protected void updateDepartmentId(String deptId) {

    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        setSpanCount(savedInstanceState.getInt(EXTRA_SPAN_COUNT));
        setFilterData(savedInstanceState.<Filter>getParcelableArrayList(EXTRA_FILTER));
        setSortData(savedInstanceState.<Sort>getParcelableArrayList(EXTRA_SORT));
        setSelectedFilter((HashMap<String, String>) savedInstanceState.getSerializable(EXTRA_SELECTED_FILTER));
        setSelectedSort((HashMap<String, String>) savedInstanceState.getSerializable(EXTRA_SELECTED_SORT));
        showBottomBar = savedInstanceState.getBoolean(EXTRA_SHOW_BOTTOM_BAR);
        isGettingDynamicFilter = savedInstanceState.getBoolean(EXTRA_IS_GETTING_DYNNAMIC_FILTER);
        setFlagFilterHelper((FilterFlagSelectedModel) savedInstanceState.getParcelable(EXTRA_FLAG_FILTER_HELPER));
    }

    @Override
    public void setTotalSearchResultCount(String formattedResultCount) {
        bottomSheetListener.setFilterResultCount(formattedResultCount);
    }

    protected RecyclerView.OnScrollListener getRecyclerViewBottomSheetScrollListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    bottomSheetListener.closeFilterBottomSheet();
                }
            }
        };
    }
}
