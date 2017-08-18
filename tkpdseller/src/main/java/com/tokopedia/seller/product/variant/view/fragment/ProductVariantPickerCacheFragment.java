package com.tokopedia.seller.product.variant.view.fragment;

import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.fragment.BaseCacheListFragment;
import com.tokopedia.seller.product.variant.view.adapter.ProductVariantPickerCacheListAdapter;
import com.tokopedia.seller.product.variant.view.listener.ProductVariantPickerItemCacheList;
import com.tokopedia.seller.product.variant.view.model.ProductVariantViewModel;

import java.util.ArrayList;

/**
 * Created by nathan on 8/4/17.
 */

public class ProductVariantPickerCacheFragment extends BaseCacheListFragment<ProductVariantViewModel> implements ProductVariantPickerCacheListAdapter.RemoveCallback<ProductVariantViewModel>, ProductVariantPickerItemCacheList<ProductVariantViewModel> {

    @Override
    protected BaseListAdapter<ProductVariantViewModel> getNewAdapter() {
        ProductVariantPickerCacheListAdapter productVariantPickerCacheListAdapter = new ProductVariantPickerCacheListAdapter();
        productVariantPickerCacheListAdapter.setRemoveCallback(this);
        return productVariantPickerCacheListAdapter;
    }

    @Override
    public void onRemove(ProductVariantViewModel productVariantViewModel) {
        itemList.remove(productVariantViewModel);
        pickerMultipleItem.removeItemFromCache(productVariantViewModel);
        resetPageAndSearch();
    }

    @Override
    public void removeAllItem() {
        itemList = new ArrayList<>();
        resetPageAndSearch();
    }
}