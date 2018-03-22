package com.tokopedia.seller.base.view.adapter;

import com.tokopedia.seller.base.view.adapter.viewholder.BaseItemPickerCacheViewHolder;

/**
 * Created by nathan on 8/14/17.
 */

public abstract class BaseItemPickerCacheAdapter<T extends ItemType> extends BaseListAdapter<T> implements BaseItemPickerCacheViewHolder.RemoveCallback<T> {

    public interface RemoveCallback<T> {
        void onRemove(T t);
    }

    protected RemoveCallback removeCallback;

    public void setRemoveCallback(RemoveCallback<T> removeCallback) {
        this.removeCallback = removeCallback;
    }

    @Override
    public void onRemove(T t) {
        if (removeCallback != null) {
            data.remove(t);
            removeCallback.onRemove(t);
        }
    }
}