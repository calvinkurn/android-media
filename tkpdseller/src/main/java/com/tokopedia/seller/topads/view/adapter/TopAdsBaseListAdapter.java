package com.tokopedia.seller.topads.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.data.model.data.Ad;
import com.tokopedia.seller.topads.view.adapter.viewholder.TopAdsViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/24/16.
 */
public abstract class TopAdsBaseListAdapter<T> extends BaseLinearRecyclerViewAdapter {

    public interface Callback<T> {

        void onClicked(T t);
    }

    public static final int UNKNOWN_TYPE = Integer.MIN_VALUE;

    protected List<T> data;
    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public TopAdsBaseListAdapter() {
        super();
        this.data = new ArrayList<>();
    }

    public int getDataSize() {
        return data.size();
    }

    @Override
    public int getItemCount() {
        return data.size() + super.getItemCount();
    }

    public void bindData(final int position, RecyclerView.ViewHolder viewHolder) {
        if (data.size() <= position) {
            return;
        }
        final T t = data.get(position);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onClicked(t);
                }
            }
        });
    }

    protected boolean isLastItemPosition(int position) {
        return position == data.size();
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
            return UNKNOWN_TYPE;
        }
    }

    protected boolean isUnknownViewType(int itemType) {
        return itemType == UNKNOWN_TYPE;
    }

    public void addData(List<T> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.data.clear();
        notifyDataSetChanged();
    }
}