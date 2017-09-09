package com.tokopedia.seller.product.picker.view.listener;

import com.tokopedia.seller.base.view.adapter.ItemPickerType;
import com.tokopedia.seller.base.view.listener.BasePickerMultipleItem;

/**
 * Created by ZulfikarRahman on 9/7/17.
 */

public interface ProductListPickerMultipleItem<T extends ItemPickerType> extends BasePickerMultipleItem<T> {

    void validateFooterAndInfoView();

    boolean allowAddItem();
}