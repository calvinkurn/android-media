package com.tokopedia.seller.topads.keyword.view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.customadapter.NoResultDataBinder;
import com.tokopedia.core.customadapter.RetryDataBinder;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.keyword.view.listener.TopAdsListViewListener;
import com.tokopedia.seller.topads.view.adapter.TopAdsBaseListAdapter;
import com.tokopedia.seller.topads.view.adapter.viewholder.TopAdsRetryDataBinder;
import com.tokopedia.seller.topads.view.model.Ad;
import com.tokopedia.seller.topads.view.presenter.TopAdsDatePickerPresenter;
import com.tokopedia.seller.topads.view.presenter.TopAdsDatePickerPresenterImpl;
import com.tokopedia.seller.topads.view.widget.DividerItemDecoration;

import java.util.List;

/**
 * @author normansyahputa on 5/17/17.
 *         another type of {@link com.tokopedia.seller.topads.view.fragment.TopAdsAdListFragment}
 */

public abstract class TopAdsBaseListFragment<T> extends TopAdsDatePickerFragment<T> implements
        TopAdsListViewListener, TopAdsBaseListAdapter.Callback<Ad> {

    protected static final int START_PAGE = 1;

    protected static final String KEY_STATUS = "KEY_STATUS";
    protected static final String KEY_PAGE = "KEY_PAGE";
    protected static final String KEY_TOTAL_ITEM = "KEY_TOTAL_ITEM";

    protected TopAdsBaseListAdapter<Ad> adapter;
    protected CoordinatorLayout coordinatorLayout;
    protected RecyclerView recyclerView;
    protected SwipeToRefresh swipeToRefresh;
    protected LinearLayoutManager layoutManager;
    protected int status;
    protected int page;
    protected int totalItem;
    protected boolean searchMode;
    private SnackbarRetry snackBarRetry;
    private ProgressDialog progressDialog;
    private RecyclerView.OnScrollListener onScrollListener;

    public TopAdsBaseListFragment() {
        // Required empty public constructor
    }

    protected abstract TopAdsBaseListAdapter getNewAdapter();

    protected NoResultDataBinder getEmptyViewDefaultBinder() {
        return new NoResultDataBinder(adapter);
    }

    protected NoResultDataBinder getEmptyViewNoResultBinder() {
        return getEmptyViewDefaultBinder();
    }

    @Override
    protected TopAdsDatePickerPresenter getDatePickerPresenter() {
        return new TopAdsDatePickerPresenterImpl(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getFragmentLayout(), container, false);
    }

    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_base_list;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initialVar();
        setViewListener();
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinator_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        swipeToRefresh = (SwipeToRefresh) view.findViewById(R.id.swipe_refresh_layout);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
    }

    @Override
    protected void setViewListener() {
        super.setViewListener();
        onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastItemPosition = layoutManager.findLastVisibleItemPosition();
                int visibleItem = layoutManager.getItemCount() - 1;
                if (lastItemPosition == visibleItem && adapter.getDataSize() < totalItem &&
                        totalItem != Integer.MAX_VALUE) {
                    searchAd(page + 1);
                    adapter.showRetryFull(false);
                    adapter.showLoading(true);
                }
            }
        };
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        recyclerView.addOnScrollListener(onScrollListener);
    }

    @Override
    protected void initialVar() {
        super.initialVar();
        page = START_PAGE;
        totalItem = Integer.MAX_VALUE;
        searchMode = false;
        new RefreshHandler(getActivity(), getView(), new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                searchAd(START_PAGE);
            }
        });
        adapter = getNewAdapter();
        adapter.setCallback(this);
        adapter.setEmptyView(getEmptyViewDefaultBinder());
        TopAdsRetryDataBinder topAdsRetryDataBinder = new TopAdsRetryDataBinder(adapter);
        topAdsRetryDataBinder.setOnRetryListenerRV(new RetryDataBinder.OnRetryListener() {
            @Override
            public void onRetryCliked() {
                hideLoading();
                adapter.showRetryFull(false);
                adapter.showLoadingFull(true);
                searchAd(START_PAGE);
            }
        });
        adapter.setRetryView(topAdsRetryDataBinder);
    }

    @Override
    protected void loadData() {
        page = START_PAGE;
        adapter.clearData();
        adapter.showRetryFull(false);
        adapter.showLoadingFull(true);
        searchAd();
    }

    protected void searchAd(int page) {
        this.page = page;
        searchAd();
    }

    protected void searchAd() {

    }

    @Override
    public void onSearchLoaded(@NonNull List list, boolean isEndOfFile) {

    }

    @Override
    public void onSearchLoaded(@NonNull List list, int totalItem) {
        recyclerView.removeOnScrollListener(onScrollListener);
        recyclerView.addOnScrollListener(onScrollListener);
        this.totalItem = totalItem;
        hideLoading();
        if (totalItem <= 0) {
            if (searchMode) {
                showViewSearchNoResult();
            } else {
                showViewEmptyList();
            }
        } else {
            showViewList(list);
        }
    }

    protected void showViewEmptyList() {
        adapter.setEmptyView(getEmptyViewDefaultBinder());
        adapter.clearData();
        layoutManager.scrollToPositionWithOffset(0, 0);
        adapter.showEmptyFull(true);
    }

    protected void showViewSearchNoResult() {
        adapter.setEmptyView(getEmptyViewNoResultBinder());
        adapter.clearData();
        layoutManager.scrollToPositionWithOffset(0, 0);
        adapter.showEmptyFull(true);
    }

    protected void showViewList(@NonNull List list) {
        if (page == START_PAGE) {
            adapter.clearData();
            layoutManager.scrollToPositionWithOffset(0, 0);
        }
        adapter.addData(list);
    }

    @Override
    public void onLoadSearchError() {
        hideLoading();
        if (adapter.getDataSize() > 0) {
            showSnackBarRetry(new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    searchAd();
                }
            });
        } else {
            recyclerView.removeOnScrollListener(onScrollListener);
            adapter.showRetryFull(true);
            swipeToRefresh.setEnabled(false);
        }
    }

    protected void hideLoading() {
        swipeToRefresh.setEnabled(true);
        adapter.showLoading(false);
        adapter.showLoadingFull(false);
        adapter.showEmptyFull(false);
        adapter.showRetryFull(false);
        if (swipeToRefresh.isRefreshing()) {
            swipeToRefresh.setRefreshing(false);
        }
        progressDialog.dismiss();
        hideSnackBarRetry();
    }

    private void showSnackBarRetry(NetworkErrorHelper.RetryClickedListener listener) {
        if (snackBarRetry == null) {
            snackBarRetry = NetworkErrorHelper.createSnackbarWithAction(getActivity(), listener);
            snackBarRetry.showRetrySnackbar();
            snackBarRetry.setColorActionRetry(ContextCompat.getColor(getActivity(), R.color.green_400));
        }
    }

    private void hideSnackBarRetry() {
        if (snackBarRetry != null) {
            snackBarRetry.hideRetrySnackbar();
            snackBarRetry = null;
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState == null)
            return;
        status = savedInstanceState.getInt(KEY_STATUS);
        page = savedInstanceState.getInt(KEY_STATUS, 0);
        totalItem = savedInstanceState.getInt(KEY_STATUS, Integer.MAX_VALUE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_STATUS, status);
        outState.putInt(KEY_PAGE, page);
        outState.putInt(KEY_TOTAL_ITEM, totalItem);
    }
}