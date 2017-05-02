package com.tokopedia.seller.topads.view.adapter.viewholder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.view.ViewGroup;

import com.tokopedia.core.customadapter.RetryDataBinder;
import com.tokopedia.core.util.DataBindAdapter;

/**
 * Created by Nisie on 2/26/16.
 */
public class TopAdsWhiteRetryDataBinder extends TopAdsRetryDataBinder {

    public TopAdsWhiteRetryDataBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public RetryDataBinder.ViewHolder newViewHolder(ViewGroup parent) {
        RetryDataBinder.ViewHolder viewHolder = super.newViewHolder(parent);
        Context context = viewHolder.itemView.getContext();
        viewHolder.itemView.setBackgroundColor(
                ContextCompat.getColor(context, com.tokopedia.core.R.color.white) );
        return viewHolder;
    }

}