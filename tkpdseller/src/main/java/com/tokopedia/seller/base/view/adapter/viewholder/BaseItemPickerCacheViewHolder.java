package com.tokopedia.seller.base.view.adapter.viewholder;

import android.view.View;

import com.tokopedia.seller.base.view.adapter.BaseViewHolder;
import com.tokopedia.seller.base.view.adapter.ItemPickerType;

/**
 * Created by nathan on 6/23/17.
 */

public abstract class BaseItemPickerCacheViewHolder<T extends ItemPickerType> extends BaseViewHolder<T> {

    public interface RemoveCallback<T> {
        void onRemove(T t);
    }


    public BaseItemPickerCacheViewHolder(View itemView) {
        super(itemView);
    }

    protected RemoveCallback<T> removeCallback;

    public void setRemoveCallback(RemoveCallback<T> removeCallback) {
        this.removeCallback = removeCallback;
    }

}