package com.tokopedia.seller.topads.view.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.presenter.TopAdsAdListPresenter;
import com.tokopedia.seller.topads.view.adapter.TopAdsAdListAdapter;
import com.tokopedia.seller.topads.view.listener.TopAdsListPromoViewListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class TopAdsAdListFragment<T extends TopAdsAdListPresenter> extends BasePresenterFragment<T> implements
        TopAdsListPromoViewListener, SearchView.OnQueryTextListener, TopAdsAdListAdapter.Callback, TopAdsAdListActionMode.Callback {

    private static final int START_PAGE = 1;

    @BindView(R2.id.list_product)
    RecyclerView listProduct;

    @BindView(R2.id.swipe_refresh_layout)
    SwipeToRefresh swipeToRefresh;

    @BindView(R2.id.mainView)
    View mainView;

    protected Date startDate;
    protected Date endDate;
    protected String keyword;
    protected int status;
    protected int page;
    protected int totalItem;

    protected TopAdsAdListAdapter adapter;
    private RefreshHandler refresh;
    private LinearLayoutManager layoutManager;
    private SearchView searchView;
    private TopAdsAdListActionMode actionMode;
    private SnackbarRetry snackBarRetry;

    protected abstract void searchAd();

    public TopAdsAdListFragment() {
        // Required empty public constructor
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return true;
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_list;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void setActionVar() {
        searchAd();
    }

    @Override
    protected void setViewListener() {
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        listProduct.setLayoutManager(layoutManager);
        listProduct.setAdapter(adapter);
        listProduct.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastItemPosition = layoutManager.findLastVisibleItemPosition();
                int visibleItem = layoutManager.getItemCount() - 1;
                if (lastItemPosition == visibleItem && adapter.getItemCount() < totalItem) {
                    searchAd(page + 1);
                }
            }
        });
    }

    @Override
    protected void initialVar() {
        initialDate();
        page = START_PAGE;
        totalItem = Integer.MAX_VALUE;
        refresh = new RefreshHandler(getActivity(), mainView, new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                searchAd(0);
            }
        });
        adapter = new TopAdsAdListAdapter();
        adapter.setCallback(this);
        adapter.showLoadingFull(true);
    }

    private void searchAd(int page) {
        this.page = page;
        searchAd();
    }

    private void initialDate() {
        startDate = presenter.getStartDate();
        endDate = presenter.getEndDate();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (searchView != null) {
            keyword = searchView.getQuery().toString();
        }
        searchAd(0);
        return true;
    }

    @Override
    public void onChecked(int position, boolean checked) {
        if (adapter.getSelectedList().size() > 0 && actionMode == null) {
            actionMode = new TopAdsAdListActionMode();
            actionMode.setCallback(this);
            getActivity().startActionMode(actionMode);
            swipeToRefresh.setEnabled(false);
        }
        if (actionMode != null) {
            actionMode.setTitle(String.valueOf(adapter.getSelectedList().size()));
        }
        hideSnackBarRetry();
    }

    @Override
    public void onActionTurnOn() {
        presenter.turnOnAddList(adapter.getSelectedList());
    }

    @Override
    public void onActionTurnOff() {
        presenter.turnOffAdList(adapter.getSelectedList());
    }

    @Override
    public void onActionModeDestroyed() {
        actionMode = null;
        adapter.clearCheckedList();
        swipeToRefresh.setEnabled(true);
        hideSnackBarRetry();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.promo_topads, menu);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onSearchAdLoaded(@NonNull List adList, int totalItem) {
        if (page == 0) {
            adapter.clearData();
            this.totalItem = totalItem;
        }
        adapter.addData(adList);
        hideLoading();
        checkEmptyData(true);
    }

    @Override
    public void onLoadSearchAdError() {
        hideLoading();
        checkEmptyData(false);
        showSnackBarRetry(new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                searchAd();
            }
        });
    }

    @Override
    public void onTurnOnAdSuccess() {
        hideLoading();
        checkEmptyData(true);
        onBulkUpdateSuccess();
    }

    @Override
    public void onTurnOnAdFailed() {
        hideLoading();
        checkEmptyData(false);
        showSnackBarRetry(new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                onActionTurnOn();
            }
        });
    }

    @Override
    public void onTurnOffAdSuccess() {
        hideLoading();
        checkEmptyData(true);
        onBulkUpdateSuccess();
    }

    @Override
    public void onTurnOffAdFailed() {
        hideLoading();
        checkEmptyData(false);
        showSnackBarRetry(new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                onActionTurnOff();
            }
        });
    }

    private void onBulkUpdateSuccess() {
        if (actionMode != null) {
            actionMode.finish();
        }
        searchAd(0);
    }

    private void checkEmptyData(boolean success) {
        if (adapter.getDataSize() > 0) {
            return;
        }
        if (success) {
            adapter.showEmpty(true);
        } else {
            adapter.showRetry(true);
        }
    }

    private void hideLoading() {
        adapter.showLoadingFull(false);
        adapter.showEmpty(false);
        adapter.showRetry(false);
        if (swipeToRefresh.isRefreshing()) {
            swipeToRefresh.setRefreshing(false);
        }
        hideSnackBarRetry();
    }

    private void showSnackBarRetry(NetworkErrorHelper.RetryClickedListener listener) {
        if (snackBarRetry == null) {
            snackBarRetry = new SnackbarRetry(SnackbarManager.make(getActivity(), getResources().getString(com.tokopedia.core.R.string.msg_network_error), -2), listener);
            snackBarRetry.showRetrySnackbar();
        }
    }

    private void hideSnackBarRetry() {
        if (snackBarRetry != null) {
            snackBarRetry.hideRetrySnackbar();
            snackBarRetry = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}