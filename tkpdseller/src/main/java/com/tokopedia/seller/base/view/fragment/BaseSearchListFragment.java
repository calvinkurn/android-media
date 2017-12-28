package com.tokopedia.seller.base.view.fragment;

import android.support.annotation.NonNull;
import android.support.annotation.Px;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.text.TextUtils;
import android.view.View;

import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.ItemType;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author normansyahputa on 5/17/17.
 *         another type of {@link com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsAdListFragment}
 */

public abstract class BaseSearchListFragment<P, T extends ItemType> extends BaseListFragment<P, T> implements SearchInputView.Listener {

    private static final long DEFAULT_DELAY_TEXT_CHANGED = TimeUnit.MILLISECONDS.toMillis(300);

    private AppBarLayout appBarLayout;
    protected SearchInputView searchInputView;

    private CoordinatorLayout.Behavior appBarBehaviour;
    private int scrollFlags;
    @Px
    private int tempTopPaddingRecycleView;
    @Px
    private int tempBottomPaddingRecycleView;

    protected long getDelayTextChanged() {
        return DEFAULT_DELAY_TEXT_CHANGED;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_base_search_list;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        appBarLayout = (AppBarLayout) view.findViewById(R.id.app_bar_layout);
        searchInputView = (SearchInputView) view.findViewById(R.id.search_input_view);
        searchInputView.setDelayTextChanged(getDelayTextChanged());
        searchInputView.setListener(this);
        AppBarLayout.LayoutParams dateLabelViewLayoutParams = (AppBarLayout.LayoutParams) searchInputView.getLayoutParams();
        scrollFlags = dateLabelViewLayoutParams.getScrollFlags();

        appBarBehaviour = new AppBarLayout.Behavior();
        tempTopPaddingRecycleView = recyclerView.getPaddingTop();
        tempBottomPaddingRecycleView = recyclerView.getPaddingBottom();
    }

    @Override
    protected void showViewEmptyList() {
        super.showViewEmptyList();
        showSearchView(false);
    }

    @Override
    protected void showViewSearchNoResult() {
        super.showViewSearchNoResult();
        showSearchView(true);
    }

    @Override
    protected void showViewList(@NonNull List<T> list) {
        super.showViewList(list);
        showSearchView(true);
    }

    @Override
    protected void onLoadSearchErrorWithDataEmpty(Throwable t) {
        super.onLoadSearchErrorWithDataEmpty(t);
        showSearchView(false);
    }

    @Override
    protected void onLoadSearchErrorWithDataExist(Throwable t) {
        super.onLoadSearchErrorWithDataExist(t);
        showSearchView(true);
    }

    @Override
    public void onSearchSubmitted(String text) {
        updateSearchMode(text);
        resetPageAndSearch();
    }

    @Override
    public void onSearchTextChanged(String text) {
        updateSearchMode(text);
        resetPageAndSearch();
    }

    private void updateSearchMode(String text) {
        setSearchMode(!TextUtils.isEmpty(text));
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
}