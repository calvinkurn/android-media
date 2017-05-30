package com.tokopedia.seller.topads.keyword.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.data.model.data.GroupAd;
import com.tokopedia.seller.topads.keyword.view.adapter.viewholder.TopAdsKeywordGroupViewHolder;
import com.tokopedia.seller.topads.view.adapter.TopAdsAdListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by normansyahputa on 5/26/17.
 */

public class TopAdsKeywordGroupListAdapter extends TopAdsAdListAdapter {
    private List<GroupAd> data;
    private Listener listener;

    public TopAdsKeywordGroupListAdapter() {
        super();
        this.data = new ArrayList<>();
    }

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public int getDataSize() {
        return data.size();
    }

    @Override
    public int getItemCount() {
        return data.size() + super.getItemCount();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case GroupAd.TYPE:
                TopAdsKeywordGroupViewHolder topAdsKeywordGroupViewHolder
                        = new TopAdsKeywordGroupViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_group_name_item, parent, false));
                topAdsKeywordGroupViewHolder.setListener(listener);
                return topAdsKeywordGroupViewHolder;
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case GroupAd.TYPE:
                TopAdsKeywordGroupViewHolder itemHolder = (TopAdsKeywordGroupViewHolder) holder;
                itemHolder.bindData(data.get(position));
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    private boolean isLastItemPosition(int position) {
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
            return data.get(position).getType();
        }
    }

    @Override
    public void addData(List data) {
        if (data != null && data.size() > 0) {
            if (data.get(0) instanceof GroupAd) {
                this.data.addAll(data);
                notifyDataSetChanged();
            }
        }
    }

    public void clearData() {
        this.data.clear();
        notifyDataSetChanged();
    }

    public interface Listener {
        void notifySelect(GroupAd groupAd, int adapterPosition);

        boolean isSelection(GroupAd groupAd);
    }
}
