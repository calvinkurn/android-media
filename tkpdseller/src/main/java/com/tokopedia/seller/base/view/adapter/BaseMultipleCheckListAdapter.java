package com.tokopedia.seller.base.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.seller.base.view.adapter.viewholder.BaseMultipleCheckViewHolder;

import java.util.HashSet;

/**
 * Created by zulfikarrahman on 11/24/16.
 */
public abstract class BaseMultipleCheckListAdapter<T extends ItemIdType> extends BaseListAdapter<T> {

    public interface CheckedCallback<T> {

        void onItemChecked(T t, boolean checked);
    }

    private CheckedCallback<T> checkedCallback;
    private HashSet<String> hashSet;

    public void setCheckedCallback(CheckedCallback<T> checkedCallback) {
        this.checkedCallback = checkedCallback;
    }

    public BaseMultipleCheckListAdapter() {
        super();
        hashSet = new HashSet<>();
    }

    private boolean isChecked(String id) {
        return hashSet.contains(id);
    }

    public void setChecked(String id, boolean checked) {
        if (checked) {
            hashSet.add(id);
        } else {
            hashSet.remove(id);
        }
    }

    public void clearCheck() {
        hashSet = new HashSet<>();
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
            ((BaseMultipleCheckViewHolder<T>) viewHolder).bindObject(t, isChecked(t.getId()));
            ((BaseMultipleCheckViewHolder<T>) viewHolder).setCheckedCallback(new BaseMultipleCheckViewHolder.CheckedCallback<T>() {
                @Override
                public void onItemChecked(T t, boolean checked) {
                    updateChecked(t, position, checked);
                }
            });
        }
    }

    private void updateChecked(T t, int position, boolean checked) {
        setChecked(t.getId(), checked);
        if (checkedCallback != null) {
            checkedCallback.onItemChecked(t, checked);
        }
    }
}