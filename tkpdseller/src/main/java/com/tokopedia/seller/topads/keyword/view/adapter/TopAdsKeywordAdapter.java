package com.tokopedia.seller.topads.keyword.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.keyword.view.adapter.viewholder.TopAdsKeywordViewHolder;
import com.tokopedia.seller.topads.keyword.view.model.KeywordAd;
import com.tokopedia.seller.topads.view.adapter.TopAdsBaseListAdapter;

import java.util.List;

/**
 * @author normansyahputa on 5/19/17.
 */

public class TopAdsKeywordAdapter extends TopAdsBaseListAdapter<KeywordAd> {

    private Callback<KeywordAd> callback;

    public TopAdsKeywordAdapter(Callback<KeywordAd> callback) {
        this.callback = callback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case KeywordAd.TYPE:
                return new TopAdsKeywordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_top_ads_ad_main, parent, false));
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case KeywordAd.TYPE:
                TopAdsKeywordViewHolder itemHolder = (TopAdsKeywordViewHolder) holder;
                itemHolder.bindDataAds(data.get(position), callback);
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
            if (data.get(0) instanceof KeywordAd) {
                this.data.addAll(data);
                notifyDataSetChanged();
            }
        }
    }
}