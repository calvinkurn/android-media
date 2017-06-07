package com.tokopedia.seller.topads.keyword.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.keyword.view.adapter.viewholder.TopAdsKeywordViewHolder;
import com.tokopedia.seller.topads.keyword.view.model.KeywordAd;
import com.tokopedia.seller.topads.keyword.view.model.NegativeKeywordAd;
import com.tokopedia.seller.topads.view.adapter.TopAdsBaseListAdapter;
import com.tokopedia.seller.topads.view.model.Ad;

/**
 * @author normansyahputa on 5/19/17.
 */

public class TopAdsKeywordAdapter extends TopAdsBaseListAdapter<KeywordAd> {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case KeywordAd.TYPE:
            case NegativeKeywordAd.TYPE:
                return new TopAdsKeywordViewHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.listview_top_ads_keyword_main, parent, false));
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case KeywordAd.TYPE:
            case NegativeKeywordAd.TYPE:
                TopAdsKeywordViewHolder itemHolder = (TopAdsKeywordViewHolder) holder;
                bindData(position, itemHolder);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    @Override
    public void bindData(final int position, RecyclerView.ViewHolder viewHolder) {
        super.bindData(position, viewHolder);
        final TopAdsKeywordViewHolder topAdsViewHolder = (TopAdsKeywordViewHolder) viewHolder;
        if (data.size() <= position) {
            return;
        }
        final Ad ad = data.get(position);
        topAdsViewHolder.bindObject(data.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        int itemType = super.getItemViewType(position);
        if (!isUnknownViewType(itemType)) {
            return itemType;
        }
        return data.get(position).getType();
    }
}