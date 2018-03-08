package com.tokopedia.seller.product.variant.view.fragment;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.fragment.BaseCacheListFragment;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantOption;
import com.tokopedia.seller.product.variant.view.adapter.ProductVariantPickerCacheListNewAdapter;
import com.tokopedia.seller.product.variant.view.listener.ProductVariantPickerItemCacheList;

import java.util.ArrayList;

/**
 * Created by nathan on 8/4/17.
 */

public class ProductVariantPickerCacheFragment extends BaseCacheListFragment<ProductVariantOption>
        implements ProductVariantPickerCacheListNewAdapter.RemoveCallback<ProductVariantOption>,
        ProductVariantPickerItemCacheList<ProductVariantOption> {

    public OnProductVariantPickerCacheNewFragmentListener onProductVariantPickerCacheNewFragmentListener;
    public interface OnProductVariantPickerCacheNewFragmentListener{
        boolean isDataColorType();
    }

    public static ProductVariantPickerCacheFragment newInstance() {

        Bundle args = new Bundle();
        ProductVariantPickerCacheFragment fragment = new ProductVariantPickerCacheFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected BaseListAdapter<ProductVariantOption> getNewAdapter() {
        ProductVariantPickerCacheListNewAdapter productVariantPickerCacheListNewAdapter =
                new ProductVariantPickerCacheListNewAdapter(
                onProductVariantPickerCacheNewFragmentListener.isDataColorType()
        );
        productVariantPickerCacheListNewAdapter.setRemoveCallback(this);
        return productVariantPickerCacheListNewAdapter;
    }

    @Override
    public void addItem(ProductVariantOption productVariantOption) {
        itemList.add(productVariantOption);
        resetPageAndSearch();
    }

    @Override
    public void removeItem(ProductVariantOption productVariantOption) {
        itemList.remove(productVariantOption);
        resetPageAndSearch();
    }

    @Override
    public void onRemove(ProductVariantOption productVariantOption) {
        itemList.remove(productVariantOption);
        pickerMultipleItem.removeItemFromCache(productVariantOption);
        resetPageAndSearch();
    }

    @Override
    public void removeAllItem() {
        itemList = new ArrayList<>();
        resetPageAndSearch();
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        onProductVariantPickerCacheNewFragmentListener = (OnProductVariantPickerCacheNewFragmentListener) context;
    }
}