package com.tokopedia.seller.topads.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.core.customadapter.BaseRecyclerViewAdapter;
import com.tokopedia.core.var.RecyclerViewItem;
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
        switch (viewType){
            case Ad.AD_TYPE:
                return new PromoSingleViewHolder(LayoutInflater.from(context).inflate(com.tokopedia.core.R.layout.listview_product_item_list, parent, false));
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
