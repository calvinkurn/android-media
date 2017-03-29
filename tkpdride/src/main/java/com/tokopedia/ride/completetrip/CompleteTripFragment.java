package com.tokopedia.ride.completetrip;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.base.presentation.BaseFragment;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompleteTripFragment extends BaseFragment {

    @BindView(R2.id.on_trip_complete_layout)
    RelativeLayout onTripCompleteLayout;
    @BindView(R2.id.layout_loader)
    LinearLayout loaderLayout;
    @BindView(R2.id.tv_total_fare)
    TextView totalFareTextView;
    @BindView(R2.id.iv_driver_pict)
    ImageView driverPictImageView;
    @BindView(R2.id.tv_driver_rating)
    TextView driverRatingTextView;
    @BindView(R2.id.tv_driver_name)
    TextView driverNameTextView;
    @BindView(R2.id.iv_vehicle_pict)
    ImageView vehiclePictImageView;
    @BindView(R2.id.tv_vehicle_license_number)
    TextView vehicleLicenseNumberTextView;
    @BindView(R2.id.tv_vehicle_desc)
    TextView vehicleDescTextView;
    @BindView(R2.id.tv_base_fare)
    TextView baseFareTextView;
    @BindView(R2.id.tv_label_distance)
    TextView labelDistanceTextView;
    @BindView(R2.id.tv_distance)
    TextView distanceTextView;
    @BindView(R2.id.label_time)
    TextView labelTimeTextView;
    @BindView(R2.id.tv_time)
    TextView timeTextView;
    @BindView(R2.id.tv_sub_total)
    TextView subTotalTextView;
    @BindView(R2.id.tv_total_charged)
    TextView totalChargedTextView;
    @BindView(R2.id.tv_sign_up_uber)
    TextView signUpUberTextView;


    public CompleteTripFragment() {
        // Required empty public constructor
    }

    public static CompleteTripFragment newInstance() {
        CompleteTripFragment fragment = new CompleteTripFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_complete_trip;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
