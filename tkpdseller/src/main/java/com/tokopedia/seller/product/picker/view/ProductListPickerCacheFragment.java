package com.tokopedia.seller.product.picker.view;

import com.tokopedia.seller.base.view.adapter.BaseItemPickerCacheAdapter;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.fragment.BaseCacheListFragment;
import com.tokopedia.seller.product.picker.view.adapter.ProductListPickerCacheAdapter;
import com.tokopedia.seller.product.picker.view.model.ProductListPickerViewModel;

/**
 * Created by zulfikarrahman on 9/7/17.
 */

public class ProductListPickerCacheFragment extends BaseCacheListFragment<ProductListPickerViewModel> implements BaseItemPickerCacheAdapter.RemoveCallback<ProductListPickerViewModel> {

    @Override
    protected BaseListAdapter<ProductListPickerViewModel> getNewAdapter() {
        ProductListPickerCacheAdapter productListPickerCacheAdapter = new ProductListPickerCacheAdapter();
        productListPickerCacheAdapter.setRemoveCallback(this);
        return new ProductListPickerCacheAdapter();
    }

    @Override
    public void onRemove(ProductListPickerViewModel productListPickerViewModel) {
        itemList.remove(productListPickerViewModel);
        pickerMultipleItem.removeItemFromCache(productListPickerViewModel);
        resetPageAndSearch();
    }
}
