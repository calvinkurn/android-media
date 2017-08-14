package com.tokopedia.seller.product.variant.view.fragment;

import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.fragment.BaseCacheListFragment;
import com.tokopedia.seller.product.variant.view.adapter.ProductVariantPickerCacheListAdapter;
import com.tokopedia.seller.product.variant.view.model.ProductVariantViewModel;

/**
 * Created by nathan on 8/4/17.
 */

public class ProductVariantPickerCacheFragment extends BaseCacheListFragment<ProductVariantViewModel> {

    @Override
    protected BaseListAdapter getNewAdapter() {
        return new ProductVariantPickerCacheListAdapter();
    }
}
