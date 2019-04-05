package com.tokopedia.seller.base.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.tokopedia.base.list.seller.view.fragment.BaseListFragment;
import com.tokopedia.seller.R;
import com.tokopedia.product.manage.item.common.util.ItemPickerType;
import com.tokopedia.seller.base.view.listener.BasePickerItemCacheList;
import com.tokopedia.seller.base.view.listener.BasePickerMultipleItem;
import com.tokopedia.seller.base.view.presenter.BlankPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 8/3/17.
 */
@Deprecated
public abstract class BaseCacheListFragment<T extends ItemPickerType> extends BaseListFragment<BlankPresenter, T> implements BasePickerItemCacheList<T> {

    protected BasePickerMultipleItem<T> pickerMultipleItem;
    protected List<T> itemList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemList = new ArrayList<>();
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return null;
    }

    @Override
    public void onItemClicked(ItemPickerType itemPickerType) {
        // No action
    }

    @Override
    protected void searchForPage(int page) {
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

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        if (context instanceof BasePickerMultipleItem) {
            pickerMultipleItem = (BasePickerMultipleItem<T>) context;
        }
    }
}