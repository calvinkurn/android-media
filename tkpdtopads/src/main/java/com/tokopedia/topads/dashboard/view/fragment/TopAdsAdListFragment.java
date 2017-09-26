package com.tokopedia.topads.dashboard.view.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Px;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.tokopedia.core.customadapter.NoResultDataBinder;
import com.tokopedia.topads.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.adapter.ItemType;
import com.tokopedia.seller.base.view.listener.BaseListViewListener;
import com.tokopedia.design.label.DateLabelView;
import com.tokopedia.topads.common.view.presenter.BaseDatePickerPresenter;
import com.tokopedia.topads.common.view.presenter.BaseDatePickerPresenterImpl;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.view.adapter.TopAdsAdListAdapter;
import com.tokopedia.topads.dashboard.view.adapter.viewholder.TopAdsEmptyAdDataBinder;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsAdListPresenter;
import com.tokopedia.topads.keyword.view.fragment.TopAdsBaseListFragment;
import com.tokopedia.topads.keyword.view.listener.AdListMenuListener;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */

public abstract class TopAdsAdListFragment<P extends
        TopAdsAdListPresenter, T extends ItemType> extends TopAdsBaseListFragment<P, T> implements
        AdListMenuListener, BaseListViewListener<T>, SearchView.OnQueryTextListener,
        BaseListAdapter.Callback<T> {

    public interface OnAdListFragmentListener {
        void startShowCase();
    }

    protected static final String EXTRA_STATUS = "EXTRA_STATUS";
    protected static final String EXTRA_KEYWORD = "EXTRA_KEYWORD";

    protected static final int REQUEST_CODE_AD_CHANGE = 2;
    protected static final int REQUEST_CODE_AD_FILTER = 3;
    protected static final int REQUEST_CODE_AD_ADD = 4;

    private AppBarLayout appBarLayout;
    private DateLabelView dateLabelView;
    private FloatingActionButton fabAdd;
    private MenuItem filterMenuItem;
    private MenuItem searchMenuItem;

    protected int status;
    protected String keyword;

    private CoordinatorLayout.Behavior appBarBehaviour;
    private int scrollFlags;
    @Px
    private int tempTopPaddingRecycleView;
    @Px
    private int tempBottomPaddingRecycleView;

    private OnAdListFragmentListener listener;

    public TopAdsAdListFragment() {
        // Required empty public constructor
    }

    @Override
    protected BaseDatePickerPresenter getDatePickerPresenter() {
        return new BaseDatePickerPresenterImpl(getActivity());
    }

    protected BaseListAdapter<T> getNewAdapter() {
        return new TopAdsAdListAdapter();
    }

    @Override
    protected NoResultDataBinder getEmptyViewNoResultBinder() {
        TopAdsEmptyAdDataBinder emptyGroupAdsDataBinder = new TopAdsEmptyAdDataBinder(adapter);
        emptyGroupAdsDataBinder.setEmptyTitleText(getString(R.string.top_ads_empty_promo_not_found_title_empty_text));
        emptyGroupAdsDataBinder.setEmptyContentText(getString(R.string.top_ads_empty_promo_not_found_content_empty_text));
        emptyGroupAdsDataBinder.setEmptyContentItemText(null);
        return emptyGroupAdsDataBinder;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_list;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        tempTopPaddingRecycleView = recyclerView.getPaddingTop();
        tempBottomPaddingRecycleView = recyclerView.getPaddingBottom();
        fabAdd = (FloatingActionButton) view.findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateAd();
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if (fabAdd.isShown()) {
                        fabAdd.hide();
                    }
                } else if (dy < 0) {
                    if (!fabAdd.isShown()) {
                        fabAdd.show();
                    }
                }
            }
        });
        initDateLabelView(view);
    }

    protected void initDateLabelView(View view) {
        appBarLayout = (AppBarLayout) view.findViewById(R.id.app_bar_layout);
        dateLabelView = (DateLabelView) view.findViewById(R.id.date_label_view);
        dateLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });
        AppBarLayout.LayoutParams dateLabelViewLayoutParams = (AppBarLayout.LayoutParams) dateLabelView.getLayoutParams();
        scrollFlags = dateLabelViewLayoutParams.getScrollFlags();

        appBarBehaviour = new AppBarLayout.Behavior();
    }

    @Override
    protected void loadData() {
        super.loadData();
        updateDateLabelViewText();
    }

    protected void updateDateLabelViewText() {
        dateLabelView.setDate(datePickerPresenter.getStartDate(), datePickerPresenter.getEndDate());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (intent != null && (requestCode == REQUEST_CODE_AD_ADD || requestCode == REQUEST_CODE_AD_CHANGE)) {
            boolean adChanged = intent.getBooleanExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, false);
            boolean adDeleted = intent.getBooleanExtra(TopAdsExtraConstant.EXTRA_AD_DELETED, false);
            if (adChanged || adDeleted) {
                resetPageAndSearch();
                setResultAdListChanged();
            }
        } else if (requestCode == REQUEST_CODE_AD_FILTER) {
            searchMode = true;
        }
    }

    @Override
    public void onSearchLoaded(@NonNull List list, int totalItem) {
        super.onSearchLoaded(list, totalItem);
        if (listener != null && adapter.getDataSize() > 0) {
            listener.startShowCase();
        }
    }

    @Override
    public void onLoadSearchError(Throwable t) {
        super.onLoadSearchError(t);
        if (adapter.getDataSize() < 1) {
            showDateLabel(false);
        }
    }

    protected void setResultAdListChanged() {
        Intent intent = new Intent();
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, true);
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

    @Override
    protected void showViewEmptyList() {
        super.showViewEmptyList();
        showDateLabel(false);
        showOption(false);
    }

    @Override
    protected void showViewSearchNoResult() {
        super.showViewSearchNoResult();
        showDateLabel(true);
        showOption(true);
    }

    @Override
    protected void showViewList(@NonNull List list) {
        super.showViewList(list);
        showDateLabel(true);
        showOption(true);
    }

    private void showOption(boolean show) {
        fabAdd.setVisibility(show ? View.VISIBLE : View.GONE);
        if (filterMenuItem != null) {
            filterMenuItem.setVisible(show);
        }
        if (searchMenuItem != null) {
            searchMenuItem.setVisible(show);
        }
    }

    private void showDateLabel(boolean show) {
        @Px int topPadding = 0;
        @Px int bottomPadding = 0;
        if (show) {
            topPadding = tempTopPaddingRecycleView;
            bottomPadding = tempBottomPaddingRecycleView;
        }
        recyclerView.setPadding(0, topPadding, 0, bottomPadding);
        if (appBarLayout != null) {
            AppBarLayout.LayoutParams dateLabelLayoutParams = (AppBarLayout.LayoutParams) dateLabelView.getLayoutParams();
            dateLabelLayoutParams.setScrollFlags(show ? scrollFlags : 0);
            dateLabelView.setLayoutParams(dateLabelLayoutParams);

            CoordinatorLayout.LayoutParams appBarLayoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
            appBarLayoutParams.setBehavior(show ? appBarBehaviour : null);
            appBarLayout.setLayoutParams(appBarLayoutParams);
        }
        if (dateLabelView != null) {
            dateLabelView.setVisibility(show ? View.VISIBLE : View.GONE);
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
        onSearch(query);
        return true;
    }

    @Override
    public void onSearch(String keyword) {
        this.keyword = keyword;
        resetPageAndSearch();
        if (!searchMode && !TextUtils.isEmpty(keyword)) {
            searchMode = true;
        }
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        menu.clear();
        initMenuItem(menu, inflater);
        super.onCreateOptionsMenu(menu, inflater);
    }

    protected void initMenuItem(final Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_top_ads_list, menu);
        filterMenuItem = menu.findItem(R.id.menu_filter);
        searchMenuItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_filter) {
            goToFilter();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.unSubscribe();
        }
    }

    @Override
    public void onAttachListener(Context context) {
        super.onAttachListener(context);
        if (context instanceof OnAdListFragmentListener) {
            listener = (OnAdListFragmentListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    // for show case
    public View getItemRecyclerView() {
        int position = layoutManager.findFirstCompletelyVisibleItemPosition();
        return layoutManager.findViewByPosition(position);
    }

    public View getDateView() {
        return getView().findViewById(R.id.date_label_view);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_STATUS, status);
        outState.putString(EXTRA_KEYWORD, keyword);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState == null) {
            return;
        }
        status = savedInstanceState.getInt(EXTRA_STATUS);
        keyword = savedInstanceState.getString(EXTRA_KEYWORD);
    }

    public View getFab() {
        return fabAdd;
    }
}