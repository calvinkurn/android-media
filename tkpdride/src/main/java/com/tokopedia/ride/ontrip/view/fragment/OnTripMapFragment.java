package com.tokopedia.ride.ontrip.view.fragment;


import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.share.ShareActivity;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.analytics.RideGATracking;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.bookingride.domain.GetFareEstimateUseCase;
import com.tokopedia.ride.bookingride.domain.GetOverviewPolylineUseCase;
import com.tokopedia.ride.bookingride.view.activity.GooglePlacePickerActivity;
import com.tokopedia.ride.bookingride.view.activity.PayPendingFareActivity;
import com.tokopedia.ride.bookingride.view.activity.RideHomeActivity;
import com.tokopedia.ride.bookingride.view.viewmodel.ConfirmBookingViewModel;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;
import com.tokopedia.ride.common.animator.RouteMapAnimator;
import com.tokopedia.ride.common.configuration.MapConfiguration;
import com.tokopedia.ride.common.configuration.RideConfiguration;
import com.tokopedia.ride.common.configuration.RideStatus;
import com.tokopedia.ride.common.ride.di.RideComponent;
import com.tokopedia.ride.common.ride.domain.model.GetPending;
import com.tokopedia.ride.common.ride.domain.model.Location;
import com.tokopedia.ride.common.ride.domain.model.PendingPayment;
import com.tokopedia.ride.common.ride.domain.model.RideRequest;
import com.tokopedia.ride.common.ride.domain.model.RideRequestAddress;
import com.tokopedia.ride.common.ride.utils.RideUtils;
import com.tokopedia.ride.completetrip.view.CompleteTripActivity;
import com.tokopedia.ride.deeplink.RidePushNotificationBuildAndShow;
import com.tokopedia.ride.ontrip.di.DaggerOnTripComponent;
import com.tokopedia.ride.ontrip.di.OnTripComponent;
import com.tokopedia.ride.ontrip.domain.CancelRideRequestUseCase;
import com.tokopedia.ride.ontrip.domain.CreateRideRequestUseCase;
import com.tokopedia.ride.ontrip.domain.GetRideProductUseCase;
import com.tokopedia.ride.ontrip.domain.GetRideRequestDetailUseCase;
import com.tokopedia.ride.ontrip.domain.GetRideRequestMapUseCase;
import com.tokopedia.ride.ontrip.view.OnTripActivity;
import com.tokopedia.ride.ontrip.view.OnTripMapContract;
import com.tokopedia.ride.ontrip.view.OnTripMapPresenter;
import com.tokopedia.ride.ontrip.view.SendCancelReasonActivity;
import com.tokopedia.ride.ontrip.view.TopupTokoCashChangeDestination;
import com.tokopedia.ride.ontrip.view.viewmodel.DriverVehicleAddressViewModel;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import rx.Observable;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.tokopedia.core.network.retrofit.utils.AuthUtil.md5;
import static com.tokopedia.ride.deeplink.RidePushNotificationBuildAndShow.ACCEPTED_UBER_NOTIFICATION_ID;
import static com.tokopedia.ride.deeplink.RidePushNotificationBuildAndShow.FINDING_UBER_NOTIFICATION_ID;

@RuntimePermissions
public class OnTripMapFragment extends BaseFragment implements OnTripMapContract.View, OnMapReadyCallback,
        DriverDetailFragment.OnFragmentInteractionListener {
    private static final int REQUEST_CODE_INTERRUPT_DIALOG = 1005;
    private static final int REQUEST_CODE_DRIVER_NOT_FOUND = 1006;
    private static final int REQUEST_CODE_CANCEL_REASON = 1007;
    private static final int REQUEST_CODE_INTERRUPT_TOKOPEDIA_DIALOG = 1008;
    private static final int PLACE_AUTOCOMPLETE_DESTINATION_REQUEST_CODE = 1009;
    private static final int REQUEST_CODE_TOPUP_PENDING_PAYMENT_CHANGE_DESTINATION = 1010;

    public static final String TAG = OnTripMapFragment.class.getSimpleName();

    private static final String EXTRA_RIDE_REQUEST = "EXTRA_RIDE_REQUEST";
    private static final String INTERRUPT_DIALOG_TAG = "interrupt_dialog";
    private static final String INTERRUPT_TOKOPEDIA_DIALOG_TAG = "interrupt_tokopedia_dialog";
    private static final float DEFAUL_MAP_ZOOM = 14;
    private static final float SELECT_SOURCE_MAP_ZOOM = 16;
    private static final String SMS_INTENT_KEY = "sms";


    @Inject
    OnTripMapPresenter presenter;

    ConfirmBookingViewModel confirmBookingViewModel;
    GoogleMap mGoogleMap;
    private Marker mDriverMarker;
    private String requestId;
    private RideRequest rideRequest;
    private boolean isOpenInterruptWebviewDialog;

    @BindView(R2.id.mapview)
    MapView mapView;
    @BindView(R2.id.crux_cabs_source)
    TextView tvSource;
    @BindView(R2.id.crux_cabs_destination)
    TextView tvDestination;
    @BindView(R2.id.layout_src_destination)
    View mSrcDestLayout;
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
    @BindView(R2.id.block_translucent_view)
    FrameLayout blockTranslucentView;
    @BindView(R2.id.cancel_panel)
    RelativeLayout cancelPanelLayout;
    @BindView(R2.id.iv_my_location_button)
    ImageView myLocationBtn;
    @BindView(R2.id.tv_canellation_charges)
    TextView cancellationFeeTextView;
    @BindView(R2.id.cancellation_charges_layout)
    LinearLayout cancellationChargesLayout;
    @BindView(R2.id.layout_destination)
    RelativeLayout destinationLayout;
    @BindView((R2.id.tv_destination_change))
    TextView changeDestinationTextView;
    @BindView(R2.id.layout_receipt_pending)
    RelativeLayout dialogReceiptPending;

    private NotificationManager mNotifyMgr;
    private Notification acceptedNotification;
    private Location source, destination;

    private boolean isBackButtonHandleByFragment;
    private boolean isBlackOverlayShow;
    private boolean isFindingUberNotificationShown = false;
    private boolean isAcceptedUberNotificationShown = false;
    private boolean isRouteAlreadyDrawed;
    private int markerId;
    private ProgressDialog mProgressDialog;
    private MapConfiguration mapConfiguration;
    private PlacePassViewModel changedDestination;

    public static OnTripMapFragment newInstance(ConfirmBookingViewModel confirmBookingViewModel) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(OnTripActivity.EXTRA_CONFIRM_BOOKING, confirmBookingViewModel);
        OnTripMapFragment fragment = new OnTripMapFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        presenter.onMapReady();
    }

    public static OnTripMapFragment newInstance(RideRequest rideRequest) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_RIDE_REQUEST, rideRequest);
        OnTripMapFragment fragment = new OnTripMapFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public interface OnFragmentInteractionListener {

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
        mapConfiguration = new MapConfiguration(getActivity());

        if (getArguments() != null) {
            confirmBookingViewModel = getArguments().getParcelable(OnTripActivity.EXTRA_CONFIRM_BOOKING);
            rideRequest = getArguments().getParcelable(EXTRA_RIDE_REQUEST);

            if (rideRequest != null) {
                requestId = rideRequest.getRequestId();
            }

            if (confirmBookingViewModel != null) {
                source = new Location();
                source.setLatitude(confirmBookingViewModel.getSource().getLatitude());
                source.setLongitude(confirmBookingViewModel.getSource().getLongitude());
                destination = new Location();
                destination.setLatitude(confirmBookingViewModel.getDestination().getLatitude());
                destination.setLongitude(confirmBookingViewModel.getDestination().getLongitude());

                saveCancellationFee(confirmBookingViewModel.getCancellationFee());
            } else if (rideRequest != null) {
                source = new Location();
                source.setLatitude(rideRequest.getPickup().getLatitude());
                source.setLongitude(rideRequest.getPickup().getLongitude());
                destination = new Location();
                destination.setLatitude(rideRequest.getDestination().getLatitude());
                destination.setLongitude(rideRequest.getDestination().getLongitude());
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // Gets an instance of the NotificationManager service
        mNotifyMgr = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        presenter.attachView(this);

        //finish activity if fragment is recreated
        if (savedInstanceState != null) {
            startActivity(RideHomeActivity.getCallingIntentForClearTop(getActivity()));
            getActivity().finish();
            return;
        } else {
            changeDestinationTextView.setVisibility(View.VISIBLE);
            presenter.initialize();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void setSourceAndDestinationTextByBookingParam() {
        if (confirmBookingViewModel != null) {
            tvSource.setText(confirmBookingViewModel.getSource().getTitle());
            tvDestination.setText(confirmBookingViewModel.getDestination().getTitle());
        }
    }

    @Override
    public void setAddressPickerText(String startAddressName, String endAddressName) {
        tvSource.setText(startAddressName);
        tvDestination.setText(endAddressName);
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
        requestParams.putString(CreateRideRequestUseCase.PARAM_START_ADDRESS, String.valueOf(confirmBookingViewModel.getSource().getAddress()));
        requestParams.putString(CreateRideRequestUseCase.PARAM_START_ADDRESS_NAME, String.valueOf(confirmBookingViewModel.getSource().getTitle()));
        requestParams.putString(CreateRideRequestUseCase.PARAM_END_ADDRESS, String.valueOf(confirmBookingViewModel.getDestination().getAddress()));
        requestParams.putString(CreateRideRequestUseCase.PARAM_END_ADDRESS_NAME, String.valueOf(confirmBookingViewModel.getDestination().getTitle()));
        requestParams.putString(CreateRideRequestUseCase.PARAM_USER_ID, userId);
        requestParams.putString(CreateRideRequestUseCase.PARAM_DEVICE_ID, deviceId);
        requestParams.putString(CreateRideRequestUseCase.PARAM_HASH, hash);
        requestParams.putString(CreateRideRequestUseCase.PARAM_OS_TYPE, "1");
        requestParams.putString(CreateRideRequestUseCase.PARAM_PRODUCT_ID, confirmBookingViewModel.getProductId());
        requestParams.putString(CreateRideRequestUseCase.PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));
        requestParams.putString(CreateRideRequestUseCase.PARAM_PRODUCT_NAME, confirmBookingViewModel.getProductDisplayName());
        requestParams.putString(CreateRideRequestUseCase.PARAM_API_VERSION, "v2");
        if (!TextUtils.isEmpty(confirmBookingViewModel.getPromoCode())) {
            requestParams.putString(CreateRideRequestUseCase.PARAM_PROMO_CODE, confirmBookingViewModel.getPromoCode());
            requestParams.putString(CreateRideRequestUseCase.PARAM_DEVICE_TYPE, presenter.getDeviceName());
        }
        return requestParams;
    }

    @Override
    public RequestParams getFareEstimateParam() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetFareEstimateUseCase.PARAM_START_LATITUDE, String.valueOf(confirmBookingViewModel.getSource().getLatitude()));
        requestParams.putString(GetFareEstimateUseCase.PARAM_START_LONGITUDE, String.valueOf(confirmBookingViewModel.getSource().getLongitude()));
        requestParams.putString(GetFareEstimateUseCase.PARAM_END_LATITUDE, String.valueOf(confirmBookingViewModel.getDestination().getLatitude()));
        requestParams.putString(GetFareEstimateUseCase.PARAM_END_LONGITUDE, String.valueOf(confirmBookingViewModel.getDestination().getLongitude()));
        requestParams.putString(GetFareEstimateUseCase.PARAM_PRODUCT_ID, String.valueOf(confirmBookingViewModel.getProductId()));

        //add seat count for Uber Pool only
        if (confirmBookingViewModel.getProductDisplayName().equalsIgnoreCase(getString(R.string.confirm_booking_uber_pool_key))) {
            requestParams.putString(GetFareEstimateUseCase.PARAM_SEAT_COUNT, String.valueOf(confirmBookingViewModel.getSeatCount()));
        }
        return requestParams;
    }

    @Override
    public boolean isAlreadyRequested() {
        return confirmBookingViewModel == null;
    }

    @Override
    public void showLoadingWaitingResponse() {
        processingLayout.setVisibility(View.VISIBLE);
        //loaderLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingWaitingResponse() {
        if (!isBlackOverlayShow) {
            processingLayout.setVisibility(View.GONE);
            loaderLayout.setVisibility(View.GONE);
        }
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
    public void setMapViewListener() {
        MapsInitializer.initialize(this.getActivity());
        if (checkLocationPermission()) return;
        mGoogleMap.setMyLocationEnabled(false);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(false);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mapConfiguration.getDefaultLatitude(), mapConfiguration.getDefaultLongitude()), DEFAUL_MAP_ZOOM));
    }

    @Override
    public RequestParams getPolyLineParam(double driverlat, double driverLon) {
        if (source == null || destination == null) {
            return null;
        } else {
            RequestParams requestParams = RequestParams.create();
            requestParams.putString(GetOverviewPolylineUseCase.PARAM_ORIGIN, String.format("%s,%s",
                    source.getLatitude(),
                    source.getLongitude()
            ));
            requestParams.putString(GetOverviewPolylineUseCase.PARAM_DESTINATION, String.format("%s,%s",
                    destination.getLatitude(),
                    destination.getLongitude()
            ));
            requestParams.putString(GetOverviewPolylineUseCase.PARAM_SENSOR, "false");
            requestParams.putString(GetOverviewPolylineUseCase.PARAM_TRAFFIC_MODEL, "best_guess");
            requestParams.putString(GetOverviewPolylineUseCase.PARAM_MODE, "driving");
            requestParams.putString(GetOverviewPolylineUseCase.PARAM_DEPARTURE_TIME, (int) (System.currentTimeMillis() / 1000) + "");
            requestParams.putString(GetOverviewPolylineUseCase.PARAM_KEY, getString(R.string.GOOGLE_API_KEY));

            if (driverlat != 0 && driverLon != 0) {
                requestParams.putString(GetOverviewPolylineUseCase.PARAM_WAYPOINTS, String.format("%s,%s",
                        driverlat,
                        driverLon
                ));
            }

//            requestParams.putString("key", getString(R.string.GOOGLE_API_KEY));
            return requestParams;
        }
    }

    @Override
    public RequestParams getPolyLineParamBetweenTwoLocations(Location origin, Location destination) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetOverviewPolylineUseCase.PARAM_ORIGIN, String.format("%s,%s",
                origin.getLatitude(),
                origin.getLongitude()
        ));
        requestParams.putString(GetOverviewPolylineUseCase.PARAM_DESTINATION, String.format("%s,%s",
                destination.getLatitude(),
                destination.getLongitude()
        ));
        requestParams.putString(GetOverviewPolylineUseCase.PARAM_SENSOR, "false");
        requestParams.putString(GetOverviewPolylineUseCase.PARAM_TRAFFIC_MODEL, "best_guess");
        requestParams.putString(GetOverviewPolylineUseCase.PARAM_MODE, "driving");
        requestParams.putString(GetOverviewPolylineUseCase.PARAM_DEPARTURE_TIME, (int) (System.currentTimeMillis() / 1000) + "");
        requestParams.putString(GetOverviewPolylineUseCase.PARAM_KEY, getString(R.string.GOOGLE_API_KEY));

        return requestParams;
    }

    @OnClick(R2.id.cabs_processing_cancel_button)
    public void actionCancelButtonClicked() {
        showCancelPanel();
        RideGATracking.eventClickCancelRequestRide(getScreenName());
        presenter.actionCancelButtonClicked();
    }

    @Override
    public void showCancellationLayout() {
        cancellationChargesLayout.setVisibility(View.VISIBLE);
        cancellationFeeTextView.setText(getCancellationFee());
    }

    @Override
    public RequestParams getCancelParams() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(CancelRideRequestUseCase.PARAM_REQUEST_ID, requestId);
        return requestParams;
    }

    @Override
    public void hideCancelRequestButton() {
        cancelButton.setVisibility(View.GONE);
    }

    @Override
    public void showCancelRequestButton() {
        if (isBackButtonHandleByFragment) {
            cancelButton.setVisibility(View.GONE);
        } else {
            cancelButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void openInterruptConfirmationWebView(String url) {
        if (!isOpenInterruptWebviewDialog) {
            RideGATracking.eventOpenInterruptScreen(url);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            android.app.Fragment previousDialog = getFragmentManager().findFragmentByTag(INTERRUPT_DIALOG_TAG);
            if (previousDialog != null) {
                fragmentTransaction.remove(previousDialog);
            }

            fragmentTransaction.addToBackStack(null);
            DialogFragment dialogFragment = InterruptConfirmationDialogFragment.newInstance(url);
            dialogFragment.setTargetFragment(this, REQUEST_CODE_INTERRUPT_DIALOG);
            //using state loss, because sometimes this dialog comes on top of location enablegit
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(dialogFragment, INTERRUPT_DIALOG_TAG);
            transaction.commitAllowingStateLoss();
            isOpenInterruptWebviewDialog = true;
        }
    }

    @Override
    public void failedToRequestRide(String message) {
        Intent intent = getActivity().getIntent();
        intent.putExtra(OnTripActivity.EXTRA_FAILED_MESSAGE, message);

        getActivity().setResult(OnTripActivity.RIDE_BOOKING_RESULT_CODE, intent);
        getActivity().finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_INTERRUPT_DIALOG:
                isOpenInterruptWebviewDialog = false;
                if (resultCode == Activity.RESULT_OK) {
                    String id = data.getStringExtra(InterruptConfirmationDialogFragment.EXTRA_ID);
                    String key = data.getStringExtra(InterruptConfirmationDialogFragment.EXTRA_KEY);
                    RequestParams requestParams = getParam();
                    if (key != null && key.length() > 0) {
                        requestParams.putString(key, id);
                    }
                    presenter.actionRideRequest(requestParams);
                } else {
                    //TODO: we may need to update the fare status again
                    getActivity().finish();
                }
                break;
            case REQUEST_CODE_INTERRUPT_TOKOPEDIA_DIALOG:
                isOpenInterruptWebviewDialog = false;
                if (resultCode == Activity.RESULT_OK) {
                    String id = data.getStringExtra(InterruptDialogFragment.EXTRA_VALUE);
                    String key = data.getStringExtra(InterruptDialogFragment.EXTRA_KEY);
                    RequestParams requestParams = getParam();
                    if (key != null && key.length() > 0) {
                        requestParams.putString(key, id);
                    }
                    presenter.actionRideRequest(requestParams);
                } else {
                    //TODO: we may need to update the fare status again
                    getActivity().finish();
                }
                break;
            case REQUEST_CODE_DRIVER_NOT_FOUND:
                if (resultCode == DriverNotFoundDialogFragment.BOOK_AGAIN_RESULT_CODE) {
                    setResultWithSourceAndDestination();
                } else {
                    getActivity().setResult(OnTripActivity.RIDE_HOME_RESULT_CODE);
                    getActivity().finish();
                }
                break;
            case REQUEST_CODE_CANCEL_REASON:
                if (resultCode == Activity.RESULT_OK) {
                    setResultWithSourceAndDestination();
                }
                break;
            case PLACE_AUTOCOMPLETE_DESTINATION_REQUEST_CODE:
                if (data != null) {
                    PlacePassViewModel destinationTemp = data.getParcelableExtra(GooglePlacePickerActivity.EXTRA_SELECTED_PLACE);
                    if (destinationTemp == null || destinationTemp.getLatitude() == 0.0 || destinationTemp.getLongitude() == 0.0) {
                        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.ride_home_map_dest_zero_error));
                    } else if (destinationTemp.getLatitude() == source.getLatitude() && destinationTemp.getLongitude() == source.getLongitude()) {
                        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.ride_home_map_dest_same_source_error));
                    } else {
                        //update destination
                        changedDestination = destinationTemp;
                        presenter.updateDestination(destinationTemp);
                    }
                }
                break;
            case REQUEST_CODE_TOPUP_PENDING_PAYMENT_CHANGE_DESTINATION:
                if (resultCode == IDigitalModuleRouter.PAYMENT_SUCCESS) {
                    //call update request again
                    if (changedDestination != null) {
                        presenter.updateDestination(changedDestination);
                    }
                }
                break;
            default:
        }
    }


    @Override
    public void startPeriodicService(String requestId) {
        presenter.startGetRequestDetailsPeriodicService(requestId);
    }

    @Override
    public String getRequestId() {
        return requestId;
    }

    @Override
    public RideRequest getRideRequest() {
        return rideRequest;
    }

    @Override
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public void showBottomSection() {
        bottomContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccessCreateRideRequest(RideRequest rideRequest) {
        this.rideRequest = rideRequest;
    }

    @Override
    public void renderAcceptedRequest(RideRequest result) {
        replaceFragment(R.id.bottom_container, DriverDetailFragment.newInstance(result, getTag()));
        if (result.getLocation() != null) {
            reDrawDriverMarker(result.getLocation().getLatitude(), result.getLocation().getLongitude(), result.getLocation().getBearing());
        }
    }

    private void replaceFragment(int containerViewId, android.app.Fragment fragment) {
        if (!getActivity().isFinishing()) {
            FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
            fragmentTransaction.replace(containerViewId, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void renderInProgressRequest(RideRequest result) {
        if (result.getLocation() != null) {
            reDrawDriverMarker(result.getLocation().getLatitude(), result.getLocation().getLongitude(), result.getLocation().getBearing());
        }

        setTitle(R.string.title_trip_in_progress);
    }

    @Override
    public void setTitle(String title) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
    }

    @Override
    public void setTitle(int resId) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(resId);
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
                        setResultWithSourceAndDestination();
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
        Toast.makeText(getActivity(), R.string.ontrip_rider_canceled_request_message, Toast.LENGTH_SHORT).show();
        PlacePassViewModel source = new PlacePassViewModel();
//        source.setType(PlacePassViewModel.TYPE.OTHER);
        source.setTitle(result.getPickup().getAddressName());
        source.setAddress(result.getPickup().getAddress());
        source.setAndFormatLatitude(result.getPickup().getLatitude());
        source.setAndFormatLongitude(result.getPickup().getLongitude());

        PlacePassViewModel destination = new PlacePassViewModel();
//        destination.setType(PlacePassViewModel.TYPE.OTHER);
        destination.setTitle(result.getDestination().getAddressName());
        destination.setAddress(result.getDestination().getAddress());
        destination.setAndFormatLatitude(result.getDestination().getLatitude());
        destination.setAndFormatLongitude(result.getDestination().getLongitude());

        Intent intent = getActivity().getIntent();
        intent.putExtra(OnTripActivity.EXTRA_PLACE_SOURCE, source);
        intent.putExtra(OnTripActivity.EXTRA_PLACE_DESTINATION, destination);
        getActivity().setResult(OnTripActivity.RIDE_HOME_RESULT_CODE, intent);
        getActivity().finish();
    }

    @Override
    public void renderCompletedRequest(RideRequest result) {
        DriverVehicleAddressViewModel driverAndVehicle = new DriverVehicleAddressViewModel();
        driverAndVehicle.setDriver(result.getDriver());
        driverAndVehicle.setVehicle(result.getVehicle());
        RideRequestAddress rideRequestAddress = new RideRequestAddress();
        rideRequestAddress.setStartAddressName(result.getPickup().getAddressName());
        rideRequestAddress.setStartAddress(result.getPickup().getAddress());
        rideRequestAddress.setEndAddressName(result.getDestination().getAddressName());
        rideRequestAddress.setEndAddress(result.getDestination().getAddress());
        driverAndVehicle.setAddress(rideRequestAddress);
        Intent intent = CompleteTripActivity.getCallingIntent(getActivity(), result.getRequestId(), driverAndVehicle);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void renderCompletedRequestWithoutReceipt(RideRequest result) {
        replaceFragment(R.id.bottom_container, DriverDetailFragment.newInstance(result, getTag()));
        setTitle(R.string.title_trip_completed);

        //show dialog the ride is completed and receipt is response is pending
        dialogReceiptPending.setVisibility(View.VISIBLE);
    }

    @Override
    public void renderArrivingDriverEvent(RideRequest result) {
        if (result.getLocation() != null) {
            reDrawDriverMarker(result.getLocation().getLatitude(), result.getLocation().getLongitude(), result.getLocation().getBearing());
        }

        setTitle(R.string.title_trip_arriving);
    }

    @Override
    public void setResultWithSourceAndDestination() {
        PlacePassViewModel source = null, destination = null;
        if (confirmBookingViewModel != null) {
            source = confirmBookingViewModel.getSource();
            destination = confirmBookingViewModel.getDestination();
        }
        Intent intent = getActivity().getIntent();
        if (source != null && destination != null) {
            intent.putExtra(OnTripActivity.EXTRA_PLACE_SOURCE, source);
            intent.putExtra(OnTripActivity.EXTRA_PLACE_DESTINATION, destination);
        }

        getActivity().setResult(OnTripActivity.RIDE_HOME_RESULT_CODE, intent);
        getActivity().finish();
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
        isRouteAlreadyDrawed = true;
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
        isRouteAlreadyDrawed = true;

        for (List<LatLng> route : routes) {
            if (route.size() > 1) {
                RouteMapAnimator.getInstance().animateRoute(mGoogleMap, route);
            }
        }
    }

    @Override
    public void renderTripRouteWithoutAnimation(List<List<LatLng>> routes) {
        mGoogleMap.clear();

        for (List<LatLng> route : routes) {
            if (route.size() > 1) {
                RouteMapAnimator.getInstance().renderRouteWithoutAnimation(mGoogleMap, route);
            }
        }
    }

    @Override
    public void renderSourceMarker(double latitude, double longitude) {
        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .icon(getMarkerIcon(R.drawable.marker_green))
        );
    }

    @Override
    public void renderDestinationMarker(double latitude, double longitude) {
        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .icon(getMarkerIcon(R.drawable.marker_red)));
    }

    @Override
    public void zoomMapFitByPolyline(List<LatLng> latLngs) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : latLngs) {
            builder.include(latLng);
        }
        int widthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;

        int heightPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
        if (bottomContainer.getVisibility() == View.VISIBLE) {
            int topYAxis = mSrcDestLayout.getBottom();
            int bottomYAxis = bottomContainer.getTop();
            heightPixels = bottomYAxis - topYAxis;
        }

        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),
                widthPixels, heightPixels,
                getResources().getDimensionPixelSize(R.dimen.map_polyline_padding))
        );
    }

    public BitmapDescriptor getMarkerIcon(int resourceId) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), resourceId);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, getResources().getDimensionPixelSize(R.dimen.marker_width), getResources().getDimensionPixelSize(R.dimen.marker_height), false);
        return BitmapDescriptorFactory.fromBitmap(resizedBitmap);
    }

    @Override
    public void reDrawDriverMarker(double latitude, double longitude, float bearing) {
        if (mGoogleMap == null) {
            return;
        }

        if (mDriverMarker != null) {
            mDriverMarker.remove();
        }

        if (confirmBookingViewModel != null) {
            markerId = R.drawable.car_map_icon;
            markerId = (RideUtils.isUberMoto(confirmBookingViewModel.getProductDisplayName())) ? R.drawable.moto_map_icon : R.drawable.car_map_icon;
        }

        MarkerOptions options;
        if (markerId != 0) {
            options = new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .icon(getCarMapIcon(markerId))
                    .rotation(bearing)
                    .title(getString(R.string.ontrip_marker_driver));
        } else {
            options = new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .rotation(bearing)
                    .title(getString(R.string.ontrip_marker_driver));
        }

        mDriverMarker = mGoogleMap.addMarker(options);
    }

    private BitmapDescriptor getCarMapIcon(int resourceId) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), resourceId);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, getResources().getDimensionPixelSize(R.dimen.car_marker_width), getResources().getDimensionPixelSize(R.dimen.car_marker_height), false);
        return BitmapDescriptorFactory.fromBitmap(resizedBitmap);
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
        if (isFindingUberNotificationShown) {
            return;
        }

        RidePushNotificationBuildAndShow.showFindingUberNotication(getActivity());

        isFindingUberNotificationShown = true;
    }

    @Override
    public void hideFindingUberNotification() {
        mNotifyMgr.cancel(FINDING_UBER_NOTIFICATION_ID);
    }

    public void showAcceptedNotification(final RideRequest result) {
        if (result.getVehicle() == null || result.getDriver() == null || result.getPickup() == null) {
            return;
        }

        if (isAcceptedUberNotificationShown) {
            return;
        }

        RidePushNotificationBuildAndShow.showRideAccepted(getActivity(), result);

        isAcceptedUberNotificationShown = true;
    }

    @Override
    public void hideAcceptedNotification() {
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(ACCEPTED_UBER_NOTIFICATION_ID);
    }

    @Override
    public void updateDriverBitmapInNotification(RemoteViews remoteView, Bitmap bitmap) {
        if (bitmap == null) return;

        remoteView.setImageViewBitmap(R.id.iv_driver_img, bitmap);
        mNotifyMgr.notify(ACCEPTED_UBER_NOTIFICATION_ID, acceptedNotification);
    }

    @Override
    public void actionCancelRide() {
        showCancelPanel();
        presenter.actionCancelButtonClicked();


    }

    @Override
    public RequestParams getShareEtaParam() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetRideRequestMapUseCase.PARAM_REQUEST_ID, getRequestId());
        return requestParams;
    }

    @Override
    public void actionShareEta() {
        presenter.actionShareEta();
    }

    @Override
    public void showFailedShare() {
        Snackbar.make(getView(), R.string.on_trip_failed_share, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showShareDialog(String shareUrl) {
        ShareData shareData = ShareData.Builder.aShareData()
                .setType(ShareData.RIDE_TYPE)
                .setName(getString(R.string.share_ride_title))
                .setTextContent(getString(R.string.share_ride_description))
                .setUri(shareUrl)
                .build();
        Intent shareIntent = ShareActivity.getCallingRideIntent(getActivity(), shareData);
        startActivity(shareIntent);
    }

    @Override
    public void hideCancelPanel() {
        isBackButtonHandleByFragment = false;

        //do not hide, layout is already hidden
        if (cancelPanelLayout.getVisibility() == View.GONE) {
            return;
        }

        cancelPanelLayout.animate().alpha(0.0f).setDuration(500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //check if trip is in process then only make it visible
                if (presenter.getRideStatus() != null && presenter.getRideStatus().equalsIgnoreCase(RideStatus.PROCESSING)) {
                    cancelButton.setVisibility(View.VISIBLE);
                }
            }
        }, 500);
    }

    public void showCancelPanel() {
        isBackButtonHandleByFragment = true;

        cancelButton.setVisibility(View.GONE);
        cancelPanelLayout.setVisibility(View.VISIBLE);
        cancelPanelLayout.setAlpha(0.0f);
        cancelPanelLayout.animate().alpha(1.0f).setDuration(500);
    }

    @Override
    public RequestParams getCurrentRequestParams(String requestId) {
        String deviceId = GCMHandler.getRegistrationId(getActivity());
        String userId = SessionHandler.getLoginID(getActivity());
        String hash = md5(userId + "~" + deviceId);
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetRideRequestDetailUseCase.PARAM_REQUEST_ID, requestId);
        requestParams.putString(GetRideRequestDetailUseCase.PARAM_USER_ID, userId);
        requestParams.putString(GetRideRequestDetailUseCase.PARAM_DEVICE_ID, deviceId);
        requestParams.putString(GetRideRequestDetailUseCase.PARAM_HASH, hash);
        requestParams.putString(GetRideRequestDetailUseCase.PARAM_OS_TYPE, "1");
        requestParams.putString(GetRideRequestDetailUseCase.PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));

        return requestParams;
    }

    @Override
    public void clearActiveNotification() {
        RidePushNotificationBuildAndShow.cancelActiveNotification(getActivity());
    }

    @Override
    public String getResourceString(int resourceId) {
        return getString(resourceId);
    }

    @Override
    public boolean isSurge() {
        return confirmBookingViewModel.getSurgeMultiplier() > 0;
    }

    @Override
    public String getSurgeConfirmationHref() {
        return confirmBookingViewModel.getSurgeConfirmationHref();
    }

    @OnClick(R2.id.btn_yes)
    public void actionYesCancelBtnClicked() {
        hideCancelPanel();
        hideFindingUberNotification();
        hideAcceptedNotification();
        presenter.actionYesCancelBtnClicked();
    }

    @OnClick(R2.id.btn_no)
    public void actionNoCancelBtnClicked() {
        hideCancelPanel();
    }

    @OnClick({R2.id.crux_cabs_destination, R2.id.tv_destination_change})
    public void actionDestinationButtonClicked() {
        RideGATracking.eventClickChangeDestinationOpenMap(AppScreen.SCREEN_RIDE_ONTRIP);

        Intent intent = GooglePlacePickerActivity.getCallingIntent(getActivity(), R.drawable.marker_red_old);
        intent.putExtra(GooglePlacePickerActivity.EXTRA_REQUEST_CODE, PLACE_AUTOCOMPLETE_DESTINATION_REQUEST_CODE);
        intent.putExtra(GooglePlacePickerActivity.EXTRA_SOURCE, source);
        startActivityForResultWithClipReveal(intent, PLACE_AUTOCOMPLETE_DESTINATION_REQUEST_CODE, destinationLayout);
    }

    @NeedsPermission({Manifest.permission.CALL_PHONE})
    public void openCallIntent(String phoneNumber) {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(callIntent);
    }

    @Override
    public void checkAndExecuteCallPermission(String phoneNumber) {
        OnTripMapFragmentPermissionsDispatcher.openCallIntentWithCheck(this, phoneNumber);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        OnTripMapFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void showBlockTranslucentLayout() {
        blockTranslucentView.setVisibility(View.VISIBLE);
        final ObjectAnimator backgroundColorAnimator = ObjectAnimator.ofObject(blockTranslucentView,
                "backgroundColor",
                new ArgbEvaluator(),
                0x00000000,
                0xBB000000);
        backgroundColorAnimator.setDuration(500);
        backgroundColorAnimator.start();
    }

    @Override
    public void hideBlockTranslucentLayout() {
        if (!isBlackOverlayShow) {
            final ObjectAnimator backgroundColorAnimator = ObjectAnimator.ofObject(blockTranslucentView,
                    "backgroundColor",
                    new ArgbEvaluator(),
                    0xBB000000,
                    0x00000000);
            backgroundColorAnimator.setDuration(500);
            backgroundColorAnimator.start();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (blockTranslucentView != null) {
                        blockTranslucentView.setVisibility(View.GONE);
                    }
                }
            }, 500);
        }
    }

    @Override
    public void moveMapToLocation(double latitude, double longitude) {
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), SELECT_SOURCE_MAP_ZOOM));
    }

    @Override
    public boolean isAlreadyRouteDrawed() {
        return isRouteAlreadyDrawed;
    }

    @Override
    public void openSmsIntent(String smsNumber) {
        if (!TextUtils.isEmpty(smsNumber)) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.fromParts(SMS_INTENT_KEY, smsNumber, null))
            );
        }
    }

    public OnTripActivity.BackButtonListener getBackButtonListener() {
        return new OnTripActivity.BackButtonListener() {
            @Override
            public void onBackPressed() {
                if (isBackButtonHandleByFragment) {
                    hideCancelPanel();
                }
            }

            @Override
            public boolean canGoBack() {
                return isBackButtonHandleByFragment;
            }

            @Override
            public boolean isAnyPendingRequest() {
                return presenter.checkIsAnyPendingRequest();
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    public OnTripActivity.OnUpdatedByPushNotification getUpdatedByPushListener() {
        return new OnTripActivity.OnUpdatedByPushNotification() {
            @Override
            public void onUpdatedByPushNotification(Observable<RideRequest> rideRequest) {
                presenter.actionOnReceivePushNotification(rideRequest);
            }
        };
    }

    @OnClick(R2.id.iv_my_location_button)
    public void actionMyLocationBtnClicked() {
        presenter.actionGoToCurrentLocation();
    }

    @Override
    public void updateSourceCoordinate(double latitude, double longitude) {
        source = new Location();
        source.setLatitude(latitude);
        source.setLongitude(longitude);
    }

    @Override
    public void updateDestinationCoordinate(double latitude, double longitude) {
        destination = new Location();
        destination.setLatitude(latitude);
        destination.setLongitude(longitude);
    }

    @Override
    public RequestParams getProductDetailParam(String productId) {
        String deviceId = GCMHandler.getRegistrationId(getActivity());
        String userId = SessionHandler.getLoginID(getActivity());
        String hash = md5(userId + "~" + deviceId);
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetRideProductUseCase.PARAM_PRODUCT_ID, productId);
        requestParams.putString(GetRideRequestDetailUseCase.PARAM_USER_ID, userId);
        requestParams.putString(GetRideRequestDetailUseCase.PARAM_DEVICE_ID, deviceId);
        requestParams.putString(GetRideRequestDetailUseCase.PARAM_HASH, hash);
        requestParams.putString(GetRideRequestDetailUseCase.PARAM_OS_TYPE, "1");
        requestParams.putString(GetRideRequestDetailUseCase.PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));
        return requestParams;
    }

    @Override
    public void setDriverIcon(RideRequest result, int drawable) {
        if (mDriverMarker != null) {
            mDriverMarker.remove();
        }
        markerId = drawable;

        //set marker on map
        if (result != null && result.getLocation() != null) {
            MarkerOptions options = new MarkerOptions()
                    .position(new LatLng(result.getLocation().getLatitude(), result.getLocation().getLongitude()))
                    .icon(getCarMapIcon(drawable))
                    .rotation(result.getLocation().getBearing())
                    .title(getString(R.string.ontrip_marker_driver));

            if (mGoogleMap != null) {
                mDriverMarker = mGoogleMap.addMarker(options);
            }
        }
    }

    @Override
    public void saveActiveRequestId(String requestId) {
        RideConfiguration rideConfiguration = new RideConfiguration(getActivity());
        rideConfiguration.setActiveRequestId(requestId);
    }

    @Override
    public void clearSavedActiveRequestId() {
        RideConfiguration configuration = new RideConfiguration(getActivity());
        configuration.clearActiveRequestId();
    }

    @Override
    public void saveActiveProductName() {
        if (confirmBookingViewModel != null) {
            RideConfiguration configuration = new RideConfiguration(getActivity());
            configuration.saveActiveProductName(confirmBookingViewModel.getProductDisplayName());
        }
    }

    @Override
    public void clearSavedActiveProductName() {
        RideConfiguration configuration = new RideConfiguration(getActivity());
        configuration.clearActiveProductName();
    }

    @Override
    public String getActiveProductNameInCache() {
        RideConfiguration configuration = new RideConfiguration(getActivity());
        return configuration.getActiveProductName();
    }

    @Override
    public void saveActiveProductName(String displayName) {
        RideConfiguration configuration = new RideConfiguration(getActivity());
        configuration.saveActiveProductName(displayName);
    }

    @Override
    public void saveCancellationFee(String cancellationFee) {
        RideConfiguration configuration = new RideConfiguration(getActivity());
        configuration.saveActiveRequestCancellationFee(cancellationFee);
    }

    @Override
    public String getCancellationFee() {
        RideConfiguration configuration = new RideConfiguration(getActivity());
        return configuration.getActiveCancellationFee();
    }

    @Override
    public void showShareEtaProgress() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getString(R.string.loading));
        mProgressDialog.show();
    }

    @Override
    public void hideShareEtaProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showCurrentLocationIndicator() {
        if (mGoogleMap != null) {
            if (checkLocationPermission()) return;
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void hideCurrentLocationIndicator() {
        if (mGoogleMap != null) {
            if (checkLocationPermission()) return;
            mGoogleMap.setMyLocationEnabled(false);
        }
    }

    private boolean checkLocationPermission() {
        return ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void actionNavigateToCancelReasonPage(String requestId, String timestamp) {
        Intent intent = SendCancelReasonActivity.getCallingIntent(getActivity(), requestId, timestamp);
        startActivityForResult(intent, REQUEST_CODE_CANCEL_REASON);
    }

    @Override
    public void actionNavigateToCancelReasonPage(String requestId) {
        Intent intent = SendCancelReasonActivity.getCallingIntent(getActivity(), requestId);
        startActivityForResult(intent, REQUEST_CODE_CANCEL_REASON);
    }

    @Override
    public void zoomMapFitByDriverAndCustomer(List<LatLng> latLngs) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : latLngs) {
            builder.include(latLng);
        }
        int widthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
        int heightPixels = Resources.getSystem().getDisplayMetrics().heightPixels;
        int boundHeight = 0;

        if (bottomContainer.getVisibility() == View.VISIBLE) {
            int topYAxis = mSrcDestLayout.getBottom();
            int bottomYAxis = bottomContainer.getTop();
            boundHeight = bottomYAxis - (heightPixels - bottomYAxis);
        }

        try {
            mGoogleMap.animateCamera(
                    CameraUpdateFactory.newLatLngBounds(builder.build(), widthPixels, boundHeight, getResources().getDimensionPixelSize(R.dimen.twenty_five_dp)),
                    1000, null
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void openInterruptConfirmationDialog(String tosUrl, String key, String value) {
        if (!isOpenInterruptWebviewDialog) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            android.app.Fragment previousDialog = getFragmentManager().findFragmentByTag(INTERRUPT_TOKOPEDIA_DIALOG_TAG);
            if (previousDialog != null) {
                fragmentTransaction.remove(previousDialog);
            }
            fragmentTransaction.addToBackStack(null);
            DialogFragment dialogFragment = InterruptDialogFragment.newInstance(key, value, tosUrl);
            dialogFragment.setTargetFragment(this, REQUEST_CODE_INTERRUPT_TOKOPEDIA_DIALOG);
//            dialogFragment.show(getFragmentManager().beginTransaction(), INTERRUPT_TOKOPEDIA_DIALOG_TAG);
            getFragmentManager().beginTransaction().add(dialogFragment, INTERRUPT_TOKOPEDIA_DIALOG_TAG).commitAllowingStateLoss();
            isOpenInterruptWebviewDialog = true;
        }
    }

    @Override
    public void saveDefaultLocation(double latitude, double longitude) {
        if (mapConfiguration != null) {
            mapConfiguration.setDefaultLocation(latitude, longitude);
        }
    }

    @Override
    public void setDestination(PlacePassViewModel destination) {
        this.destination = new Location();
        this.destination.setLatitude(destination.getLatitude());
        this.destination.setLongitude(destination.getLongitude());
        tvDestination.setText(destination.getTitle());
    }

    @Override
    public void showUpdateDestinationLoading() {
        loaderLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideUpdateDestinationLoading() {
        loaderLayout.setVisibility(View.GONE);
    }

    private void startActivityForResultWithClipReveal(Intent intent, int requestCode, View view) {
        ActivityOptions options = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            options = ActivityOptions.makeClipRevealAnimation(view, 0, 0,
                    view.getWidth(), view.getHeight());
            startActivityForResult(intent, requestCode, options.toBundle());
        } else {
            startActivityForResult(intent, requestCode);
        }
    }

    @Override
    protected void initInjector() {
        RideComponent component = getComponent(RideComponent.class);
        OnTripComponent onTripComponent = DaggerOnTripComponent
                .builder()
                .rideComponent(component)
                .build();
        onTripComponent.inject(this);
    }

    @Override
    public void startTopupTokoCashChangeDestinationActivity(PendingPayment pendingPayment, String requestId) {
        Intent topupIntent = TopupTokoCashChangeDestination.getCallingIntent(getActivity(), pendingPayment, requestId);
        startActivityForResult(topupIntent, REQUEST_CODE_TOPUP_PENDING_PAYMENT_CHANGE_DESTINATION);
    }

    @Override
    public void showPendingFareInterrupt(GetPending getPending) {
        if (getActivity() != null) {
            startActivity(PayPendingFareActivity.getCallingIntent(getActivity(), getPending));
            getActivity().finish();
        }
    }
}
