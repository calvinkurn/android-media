package com.tokopedia.seller.topads.keyword.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.keyword.view.adapter.viewholder.TopAdsKeywordViewHolder;
import com.tokopedia.seller.topads.keyword.domain.model.Datum;
import com.tokopedia.seller.topads.view.adapter.TopAdsAdListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author normansyahputa on 5/19/17.
 */

public class TopAdsKeywordAdapter extends TopAdsAdListAdapter {
    private List<Datum> data;
    private TopAdsAdListAdapter.Callback callback;

    public TopAdsKeywordAdapter() {
        super();
        this.data = new ArrayList<>();
    }

    public void setCallback(TopAdsAdListAdapter.Callback callback) {
        this.callback = callback;
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
            case Datum.TYPE:
                return new TopAdsKeywordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_top_ads_ad_main, parent, false));
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case Datum.TYPE:
                TopAdsKeywordViewHolder itemHolder = (TopAdsKeywordViewHolder) holder;
                itemHolder.bindDataAds(data.get(position));
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
            if (data.get(0) instanceof Datum) {
                this.data.addAll(data);
                notifyDataSetChanged();
            }
        }
    }

    public void clearData() {
        this.data.clear();
        notifyDataSetChanged();
    }

}
