package com.tokopedia.seller.base.view.adapter;

/**
 * Created by nathan on 8/14/17.
 */

public abstract class BaseItemPickerCacheAdapter<T extends ItemType> extends BaseListAdapter<T> {

    public interface RemoveCallback<T> {
        void onRemove(T t);
    }

    protected RemoveCallback removeCallback;

    public void setRemoveCallback(RemoveCallback<T> removeCallback) {
        this.removeCallback = removeCallback;
    }
}