package com.tokopedia.seller.base.view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.adapter.ItemType;
import com.tokopedia.seller.base.view.listener.BaseListViewListener;
import com.tokopedia.seller.topads.dashboard.view.adapter.viewholder.TopAdsRetryDataBinder;
import com.tokopedia.seller.topads.dashboard.view.widget.DividerItemDecoration;

import java.util.List;

/**
 * @author normansyahputa on 5/17/17.
 *         another type of {@link com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsAdListFragment}
 */

public abstract class BaseListFragment<P, T extends ItemType> extends BasePresenterFragment<P> implements
        BaseListViewListener<T>, BaseListAdapter.Callback<T> {

    protected static final int START_PAGE = 1;

    protected BaseListAdapter<T> adapter;
    protected RecyclerView recyclerView;
    protected SwipeToRefresh swipeToRefresh;
    protected LinearLayoutManager layoutManager;
    protected int page;
    protected int totalItem;
    protected boolean searchMode;
    private SnackbarRetry snackBarRetry;
    private ProgressDialog progressDialog;
    private RecyclerView.OnScrollListener onScrollListener;

    public BaseListFragment() {
        // Required empty public constructor
    }

    protected abstract BaseListAdapter getNewAdapter();

    protected abstract void searchForPage(int page);

    protected NoResultDataBinder getEmptyViewDefaultBinder() {
        return new NoResultDataBinder(adapter);
    }

    protected NoResultDataBinder getEmptyViewNoResultBinder() {
        return getEmptyViewDefaultBinder();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getFragmentLayout(), container, false);
    }

    protected int getFragmentLayout() {
        return R.layout.fragment_base_list;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
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
                    setAndSearchForPage(page + 1);
                    adapter.showRetryFull(false);
                    adapter.showLoading(true);
                }
            }
        };
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        RecyclerView.ItemDecoration itemDecoration = getItemDecoration();
        if (itemDecoration!= null) {
            recyclerView.addItemDecoration(itemDecoration);
        }
        recyclerView.addOnScrollListener(onScrollListener);
        if (swipeToRefresh!= null) {
            new RefreshHandler(getActivity(), getView(), new RefreshHandler.OnRefreshHandlerListener() {
                @Override
                public void onRefresh(View view) {
                    setAndSearchForPage(getStartPage());
                }
            });
        }
    }

    protected RecyclerView.ItemDecoration getItemDecoration(){
        return new DividerItemDecoration(getActivity());
    }

    @Override
    protected void initialVar() {
        super.initialVar();
        page = getStartPage();
        totalItem = Integer.MAX_VALUE;
        searchMode = false;
        adapter = getNewAdapter();
        adapter.setCallback(this);
        adapter.setEmptyView(getEmptyViewDefaultBinder());
        RetryDataBinder retryDataBinder = getRetryViewDataBinder(adapter);
        retryDataBinder.setOnRetryListenerRV(new RetryDataBinder.OnRetryListener() {
            @Override
            public void onRetryCliked() {
                hideLoading();
                adapter.showRetryFull(false);
                adapter.showLoadingFull(true);
                setAndSearchForPage(getStartPage());
            }
        });
        adapter.setRetryView(retryDataBinder);
    }

    public RetryDataBinder getRetryViewDataBinder(BaseListAdapter adapter){
        return new TopAdsRetryDataBinder(adapter);
    }

    @Override
    protected void setActionVar() {
        super.setActionVar();
        loadData();
    }

    protected void loadData() {
        page = getStartPage();
        adapter.clearData();
        adapter.showRetryFull(false);
        adapter.showLoadingFull(true);
        searchForPage(page);
    }

    protected void setAndSearchForPage(int page) {
        this.page = page;
        searchForPage(page);
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
        if (page == getStartPage()) {
            adapter.clearData();
            layoutManager.scrollToPositionWithOffset(0, 0);
        }
        adapter.addData(list);
    }

    @Override
    public void onLoadSearchError(Throwable t) {
        hideLoading();
        if (adapter.getDataSize() > 0) {
            showSnackBarRetry(new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    searchForPage(page);
                }
            });
        } else {
            recyclerView.removeOnScrollListener(onScrollListener);
            adapter.showRetryFull(true);
            if (swipeToRefresh!= null) {
                swipeToRefresh.setEnabled(false);
            }
        }
    }

    protected void hideLoading() {
        adapter.showLoading(false);
        adapter.showLoadingFull(false);
        adapter.showEmptyFull(false);
        adapter.showRetryFull(false);
        if (swipeToRefresh!= null){
            swipeToRefresh.setEnabled(true);
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

    protected int getStartPage(){
        return START_PAGE;
    }
}