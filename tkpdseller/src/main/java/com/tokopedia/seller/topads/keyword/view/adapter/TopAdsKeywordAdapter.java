package com.tokopedia.seller.topads.keyword.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.keyword.view.adapter.viewholder.TopAdsKeywordViewHolder;
import com.tokopedia.seller.topads.keyword.domain.model.Datum;
import com.tokopedia.seller.topads.view.adapter.TopAdsAdListAdapter;
import com.tokopedia.seller.topads.view.adapter.TopAdsBaseListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author normansyahputa on 5/19/17.
 */

public class TopAdsKeywordAdapter extends TopAdsBaseListAdapter<Datum> {

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

    @Override
    public int getItemViewType(int position) {
        int itemType = super.getItemViewType(position);
        if (!isUnknownViewType(itemType)) {
            return itemType;
        }
        return data.get(position).getType();
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
}