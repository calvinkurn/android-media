package com.tokopedia.ride.ontrip.view.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.analytics.RideGATracking;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.bookingride.view.activity.SMSChatActivity;
import com.tokopedia.ride.common.configuration.PaymentMode;
import com.tokopedia.ride.common.configuration.RideStatus;
import com.tokopedia.ride.common.ride.domain.model.Driver;
import com.tokopedia.ride.common.ride.domain.model.LocationLatLng;
import com.tokopedia.ride.common.ride.domain.model.RideRequest;
import com.tokopedia.ride.common.ride.domain.model.Vehicle;

import butterknife.BindView;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class DriverDetailFragment extends BaseFragment {
    private static final String EXTRA_PARENT_TAG = "EXTRA_PARENT_TAG";
    private static final String EXTRA_DRIVER = "EXTRA_DRIVER";
    private static final String EXTRA_VEHICLE = "EXTRA_VEHICLE";
    private static final String EXTRA_DESTINATION = "EXTRA_DESTINATION";
    private static final String EXTRA_TIME_EST = "EXTRA_TIME_EST";
    private static final String EXTRA_STATUS = "EXTRA_STATUS";
    private static final String EXTRA_SHARED = "EXTRA_SHARED";
    private static final String EXTRA_PAYMENT_METHOD = "EXTRA_PAYMENT_METHOD";

    @BindView(R2.id.cab_on_trip_container)
    LinearLayout driverDetailLayoutLinearLayout;
    @BindView(R2.id.cab_driver_image)
    ImageView driverImageView;
    @BindView(R2.id.tv_driver_name)
    TextView driverNameTextView;
    @BindView(R2.id.tv_cab_detail)
    TextView vehicleDetailTextView;
    @BindView(R2.id.tv_cab_plate)
    TextView vehiclePlateTextView;
    @BindView(R2.id.tv_driver_rating)
    TextView driverRatingTextView;
    @BindView(R2.id.driver_eta_text)
    TextView driverEtaTextView;
    @BindView(R2.id.layout_cancel_ride)
    LinearLayout cancelRideLayout;
    @BindView(R2.id.tv_pool_status)
    TextView poolStatusTextView;
    @BindView(R2.id.help_layout)
    LinearLayout shareRideLayout;
    @BindView(R2.id.tv_payment_method)
    TextView paymentMethodTextView;
    @BindView(R2.id.image_payment_method_icon)
    ImageView paymentIconImageView;

    private Driver driver;
    private Vehicle vehicle;
    private LocationLatLng destination;
    private int eta;
    private String status;
    private String paymentMethod;


    private OnFragmentInteractionListener onFragmentInteractionListener;

    public DriverDetailFragment() {
        // Required empty public constructor
    }

    public static DriverDetailFragment newInstance(RideRequest rideRequest, String tag) {
        DriverDetailFragment fragment = new DriverDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_DRIVER, rideRequest.getDriver());
        bundle.putParcelable(EXTRA_VEHICLE, rideRequest.getVehicle());
        bundle.putParcelable(EXTRA_DESTINATION, rideRequest.getDestination());
        bundle.putFloat(EXTRA_TIME_EST, rideRequest.getPickup().getEta());
        bundle.putString(EXTRA_STATUS, rideRequest.getStatus());
        bundle.putString(EXTRA_PARENT_TAG, tag);
        bundle.putBoolean(EXTRA_SHARED, rideRequest.isShared());
        bundle.putString(EXTRA_PAYMENT_METHOD, rideRequest.getPayment().getPaymentMethod());
        fragment.setArguments(bundle);
        return fragment;
    }

    public interface OnFragmentInteractionListener {
        void actionCancelRide();

        void actionShareEta();
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_driver_detail;
    }

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
        destination = getArguments().getParcelable(EXTRA_DESTINATION);
        eta = (int) getArguments().getFloat(EXTRA_TIME_EST);
        status = getArguments().getString(EXTRA_STATUS);
        paymentMethod = getArguments().getString(EXTRA_PAYMENT_METHOD);
        renderUi();
    }

    private void renderUi() {
        driverDetailLayoutLinearLayout.setVisibility(View.VISIBLE);
        driverNameTextView.setText(driver.getName());
        driverRatingTextView.setText(driver.getRating());
        paymentMethodTextView.setText(getString(R.string.str_payment_with) + " " + paymentMethod);
        paymentIconImageView.setImageResource(paymentMethod.equalsIgnoreCase(PaymentMode.WALLET_DISPLAY_NAME) ? R.drawable.tokocash : R.drawable.cc_image);

        if (status != null && (status.equalsIgnoreCase(RideStatus.ACCEPTED) || status.equalsIgnoreCase(RideStatus.ARRIVING))) {
            cancelRideLayout.setVisibility(View.VISIBLE);
            driverEtaTextView.setVisibility(View.VISIBLE);
            driverEtaTextView.setText(String.format("%s %s away", String.valueOf(eta), getResources().getString(eta > 1 ? R.string.minutes : R.string.minute)));
        } else if (status != null && status.equalsIgnoreCase(RideStatus.IN_PROGRESS)) {
            cancelRideLayout.setVisibility(View.GONE);
            driverEtaTextView.setVisibility(View.VISIBLE);
            int duration = (int) destination.getEta();
            driverEtaTextView.setText(String.format("ETA %s", duration > 1 ? duration + getString(R.string.mins) : duration + getString(R.string.min)));
        } else if (status != null && status.equalsIgnoreCase(RideStatus.COMPLETED)) {
            cancelRideLayout.setVisibility(View.GONE);
            driverEtaTextView.setVisibility(View.VISIBLE);
            driverEtaTextView.setText(R.string.receipt_pending);
        } else {
            cancelRideLayout.setVisibility(View.GONE);
            driverEtaTextView.setVisibility(View.GONE);
        }

        shareRideLayout.setVisibility(View.VISIBLE);
        Glide.with(getActivity()).load(driver.getPictureUrl())
                .asBitmap()
                .centerCrop()
                .error(R.drawable.default_user_pic_light)
                .into(new BitmapImageViewTarget(driverImageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        if (getActivity() != null && !getActivity().isFinishing()) {
                            RoundedBitmapDrawable roundedBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getResources(), resource);
                            roundedBitmapDrawable.setCircular(true);
                            driverImageView.setImageDrawable(roundedBitmapDrawable);
                        }
                    }
                });

        vehicleDetailTextView.setText(String.format("%s %s", vehicle.getMake(), vehicle.getVehicleModel()));
        vehiclePlateTextView.setText(vehicle.getLicensePlate());
    }

    @OnClick(R2.id.icon_call)
    public void actionCallDriver() {
        RideGATracking.eventClickCall(status);
        DriverDetailFragmentPermissionsDispatcher.openCallIntentWithCheck(this, driver.getPhoneNumber());
    }

    @OnClick(R2.id.icon_message)
    public void actionSMSDriver() {
        RideGATracking.eventClickSMS(status);

        /*Log.e("Driver phone no", driver.getPhoneNumber());
        if (driver.getSmsNumber() != null && !driver.getSmsNumber().isEmpty())
            Log.e("Driver sms phone no", driver.getSmsNumber());
        Log.e("Driver pic url", driver.getPictureUrl());*/

//        driver.setPhoneNumber("+919896386254");
        Intent intent = new Intent(getActivity(), SMSChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(SMSChatActivity.DRIVER_INFO, driver);
        bundle.putParcelable(SMSChatActivity.VEHICLE_INFO, vehicle);
        intent.putExtras(bundle);
        startActivity(intent);
//        openSmsIntent(driver.getPhoneNumber());
    }

    @NeedsPermission({Manifest.permission.CALL_PHONE})
    public void openCallIntent(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(callIntent);
    }

    public void openSmsIntent(String smsNumber) {
        if (!TextUtils.isEmpty(smsNumber)) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.fromParts("sms", smsNumber, null))
            );
        }
    }

    /**
     * This function set the trip status
     *
     * @param message
     */
    public void setStatus(String message) {
        if (driverEtaTextView != null) {
            driverEtaTextView.setText(message);
        }
    }

    @OnClick(R2.id.layout_cancel_ride)
    public void actionCancelRideBtnClicked() {
        RideGATracking.eventClickCancel(status);
        onFragmentInteractionListener.actionCancelRide();
    }

    @OnClick(R2.id.help_layout)
    public void actionShareRideBtnClicked() {
        RideGATracking.eventClickShareEta(status);
        onFragmentInteractionListener.actionShareEta();
    }
}