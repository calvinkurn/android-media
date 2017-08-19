package com.tokopedia.seller.product.variant.view.fragment;

import android.os.Bundle;

import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.fragment.BaseCacheListFragment;
import com.tokopedia.seller.product.variant.constant.ExtraConstant;
import com.tokopedia.seller.product.variant.view.adapter.ProductVariantPickerCacheListAdapter;
import com.tokopedia.seller.product.variant.view.listener.ProductVariantPickerItemCacheList;
import com.tokopedia.seller.product.variant.view.model.ProductVariantViewModel;

import java.util.ArrayList;

/**
 * Created by nathan on 8/4/17.
 */

public class ProductVariantPickerCacheFragment extends BaseCacheListFragment<ProductVariantViewModel> implements ProductVariantPickerCacheListAdapter.RemoveCallback<ProductVariantViewModel>, ProductVariantPickerItemCacheList<ProductVariantViewModel> {

    private int startTempId;
    private int currentTempId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startTempId = getActivity().getIntent().getIntExtra(ExtraConstant.EXTRA_PRODUCT_VARIANT_START_TEMP_ID, 0);
        currentTempId = startTempId;
    }

    @Override
    protected BaseListAdapter<ProductVariantViewModel> getNewAdapter() {
        ProductVariantPickerCacheListAdapter productVariantPickerCacheListAdapter = new ProductVariantPickerCacheListAdapter();
        productVariantPickerCacheListAdapter.setRemoveCallback(this);
        return productVariantPickerCacheListAdapter;
    }

    @Override
    public void addItem(ProductVariantViewModel productVariantViewModel) {
        // Set id as temp id
        productVariantViewModel.setId(currentTempId++);
        itemList.add(productVariantViewModel);
        resetPageAndSearch();
    }

    @Override
    public void removeItem(ProductVariantViewModel productVariantViewModel) {
        ProductVariantViewModel productVariantViewModelTemp = getVariantByUnitValueId(productVariantViewModel.getUnitValueId());
        itemList.remove(productVariantViewModelTemp);
        resetPageAndSearch();
    }

    @Override
    public void onRemove(ProductVariantViewModel productVariantViewModel) {
        productVariantViewModel.setId(0);
        itemList.remove(productVariantViewModel);
        pickerMultipleItem.removeItemFromCache(productVariantViewModel);
        resetPageAndSearch();
    }

    @Override
    public void removeAllItem() {
        itemList = new ArrayList<>();
        resetPageAndSearch();
    }

    private ProductVariantViewModel getVariantByUnitValueId(long unitValueId) {
        for (ProductVariantViewModel productVariantViewModel: itemList) {
            if (productVariantViewModel.getUnitValueId() == unitValueId) {
                return productVariantViewModel;
            }
        }
        return null;
    }
}