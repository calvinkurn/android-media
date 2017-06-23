package com.tokopedia.seller.topads.keyword.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.seller.topads.keyword.view.adapter.viewholder.TopAdsKeywordGroupViewHolder;

/**
 * @author normansyahputa on 5/26/17.
 */

public class TopAdsKeywordGroupListAdapter extends BaseListAdapter<GroupAd> {
    public int getDataSize() {
        return data.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case GroupAd.TYPE:
                TopAdsKeywordGroupViewHolder topAdsKeywordGroupViewHolder
                        = new TopAdsKeywordGroupViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_group_name_item, parent, false));
                return topAdsKeywordGroupViewHolder;
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case GroupAd.TYPE:
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
        return data.get(position).getType();
    }

    public interface Listener {
        void notifySelect(GroupAd groupAd, int position);
    }
}