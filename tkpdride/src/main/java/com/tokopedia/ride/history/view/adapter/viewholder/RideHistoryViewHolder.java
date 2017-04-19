package com.tokopedia.ride.history.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.common.animator.RouteMapAnimator;
import com.tokopedia.ride.history.view.adapter.ItemClickListener;
import com.tokopedia.ride.history.view.viewmodel.RideHistoryViewModel;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by alvarisi on 4/11/17.
 */

public class RideHistoryViewHolder extends AbstractViewHolder<RideHistoryViewModel> implements OnMapReadyCallback {
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
    @BindView(R2.id.mapview)
    MapView mapView;

    GoogleMap googleMap;

    private final ItemClickListener mItemClickListener;

    public RideHistoryViewHolder(View itemView, ItemClickListener mItemClickListener) {
        super(itemView);
        this.mItemClickListener = mItemClickListener;
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void bind(RideHistoryViewModel element) {
        mItem = element;
        rideStartTimeTextView.setText(element.getRequestTime());
        driverCarDisplayNameTextView.setText(element.getDriverCarDisplay());
        rideFareTextView.setText(element.getFare());
        rideStatusTextView.setText(element.getStatus());
        if (element.getLatLngs() != null && element.getLatLngs().size() > 1) {
            RouteMapAnimator.getInstance().animateRoute(googleMap, element.getLatLngs());
        }
    }

    @OnClick(R2.id.container)
    public void actionItemClicked() {
        mItemClickListener.onHistoryClicked(mItem);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }
}
