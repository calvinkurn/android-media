package com.tokopedia.seller.topads.keyword.view.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.customadapter.RetryDataBinder;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.keyword.view.listener.TopAdsListViewListener;
import com.tokopedia.seller.topads.keyword.view.presenter.TopAdsKeywordListPresenter;
import com.tokopedia.seller.topads.view.adapter.TopAdsAdListAdapter;
import com.tokopedia.seller.topads.view.adapter.viewholder.TopAdsEmptyAdDataBinder;
import com.tokopedia.seller.topads.view.adapter.viewholder.TopAdsRetryDataBinder;
import com.tokopedia.seller.topads.view.presenter.TopAdsDatePickerPresenter;
import com.tokopedia.seller.topads.view.presenter.TopAdsDatePickerPresenterImpl;
import com.tokopedia.seller.topads.view.widget.DividerItemDecoration;

import java.util.List;

/**
 * @author normansyahputa on 5/17/17.
 *         another type of {@link com.tokopedia.seller.topads.view.fragment.TopAdsAdListFragment}
 */

public abstract class TopAdsAdListFragment<T extends TopAdsKeywordListPresenter> extends TopAdsDatePickerFragment<T> implements
        TopAdsListViewListener, TopAdsAdListAdapter.Callback {
    protected static final int REQUEST_CODE_AD_STATUS = 2;
    protected static final int REQUEST_CODE_AD_FILTER = 3;
    protected static final int START_PAGE = 1;


    protected int status;
    protected int page;
    protected int totalItem;

    protected TopAdsAdListAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeToRefresh swipeToRefresh;
    private boolean searchMode;
    private LinearLayoutManager layoutManager;
    private SnackbarRetry snackBarRetry;
    private ProgressDialog progressDialog;
    private RecyclerView.OnScrollListener onScrollListener;

    public TopAdsAdListFragment() {
        // Required empty public constructor
    }

    protected abstract TopAdsEmptyAdDataBinder getEmptyViewBinder();

    protected abstract void goToFilter();

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
        return R.layout.fragment_top_ads_list_without_fab;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initialVar();
        setViewListener();
    }

    protected void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.list_product);
        swipeToRefresh = (SwipeToRefresh) view.findViewById(R.id.swipe_refresh_layout);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
    }

    protected void setViewListener() {
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

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            }
        };
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        recyclerView.addOnScrollListener(onScrollListener);
    }

    protected void initialVar() {
        page = START_PAGE;
        totalItem = Integer.MAX_VALUE;
        searchMode = false;
        new RefreshHandler(getActivity(), getView(), new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                searchAd(START_PAGE);
            }
        });
        adapter = initializeTopAdsAdapter();
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

    protected TopAdsAdListAdapter initializeTopAdsAdapter() {
        return new TopAdsAdListAdapter();
    }

    protected void updateEmptyViewNoResult() {
        TopAdsEmptyAdDataBinder emptyGroupAdsDataBinder = new TopAdsEmptyAdDataBinder(adapter);
        emptyGroupAdsDataBinder.setEmptyTitleText(getString(R.string.top_ads_empty_promo_not_found_title_empty_text));
        emptyGroupAdsDataBinder.setEmptyContentText(getString(R.string.top_ads_empty_promo_not_found_content_empty_text));
        emptyGroupAdsDataBinder.setEmptyContentItemText(null);
        adapter.setEmptyView(emptyGroupAdsDataBinder);
        adapter.notifyDataSetChanged();
    }

    private void updateEmptyViewDefault() {
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

    protected void searchAd(int page) {
        this.page = page;
        searchAd();
    }

    protected void searchAd() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // check if the request code is the same
        if (requestCode == REQUEST_CODE_AD_STATUS && intent != null) {
            boolean adStatusChanged = intent.getBooleanExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, false);
            boolean adDeleted = intent.getBooleanExtra(TopAdsExtraConstant.EXTRA_AD_DELETED, false);
            if (adStatusChanged || adDeleted) {
                searchAd(START_PAGE);
                setResultAdListChanged();
            }
        }
    }

    protected void setResultAdListChanged() {
        Intent intent = new Intent();
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, true);
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

    @Override
    public void onSearchAdLoaded(@NonNull List adList, boolean isEndOfFile) {

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
            snackBarRetry.setColorActionRetry(ContextCompat.getColor(getActivity(), R.color.green_400));
        }
    }

    private void hideSnackBarRetry() {
        if (snackBarRetry != null) {
            snackBarRetry.hideRetrySnackbar();
            snackBarRetry = null;
        }
    }
}
