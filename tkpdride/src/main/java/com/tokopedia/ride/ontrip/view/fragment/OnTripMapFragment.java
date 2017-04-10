package com.tokopedia.ride.ontrip.view.fragment;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.bookingride.view.viewmodel.ConfirmBookingViewModel;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;
import com.tokopedia.ride.common.configuration.RideConfiguration;
import com.tokopedia.ride.common.ride.domain.model.RideRequest;
import com.tokopedia.ride.completetrip.view.CompleteTripActivity;
import com.tokopedia.ride.ontrip.di.OnTripDependencyInjection;
import com.tokopedia.ride.ontrip.domain.CancelRideRequestUseCase;
import com.tokopedia.ride.ontrip.domain.CreateRideRequestUseCase;
import com.tokopedia.ride.ontrip.service.GetCurrentRideRequestService;
import com.tokopedia.ride.ontrip.view.OnTripActivity;
import com.tokopedia.ride.ontrip.view.OnTripMapContract;
import com.tokopedia.ride.ontrip.view.viewmodel.DriverVehicleViewModel;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.tokopedia.core.network.retrofit.utils.AuthUtil.md5;
import static com.tokopedia.ride.ontrip.view.OnTripActivity.TASK_TAG_PERIODIC;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnTripMapFragment extends BaseFragment implements OnTripMapContract.View, OnMapReadyCallback {
    private static final int REQUEST_CODE_TOS_CONFIRM_DIALOG = 1005;
    private static final int REQUEST_CODE_DRIVER_NOT_FOUND = 1006;
    public static final String EXTRA_RIDE_REQUEST_RESULT = "EXTRA_RIDE_REQUEST_RESULT";

    private final int FINDING_UBER_NOTIFICATION_ID = 0001;
    private final int ACCEPTED_UBER_NOTIFICATION_ID = 0002;


    OnTripMapContract.Presenter presenter;
    ConfirmBookingViewModel confirmBookingViewModel;
    GoogleMap mGoogleMap;
    private Marker mDriverMarker;
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
    FrameLayout bottomContainer;

    OnFragmentInteractionListener onFragmentInteractionListener;

    private NotificationManager mNotifyMgr;
    private Notification acceptedNotification;

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

        void actionNoDriverAvailable();
    }

    public OnTripMapFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_on_trip_map;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            confirmBookingViewModel = getArguments().getParcelable(OnTripActivity.EXTRA_CONFIRM_BOOKING);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rideConfiguration = new RideConfiguration();

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        mGcmNetworkManager = GcmNetworkManager.getInstance(getActivity());

        // Gets an instance of the NotificationManager service
        mNotifyMgr = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);

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
        System.out.println("Vishal OnStop");
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
        loaderLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingWaitingResponse() {
        processingLayout.setVisibility(View.GONE);
        loaderLayout.setVisibility(View.GONE);
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

        if (confirmBookingViewModel != null) {
            presenter.getOverViewPolyLine(
                    confirmBookingViewModel.getSource().getLatitude(),
                    confirmBookingViewModel.getSource().getLongitude(),
                    confirmBookingViewModel.getDestination().getLatitude(),
                    confirmBookingViewModel.getDestination().getLongitude()
            );
        } else {
            presenter.getOverViewPolyLine(
                    rideConfiguration.getActiveSource().getLatitude(),
                    rideConfiguration.getActiveSource().getLongitude(),
                    rideConfiguration.getActiveDestination().getLatitude(),
                    rideConfiguration.getActiveDestination().getLongitude()
            );
        }

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
        getActivity().setResult(OnTripActivity.RIDE_BOOKING_RESULT_CODE);
        getActivity().finish();
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
            case REQUEST_CODE_DRIVER_NOT_FOUND:
                if (resultCode == DriverNotFoundDialogFragment.BOOK_AGAIN_RESULT_CODE) {
                    getActivity().setResult(OnTripActivity.RIDE_HOME_RESULT_CODE);
                    getActivity().finish();
                } else {
                    getActivity().setResult(OnTripActivity.RIDE_HOME_RESULT_CODE);
                    getActivity().finish();
                }
        }
    }


    @Override
    public void startPeriodicService(String requestId) {
        System.out.println("Vishal startPeriodicService");
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_RIDE_REQUEST_RESULT, requestId);
        PeriodicTask task = new PeriodicTask.Builder()
                .setService(GetCurrentRideRequestService.class)
                .setTag(TASK_TAG_PERIODIC)
                .setExtras(bundle)
                .setPeriod(2L)
                .build();
        mGcmNetworkManager.schedule(task);
    }

    @Override
    public String getRequestId() {
        return rideConfiguration.getActiveRequest();
    }

    @Override
    public void showBottomSection() {
        bottomContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccessCreateRideRequest(RideRequest rideRequest) {
        rideConfiguration.setActiveSource(confirmBookingViewModel.getSource());
        rideConfiguration.setActiveDestination(confirmBookingViewModel.getDestination());
    }

    @Override
    public void renderAcceptedRequest(RideRequest result) {
        replaceFragment(R.id.bottom_container, DriverDetailFragment.newInstance(result));
        if (result.getLocation() != null) {
            reDrawDriverMarker(result.getLocation().getLatitude(), result.getLocation().getLongitude());
        }
    }

    private void replaceFragment(int containerViewId, android.app.Fragment fragment) {
        if (!getActivity().isFinishing()) {
            FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
            fragmentTransaction.replace(containerViewId, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void renderInProgressRequest(RideRequest result) {
        if (result.getLocation() != null) {
            reDrawDriverMarker(result.getLocation().getLatitude(), result.getLocation().getLongitude());
        }
    }

    @Override
    public void renderDriverCanceledRequest(RideRequest result) {
        //Toast.makeText(getActivity(), "Driver Canceled Request", Toast.LENGTH_SHORT).show();
        //Show a modal dialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                .setMessage(getResources().getString(R.string.driver_cancelled_ride_message))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        getActivity().setResult(OnTripActivity.RIDE_HOME_RESULT_CODE);
                        getActivity().finish();
                    }
                });

        AlertDialog dialog = dialogBuilder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @Override
    public void showNoDriverAvailableDialog() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        android.app.Fragment previousDialog = getFragmentManager().findFragmentByTag("driver_not_found");
        if (previousDialog != null) {
            fragmentTransaction.remove(previousDialog);
        }
        fragmentTransaction.addToBackStack(null);
        DialogFragment dialogFragment = DriverNotFoundDialogFragment.newInstance();
        dialogFragment.setTargetFragment(this, REQUEST_CODE_DRIVER_NOT_FOUND);
        dialogFragment.show(getFragmentManager().beginTransaction(), "driver_not_found");
    }

    @Override
    public void renderRiderCanceledRequest(RideRequest result) {
        Toast.makeText(getActivity(), "Rider Canceled Request", Toast.LENGTH_SHORT).show();
        getActivity().setResult(OnTripActivity.RIDE_HOME_RESULT_CODE);
        getActivity().finish();
    }

    @Override
    public void renderCompletedRequest(RideRequest result) {
        DriverVehicleViewModel driverAndVehicle = new DriverVehicleViewModel();
        driverAndVehicle.setDriver(rideConfiguration.getActiveRequestObj().getDriver());
        driverAndVehicle.setVehicle(rideConfiguration.getActiveRequestObj().getVehicle());
        Intent intent = CompleteTripActivity.getCallingIntent(getActivity(), result.getRequestId(), driverAndVehicle);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void clearRideConfiguration() {
        rideConfiguration.clearActiveRequest();
    }

    @Override
    public void renderArrivingDriverEvent(RideRequest result) {
        if (result.getLocation() != null) {
            reDrawDriverMarker(result.getLocation().getLatitude(), result.getLocation().getLongitude());
        }
    }

    @Override
    public void onSuccessCancelRideRequest() {
        rideConfiguration.clearActiveRequest();
        getActivity().setResult(OnTripActivity.RIDE_HOME_RESULT_CODE);
        getActivity().finish();
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null)
            mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null)
            mapView.onLowMemory();
    }

    @Override
    public void renderTripRoute(List<List<LatLng>> routes) {
        mGoogleMap.clear();

        for (List<LatLng> route : routes) {
            mGoogleMap.addPolyline(new PolylineOptions()
                    .addAll(route)
                    .width(10)
                    .color(Color.BLACK)
                    .geodesic(true));
        }

        //add markers on source and destination
        PlacePassViewModel source, destination;
        if (confirmBookingViewModel != null) {
            source = confirmBookingViewModel.getSource();
            destination = confirmBookingViewModel.getDestination();
        } else {
            source = rideConfiguration.getActiveSource();
            destination = rideConfiguration.getActiveDestination();
        }
        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(source.getLatitude(), source.getLongitude()))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(source.getAddress()));

        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(destination.getLatitude(), destination.getLongitude()))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .title(destination.getAddress()));

        //zoom map to fit both source and dest
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(source.getLatitude(), source.getLongitude()));
        builder.include(new LatLng(destination.getLatitude(), destination.getLongitude()));
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), getResources().getDimensionPixelSize(R.dimen.map_polyline_padding)));
    }

    private void reDrawDriverMarker(double latitude, double longitude) {
        if (mDriverMarker != null) {
            mDriverMarker.remove();
        }
        MarkerOptions options = new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .title("Driver");

        mDriverMarker = mGoogleMap.addMarker(options);
    }

    @Override
    public void hideRideRequestStatus() {
        processingDescription.setVisibility(View.GONE);
    }

    @Override
    public void showRequestRideStatus(String message) {
        processingDescription.setVisibility(View.VISIBLE);
        processingDescription.setText(message);
    }

    @Override
    public void showFindingUberNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity())
                .setSmallIcon(R.drawable.ic_stat_notify)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.qc_launcher))
                .setProgress(0, 0, true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle(getResources().getString(R.string.notification_title_finding_uber));


        // Builds the notification and issues it.
        mNotifyMgr.notify(FINDING_UBER_NOTIFICATION_ID, mBuilder.build());
    }

    @Override
    public void cancelFindingUberNotification() {
        mNotifyMgr.cancel(FINDING_UBER_NOTIFICATION_ID);
    }

    public void showAcceptedNotification(final RideRequest result) {
        // Create remote view and set bigContentView.
        final RemoteViews remoteView = new RemoteViews(getActivity().getPackageName(),
                R.layout.notification_remote_view_ride_accepted);

        remoteView.setTextViewText(R.id.tv_cab_name, result.getVehicle().getVehicleModel());
        remoteView.setTextViewText(R.id.tv_cab_number, result.getVehicle().getLicensePlate());
        remoteView.setTextViewText(R.id.tv_driver_name, result.getDriver().getName());
        remoteView.setTextViewText(R.id.tv_driver_star, result.getDriver().getRating());
        //remoteView.setImageViewUri(R.id.iv_driver_img, Uri.parse(result.getDriver().getPictureUrl()));


        //add event for call to driver
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + result.getDriver().getPhoneNumber()));
        PendingIntent callPendingIntent = PendingIntent.getService(getActivity(), 0, callIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        remoteView.setOnClickPendingIntent(R.id.layout_call_driver, callPendingIntent);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity())
                .setSmallIcon(R.drawable.ic_stat_notify)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.crux_cabs_uber_ic))
                .setContentTitle(getString(R.string.accepted_push_title))
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentText(getResources().getString(R.string.accepted_push_message, result.getDriver().getName(), result.getDriver().getRating()))
                .setCustomBigContentView(remoteView);

        // Builds the notification and issues it.
        acceptedNotification = mBuilder.build();
        mNotifyMgr.notify(ACCEPTED_UBER_NOTIFICATION_ID, acceptedNotification);

        //update driver botmap after downloading
        presenter.getDriverBitmap(remoteView, result.getDriver().getPictureUrl());
    }

    @Override
    public void hideAcceptedNotification(RideRequest result) {
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(ACCEPTED_UBER_NOTIFICATION_ID);
    }

    @Override
    public void updateDriverBitmapInNotification(RemoteViews remoteView, Bitmap bitmap) {
        if (bitmap == null) return;

        remoteView.setImageViewBitmap(R.id.iv_driver_img, bitmap);
        mNotifyMgr.notify(ACCEPTED_UBER_NOTIFICATION_ID, acceptedNotification);
    }
}
