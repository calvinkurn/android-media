package com.tokopedia.ride.ontrip.view.fragment;


import android.Manifest;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.pkmmte.view.CircularImageView;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.bookingride.view.viewmodel.ConfirmBookingViewModel;
import com.tokopedia.ride.common.configuration.RideConfiguration;
import com.tokopedia.ride.common.ride.domain.model.RideRequest;
import com.tokopedia.ride.completetrip.CompleteTripActivity;
import com.tokopedia.ride.ontrip.di.OnTripDependencyInjection;
import com.tokopedia.ride.ontrip.domain.CancelRideRequestUseCase;
import com.tokopedia.ride.ontrip.domain.CreateRideRequestUseCase;
import com.tokopedia.ride.ontrip.service.GetCurrentRideRequestService;
import com.tokopedia.ride.ontrip.view.OnTripActivity;
import com.tokopedia.ride.ontrip.view.OnTripMapContract;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

import static com.tokopedia.core.network.retrofit.utils.AuthUtil.md5;
import static com.tokopedia.ride.ontrip.view.OnTripActivity.TASK_TAG_PERIODIC;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnTripMapFragment extends BaseFragment implements OnTripMapContract.View, OnMapReadyCallback {
    private static final int REQUEST_CODE_TOS_CONFIRM_DIALOG = 1005;
    public static final String EXTRA_RIDE_REQUEST_RESULT = "EXTRA_RIDE_REQUEST_RESULT";
    OnTripMapContract.Presenter presenter;
    ConfirmBookingViewModel confirmBookingViewModel;
    GoogleMap mGoogleMap;
    RideConfiguration rideConfiguration;
    private GcmNetworkManager mGcmNetworkManager;
    private BroadcastReceiver mReceiver;

    @BindView(R2.id.mapview)
    MapView mapView;
    @BindView(R2.id.crux_cabs_source)
    TextView tvSource;
    @BindView(R2.id.crux_cabs_destination)
    TextView tvDestination;
    @BindView(R2.id.layout_src_destination)
    View mSrcDestLayout;
    @BindView(R2.id.iv_my_location_button)
    ImageView myLocationButton;
    @BindView(R2.id.layout_loader)
    LinearLayout loaderLayout;
    @BindView(R2.id.cab_processing_layout)
    RelativeLayout processingLayout;
    @BindView(R2.id.crux_cabs_uber_processing_note)
    TextView processingDescription;
    @BindView(R2.id.cabs_processing_cancel_button)
    LinearLayout cancelButton;
    @BindView(R2.id.bottom_container)
    LinearLayout bottomContainer;

    OnFragmentInteractionListener onFragmentInteractionListener;

    public static OnTripMapFragment newInstance(Bundle bundle) {
        OnTripMapFragment fragment = new OnTripMapFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        setMapViewListener();
    }

    public static OnTripMapFragment newInstance() {
        return new OnTripMapFragment();
    }

    public interface OnFragmentInteractionListener {
        void actionCancelBooking();
    }

    public OnTripMapFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_on_trip_map;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rideConfiguration = new RideConfiguration();

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        mGcmNetworkManager = GcmNetworkManager.getInstance(getActivity());

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(GetCurrentRideRequestService.ACTION_DONE)) {
                    RideRequest result = intent.getParcelableExtra(GetCurrentRideRequestService.EXTRA_RESULT);
                    Toast.makeText(context, "get detail response", Toast.LENGTH_SHORT).show();
                    presenter.proccessGetCurrentRideRequest(result);
                }
            }
        };

        presenter = OnTripDependencyInjection.createOnTripMapPresenter(getActivity());
        presenter.attachView(this);
        if (getArguments() != null) {
            confirmBookingViewModel = getArguments().getParcelable(OnTripActivity.EXTRA_CONFIRM_BOOKING);
        }
        presenter.initialize();
        setViewListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(GetCurrentRideRequestService.ACTION_DONE);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getActivity());
        manager.registerReceiver(mReceiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getActivity());
        manager.unregisterReceiver(mReceiver);
        mGcmNetworkManager.cancelAllTasks(GetCurrentRideRequestService.class);
    }

    private void setViewListener() {
        if (confirmBookingViewModel != null) {
            tvSource.setText(confirmBookingViewModel.getSource().getTitle());
            tvDestination.setText(confirmBookingViewModel.getDestination().getTitle());
        } else {
            tvSource.setText(rideConfiguration.getActiveSource().getTitle());
            tvDestination.setText(rideConfiguration.getActiveDestination().getTitle());
        }
    }

    @Override
    public RequestParams getParam() {
        String deviceId = GCMHandler.getRegistrationId(getActivity());
        String userId = SessionHandler.getLoginID(getActivity());
        String hash = md5(userId + "~" + deviceId);
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(CreateRideRequestUseCase.PARAM_FARE_ID, confirmBookingViewModel.getFareId());
        requestParams.putString(CreateRideRequestUseCase.PARAM_START_LATITUDE, String.valueOf(confirmBookingViewModel.getSource().getLatitude()));
        requestParams.putString(CreateRideRequestUseCase.PARAM_START_LONGITUDE, String.valueOf(confirmBookingViewModel.getSource().getLongitude()));
        requestParams.putString(CreateRideRequestUseCase.PARAM_END_LATITUDE, String.valueOf(confirmBookingViewModel.getDestination().getLatitude()));
        requestParams.putString(CreateRideRequestUseCase.PARAM_END_LONGITUDE, String.valueOf(confirmBookingViewModel.getDestination().getLongitude()));
        requestParams.putString(CreateRideRequestUseCase.PARAM_USER_ID, userId);
        requestParams.putString(CreateRideRequestUseCase.PARAM_DEVICE_ID, deviceId);
        requestParams.putString(CreateRideRequestUseCase.PARAM_HASH, hash);
        requestParams.putString(CreateRideRequestUseCase.PARAM_OS_TYPE, "1");
        requestParams.putString(CreateRideRequestUseCase.PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));
        return requestParams;
    }

    @Override
    public boolean isWaitingResponse() {
        return rideConfiguration.isWaitingDriverState();
    }

    @Override
    public void showLoadingWaitingResponse() {
        processingLayout.setVisibility(View.VISIBLE);
        processingDescription.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingWaitingResponse() {
        processingLayout.setVisibility(View.GONE);
        processingDescription.setVisibility(View.GONE);
    }

    @Override
    public void showFailedRideRequestMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void moveToCurrentLocation(double latitude, double longitude) {
        LatLng currentLocation = new LatLng(latitude, longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation, 18);
        mGoogleMap.animateCamera(cameraUpdate);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            onFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException("must implement fragment listener");
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            onFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException("must implement fragment listener");
        }
    }

    private void setMapViewListener() {
        MapsInitializer.initialize(this.getActivity());
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mGoogleMap.setMyLocationEnabled(false);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    @OnClick(R2.id.iv_my_location_button)
    public void actionMyLocationButtonClicked() {
        presenter.goToMyLocation();
    }

    @OnClick(R2.id.cabs_processing_cancel_button)
    public void actionCancelButtonClicked() {
        presenter.actionCancelRide();
    }


    @Override
    public RequestParams getCancelParams() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(CancelRideRequestUseCase.PARAM_REQUEST_ID, rideConfiguration.getActiveRequest());
        return requestParams;
    }

    @Override
    public void hideCancelRequestButton() {
        cancelButton.setVisibility(View.GONE);
    }

    @Override
    public void showCancelRequestButton() {
        cancelButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void navigateToBack() {
        onFragmentInteractionListener.actionCancelBooking();
    }

    @Override
    public void openTosConfirmationWebView(String tosUrl) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        android.app.Fragment previousDialog = getFragmentManager().findFragmentByTag("tos_dialog");
        if (previousDialog != null) {
            fragmentTransaction.remove(previousDialog);
        }
        fragmentTransaction.addToBackStack(null);
        DialogFragment dialogFragment = TosConfirmationDialogFragment.newInstance(tosUrl);
        dialogFragment.setTargetFragment(this, REQUEST_CODE_TOS_CONFIRM_DIALOG);
        dialogFragment.show(getFragmentManager().beginTransaction(), "tos_dialog");
    }

    @Override
    public void failedToRequestRide() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_TOS_CONFIRM_DIALOG:
                if (resultCode == Activity.RESULT_OK) {
                    String id = data.getStringExtra(TosConfirmationDialogFragment.EXTRA_ID);
                    presenter.actionRetryRideRequest(id);
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(getActivity(), "User doenst accept tos", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    @Override
    public void startPeriodicService(String requestId) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_RIDE_REQUEST_RESULT, requestId);
        PeriodicTask task = new PeriodicTask.Builder()
                .setService(GetCurrentRideRequestService.class)
                .setTag(TASK_TAG_PERIODIC)
                .setExtras(bundle)
                .setPeriod(5L)
                .build();
        mGcmNetworkManager.schedule(task);
    }

    @Override
    public String getRequestId() {
        return rideConfiguration.getActiveRequest();
    }

    @Override
    public void onSuccessCreateRideRequest(RideRequest rideRequest) {
        rideConfiguration.setActiveSource(confirmBookingViewModel.getSource());
        rideConfiguration.setActiveDestination(confirmBookingViewModel.getDestination());
    }

    @Override
    public void renderAcceptedRequest(RideRequest result) {
        bottomContainer.setVisibility(View.VISIBLE);
        addFragment(R.id.bottom_container, DriverDetailFragment.newInstance(result));
    }

    private void addFragment(int containerViewId, android.app.Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void renderInProgressRequest(RideRequest result) {

    }

    @Override
    public void renderDriverCanceledRequest(RideRequest result) {

    }

    @Override
    public void renderCompletedRequest(RideRequest result) {
        Intent intent = CompleteTripActivity.getCallingIntent(getActivity());
        startActivity(intent);
    }
}
