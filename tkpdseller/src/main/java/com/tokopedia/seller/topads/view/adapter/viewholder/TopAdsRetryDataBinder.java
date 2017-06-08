package com.tokopedia.seller.topads.view.adapter.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.tokopedia.core.customadapter.RetryDataBinder;
import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.seller.R;

/**
 * Created by Nisie on 2/26/16.
 */
public class TopAdsRetryDataBinder extends RetryDataBinder {
    private int errorDrawableRes;

    public TopAdsRetryDataBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
        this.errorDrawableRes = R.drawable.ic_error_network;
    }

    public TopAdsRetryDataBinder(DataBindAdapter dataBindAdapter, int errorDrawableRes) {
        super(dataBindAdapter);
        this.errorDrawableRes = errorDrawableRes;
    }

    public void setErrorDrawableRes(int errorDrawableRes) {
        this.errorDrawableRes = errorDrawableRes;
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_top_ads_network_error, null);
        ((ImageView) view.findViewById(R.id.image_error)).setImageResource(errorDrawableRes);
        if (parent.getMeasuredHeight() < parent.getMeasuredWidth()) {
            view.setLayoutParams(new AbsListView.LayoutParams(-1, parent.getMeasuredWidth()));
        } else {
            view.setLayoutParams(new AbsListView.LayoutParams(-1, parent.getMeasuredHeight()));
        }
        return new ViewHolder(view);
    }
}