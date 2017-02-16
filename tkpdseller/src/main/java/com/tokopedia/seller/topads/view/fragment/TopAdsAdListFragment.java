package com.tokopedia.seller.topads.view.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.presenter.TopAdsAdListPresenter;
import com.tokopedia.seller.topads.presenter.TopAdsDatePickerPresenter;
import com.tokopedia.seller.topads.presenter.TopAdsDatePickerPresenterImpl;
import com.tokopedia.seller.topads.view.adapter.TopAdsAdListAdapter;
import com.tokopedia.seller.topads.view.adapter.viewholder.TopAdsEmptyAdDataBinder;
import com.tokopedia.seller.topads.view.adapter.viewholder.TopAdsRetryDataBinder;
import com.tokopedia.seller.topads.view.listener.TopAdsListPromoViewListener;
import com.tokopedia.seller.topads.view.widget.DividerItemDecoration;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class TopAdsAdListFragment<T extends TopAdsAdListPresenter> extends TopAdsDatePickerFragment<T> implements
        TopAdsListPromoViewListener, SearchView.OnQueryTextListener, TopAdsAdListAdapter.Callback {

    private static final int START_PAGE = 1;
    protected static final int REQUEST_CODE_AD_STATUS = 2;
    protected static final int REQUEST_CODE_AD_FILTER = 3;

    private RecyclerView recyclerView;
    private SwipeToRefresh swipeToRefresh;
    private FloatingActionButton fabFilter;

    protected String keyword;
    protected int status;
    protected int page;

    protected int totalItem;
    private boolean searchMode;

    protected TopAdsAdListAdapter adapter;
    private LinearLayoutManager layoutManager;
    private SnackbarRetry snackBarRetry;
    private ProgressDialog progressDialog;
    private RecyclerView.OnScrollListener onScrollListener;

    protected abstract TopAdsEmptyAdDataBinder getEmptyViewBinder();

    protected abstract void goToFilter();

    public TopAdsAdListFragment() {
        // Required empty public constructor
    }

    @Override
    protected TopAdsDatePickerPresenter getDatePickerPresenter() {
        return new TopAdsDatePickerPresenterImpl(getActivity());
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_list;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        recyclerView = (RecyclerView) view.findViewById(R.id.list_product);
        swipeToRefresh = (SwipeToRefresh) view.findViewById(R.id.swipe_refresh_layout);
        fabFilter = (FloatingActionButton) view.findViewById(R.id.fab_filter);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
        fabFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToFilter();
            }
        });
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
                if (lastItemPosition == visibleItem && adapter.getDataSize() < totalItem) {
                    searchAd(page + 1);
                    adapter.showLoading(true);
                }
            }
        };
        swipeToRefresh.setEnabled(false);
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
        emptyGroupAdsDataBinder.setEmptyContentItemText(null);
        adapter.setEmptyView(emptyGroupAdsDataBinder);
        adapter.notifyDataSetChanged();
    }

    private void updateEmptyViewDefault(){
        adapter.setEmptyView(getEmptyViewBinder());
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void loadData() {
        page = START_PAGE;
        adapter.clearData();
        adapter.showLoadingFull(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(datePickerPresenter.getRangeDateFormat(startDate, endDate));
        searchAd();
    }

    private void searchAd(int page) {
        this.page = page;
        searchAd();
    }

    protected void searchAd() {
        swipeToRefresh.setEnabled(false);
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
        recyclerView.removeOnScrollListener(onScrollListener);
        recyclerView.addOnScrollListener(onScrollListener);
        this.totalItem = totalItem;
        if (totalItem > 0 && !searchMode) {
            updateEmptyViewNoResult();
        }
        if (page == START_PAGE) {
            adapter.clearData();
            layoutManager.scrollToPositionWithOffset(0, 0);
        }
        adapter.addData(adList);
        hideLoading();
        if (adapter.getDataSize() < 1) {
            adapter.showEmptyFull(true);
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
            recyclerView.removeOnScrollListener(onScrollListener);
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
        swipeToRefresh.setEnabled(true);
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
        updateEmptyViewNoResult();
        return true;
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.promo_topads, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
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