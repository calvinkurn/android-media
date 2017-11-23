package com.tokopedia.seller.base.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.core.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/24/16.
 */
public abstract class BaseListAdapter<T extends ItemType> extends BaseLinearRecyclerViewAdapter {


    protected List<T> data;
    protected Callback<T> callback;

    public BaseListAdapter() {
        super();
        this.data = new ArrayList<>();
    }

    public List<T> getData() {
        return data;
    }

    public void setCallback(Callback<T> callback) {
        this.callback = callback;
    }

    @Override
    public int getItemCount() {
        return data.size() + super.getItemCount();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_LOADING:
            case VIEW_RETRY:
            case VIEW_EMPTY:
                super.onBindViewHolder(holder, position);
                break;
            default:
                bindViewHolder(position, holder);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isLastItemPosition(position) && (data.isEmpty() || isLoading() || isRetry())) {
            if (isLoading()) {
                return VIEW_LOADING;
            } else if (isRetry()) {
                return VIEW_RETRY;
            } else {
                return VIEW_EMPTY;
            }
        } else {
            return data.get(position).getType();
        }
    }

    protected View getLayoutView(ViewGroup parent, @LayoutRes int layoutRes) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
    }

    private void bindViewHolder(final int position, RecyclerView.ViewHolder viewHolder) {
        if (data.size() <= position) {
            return;
        }
        bindData(position, viewHolder);
    }

    protected void bindData(final int position, RecyclerView.ViewHolder viewHolder) {
        final T t = data.get(position);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onItemClicked(t);
                }
            }
        });
        if (viewHolder instanceof BaseViewHolder) {
            ((BaseViewHolder<T>) viewHolder).bindObject(t);
        }
    }

    private boolean isLastItemPosition(int position) {
        return position == data.size();
    }

    public int getDataSize() {
        return data.size();
    }

    public void addData(List<T> data) {
        if (data != null && data.size() > 0) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void addSingleDataWithPosition(Pair<Integer, T> pairData) {
        if (data != null && data.size() > 0) {
            this.data.add(pairData.getModel1(), pairData.getModel2());
            notifyItemInserted(pairData.getModel1());
        }
    }

    public void clearData() {
        this.data.clear();
        notifyDataSetChanged();
    }

    public interface Callback<T> {

        void onItemClicked(T t);
    }
}