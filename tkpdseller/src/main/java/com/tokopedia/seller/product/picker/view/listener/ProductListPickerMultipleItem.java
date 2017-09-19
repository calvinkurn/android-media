package com.tokopedia.seller.product.picker.view.listener;

import com.tokopedia.seller.base.view.adapter.ItemPickerType;
import com.tokopedia.seller.base.view.listener.BasePickerMultipleItem;
import com.tokopedia.seller.product.picker.view.model.ProductListPickerViewModel;

/**
 * Created by ZulfikarRahman on 9/7/17.
 */

public interface ProductListPickerMultipleItem<T extends ItemPickerType> extends BasePickerMultipleItem<T> {

    void validateFooterAndInfoView();

    boolean allowAddItem(ProductListPickerViewModel productListPickerViewModel);
}