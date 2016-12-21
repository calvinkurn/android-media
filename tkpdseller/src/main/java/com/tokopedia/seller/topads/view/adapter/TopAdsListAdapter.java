package com.tokopedia.seller.topads.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.model.data.Ad;
import com.tokopedia.seller.topads.view.viewholder.TopAdsSingleViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/24/16.
 */
public class TopAdsListAdapter extends BaseLinearRecyclerViewAdapter {

    private final Context context;
    private List<Ad> data = new ArrayList<>();

    public TopAdsListAdapter(Context context, List<Ad> data) {
        super();
        this.context = context;
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data.size()
                + super.getItemCount();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TkpdState.RecyclerViewItemAd.AD_TYPE:
                return TopAdsSingleViewHolder.createInstance(context, parent);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TkpdState.RecyclerViewItemAd.AD_TYPE:
                TopAdsSingleViewHolder itemHolder = (TopAdsSingleViewHolder) holder;
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
            return super.getItemViewType(position);
        } else {
            return TkpdState.RecyclerViewItemAd.AD_TYPE;
        }
    }
}
