package com.tokopedia.seller.base.view.listener;

import com.tokopedia.seller.manageitem.common.util.ItemPickerType;

/**
 * Created by nathan on 8/3/17.
 */
@Deprecated
public interface BasePickerMultipleItem<T extends ItemPickerType> {

    void addItemFromSearch(T itemPickerType);

    void removeItemFromSearch(T itemPickerType);

    void removeItemFromCache(T itemPickerType);

    int getMaxItemSelection();
}