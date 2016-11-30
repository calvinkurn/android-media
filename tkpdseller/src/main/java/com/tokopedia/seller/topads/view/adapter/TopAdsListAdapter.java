package com.tokopedia.seller.topads.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.core.customadapter.BaseRecyclerViewAdapter;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.model.data.Ad;
import com.tokopedia.seller.topads.view.viewholder.PromoSingleViewHolder;

import java.util.List;

/**
 * Created by zulfikarrahman on 11/24/16.
 */
public class TopAdsListAdapter extends BaseRecyclerViewAdapter {

    public TopAdsListAdapter(Context context, List<RecyclerViewItem> data) {
        super(context, data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TkpdState.RecyclerViewItemAd.AD_TYPE:
                return new PromoSingleViewHolder(LayoutInflater.from(context).inflate(R.layout.list_promo_single_topads, parent, false));
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TkpdState.RecyclerViewItemAd.AD_TYPE:
                PromoSingleViewHolder itemHolder = (PromoSingleViewHolder) holder;
                itemHolder.bindData((Ad) data.get(position));
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (checkDataSize(position)) {
            RecyclerViewItem recyclerViewItem = data.get(position);
            return isInType(recyclerViewItem);
        } else {
            return super.getItemViewType(position);
        }
    }

    /**
     * check if position is in range with data size
     *
     * @param position
     * @return true whether is in range of data
     */
    private boolean checkDataSize(int position) {
        return data != null && data.size() > 0 && position > -1 && position < data.size();
    }

    /**
     * this is for registered type
     *
     * @param recyclerViewItem
     * @return
     */
    protected int isInType(RecyclerViewItem recyclerViewItem) {
        switch (recyclerViewItem.getType()) {
            case TkpdState.RecyclerViewItemAd.AD_TYPE:
                return recyclerViewItem.getType();
            default:
                return -1;
        }
    }
}
