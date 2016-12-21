package com.tokopedia.seller.topads.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.CompoundButton;

import com.tokopedia.seller.topads.model.data.Ad;
import com.tokopedia.seller.topads.view.listener.TopAdsListPromoViewListener;
import com.tokopedia.seller.topads.view.viewholder.TopAdsSingleViewHolder;

import java.util.List;

/**
 * Created by zulfikarrahman on 12/14/16.
 */

public class TopAdsSingleListAdapter extends TopAdsListAdapter<Ad> {

    public TopAdsSingleListAdapter(Context context, List<Ad> data, TopAdsListPromoViewListener topAdsListPromoViewListener) {
        super(context, data, topAdsListPromoViewListener);
    }

    @Override
    public int getAdType() {
        return TopAdsListAdapter.AD_SINGLE_TYPE;
    }

    @Override
    public void bindDataAds(Ad ad, RecyclerView.ViewHolder viewHolder) {
        TopAdsSingleViewHolder topAdsSingleViewHolder = (TopAdsSingleViewHolder) viewHolder;
        topAdsSingleViewHolder.titleProduct.setText(ad.getProductName());
        topAdsSingleViewHolder.statusActive.setText(String.valueOf(ad.getAdStatus()));
        topAdsSingleViewHolder.promoPriceUsed.setText(ad.getAdPriceBidFmt());
        topAdsSingleViewHolder.totalPricePromo.setText(ad.getAdPriceDailySpentFmt());
        topAdsSingleViewHolder.checkedPromo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }
}
