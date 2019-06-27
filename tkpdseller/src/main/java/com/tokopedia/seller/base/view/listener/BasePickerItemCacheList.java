package com.tokopedia.seller.base.view.listener;

import java.util.List;

/**
 * Created by nathan on 8/3/17.
 */
@Deprecated
public interface BasePickerItemCacheList<T> {

    void addItem(T t);

    void removeItem(T t);

    List<T> getItemList();

}