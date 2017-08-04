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

public abstract class BaseCacheListFragment extends BaseListFragment<BlankPresenter, ItemPickerType> implements BasePickerItemCacheList {

    private BasePickerMultipleItem pickerMultipleItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof BasePickerMultipleItem) {
            pickerMultipleItem = (BasePickerMultipleItem) getActivity();
        }
    }

    @Override
    public void onItemClicked(ItemPickerType itemPickerType) {
        // No action
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return null;
    }

    @Override
    protected void searchForPage(int page) {
        // Never used
    }

    @Override
    public void notifyChange() {
        List<ItemPickerType> itemPickerTypeList = getItemList();
        onSearchLoaded(itemPickerTypeList, itemPickerTypeList.size());
    }

    private List<ItemPickerType> getItemList() {
        List<ItemPickerType> itemPickerTypeList = new ArrayList<>();
        if (pickerMultipleItem != null) {
            for (ItemPickerType itemPickerType: pickerMultipleItem.getItemPickerTypeSet()) {
                itemPickerTypeList.add(itemPickerType);
            }
        }
        return itemPickerTypeList;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_base_cache_list;
    }
}