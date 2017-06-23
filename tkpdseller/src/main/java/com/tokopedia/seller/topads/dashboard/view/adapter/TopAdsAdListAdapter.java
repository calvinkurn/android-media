package com.tokopedia.seller.topads.dashboard.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.topads.dashboard.view.model.Ad;
import com.tokopedia.seller.topads.dashboard.view.adapter.viewholder.TopAdsViewHolder;

/**
 * Created by zulfikarrahman on 11/24/16.
 */
public class TopAdsAdListAdapter<T extends Ad> extends BaseListAdapter<T> {

    private static final int AD_TYPE = 1;

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
                bindData(position, holder);
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
        return AD_TYPE;
    }
}