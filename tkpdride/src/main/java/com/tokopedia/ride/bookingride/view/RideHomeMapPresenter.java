package com.tokopedia.ride.bookingride.view;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.geolocation.utils.GeoLocationUtils;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.domain.GetOverviewPolylineUseCase;
import com.tokopedia.ride.bookingride.view.fragment.RideHomeMapFragment;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;
import com.tokopedia.ride.common.configuration.MapConfiguration;
import com.tokopedia.ride.common.place.domain.model.OverviewPolyline;
import com.tokopedia.ride.common.ride.utils.GoogleAPIClientObservable;
import com.tokopedia.ride.common.ride.utils.PendingResultObservable;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

/**
 * Created by alvarisi on 3/13/17.
 */

public class RideHomeMapPresenter extends BaseDaggerPresenter<RideHomeMapContract.View>
        implements RideHomeMapContract.Presenter {

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private final GetOverviewPolylineUseCase getOverviewPolylineUseCase;
    private boolean isMapDragging = false;
    private Location mCurrentLocation;
    private boolean mRenderProductListBasedOnLocationUpdates;
    private boolean mSourceIsCurrentLocation;
    private MapConfiguration mapConfiguration;

    public RideHomeMapPresenter(GetOverviewPolylineUseCase getOverviewPolylineUseCase) {
        this.getOverviewPolylineUseCase = getOverviewPolylineUseCase;
    }

    @Override
    public void initialize() {
        mapConfiguration = new MapConfiguration(getView().getActivityContext());

        if (getView().isUserLoggedIn()) {
            if (checkPlayServices()) {
                createLocationRequest();
                initializeLocationService();
            } else {
                getView().moveMapToLocation(mapConfiguration.getDefaultLatitude(), mapConfiguration.getDefaultLongitude());
                getView().showMessage(getView().getActivity().getString(R.string.msg_enter_location), getView().getActivity().getString(R.string.btn_enter_location));
            }

        } else {
            getView().navigateToLoginPage();
        }
    }


    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(getView().getActivity());
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result))
                getView().showMessage(getView().getActivity().getString(R.string.unavailable_play_service), null);
            else {
                getView().showMessage(getView().getActivity().getString(R.string.unsupported_device), null);
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
                                if (!getView().isLaunchedWithLocation()) {
                                    setSourceAsCurrentLocation();
                                    mRenderProductListBasedOnLocationUpdates = true;
                                } else {
                                    mRenderProductListBasedOnLocationUpdates = false;
                                }

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

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.
                        mCurrentLocation = getFuzedLocation();
                        startLocationUpdates();
                        if (!getView().isLaunchedWithLocation()) {
                            setSourceAsCurrentLocation();
                        }

                        if (mapConfiguration != null) {
                            mapConfiguration.setDefaultLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                        }

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(getView().getActivity(), RideHomeMapFragment.REQUEST_CHECK_LOCATION_SETTING_REQUEST_CODE);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.

                        getView().showMessage(getView().getActivity().getString(R.string.msg_enter_location), getView().getActivity().getString(R.string.btn_enter_location));
                        break;
                }
            }
        });
    }

    private void setSourceAsCurrentLocation() {
        if (getView() == null) return;

        if (mCurrentLocation == null) {
            return;
        }

        mSourceIsCurrentLocation = true;

        if (ActivityCompat.checkSelfPermission(getView().getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            getView().moveMapToLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

            //set source as current location
            Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
                    try {
                        subscriber.onNext(GeoLocationUtils.reverseGeoCodeToShortAdd(getView().getActivity(),
                                mCurrentLocation.getLatitude(),
                                mCurrentLocation.getLongitude()
                        ));
                    } catch (IOException e) {
                        e.printStackTrace();
                        subscriber.onNext(String.valueOf(mCurrentLocation.getLatitude()) + ", " + String.valueOf(mCurrentLocation.getLongitude()));

                        //if address not found and destination not selected keep looking for address using current current location
                        if (!getView().isLaunchedWithLocation() && !getView().isAlreadySelectDestination()) {
                            mRenderProductListBasedOnLocationUpdates = true;
                        }
                    }
                }
            }).onErrorReturn(new Func1<Throwable, String>() {
                @Override
                public String call(Throwable throwable) {
                    return String.valueOf(mCurrentLocation.getLatitude()) + ", " + String.valueOf(mCurrentLocation.getLongitude());
                }
            })
                    .subscribeOn(Schedulers.newThread())
                    .unsubscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onNext(String sourceAddress) {
                            if (isViewAttached()) {
                                PlacePassViewModel placeVm = new PlacePassViewModel();
                                placeVm.setAddress(sourceAddress);
                                placeVm.setAndFormatLatitude(mCurrentLocation.getLatitude());
                                placeVm.setAndFormatLongitude(mCurrentLocation.getLongitude());
                                placeVm.setTitle(sourceAddress);
                                getView().setSourceLocation(placeVm);
                            }
                        }
                    });
        } else {
            getCurrentPlace();
        }
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
                if (mRenderProductListBasedOnLocationUpdates) {
                    mRenderProductListBasedOnLocationUpdates = false;
                    setSourceAsCurrentLocation();
                }

                if (mapConfiguration != null) {
                    mapConfiguration.setDefaultLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                }

            }
        });
    }

    private boolean isLocationPermissionAvailable() {
        return (ActivityCompat.checkSelfPermission(getView().getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
                && (ActivityCompat.checkSelfPermission(getView().getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onMapMoveCameraStarted() {
        if (!isMapDragging) {
            getView().onMapDragStarted();
            mSourceIsCurrentLocation = false;
        }
        isMapDragging = true;
    }

    @Override
    public void onMapMoveCameraIdle() {
        if (isMapDragging) {
            getView().onMapDragStopped();
            isMapDragging = false;
        }
    }

    @Override
    public void getOverviewPolyline(double sourceLat, double sourceLng, double destinationLat, double destinationLng) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetOverviewPolylineUseCase.PARAM_ORIGIN, String.format("%s,%s",
                sourceLat,
                sourceLng
        ));
        requestParams.putString(GetOverviewPolylineUseCase.PARAM_DESTINATION, String.format("%s,%s",
                destinationLat,
                destinationLng
        ));
        requestParams.putString(GetOverviewPolylineUseCase.PARAM_SENSOR, "false");
        requestParams.putString(GetOverviewPolylineUseCase.PARAM_KEY, getView().getActivity().getString(R.string.google_api_key));

        getOverviewPolylineUseCase.execute(requestParams, new Subscriber<List<OverviewPolyline>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<OverviewPolyline> overviewPolylines) {
                if (isViewAttached() && !isUnsubscribed()) {
                    getView().renderTripPolyline(overviewPolylines);
                }
            }
        });
    }

    @Override
    public void actionMyLocation() {
        if (!getView().isAlreadySelectDestination()) {

            //if current location is null, ask permission to enable location
            if (mCurrentLocation == null) {
                checkLocationSettings();
                return;
            }

            setSourceAsCurrentLocation();
        } else {
            if (mCurrentLocation != null) {
                getView().moveMapToLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            }
        }
    }

    @Override
    public void handleEnableLocationDialogResult(int resultCode) {
        if (resultCode == RESULT_OK) {
            if (getFuzedLocation() != null) {
                mCurrentLocation = getFuzedLocation();
                startLocationUpdates();
                setSourceAsCurrentLocation();
            } else {
                mRenderProductListBasedOnLocationUpdates = true;
                startLocationUpdates();
            }
        } else {
            getView().showMessage(getView().getActivity().getString(R.string.msg_enter_location), getView().getActivity().getString(R.string.btn_enter_location));
        }
    }

    @Override
    public void actionMapDragStopped(final double latitude, final double longitude) {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    subscriber.onNext(GeoLocationUtils.reverseGeoCodeToShortAdd(getView().getActivity(),
                            latitude,
                            longitude
                    ));
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onNext(String.valueOf(latitude) + ", " + String.valueOf(longitude));
                }
            }
        }).onErrorReturn(new Func1<Throwable, String>() {
            @Override
            public String call(Throwable throwable) {
                return String.valueOf(latitude) + ", " + String.valueOf(longitude);
            }
        })
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(String sourceAddress) {
                        if (isViewAttached() && !isUnsubscribed()) {
                            getView().renderDefaultPickupLocation(latitude, longitude, sourceAddress);
                        }
                    }
                });
    }

    @Override
    public void detachView() {
        getOverviewPolylineUseCase.unsubscribe();
        super.detachView();
    }

    @Override
    public void appResumedFromBackground() {
        if (!getView().isLaunchedWithLocation() && !getView().isAlreadySelectDestination() && mSourceIsCurrentLocation) {
            mRenderProductListBasedOnLocationUpdates = true;
        }
    }

    @Override
    public void setSourceSelectedFromAddress() {
        mSourceIsCurrentLocation = false;
    }

    private void getCurrentPlace() {
        getCurrentPlace(new PlaceFilter())
                .map(new Func1<PlaceLikelihoodBuffer, Place>() {
                    @Override
                    public Place call(PlaceLikelihoodBuffer buffer) {
                        if (buffer != null) {
                            PlaceLikelihood likelihood = buffer.get(0);
                            if (likelihood != null) {
                                return likelihood.getPlace();
                            }
                            buffer.release();
                            return null;
                        } else {
                            return null;
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Place>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (mCurrentLocation != null && getView() != null) {
                            getView().moveMapToLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                            PlacePassViewModel placeVm = new PlacePassViewModel();
                            placeVm.setAddress(String.valueOf(mCurrentLocation.getLatitude()) + ", " + String.valueOf(mCurrentLocation.getLongitude()));
                            placeVm.setAndFormatLatitude(mCurrentLocation.getLatitude());
                            placeVm.setAndFormatLongitude(mCurrentLocation.getLongitude());
                            placeVm.setTitle(String.valueOf(mCurrentLocation.getLatitude()) + ", " + String.valueOf(mCurrentLocation.getLongitude()));
                            getView().setSourceLocation(placeVm);
                        }
                    }

                    @Override
                    public void onNext(Place place) {
                        if (place != null && getView() != null) {
                            getView().moveMapToLocation(place.getLatLng().latitude, place.getLatLng().longitude);
                            PlacePassViewModel placeVm = new PlacePassViewModel();
                            placeVm.setAddress(String.valueOf(place.getName()));
                            placeVm.setAndFormatLatitude(place.getLatLng().latitude);
                            placeVm.setAndFormatLongitude(place.getLatLng().longitude);
                            placeVm.setPlaceId(place.getId());
                            placeVm.setTitle(String.valueOf(place.getName()));
                            getView().setSourceLocation(placeVm);
                        }
                    }
                });
    }

    public Observable<PlaceLikelihoodBuffer> getCurrentPlace(@javax.annotation.Nullable final PlaceFilter placeFilter) {
        return getGoogleApiClientObservable(Places.PLACE_DETECTION_API, Places.GEO_DATA_API)
                .flatMap(new Func1<GoogleApiClient, Observable<PlaceLikelihoodBuffer>>() {
                    @Override
                    public Observable<PlaceLikelihoodBuffer> call(GoogleApiClient api) {
                        if (ActivityCompat.checkSelfPermission(getView().getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return Observable.empty();
                        }
                        return fromPendingResult(Places.PlaceDetectionApi.getCurrentPlace(api, placeFilter));
                    }
                });
    }

    public Observable<GoogleApiClient> getGoogleApiClientObservable(Api... apis) {
        //noinspection unchecked
        return GoogleAPIClientObservable.create(getView().getActivity(), apis);
    }


    public <T extends Result> Observable<T> fromPendingResult(PendingResult<T> result) {
        return Observable.create(new PendingResultObservable<>(result));
    }

    @Override
    public void saveLocation(Location location) {

    }
}