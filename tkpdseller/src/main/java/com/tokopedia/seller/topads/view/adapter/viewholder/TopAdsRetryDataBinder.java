package com.tokopedia.seller.topads.view.adapter.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.tokopedia.core.customadapter.RetryDataBinder;
import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.seller.R;

/**
 * Created by Nisie on 2/26/16.
 */
public class TopAdsRetryDataBinder extends RetryDataBinder {

    public TopAdsRetryDataBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_top_ads_network_error, null);
        if (parent.getMeasuredHeight() < parent.getMeasuredWidth()) {
            view.setLayoutParams(new AbsListView.LayoutParams(-1, parent.getMeasuredWidth()));
        } else {
            view.setLayoutParams(new AbsListView.LayoutParams(-1, parent.getMeasuredHeight()));
        }
        return new ViewHolder(view);
    }
}