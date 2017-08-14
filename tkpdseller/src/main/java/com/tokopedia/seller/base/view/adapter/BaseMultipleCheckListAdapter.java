package com.tokopedia.seller.base.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;

/**
 * Created by zulfikarrahman on 11/24/16.
 */
public abstract class BaseMultipleCheckListAdapter<T extends ItemType> extends BaseListAdapter<T> {

    public interface CheckedCallback<T> {

        void onItemChecked(T t, boolean checked);
    }

    protected CheckedCallback<T> checkedCallback;
    private SparseBooleanArray selectedItems;

    public void setCheckedCallback(CheckedCallback<T> checkedCallback) {
        this.checkedCallback = checkedCallback;
    }

    public BaseMultipleCheckListAdapter() {
        super();
        selectedItems = new SparseBooleanArray();
    }

    protected void bindData(final int position, final RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof BaseMultipleCheckViewHolder) {
            final T t = data.get(position);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onItemClicked(t);
                    }
                    boolean checked = ((BaseMultipleCheckViewHolder<T>) viewHolder).isChecked();
                    ((BaseMultipleCheckViewHolder<T>) viewHolder).setChecked(!checked);
                    updateChecked(t, position, !checked);
                }
            });
            ((BaseMultipleCheckViewHolder<T>) viewHolder).bindObject(t, selectedItems.get(position, false), new BaseMultipleCheckViewHolder.CheckedCallback<T>() {
                @Override
                public void onItemChecked(T t, boolean checked) {
                    updateChecked(t, position, checked);
                }
            });
        }
    }

    private void updateChecked(T t, int position, boolean checked) {
        if (checked) {
            selectedItems.append(position, checked);
        } else {
            selectedItems.delete(position);
        }
        if (checkedCallback != null) {
            checkedCallback.onItemChecked(t, checked);
        }
    }
}