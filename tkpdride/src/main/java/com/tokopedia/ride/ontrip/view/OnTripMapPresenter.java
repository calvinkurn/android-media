package com.tokopedia.ride.ontrip.view;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.RemoteViews;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonSyntaxException;
import com.google.maps.android.PolyUtil;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.domain.GetFareEstimateUseCase;
import com.tokopedia.ride.bookingride.domain.GetOverviewPolylineUseCase;
import com.tokopedia.ride.bookingride.view.fragment.RideHomeMapFragment;
import com.tokopedia.ride.common.configuration.RideStatus;
import com.tokopedia.ride.common.exception.InterruptConfirmationHttpException;
import com.tokopedia.ride.common.exception.UnprocessableEntityHttpException;
import com.tokopedia.ride.common.ride.domain.model.FareEstimate;
import com.tokopedia.ride.common.ride.domain.model.RideRequest;
import com.tokopedia.ride.ontrip.domain.CancelRideRequestUseCase;
import com.tokopedia.ride.ontrip.domain.CreateRideRequestUseCase;
import com.tokopedia.ride.ontrip.domain.GetRideRequestDetailUseCase;
import com.tokopedia.ride.ontrip.domain.GetRideRequestMapUseCase;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by alvarisi on 3/24/17.
 */

public class OnTripMapPresenter extends BaseDaggerPresenter<OnTripMapContract.View>
        implements OnTripMapContract.Presenter {

    private static final long CURRENT_REQUEST_DETAIL_POLLING_TIME_DELAY = 4000;

    private CreateRideRequestUseCase createRideRequestUseCase;
    private CancelRideRequestUseCase cancelRideRequestUseCase;
    private GetOverviewPolylineUseCase getOverviewPolylineUseCase;
    private GetRideRequestMapUseCase getRideRequestMapUseCase;
    private GetRideRequestDetailUseCase getRideRequestUseCase;
    private GetFareEstimateUseCase getFareEstimateUseCase;
    private RideRequest activeRideRequest;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;

    private Handler handler = new Handler();

    public OnTripMapPresenter(CreateRideRequestUseCase createRideRequestUseCase,
                              CancelRideRequestUseCase cancelRideRequestUseCase,
                              GetOverviewPolylineUseCase getOverviewPolylineUseCase,
                              GetRideRequestMapUseCase getRideRequestMapUseCase,
                              GetRideRequestDetailUseCase getRideRequestUseCase,
                              GetFareEstimateUseCase getFareEstimateUseCase) {
        this.createRideRequestUseCase = createRideRequestUseCase;
        this.cancelRideRequestUseCase = cancelRideRequestUseCase;
        this.getOverviewPolylineUseCase = getOverviewPolylineUseCase;
        this.getRideRequestMapUseCase = getRideRequestMapUseCase;
        this.getRideRequestUseCase = getRideRequestUseCase;
        this.getFareEstimateUseCase = getFareEstimateUseCase;
    }


    @Override
    public void initialize() {
        getView().showLoadingWaitingResponse();
        getView().hideRideRequestStatus();
        getView().hideCancelRequestButton();

        if (getView().isAlreadyRequested()) {
            getView().startPeriodicService(getView().getRequestId());
        } else {
            getView().setViewListener();
            if (getView().isSurge()) {
                getView().openInterruptConfirmationWebView(getView().getSurgeConfirmationHref());
            } else {
                actionRideRequest(getView().getParam());
            }
        }

        if (checkPlayServices()) {
            createLocationRequest();
            initializeLocationService();
        }
    }

    /**
     * This function combines first checks the fare estimate again and then make a create ride request with latest fare id.
     *
     * @param requestParam, parameters required for ride request
     */
    @Override
    public void actionRideRequest(final RequestParams requestParam) {
        getView().showRequestLoadingLayout();
        getFareEstimateUseCase.execute(getView().getFareEstimateParam(), new Subscriber<FareEstimate>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().hideRequestLoadingLayout();
                    getView().showFailedRideRequestMessage(e.getMessage());
                    getView().failedToRequestRide(e.getMessage());
                }
            }

            @Override
            public void onNext(FareEstimate fareEstimate) {
                if (fareEstimate.getFare() != null) {
                    //update fare id with latest fare id
                    requestParam.putString(CreateRideRequestUseCase.PARAM_FARE_ID, fareEstimate.getFare().getFareId());
                    createRideRequest(requestParam);
                } else if (fareEstimate.getEstimate() != null) {
                    //open surge confirmation webview
                    getView().openInterruptConfirmationWebView(getView().getSurgeConfirmationHref());
                }
            }
        });
    }

    public void createRideRequest(RequestParams requestParams) {
        createRideRequestUseCase.execute(requestParams, new Subscriber<RideRequest>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();

                if (!isViewAttached()) return;
                getView().hideRequestLoadingLayout();
                if (e instanceof InterruptConfirmationHttpException) {
                    if (!(e.getCause() instanceof JsonSyntaxException)) {
                        getView().openInterruptConfirmationWebView(((InterruptConfirmationHttpException) e).getTosUrl());
                    } else {
                        getView().showFailedRideRequestMessage(e.getMessage());
                        getView().failedToRequestRide(e.getMessage());
                    }
                    ///getView().hideLoadingWaitingResponse();
                    //getView().hideRideRequestStatus();
                } else if (e instanceof UnprocessableEntityHttpException) {
                    //get fare id again
                    getView().showFailedRideRequestMessage(getView().getResourceString(R.string.error_invalid_fare_id));
                    getView().failedToRequestRide(getView().getResourceString(R.string.error_invalid_fare_id));
                } else {
                    getView().showFailedRideRequestMessage(e.getMessage());
                    getView().failedToRequestRide(e.getMessage());
                }
            }

            @Override
            public void onNext(RideRequest rideRequest) {
                if (isViewAttached()) {
                    activeRideRequest = rideRequest;
                    getView().setRequestId(rideRequest.getRequestId());
                    getView().onSuccessCreateRideRequest(rideRequest);
                    getView().startPeriodicService(rideRequest.getRequestId());
                    proccessGetCurrentRideRequest(rideRequest);
                }
            }
        });
    }

    @Override
    public void actionCancelRide() {
        cancelRideRequestUseCase.execute(getView().getCancelParams(), new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showMessage(e.getMessage());
            }

            @Override
            public void onNext(String s) {
                if (isViewAttached()) {
                    getView().onSuccessCancelRideRequest();
                    getView().clearActiveNotification();
                }
            }
        });

    }

    @Override
    public void proccessGetCurrentRideRequest(RideRequest result) {
        getView().setRequestId(result.getRequestId());
        if (result.getAddress() != null) {
            getView().setAddressPickerText(result.getAddress().getStartAddressName(), result.getAddress().getEndAddressName());
        }
        //processing accepted arriving in_progress driver_canceled completed
        switch (result.getStatus()) {
            case RideStatus.NO_DRIVER_AVAILABLE:
                getView().hideRequestLoadingLayout();
                getView().hideFindingUberNotification();
                getView().hideLoadingWaitingResponse();
                getView().showNoDriverAvailableDialog();
                break;
            case RideStatus.PROCESSING:
                getView().showFindingUberNotification();
                getView().showLoadingWaitingResponse();
                getView().showCancelRequestButton();
                break;
            case RideStatus.ACCEPTED:
                getView().hideRequestLoadingLayout();
                getView().hideFindingUberNotification();
                getView().hideCancelRequestButton();
                getView().hideLoadingWaitingResponse();
                getView().showAcceptedNotification(result);
                getView().renderAcceptedRequest(result);
                getView().showBottomSection();
                break;
            case RideStatus.ARRIVING:
                getView().hideRequestLoadingLayout();
                getView().hideFindingUberNotification();
                getView().hideCancelRequestButton();
                getView().hideLoadingWaitingResponse();
                getView().showBottomSection();
                getView().renderAcceptedRequest(result);
                getView().renderArrivingDriverEvent(result);

                break;
            case RideStatus.IN_PROGRESS:
                getView().hideRequestLoadingLayout();
                getView().hideFindingUberNotification();
                getView().hideAcceptedNotification();
                getView().hideCancelRequestButton();
                getView().hideLoadingWaitingResponse();
                getView().showBottomSection();
                getView().renderAcceptedRequest(result);
                getView().renderInProgressRequest(result);
                break;
            case RideStatus.DRIVER_CANCELED:
                getView().hideRequestLoadingLayout();
                getView().hideFindingUberNotification();
                getView().hideAcceptedNotification();
                getView().renderDriverCanceledRequest(result);
                break;
            case RideStatus.RIDER_CANCELED:
                getView().hideRequestLoadingLayout();
                getView().hideFindingUberNotification();
                getView().hideAcceptedNotification();
                getView().renderRiderCanceledRequest(result);
                break;
            case RideStatus.COMPLETED:
                getView().hideRequestLoadingLayout();
                getView().hideFindingUberNotification();
                getView().hideAcceptedNotification();
                getView().renderCompletedRequest(result);
                break;
            default:

        }
    }

    @Override
    public void getOverViewPolyLine() {
        getOverviewPolylineUseCase.execute(getView().getPolyLineParam(), new Subscriber<List<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(List<String> strings) {
                if (isViewAttached()) {
                    List<List<LatLng>> routes = new ArrayList<>();
                    for (String route : strings) {
                        routes.add(PolyUtil.decode(route));
                    }
                    getView().renderTripRoute(routes);
                    if (activeRideRequest != null) {
                        getView().renderSourceMarker(activeRideRequest.getPickup().getLatitude(),
                                activeRideRequest.getDestination().getLongitude());
                        getView().renderDestinationMarker(activeRideRequest.getPickup().getLatitude(),
                                activeRideRequest.getDestination().getLongitude());
                        getView().zoomMapFitWithSourceAndDestination(
                                activeRideRequest.getPickup().getLatitude(),
                                activeRideRequest.getDestination().getLongitude(),
                                activeRideRequest.getPickup().getLatitude(),
                                activeRideRequest.getDestination().getLongitude()
                        );
                    }
                }
            }
        });
    }

    @Override
    public void actionShareEta() {
        getRideRequestMapUseCase.execute(getView().getShareEtaParam(), new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().showFailedShare();
                }
            }

            @Override
            public void onNext(String shareUrl) {
                if (isViewAttached()) {
                    getView().showShareDialog(shareUrl);
                }
            }
        });
    }


    /**
     * This is a task to poll the request details api after every 2 seconds
     */
    private Runnable timedTask = new Runnable() {
        @Override
        public void run() {

            //if get view is null
            if (getView() == null) {
                if (handler != null) {
                    handler.postDelayed(timedTask, CURRENT_REQUEST_DETAIL_POLLING_TIME_DELAY);
                }

                return;
            }


            //request the current ride details
            getRideRequestUseCase.execute(getView().getCurrentRequestParams(getView().getRequestId()),
                    new Subscriber<RideRequest>() {
                        @Override
                        public void onCompleted() {
                            CommonUtils.dumper("GetCurrentRideRequestService timedTask complete");
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.dumper("GetCurrentRideRequestService timedTask error = " + e.toString());
                        }

                        @Override
                        public void onNext(RideRequest rideRequest) {
                            CommonUtils.dumper("GetCurrentRideRequestService timedTask onNext");

                            //return of fragment finished on not present
                            if (getView() == null) {
                                return;
                            }
                            activeRideRequest = rideRequest;

                            proccessGetCurrentRideRequest(rideRequest);
                            handler.postDelayed(timedTask, CURRENT_REQUEST_DETAIL_POLLING_TIME_DELAY);
                        }
                    });
        }
    };

    @Override
    public void startGetRequestDetailsPeriodicService(String requestId) {
        handler.postDelayed(timedTask, CURRENT_REQUEST_DETAIL_POLLING_TIME_DELAY);
    }

    @Override
    public void actionOnReceivePushNotification(Observable<RideRequest> rideObservable) {
        rideObservable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RideRequest>() {
                    @Override
                    public void call(RideRequest rideRequest) {
                        if (isViewAttached()) {
                            activeRideRequest = rideRequest;
                            proccessGetCurrentRideRequest(rideRequest);
                        }
                    }
                });
    }

    @Override
    public void getDriverBitmap(final RemoteViews remoteView, final String imgUrl) {
        Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                Bitmap bm = null;
                try {
                    URL aURL = new URL(imgUrl);
                    URLConnection conn = aURL.openConnection();
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(is);
                    bm = BitmapFactory.decodeStream(bis);
                    bis.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                subscriber.onNext(bm);

            }
        })
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Bitmap>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        if (isViewAttached()) {
                            getView().updateDriverBitmapInNotification(remoteView, bitmap);
                        }
                    }
                });
    }

    @Override
    public void detachView() {
        cancelRideRequestUseCase.unsubscribe();
        createRideRequestUseCase.unsubscribe();
        getOverviewPolylineUseCase.unsubscribe();
        getRideRequestMapUseCase.unsubscribe();
        super.detachView();
    }

    @Override
    public void onMapReady() {
        getView().setMapViewListener();
        if (!getView().isAlreadyRequested()) {
            getOverViewPolyLine();
        }
    }

    @Override
    public void actionCallDriver() {
        getView().checkAndExecuteCallPermission(activeRideRequest.getDriver().getPhoneNumber());
    }

    @Override
    public void actionMessageDriver() {
        getView().openSmsIntent(activeRideRequest.getDriver().getPhoneNumber());
    }

    @Override
    public boolean checkIsAnyPendingRequest() {
        return activeRideRequest != null;
    }

    @Override
    public void actionGoToCurrentLocation() {
        if (mCurrentLocation != null) {
            getView().moveMapToLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(getView().getActivity());
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result))
                getView().showMessage(getView().getActivity().getString(R.string.unavailable_play_service));
            else {
                getView().showMessage(getView().getActivity().getString(R.string.unsupported_device));
            }
            return false;
        }
        return true;
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void initializeLocationService() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getView().getActivity())
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(@Nullable Bundle bundle) {
                            if (getFuzedLocation() != null) {
                                mCurrentLocation = getFuzedLocation();
                                startLocationUpdates();
                            } else {
                                checkLocationSettings();
                            }
                        }

                        @Override
                        public void onConnectionSuspended(int i) {

                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                        }
                    })
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .build();
        }

        mGoogleApiClient.connect();
    }

    /**
     * This functions checks if locations is not enabledm shows a dialog to enable to location
     */
    private void checkLocationSettings() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                //final LocationSettingsStates s= result.getLocationSettingsStates();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.
                        mCurrentLocation = getFuzedLocation();
                        startLocationUpdates();

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(getView().getActivity(), RideHomeMapFragment.REQUEST_CHECK_LOCATION_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.

                        getView().showMessage(getView().getActivity().getString(R.string.msg_enter_location));
                        break;
                }
            }
        });
    }

    public Location getFuzedLocation() {
        if ((ActivityCompat.checkSelfPermission(getView().getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
                && (ActivityCompat.checkSelfPermission(getView().getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)) {
            return null;
        }

        return LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    private void startLocationUpdates() {
        if ((ActivityCompat.checkSelfPermission(getView().getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
                && (ActivityCompat.checkSelfPermission(getView().getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mCurrentLocation = location;
            }
        });
    }


}
