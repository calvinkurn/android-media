package com.tokopedia.ride.ontrip.view.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.pkmmte.view.CircularImageView;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.common.ride.domain.model.Driver;
import com.tokopedia.ride.common.ride.domain.model.RideRequest;
import com.tokopedia.ride.common.ride.domain.model.Vehicle;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class DriverDetailFragment extends BaseFragment {
    private static final String EXTRA_DRIVER = "EXTRA_DRIVER";
    private static final String EXTRA_VEHICLE = "EXTRA_VEHICLE";
    private static final String EXTRA_TIME_EST = "EXTRA_TIME_EST";

    @BindView(R2.id.cab_on_trip_container)
    LinearLayout driverDetailLayoutLinearLayout;
    @BindView(R2.id.cab_driver_image)
    ImageView driverImageView;
    @BindView(R2.id.tv_driver_name)
    TextView driverNameTextView;
    @BindView(R2.id.tv_cab_detail)
    TextView vehicleDetailTextView;
    @BindView(R2.id.rb_driver_rating)
    RatingBar driverRateRatingBar;
    @BindView(R2.id.tv_driver_rating)
    TextView driverRatingTextView;
    @BindView(R2.id.driver_eta_text)
    TextView driverEtaTextView;
    @BindView(R2.id.call_driver_layout)
    RelativeLayout callDriverLayout;
    @BindView(R2.id.layout_cancel_ride)
    RelativeLayout cancelRideLayout;
    @BindView(R2.id.tv_pool_status)
    TextView poolStatusTextView;

    private Driver driver;
    private Vehicle vehicle;
    private int eta;

    public DriverDetailFragment() {
        // Required empty public constructor
    }

    public static DriverDetailFragment newInstance(RideRequest rideRequest) {
        DriverDetailFragment fragment = new DriverDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_DRIVER, rideRequest.getDriver());
        bundle.putParcelable(EXTRA_VEHICLE, rideRequest.getVehicle());
        bundle.putInt(EXTRA_TIME_EST, rideRequest.getEta());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_driver_detail;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        driver = getArguments().getParcelable(EXTRA_DRIVER);
        vehicle = getArguments().getParcelable(EXTRA_VEHICLE);
        eta = getArguments().getInt(EXTRA_TIME_EST);
        setViewListener();
    }

    private void setViewListener() {
        driverDetailLayoutLinearLayout.setVisibility(View.VISIBLE);
        driverNameTextView.setText(driver.getName());
        driverRatingTextView.setText(driver.getRating());
        driverEtaTextView.setText(String.valueOf(eta));
        Glide.with(getActivity()).load(driver.getPictureUrl())
                .asBitmap()
                .centerCrop()
                .error(R.mipmap.ic_launcher)
                .into(new BitmapImageViewTarget(driverImageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable roundedBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                        roundedBitmapDrawable.setCircular(true);
                        driverImageView.setImageDrawable(roundedBitmapDrawable);
                    }
                });

        vehicleDetailTextView.setText(String.format("%s %s %s", vehicle.getMake(), vehicle.getVehicleModel(), vehicle.getLicensePlate()));
    }

    @OnClick(R2.id.call_driver_layout)
    public void actionCallDriver() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + driver.getPhoneNumber()));
        startActivity(callIntent);
    }
}
