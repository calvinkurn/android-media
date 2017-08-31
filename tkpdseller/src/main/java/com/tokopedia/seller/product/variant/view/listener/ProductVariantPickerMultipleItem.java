package com.tokopedia.seller.product.variant.view.listener;

import com.tokopedia.seller.base.view.adapter.ItemPickerType;
import com.tokopedia.seller.base.view.listener.BasePickerMultipleItem;

/**
 * Created by nathan on 8/3/17.
 */

public interface ProductVariantPickerMultipleItem<T extends ItemPickerType> extends BasePickerMultipleItem<T> {

    void removeAllItemFromSearch();

    void validateFooterAndInfoView();

    boolean allowAddItem();
}