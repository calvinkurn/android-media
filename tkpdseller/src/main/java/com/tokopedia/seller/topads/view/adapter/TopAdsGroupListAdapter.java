package com.tokopedia.seller.topads.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.CompoundButton;

import com.tokopedia.seller.topads.model.data.GroupAd;
import com.tokopedia.seller.topads.view.listener.TopAdsListPromoViewListener;
import com.tokopedia.seller.topads.view.viewholder.TopAdsSingleViewHolder;

import java.util.List;

/**
 * Created by zulfikarrahman on 12/14/16.
 */

public class TopAdsGroupListAdapter extends TopAdsListAdapter<GroupAd> {
    public TopAdsGroupListAdapter(Context context, List<GroupAd> data, TopAdsListPromoViewListener topAdsListPromoViewListener) {
        super(context, data, topAdsListPromoViewListener);
    }

    @Override
    public int getAdType() {
        return TopAdsListAdapter.AD_GROUP_TYPE;
    }

    @Override
    public void bindDataAds(final int position, RecyclerView.ViewHolder viewHolder) {
        GroupAd groupAd = data.get(position);
        TopAdsSingleViewHolder topAdsSingleViewHolder = (TopAdsSingleViewHolder) viewHolder;
        topAdsSingleViewHolder.titleProduct.setText(groupAd.getGroupName());
        topAdsSingleViewHolder.statusActive.setText(String.valueOf(groupAd.getGroupStatus()));
        topAdsSingleViewHolder.promoPriceUsed.setText(groupAd.getGroupPriceBidFmt());
        topAdsSingleViewHolder.totalPricePromo.setText(groupAd.getGroupPriceDailyFmt());
        topAdsSingleViewHolder.checkedPromo.setChecked(isChecked(position));
        topAdsSingleViewHolder.checkedPromo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setChecked(position, isChecked);
            }
        });
    }
}
