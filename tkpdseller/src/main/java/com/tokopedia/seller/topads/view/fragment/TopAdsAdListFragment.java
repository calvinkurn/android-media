package com.tokopedia.seller.topads.view.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.tokopedia.core.customadapter.RetryDataBinder;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.presenter.TopAdsAdListPresenter;
import com.tokopedia.seller.topads.view.adapter.TopAdsAdListAdapter;
import com.tokopedia.seller.topads.view.adapter.viewholder.TopAdsEmptyAdDataBinder;
import com.tokopedia.seller.topads.view.adapter.viewholder.TopAdsRetryDataBinder;
import com.tokopedia.seller.topads.view.listener.TopAdsListPromoViewListener;
import com.tokopedia.seller.topads.view.widget.DividerItemDecoration;

import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class TopAdsAdListFragment<T extends TopAdsAdListPresenter> extends TopAdsDatePickerFragment<T> implements
        TopAdsListPromoViewListener, SearchView.OnQueryTextListener, TopAdsAdListAdapter.Callback {

    private static final int START_PAGE = 1;
    protected static final int REQUEST_CODE_AD_STATUS = TopAdsAdListFragment.class.hashCode();

    @BindView(R2.id.list_product)
    RecyclerView recyclerView;

    @BindView(R2.id.swipe_refresh_layout)
    SwipeToRefresh swipeToRefresh;

    @BindView(R2.id.mainView)
    View mainView;

    protected String keyword;
    protected int status;
    protected int page;
    protected int totalItem;

    protected TopAdsAdListAdapter adapter;
    private RefreshHandler refresh;
    private LinearLayoutManager layoutManager;
    private SearchView searchView;
    private SnackbarRetry snackBarRetry;
    private ProgressDialog progressDialog;

    protected abstract void searchAd();

    protected abstract TopAdsEmptyAdDataBinder getEmptyViewBinder();

    public TopAdsAdListFragment() {
        // Required empty public constructor
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
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    protected void setViewListener() {
        swipeToRefresh.setEnabled(false);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastItemPosition = layoutManager.findLastVisibleItemPosition();
                int visibleItem = layoutManager.getItemCount() - 1;
                if (lastItemPosition == visibleItem && adapter.getDataSize() < totalItem) {
                    searchAd(page + 1);
                    adapter.showLoading(true);
                }
            }
        });
    }

    @Override
    protected void initialVar() {
        page = START_PAGE;
        totalItem = Integer.MAX_VALUE;
        refresh = new RefreshHandler(getActivity(), mainView, new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                searchAd(START_PAGE);
            }
        });
        adapter = new TopAdsAdListAdapter();
        adapter.setCallback(this);
        adapter.setEmptyView(getEmptyViewBinder());
        TopAdsRetryDataBinder topAdsRetryDataBinder = new TopAdsRetryDataBinder(adapter);
        topAdsRetryDataBinder.setOnRetryListenerRV(new RetryDataBinder.OnRetryListener() {
            @Override
            public void onRetryCliked() {
                hideLoading();
                adapter.showLoadingFull(true);
                searchAd(START_PAGE);
            }
        });
        adapter.setRetryView(topAdsRetryDataBinder);
    }

    private void updateEmptyViewNoResult() {
        TopAdsEmptyAdDataBinder emptyGroupAdsDataBinder = new TopAdsEmptyAdDataBinder(adapter);
        emptyGroupAdsDataBinder.setEmptyTitleText(getString(R.string.top_ads_empty_promo_not_found_title_empty_text));
        emptyGroupAdsDataBinder.setEmptyContentText(getString(R.string.top_ads_empty_promo_not_found_content_empty_text));
        adapter.setEmptyView(emptyGroupAdsDataBinder);
    }

    @Override
    protected void loadData() {
        page = START_PAGE;
        adapter.clearData();
        adapter.showLoadingFull(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(presenter.getRangeDateFormat(startDate, endDate));
        searchAd();
    }

    private void searchAd(int page) {
        this.page = page;
        searchAd();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // check if the request code is the same
        if (requestCode == REQUEST_CODE_AD_STATUS && intent != null) {
            boolean adStatusChanged = intent.getBooleanExtra(TopAdsExtraConstant.EXTRA_AD_STATUS_CHANGED, false);
            if (adStatusChanged) {
                searchAd(START_PAGE);
            }
        }
    }

    @Override
    public void onSearchAdLoaded(@NonNull List adList, int totalItem) {
        swipeToRefresh.setEnabled(true);
        this.totalItem = totalItem;
        if (page == START_PAGE) {
            adapter.clearData();
            layoutManager.scrollToPositionWithOffset(0, 0);
        }
        adapter.addData(adList);
        hideLoading();
        if (adapter.getDataSize() < 1) {
            adapter.showEmptyFull(true);
            if (TextUtils.isEmpty(keyword)) {
                searchView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onLoadSearchAdError() {
        hideLoading();
        if (adapter.getDataSize() > 0) {
            showSnackBarRetry(new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    searchAd();
                }
            });
        } else {
            swipeToRefresh.setEnabled(false);
            adapter.showRetryFull(true);
        }
    }

    private void hideLoading() {
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
        }
    }

    private void hideSnackBarRetry() {
        if (snackBarRetry != null) {
            snackBarRetry.hideRetrySnackbar();
            snackBarRetry = null;
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            onQueryTextSubmit(newText);
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        keyword = query;
        searchAd(START_PAGE);
        return true;
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.promo_topads, menu);
        final MenuItem searchItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmptyViewNoResult();
                setItemsVisibility(menu, searchItem, false);
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                setItemsVisibility(menu, searchItem, true);
                return false;
            }
        });
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                setItemsVisibility(menu, searchItem, true);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setItemsVisibility(Menu menu, MenuItem exception, boolean visible) {
        for (int i = 0; i < menu.size(); ++i) {
            MenuItem item = menu.getItem(i);
            if (item != exception) item.setVisible(visible);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_date) {
            openDatePicker();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }
}