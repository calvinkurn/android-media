package com.tokopedia.seller.product.variant.view.listener;

import com.tokopedia.seller.base.view.listener.BasePickerItemCacheList;

/**
 * Created by nathan on 8/3/17.
 */

public interface ProductVariantPickerItemCacheList<T> extends BasePickerItemCacheList<T> {

    void removeAllItem();

}