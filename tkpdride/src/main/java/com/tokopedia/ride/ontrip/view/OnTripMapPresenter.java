package com.tokopedia.ride.ontrip.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.RemoteViews;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonSyntaxException;
import com.google.maps.android.PolyUtil;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.domain.GetOverviewPolylineUseCase;
import com.tokopedia.ride.common.exception.TosConfirmationHttpException;
import com.tokopedia.ride.common.ride.domain.model.RideRequest;
import com.tokopedia.ride.ontrip.domain.CancelRideRequestUseCase;
import com.tokopedia.ride.ontrip.domain.CreateRideRequestUseCase;
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
import rx.schedulers.Schedulers;

/**
 * Created by alvarisi on 3/24/17.
 */

public class OnTripMapPresenter extends BaseDaggerPresenter<OnTripMapContract.View>
        implements OnTripMapContract.Presenter {

    private CreateRideRequestUseCase createRideRequestUseCase;
    private CancelRideRequestUseCase cancelRideRequestUseCase;
    private GetOverviewPolylineUseCase getOverviewPolylineUseCase;
    private GetRideRequestMapUseCase getRideRequestMapUseCase;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;

    public OnTripMapPresenter(CreateRideRequestUseCase createRideRequestUseCase,
                              CancelRideRequestUseCase cancelRideRequestUseCase,
                              GetOverviewPolylineUseCase getOverviewPolylineUseCase,
                              GetRideRequestMapUseCase getRideRequestMapUseCase) {
        this.createRideRequestUseCase = createRideRequestUseCase;
        this.cancelRideRequestUseCase = cancelRideRequestUseCase;
        this.getOverviewPolylineUseCase = getOverviewPolylineUseCase;
        this.getRideRequestMapUseCase = getRideRequestMapUseCase;
    }

    @Override
    public void initialize() {
        if (checkPlayServices()) {
            createLocationRequest();
            initializeLocationService();
            getView().showLoadingWaitingResponse();
            getView().hideRideRequestStatus();
            getView().hideCancelRequestButton();

            if (getView().isWaitingResponse()) {
                getView().startPeriodicService(getView().getRequestId());
            } else {
                actionRideRequest(getView().getParam());
            }
        }
    }


    private void actionRideRequest(RequestParams requestParams) {
        createRideRequestUseCase.execute(requestParams, new Subscriber<RideRequest>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof TosConfirmationHttpException) {
                    if (!(e.getCause() instanceof JsonSyntaxException)) {
                        getView().openTosConfirmationWebView(((TosConfirmationHttpException) e).getTosUrl());
                    } else {
                        getView().failedToRequestRide();
                    }
                    getView().hideLoadingWaitingResponse();
                    getView().hideRideRequestStatus();
                } else {
                    getView().showFailedRideRequestMessage(e.getMessage());
                    getView().failedToRequestRide();
                }
            }

            @Override
            public void onNext(RideRequest rideRequest) {
                getView().onSuccessCreateRideRequest(rideRequest);
                getView().startPeriodicService(rideRequest.getRequestId());
                //getView().showCancelRequestButton();
                proccessGetCurrentRideRequest(rideRequest);
            }
        });
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

                                getView().moveToCurrentLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                            } else {
                                getView().showMessage(getView().getActivity().getString(R.string.location_permission_required));
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

    @Override
    public void goToMyLocation() {
        getView().moveToCurrentLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
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
                getView().showMessage(s);
                getView().onSuccessCancelRideRequest();
            }
        });

    }

    @Override
    public void actionRetryRideRequest(String id) {
        RequestParams requestParams = getView().getParam();
        requestParams.putString("tos_confirmation_id", id);
        createRideRequestUseCase.execute(requestParams, new Subscriber<RideRequest>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof TosConfirmationHttpException) {
                    if (!(e.getCause() instanceof JsonSyntaxException)) {
                        getView().openTosConfirmationWebView(((TosConfirmationHttpException) e).getTosUrl());
                    } else {
                        getView().failedToRequestRide();
                    }
                    getView().hideLoadingWaitingResponse();
                    getView().hideRideRequestStatus();
                } else {
                    getView().showFailedRideRequestMessage(e.getMessage());
                    getView().failedToRequestRide();
                }
            }

            @Override
            public void onNext(RideRequest rideRequest) {
                proccessGetCurrentRideRequest(rideRequest);
                getView().onSuccessCreateRideRequest(rideRequest);
                //getView().showCancelRequestButton();
            }
        });
    }

    @Override
    public void proccessGetCurrentRideRequest(RideRequest result) {
        //processing accepted arriving in_progress driver_canceled completed
        switch (result.getStatus()) {
            case "no_drivers_available":
                getView().cancelFindingUberNotification();
                getView().showLoadingWaitingResponse();
                getView().clearRideConfiguration();
                getView().showNoDriverAvailableDialog();
                break;
            case "processing":
                getView().showFindingUberNotification();
                getView().showLoadingWaitingResponse();
                getView().showCancelRequestButton();
                break;
            case "accepted":
                getView().cancelFindingUberNotification();
                getView().hideCancelRequestButton();
                getView().hideLoadingWaitingResponse();
                getView().showAcceptedNotification(result);
                getView().showRequestRideStatus(String.format("Driver will pick in %s minutes", String.valueOf(result.getPickup().getEta())));
                getView().renderAcceptedRequest(result);
                getView().showBottomSection();
                break;
            case "arriving":
                getView().cancelFindingUberNotification();
                getView().hideCancelRequestButton();
                getView().hideLoadingWaitingResponse();
                getView().showBottomSection();
                getView().showRequestRideStatus(String.format("Driver will pick in %s minutes", String.valueOf(result.getPickup().getEta())));
                getView().renderArrivingDriverEvent(result);
                break;
            case "in_progress":
                getView().cancelFindingUberNotification();
                getView().hideCancelRequestButton();
                getView().hideLoadingWaitingResponse();
                getView().showBottomSection();
                getView().renderInProgressRequest(result);
                getView().showRequestRideStatus(String.format("Will arrive to destination in %s minutes", String.valueOf(result.getDestination().getEta())));
                break;
            case "driver_canceled":
                getView().cancelFindingUberNotification();
                getView().renderDriverCanceledRequest(result);
                getView().clearRideConfiguration();
                break;
            case "rider_canceled":
                getView().cancelFindingUberNotification();
                getView().renderRiderCanceledRequest(result);
                getView().clearRideConfiguration();
                break;
            case "completed":
                getView().cancelFindingUberNotification();
                getView().renderCompletedRequest(result);
                getView().clearRideConfiguration();
                break;
            default:

        }
    }

    @Override
    public void getOverViewPolyLine(double startLat, double startLng, double destLat, double destLng) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("origin", String.format("%s,%s",
                startLat,
                startLng
        ));
        requestParams.putString("destination", String.format("%s,%s",
                destLat,
                destLng
        ));
        requestParams.putString("sensor", "false");
        getOverviewPolylineUseCase.execute(requestParams, new Subscriber<List<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(List<String> strings) {
                List<List<LatLng>> routes = new ArrayList<>();
                for (String route : strings) {
                    routes.add(PolyUtil.decode(route));
                }
                getView().renderTripRoute(routes);
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
                getView().showFailedShare();
            }

            @Override
            public void onNext(String shareUrl) {
                getView().showShareDialog(shareUrl);
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
                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        getView().updateDriverBitmapInNotification(remoteView, bitmap);
                    }
                });
    }
}
