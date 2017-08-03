package com.tokopedia.seller.base.view.listener;

import com.tokopedia.seller.base.view.adapter.ItemPickerType;

import java.util.HashSet;

/**
 * Created by nathan on 8/3/17.
 */

public interface BasePickerMultipleItem {

    HashSet<ItemPickerType> getItemPickerTypeSet();

    void addItem(ItemPickerType itemPickerType, String fromFragmentTag);

    void removeItem(ItemPickerType itemPickerType, String fromFragmentTag);
}