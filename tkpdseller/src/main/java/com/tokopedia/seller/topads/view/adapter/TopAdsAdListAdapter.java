package com.tokopedia.seller.topads.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.model.data.Ad;
import com.tokopedia.seller.topads.view.adapter.viewholder.TopAdsViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/24/16.
 */
public class TopAdsAdListAdapter<T extends Ad> extends BaseLinearRecyclerViewAdapter {

    public interface Callback {

        void onClicked(Ad ad);
    }

    public static final int AD_TYPE = 1;

    private List<T> data;
    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public TopAdsAdListAdapter() {
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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case AD_TYPE:
                return new TopAdsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_top_ads_ad, parent, false));
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case AD_TYPE:
                TopAdsViewHolder itemHolder = (TopAdsViewHolder) holder;
                bindDataAds(position, itemHolder);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    public void bindDataAds(final int position, RecyclerView.ViewHolder viewHolder) {
        final TopAdsViewHolder topAdsViewHolder = (TopAdsViewHolder) viewHolder;
        if (data.size() <= position) {
            return;
        }
        final Ad ad = data.get(position);
        topAdsViewHolder.bindObject(ad);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onClicked(ad);
                }
            }
        });
    }

    private boolean isLastItemPosition(int position) {
        return position == data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isLastItemPosition(position) && (data.isEmpty() || isLoading() || isRetry())) {
            return super.getItemViewType(position);
        } else {
            return AD_TYPE;
        }
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