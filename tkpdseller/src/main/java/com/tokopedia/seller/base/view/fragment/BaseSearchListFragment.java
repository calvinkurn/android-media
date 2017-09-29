package com.tokopedia.seller.base.view.fragment;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.ItemType;

import java.util.List;

/**
 * @author normansyahputa on 5/17/17.
 *         another type of {@link com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsAdListFragment}
 */

public abstract class BaseSearchListFragment<P, T extends ItemType> extends BaseListFragment<P, T> implements SearchInputView.Listener {

    protected SearchInputView searchInputView;

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_base_search_list;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        searchInputView = (SearchInputView) view.findViewById(R.id.search_input_view);
        searchInputView.setListener(this);
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
    }

    @Override
    public void onSearchTextChanged(String text) {
        updateSearchMode(text);
    }

    private void updateSearchMode(String text) {
        searchMode = !TextUtils.isEmpty(text);
    }

    private void showSearchView(boolean isVisible) {
        if(isVisible) {
            searchInputView.setVisibility(View.VISIBLE);
        }else{
            searchInputView.setVisibility(View.GONE);
        }
    }
}