package com.tokopedia.seller.base.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.core.util.Pair;
import com.tokopedia.product.common.util.ItemIdType;
import com.tokopedia.seller.base.view.adapter.viewholder.BaseMultipleCheckViewHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/24/16.
 */
public abstract class BaseMultipleCheckListAdapter<T extends ItemIdType> extends BaseListAdapter<T> {

    protected HashSet<String> hashSet;
    private CheckedCallback<T> checkedCallback;

    public BaseMultipleCheckListAdapter() {
        super();
        hashSet = new HashSet<>();
    }

    public void setCheckedCallback(CheckedCallback<T> checkedCallback) {
        this.checkedCallback = checkedCallback;
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

    public int getTotalChecked() {
        return hashSet.size();
    }

    public void resetCheckedItemSet() {
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
            ((BaseMultipleCheckViewHolder<T>) viewHolder).bindObject(t, isChecked(t.getItemId()));
            ((BaseMultipleCheckViewHolder<T>) viewHolder).setCheckedCallback(new BaseMultipleCheckViewHolder.CheckedCallback<T>() {
                @Override
                public void onItemChecked(T t, boolean checked) {
                    updateChecked(t, position, checked);
                }
            });
        }
    }

    protected void updateChecked(T t, int position, boolean checked) {
        setChecked(t.getItemId(), checked);
        if (checkedCallback != null) {
            checkedCallback.onItemChecked(t, checked);
        }
    }

    public List<Pair<Integer, T>> deleteCheckedItem() {
        List<Pair<Integer, T>> deletedItems = new ArrayList<>();

        int index = 0;
        for (Iterator<T> iterator = data.iterator(); iterator.hasNext(); index++) {
            T t = iterator.next();
            if (hashSet.contains(t.getItemId())) {
                deletedItems.add(new Pair<Integer, T>(index, t));
                hashSet.remove(t.getItemId());
                iterator.remove();
            }
        }

        notifyDataSetChanged();

        return deletedItems.isEmpty() ? null : deletedItems;
    }

    public List<String> getListChecked() {
        return new ArrayList<>(hashSet);
    }


    public interface CheckedCallback<T> {

        void onItemChecked(T t, boolean checked);
    }
}