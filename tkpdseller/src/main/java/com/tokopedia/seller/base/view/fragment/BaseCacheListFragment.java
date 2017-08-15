package com.tokopedia.seller.base.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.ItemPickerType;
import com.tokopedia.seller.base.view.listener.BasePickerItemCacheList;
import com.tokopedia.seller.base.view.listener.BasePickerMultipleItem;
import com.tokopedia.seller.base.view.presenter.BlankPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 8/3/17.
 */

public abstract class BaseCacheListFragment<T extends ItemPickerType> extends BaseListFragment<BlankPresenter, T> implements BasePickerItemCacheList<T> {

    protected BasePickerMultipleItem pickerMultipleItem;
    protected List<T> itemList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof BasePickerMultipleItem) {
            pickerMultipleItem = (BasePickerMultipleItem) getActivity();
        }
        itemList = new ArrayList<>();
    }

    @Override
    public void onItemClicked(ItemPickerType itemPickerType) {
        // No action
    }

    @Override
    protected void searchForPage(int page) {
        // Never used
        onSearchLoaded(itemList, itemList.size());
    }

    @Override
    public void addItem(T t) {
        itemList.add(t);
        resetPageAndSearch();
    }

    @Override
    public void removeItem(T t) {
        itemList.remove(t);
        resetPageAndSearch();
    }

    @Override
    public List<T> getItemList() {
        return itemList;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_base_cache_list;
    }
}