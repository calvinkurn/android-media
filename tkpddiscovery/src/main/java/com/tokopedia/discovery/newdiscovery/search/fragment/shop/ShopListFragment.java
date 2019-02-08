package com.tokopedia.discovery.newdiscovery.search.fragment.shop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.EndlessRecyclerviewListener;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.discovery.DiscoveryRouter;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.analytics.SearchTracking;
import com.tokopedia.discovery.newdiscovery.di.component.DaggerSearchComponent;
import com.tokopedia.discovery.newdiscovery.di.component.SearchComponent;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionFragment;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionFragmentPresenter;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionGeneralAdapter;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.adapter.ShopListAdapter;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.adapter.decoration.ShopListItemDecoration;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.adapter.listener.ShopListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.adapter.typefactory.ShopListTypeFactoryImpl;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.listener.FavoriteActionListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.viewmodel.ShopViewModel;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;
import com.tokopedia.discovery.newdiscovery.util.SearchParameterBuilder;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.List;

import javax.inject.Inject;


/**
 * Created by henrypriyono on 10/6/17.
 */

public class ShopListFragment extends SearchSectionFragment
        implements ShopListFragmentView,
        FavoriteActionListener, SearchSectionGeneralAdapter.OnItemChangeView, ShopListener {

    private static final String SHOP_STATUS_FAVOURITE = "SHOP_STATUS_FAVOURITE";
    private static final String EXTRA_QUERY = "EXTRA_QUERY";
    private static final int REQUEST_CODE_GOTO_SHOP_DETAIL = 125;
    private static final int REQUEST_CODE_LOGIN = 561;
    private static final int REQUEST_ACTIVITY_SORT_SHOP = 1235;
    private static final int REQUEST_ACTIVITY_FILTER_SHOP = 4322;
    private static final String SEARCH_SHOP_TRACE = "search_shop_trace";

    private RecyclerView recyclerView;
    private ShopListAdapter adapter;
    private String query;

    @Inject
    ShopListPresenter presenter;
    private UserSessionInterface userSession;
    private GCMHandler gcmHandler;
    private int lastSelectedItemPosition = -1;
    private boolean isLoadingData;
    private boolean isNextPageAvailable = true;

    private EndlessRecyclerViewScrollListener linearLayoutLoadMoreTriggerListener;
    private EndlessRecyclerViewScrollListener gridLayoutLoadMoreTriggerListener;
    private PerformanceMonitoring performanceMonitoring;

    public static ShopListFragment newInstance(String query) {
        Bundle args = new Bundle();
        args.putString(EXTRA_QUERY, query);
        ShopListFragment ShopListFragment = new ShopListFragment();
        ShopListFragment.setArguments(args);
        return ShopListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            loadDataFromSavedState(savedInstanceState);
        } else {
            loadDataFromArguments();
        }
        userSession = new UserSession(getContext());
        gcmHandler = new GCMHandler(getContext());
    }

    private void loadDataFromSavedState(Bundle savedInstanceState) {
        query = savedInstanceState.getString(EXTRA_QUERY);
    }

    private void loadDataFromArguments() {
        query = getArguments().getString(EXTRA_QUERY);
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
        return inflater.inflate(R.layout.fragment_shop_list_search, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListener();
        bindView(view);
    }

    private void bindView(View rootView) {

        adapter = new ShopListAdapter(this, new ShopListTypeFactoryImpl(this));

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(getGridLayoutManager());
        recyclerView.addItemDecoration(
                new ShopListItemDecoration(
                        getContext().getResources().getDimensionPixelSize(R.dimen.dp_2),
                        getContext().getResources().getDimensionPixelSize(R.dimen.dp_2),
                        -(getContext().getResources().getDimensionPixelSize(R.dimen.dp_1)),
                        0
                )
        );

        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(gridLayoutLoadMoreTriggerListener);

        adapter.addLoading();
    }

    private void initListener() {
        gridLayoutLoadMoreTriggerListener = new EndlessRecyclerViewScrollListener(getGridLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (isAllowLoadMore()) {
                    loadMoreShop(totalItemsCount - 1);
                }
            }
        };

        linearLayoutLoadMoreTriggerListener = new EndlessRecyclerViewScrollListener(getLinearLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (isAllowLoadMore()) {
                    loadMoreShop(totalItemsCount - 1);
                }
            }
        };
    }

    public void updateScrollListenerState(boolean hasNextPage){
        switch (getAdapter().getCurrentLayoutType()) {
            case GRID_1: // List
                if (linearLayoutLoadMoreTriggerListener != null) {
                    linearLayoutLoadMoreTriggerListener.updateStateAfterGetData();
                    linearLayoutLoadMoreTriggerListener.setHasNextPage(hasNextPage);
                }
                break;
            case GRID_2: // Grid 2x2
            case GRID_3: // Grid 1x1
                if (gridLayoutLoadMoreTriggerListener != null) {
                    gridLayoutLoadMoreTriggerListener.updateStateAfterGetData();
                    gridLayoutLoadMoreTriggerListener.setHasNextPage(hasNextPage);
                }
                break;
        }

    }

    private boolean isAllowLoadMore() {
        return getUserVisibleHint()
                && !isLoadingData
                && !isRefreshing()
                && isNextPageAvailable;
    }

    private void loadShopFirstTime() {
        performanceMonitoring = PerformanceMonitoring.start(SEARCH_SHOP_TRACE);
        loadMoreShop(START_ROW_FIRST_TIME_LOAD);
    }

    private void loadMoreShop(final int startRow) {
        SearchParameter searchParameter
                = generateLoadMoreParameter(startRow, query);

        isLoadingData = true;
        presenter.loadShop(searchParameter, new ShopListPresenterImpl.LoadMoreListener() {
            @Override
            public void onSuccess(List<ShopViewModel.ShopItem> shopItemList, boolean isHasNextPage) {
                if (shopItemList.isEmpty()) {
                    handleEmptySearchResult();
                } else {
                    if (performanceMonitoring != null) {
                        performanceMonitoring.stopTrace();
                    }
                    handleSearchResult(shopItemList, isHasNextPage, startRow);
                }
                isLoadingData = false;
                hideRefreshLayout();
            }

            @Override
            public void onFailed() {
                adapter.removeLoading();

                if (adapter.isListEmpty()) {
                    NetworkErrorHelper.showEmptyState(getActivity(), getView(), new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            loadMoreShop(startRow);
                        }
                    });
                } else {
                    NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            loadMoreShop(startRow);
                        }
                    }).showRetrySnackbar();
                }

                isLoadingData = false;
                hideRefreshLayout();
            }
        });
    }

    private SearchParameter generateLoadMoreParameter(int startRow, String query) {
        SearchParameterBuilder builder = SearchParameterBuilder.createInstance();
        builder.setUniqueID(generateUniqueId());
        builder.setUserID(generateUserId());
        builder.setQueryKey(query);
        builder.setStartRow(startRow);
        return builder.build();
    }

    private String generateUserId() {
        return userSession.isLoggedIn() ? userSession.getUserId() : null;
    }

    private String generateUniqueId() {
        return userSession.isLoggedIn() ?
                AuthUtil.md5(userSession.getUserId()) :
                AuthUtil.md5(gcmHandler.getRegistrationId());
    }

    private void handleSearchResult(List<ShopViewModel.ShopItem> shopItemList, boolean isHasNextPage, int startRow) {
        enrichPositionData(shopItemList, startRow);
        isNextPageAvailable = isHasNextPage;
        adapter.removeLoading();
        adapter.appendItems(shopItemList);

        updateScrollListenerState(isHasNextPage);

        if (isHasNextPage) {
            adapter.addLoading();
        }
    }

    private void enrichPositionData(List<ShopViewModel.ShopItem> shopItemList, int startRow) {
        int position = startRow;
        for (ShopViewModel.ShopItem shopItem : shopItemList) {
            position++;
            shopItem.setPosition(position);
        }
    }

    private void handleEmptySearchResult() {
        isNextPageAvailable = false;
        adapter.removeLoading();
        if (adapter.isListEmpty()) {
            adapter.showEmptyState(getActivity(), query, isFilterActive(), getFlagFilterHelper(), getString(R.string.shop_tab_title).toLowerCase());
            SearchTracking.eventSearchNoResult(getActivity(), query, getScreenName(), getSelectedFilter());
        }
    }

    @Override
    public String getScreenNameId() {
        return AppScreen.SCREEN_SEARCH_PAGE_SHOP_TAB;
    }

    @Override
    protected void onFirstTimeLaunch() {
        super.onFirstTimeLaunch();
        loadShopFirstTime();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onFirstTimeLaunch();
    }

    @Override
    public void onItemClicked(ShopViewModel.ShopItem shopItem, int adapterPosition) {
        Intent intent = ((DiscoveryRouter) getActivity().getApplication()).getShopPageIntent(getActivity(), shopItem.getShopId());
        lastSelectedItemPosition = adapterPosition;
        SearchTracking.eventSearchResultShopItemClick(getActivity(), query, shopItem.getShopName(),
                shopItem.getPage(), shopItem.getPosition());
        startActivityForResult(intent, REQUEST_CODE_GOTO_SHOP_DETAIL);
    }

    @Override
    public void onFavoriteButtonClicked(ShopViewModel.ShopItem shopItem,
                                        int adapterPosition) {
        SearchTracking.eventSearchResultFavoriteShopClick(getActivity(), query, shopItem.getShopName(),
                shopItem.getPage(), shopItem.getPosition());
        presenter.handleFavoriteButtonClicked(shopItem, adapterPosition);
    }

    @Override
    public void onBannerAdsClicked(String appLink) {

    }

    @Override
    public void onSelectedFilterRemoved(String uniqueId) {
        removeSelectedFilter(uniqueId);
    }

    @Override
    public void onEmptyButtonClicked() {
        showSearchInputView();
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
    protected GridLayoutManager.SpanSizeLookup onSpanSizeLookup() {
        return new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.isLoading(position) || adapter.isEmptyItem(position)) {
                    return spanCount;
                } else {
                    return 1;
                }
            }
        };
    }

    @Override
    protected boolean isSortEnabled() {
        return false;
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
    public void disableFavoriteButton(int adapterPosition) {
        adapter.setFavoriteButtonEnabled(adapterPosition, false);
    }

    @Override
    public void enableFavoriteButton(int adapterPosition) {
        adapter.setFavoriteButtonEnabled(adapterPosition, true);
    }

    @Override
    public String getQueryKey() {
        return query;
    }

    @Override
    public void onErrorToggleFavorite(String errorMessage, int adapterPosition) {
        enableFavoriteButton(adapterPosition);
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessToggleFavorite(int adapterPosition, boolean targetFavoritedStatus) {
        adapter.updateFavoritedStatus(targetFavoritedStatus, adapterPosition);
        enableFavoriteButton(adapterPosition);
        if (targetFavoritedStatus) {
            NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.message_success_people_fav));
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.message_success_people_unfav));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == REQUEST_CODE_GOTO_SHOP_DETAIL && resultCode == Activity.RESULT_OK) {
            boolean isFavorited = data.getBooleanExtra(SHOP_STATUS_FAVOURITE, false);
            if (lastSelectedItemPosition != -1) {
                updateFavoriteStatusFromShopDetailPage(lastSelectedItemPosition, isFavorited);
            }
        }
    }

    private void updateFavoriteStatusFromShopDetailPage(int position, boolean isFavorited) {
        if (adapter != null && adapter.isShopItem(position)) {
            adapter.updateFavoritedStatus(isFavorited, position);
        }
    }

    @Override
    protected void switchLayoutType() {
        super.switchLayoutType();

        if (!getUserVisibleHint()) {
            return;
        }
        recyclerView.clearOnScrollListeners();

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
    public void reloadData() {
        adapter.clearData();
        loadShopFirstTime();
    }

    @Override
    protected void onSwipeToRefresh() {
        showRefreshLayout();
        reloadData();
    }

    @Override
    protected int getFilterRequestCode() {
        return REQUEST_ACTIVITY_FILTER_SHOP;
    }

    @Override
    protected int getSortRequestCode() {
        return REQUEST_ACTIVITY_SORT_SHOP;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_QUERY, query);
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
