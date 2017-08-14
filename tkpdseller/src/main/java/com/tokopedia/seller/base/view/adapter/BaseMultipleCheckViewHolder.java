package com.tokopedia.seller.base.view.adapter;

import android.view.View;
import android.widget.CheckBox;

/**
 * Created by nathan on 6/23/17.
 */

public abstract class BaseMultipleCheckViewHolder<T> extends BaseViewHolder<T> {

    public interface CheckedCallback<T> {

        void onItemChecked(T t, boolean checked);
    }

    public abstract void bindObject(T t, boolean checked, CheckedCallback<T> checkedCallback);

    public abstract boolean isChecked();

    public abstract void setChecked(boolean checked);

    public BaseMultipleCheckViewHolder(View itemView) {
        super(itemView);
    }
}