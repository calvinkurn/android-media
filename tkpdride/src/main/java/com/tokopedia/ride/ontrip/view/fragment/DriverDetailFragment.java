package com.tokopedia.ride.ontrip.view.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.common.ride.domain.model.Driver;
import com.tokopedia.ride.common.ride.domain.model.RideRequest;
import com.tokopedia.ride.common.ride.domain.model.Vehicle;

import butterknife.BindView;
import butterknife.OnClick;

public class DriverDetailFragment extends BaseFragment {
    private static final String EXTRA_PARENT_TAG = "EXTRA_PARENT_TAG";
    private static final String EXTRA_DRIVER = "EXTRA_DRIVER";
    private static final String EXTRA_VEHICLE = "EXTRA_VEHICLE";
    private static final String EXTRA_TIME_EST = "EXTRA_TIME_EST";
    private static final String EXTRA_STATUS = "EXTRA_STATUS";
    private static final String EXTRA_SHARED = "EXTRA_SHARED";

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
    @BindView(R2.id.help_layout)
    RelativeLayout shareRideLayout;

    private Driver driver;
    private Vehicle vehicle;
    private int eta;
    private String status;

    private OnFragmentInteractionListener onFragmentInteractionListener;

    public DriverDetailFragment() {
        // Required empty public constructor
    }

    public static DriverDetailFragment newInstance(RideRequest rideRequest, String tag) {
        DriverDetailFragment fragment = new DriverDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_DRIVER, rideRequest.getDriver());
        bundle.putParcelable(EXTRA_VEHICLE, rideRequest.getVehicle());
        bundle.putFloat(EXTRA_TIME_EST, rideRequest.getPickup().getEta());
        bundle.putString(EXTRA_STATUS, rideRequest.getStatus());
        bundle.putString(EXTRA_PARENT_TAG, tag);
        bundle.putBoolean(EXTRA_SHARED, rideRequest.isShared());
        fragment.setArguments(bundle);
        return fragment;
    }

    public interface OnFragmentInteractionListener {
        void actionCancelRide();

        void actionShareEta();

        void actionContactDriver(String telp);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_driver_detail;
    }

//    private void setInitialVariable() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            Fragment getParentFragment = getParentFragment();
//            if (getParentFragment() instanceof OnFragmentInteractionListener) {
//                onFragmentInteractionListener = (OnFragmentInteractionListener) getParentFragment();
//            } else {
//                throw new RuntimeException("must implement OnFragmentInteractionListener");
//            }
//        }
//    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        String tag = getArguments().getString(EXTRA_PARENT_TAG);
        if (((Activity) activity).getFragmentManager().findFragmentByTag(tag) instanceof OnFragmentInteractionListener) {
            onFragmentInteractionListener = (OnFragmentInteractionListener) ((Activity) activity).getFragmentManager().findFragmentByTag(tag);
        } else {
            throw new RuntimeException("must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        String tag = getArguments().getString(EXTRA_PARENT_TAG);
        Fragment fragment = activity.getFragmentManager().findFragmentByTag(tag);
        if (activity.getFragmentManager().findFragmentByTag(tag) instanceof OnFragmentInteractionListener) {
            onFragmentInteractionListener = (OnFragmentInteractionListener) activity.getFragmentManager().findFragmentByTag(tag);
        } else {
            throw new RuntimeException("must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        driver = getArguments().getParcelable(EXTRA_DRIVER);
        vehicle = getArguments().getParcelable(EXTRA_VEHICLE);
        eta = (int) getArguments().getFloat(EXTRA_TIME_EST);
        status = getArguments().getString(EXTRA_STATUS);
        renderUi();
    }

    private void renderUi() {
        driverDetailLayoutLinearLayout.setVisibility(View.VISIBLE);
        driverNameTextView.setText(driver.getName());
        driverRatingTextView.setText(driver.getRating());

        if (status != null && (status.equalsIgnoreCase("accepted") || status.equalsIgnoreCase("arriving"))) {
            cancelRideLayout.setVisibility(View.VISIBLE);
            driverEtaTextView.setVisibility(View.VISIBLE);
            driverEtaTextView.setText(String.valueOf(eta) + " " + getResources().getString(R.string.minute) + " away");
        } else if (status != null && status.equalsIgnoreCase("in_progress")) {
            cancelRideLayout.setVisibility(View.GONE);
            driverEtaTextView.setVisibility(View.VISIBLE);
            driverEtaTextView.setText(getResources().getString(R.string.en_route));
        } else {
            cancelRideLayout.setVisibility(View.GONE);
            driverEtaTextView.setVisibility(View.GONE);
        }

        if (getArguments().getBoolean(EXTRA_SHARED)) {
            shareRideLayout.setVisibility(View.VISIBLE);
        } else {
            shareRideLayout.setVisibility(View.GONE);
        }

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
//        openCallIntent();
        onFragmentInteractionListener.actionContactDriver(driver.getPhoneNumber());
    }

    private void openCallIntent() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + driver.getPhoneNumber()));
        startActivity(callIntent);
    }

    private void openSmsIntent(){

    }

    @OnClick(R2.id.layout_cancel_ride)
    public void actionCancelRideBtnClicked() {
        onFragmentInteractionListener.actionCancelRide();
    }

    @OnClick(R2.id.help_layout)
    public void actionShareRideBtnClicked() {
        onFragmentInteractionListener.actionShareEta();
    }
}
