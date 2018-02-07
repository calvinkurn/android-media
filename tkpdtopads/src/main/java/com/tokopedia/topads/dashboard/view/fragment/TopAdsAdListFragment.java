package com.tokopedia.topads.dashboard.view.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.customadapter.NoResultDataBinder;
import com.tokopedia.design.button.BottomActionView;
import com.tokopedia.design.label.DateLabelView;
import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.adapter.ItemType;
import com.tokopedia.seller.base.view.listener.BaseListViewListener;
import com.tokopedia.topads.R;
import com.tokopedia.topads.common.util.TopAdsComponentUtils;
import com.tokopedia.topads.common.view.presenter.BaseDatePickerPresenter;
import com.tokopedia.topads.common.view.presenter.BaseDatePickerPresenterImpl;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.dashboard.view.adapter.TopAdsAdListAdapter;
import com.tokopedia.topads.dashboard.view.adapter.viewholder.TopAdsEmptyAdDataBinder;
import com.tokopedia.topads.dashboard.view.listener.TopAdsFilterViewListener;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsAdListPresenter;
import com.tokopedia.topads.keyword.view.fragment.TopAdsBaseListFragment;
import com.tokopedia.topads.keyword.view.listener.AdListMenuListener;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 */

public abstract class TopAdsAdListFragment<P extends
        TopAdsAdListPresenter, T extends ItemType> extends TopAdsBaseListFragment<P, T> implements
        AdListMenuListener, BaseListViewListener<T>, TopAdsFilterViewListener,
        BaseListAdapter.Callback<T>,SearchInputView.Listener {

    protected static final long DEFAULT_DELAY_TEXT_CHANGED = TimeUnit.MILLISECONDS.toMillis(300);
    protected static final String EXTRA_STATUS = "EXTRA_STATUS";
    protected static final String EXTRA_KEYWORD = "EXTRA_KEYWORD";
    protected static final int REQUEST_CODE_AD_CHANGE = 2;
    protected static final int REQUEST_CODE_AD_FILTER = 3;
    protected static final int REQUEST_CODE_AD_ADD = 4;
    protected SearchInputView searchInputView;
    protected int status;
    protected String keyword;
    private AppBarLayout appBarLayout;
    private DateLabelView dateLabelView;
    private BottomActionView buttonActionView;
    private MenuItem menuAdd;
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
    public void onSearchSubmitted(String newText) {
        CommonUtils.hideKeyboard(getActivity(), getActivity().getCurrentFocus());
        onSearch(newText);
    }

    @Override
    public void onSearchTextChanged(String query) {
        if (TextUtils.isEmpty(query)) {
            onQueryTextSubmit(query);
        }
    }

    protected TopAdsComponent getTopAdsComponent(){
        return TopAdsComponentUtils.getTopAdsComponent(getActivity());
    }

    @Override
    protected BaseDatePickerPresenter getDatePickerPresenter() {
        BaseDatePickerPresenterImpl baseDatePickerPresenter = new BaseDatePickerPresenterImpl(getActivity());
        return baseDatePickerPresenter;
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
        searchInputView = (SearchInputView) view.findViewById(R.id.search_input_view);
        searchInputView.setDelayTextChanged(DEFAULT_DELAY_TEXT_CHANGED);
        searchInputView.setListener(this);
        buttonActionView = (BottomActionView) view.findViewById(R.id.bottom_action_view);
        buttonActionView.setButton1OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFilter();
            }
        });
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
            setSearchMode(true);
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
        if(buttonActionView != null)
            buttonActionView.setVisibility(show ? View.VISIBLE : View.GONE);
        showSearchView(show);
        if(menuAdd != null){
            menuAdd.setVisible(show);
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

    public boolean onQueryTextSubmit(String query) {
        onSearch(query);
        return true;
    }

    public void onSearch(String keyword) {
        this.keyword = keyword;
        resetPageAndSearch();
        if (!isSearchMode() && !TextUtils.isEmpty(keyword)) {
            setSearchMode(true);
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
        menuAdd = menu.findItem(R.id.menu_add);
        menuAdd.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onCreateAd();
                return true;
            }
        });
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
        if(menuAdd != null)
            menuAdd.getActionView();
        return null;
    }

    protected void showSearchView(boolean show) {
        @Px int topPadding = 0;
        @Px int bottomPadding = 0;
        if (show) {
            topPadding = tempTopPaddingRecycleView;
            bottomPadding = tempBottomPaddingRecycleView;
        }
        recyclerView.setPadding(0, topPadding, 0, bottomPadding);
        if (appBarLayout != null) {
            AppBarLayout.LayoutParams dateLabelLayoutParams = (AppBarLayout.LayoutParams) searchInputView.getLayoutParams();
            dateLabelLayoutParams.setScrollFlags(show ? scrollFlags : 0);
            searchInputView.setLayoutParams(dateLabelLayoutParams);

            CoordinatorLayout.LayoutParams appBarLayoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
            appBarLayoutParams.setBehavior(show ? appBarBehaviour : null);
            appBarLayout.setLayoutParams(appBarLayoutParams);
        }
        if (searchInputView != null) {
            searchInputView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public View getFilterView() {
        return buttonActionView;
    }

    @Nullable
    @Override
    public View getSearchView() {
        return searchInputView;
    }

    public interface OnAdListFragmentListener {
        void startShowCase();
    }
}