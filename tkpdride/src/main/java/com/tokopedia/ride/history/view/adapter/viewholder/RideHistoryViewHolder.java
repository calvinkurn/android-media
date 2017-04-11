package com.tokopedia.ride.history.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.history.view.adapter.ItemClickListener;
import com.tokopedia.ride.history.view.viewmodel.RideHistoryViewModel;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by alvarisi on 4/11/17.
 */

public class RideHistoryViewHolder extends AbstractViewHolder<RideHistoryViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.row_ride_history;

    private RideHistoryViewModel mItem;

    @BindView(R2.id.tv_ride_start_time)
    TextView rideStartTimeTextView;
    @BindView(R2.id.tv_driver_car_display_name)
    TextView driverCarDisplayNameTextView;
    @BindView(R2.id.tv_ride_fare)
    TextView rideFareTextView;
    @BindView(R2.id.tv_ride_status)
    TextView rideStatusTextView;

    private final ItemClickListener mItemClickListener;

    public RideHistoryViewHolder(View itemView, ItemClickListener mItemClickListener) {
        super(itemView);
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public void bind(RideHistoryViewModel element) {
        mItem = element;
    }

    @OnClick(R2.id.container)
    public void actionItemClicked(){
        mItemClickListener.onHistoryClicked(mItem);
    }
}
