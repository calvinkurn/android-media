package com.tokopedia.seller.product.picker.view;

import com.tokopedia.seller.base.view.adapter.BaseItemPickerCacheAdapter;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.fragment.BaseCacheListFragment;
import com.tokopedia.seller.product.picker.view.adapter.ProductListPickerCacheAdapter;
import com.tokopedia.seller.product.picker.view.model.ProductListPickerViewModel;

import java.util.Iterator;

/**
 * Created by zulfikarrahman on 9/7/17.
 */

public class ProductListPickerCacheFragment extends BaseCacheListFragment<ProductListPickerViewModel> implements BaseItemPickerCacheAdapter.RemoveCallback<ProductListPickerViewModel> {

    @Override
    protected BaseListAdapter<ProductListPickerViewModel> getNewAdapter() {
        ProductListPickerCacheAdapter productListPickerCacheAdapter = new ProductListPickerCacheAdapter();
        productListPickerCacheAdapter.setRemoveCallback(this);
        return productListPickerCacheAdapter;
    }

    @Override
    public void onRemove(ProductListPickerViewModel productListPickerViewModel) {
        itemList.remove(productListPickerViewModel);
        pickerMultipleItem.removeItemFromCache(productListPickerViewModel);
        resetPageAndSearch();
    }

    @Override
    public void removeItem(ProductListPickerViewModel productListPickerViewModel) {
        Iterator<ProductListPickerViewModel> itemListTemp = itemList.iterator();

        while (itemListTemp.hasNext()) {
            ProductListPickerViewModel productListPickerViewModelTemp = itemListTemp.next();
            if(productListPickerViewModel.getId().equals(productListPickerViewModelTemp.getId())) {
                itemListTemp.remove();
                resetPageAndSearch();
            }
        }
    }
}
