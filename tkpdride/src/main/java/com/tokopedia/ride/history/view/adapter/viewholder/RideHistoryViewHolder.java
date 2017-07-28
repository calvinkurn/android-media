package com.tokopedia.ride.history.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.common.configuration.RideStatus;
import com.tokopedia.ride.common.ride.utils.RideUtils;
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
    private final Context mContext;

    private RideHistoryViewModel mItem;

    @BindView(R2.id.tv_ride_start_time)
    TextView rideStartTimeTextView;
    @BindView(R2.id.tv_driver_car_display_name)
    TextView driverCarDisplayNameTextView;
    @BindView(R2.id.tv_ride_fare)
    TextView rideFareTextView;
    @BindView(R2.id.tv_ride_status)
    TextView rideStatusTextView;
    @BindView(R2.id.mapview)
    ImageView mapView;

    private final ItemClickListener mItemClickListener;

    public RideHistoryViewHolder(View parent, ItemClickListener mItemClickListener) {
        super(parent);
        mContext = parent.getContext();
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public void bind(RideHistoryViewModel element) {
        mItem = element;
        rideStartTimeTextView.setText(RideUtils.convertTime(element.getRequestTime()));
        driverCarDisplayNameTextView.setText(element.getDriverCarDisplay());
        rideFareTextView.setText(element.getTotalFare());
        if (element.getStatus().equalsIgnoreCase(RideStatus.COMPLETED)){
            rideStatusTextView.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        }else {
            rideStatusTextView.setTextColor(mContext.getResources().getColor(R.color.grey_600));
        }
        rideStatusTextView.setText(element.getDisplayStatus());

        Glide.with(mContext).load(element.getMapImage())
                .asBitmap()
                .centerCrop()
                .error(R.drawable.staticmap_dummy)
                .into(mapView);
    }

    @OnClick(R2.id.container)
    public void actionItemClicked() {
        mItemClickListener.onHistoryClicked(mItem);
    }
}
