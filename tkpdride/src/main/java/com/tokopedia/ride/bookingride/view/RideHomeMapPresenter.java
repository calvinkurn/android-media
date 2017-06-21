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
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.geolocation.utils.GeoLocationUtils;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.domain.GetOverviewPolylineUseCase;
import com.tokopedia.ride.bookingride.domain.GetUberProductsUseCase;
import com.tokopedia.ride.bookingride.view.fragment.RideHomeMapFragment;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;
import com.tokopedia.ride.common.place.domain.model.OverviewPolyline;
import com.tokopedia.ride.common.ride.domain.model.Product;

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
    private final GetUberProductsUseCase mGetUberProductsUseCase;
    private final GetOverviewPolylineUseCase getOverviewPolylineUseCase;
    private boolean isMapDragging = false;
    private Location mCurrentLocation;
    private boolean mRenderProductListBasedOnLocationUpdates;
    private boolean mSourceIsCurrentLocation;

    public RideHomeMapPresenter(GetUberProductsUseCase getUberProductsUseCase,
                                GetOverviewPolylineUseCase getOverviewPolylineUseCase) {
        mGetUberProductsUseCase = getUberProductsUseCase;
        this.getOverviewPolylineUseCase = getOverviewPolylineUseCase;
    }

    @Override
    public void initialize() {
        if (getView().isUserLoggedIn()) {
            if (getView().isUserPhoneNumberVerified()) {
                if (checkPlayServices()) {
                    createLocationRequest();
                    initializeLocationService();
                }
            } else {
                getView().showVerificationPhoneNumberPage();
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
                                //getView().showMessage(getView().getActivity().getString(R.string.msg_enter_location), getView().getActivity().getString(R.string.btn_enter_location));
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
                        if (!getView().isLaunchedWithLocation()) {
                            setSourceAsCurrentLocation();
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
        if (mCurrentLocation == null) return;

        if (getView() == null) return;

        mSourceIsCurrentLocation = true;

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
//                            placeVm.setType(PlacePassViewModel.TYPE.OTHER);
                            getView().setSourceLocation(placeVm);
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
                if (mRenderProductListBasedOnLocationUpdates) {
                    mRenderProductListBasedOnLocationUpdates = false;
                    setSourceAsCurrentLocation();
                }
            }
        });
    }

    @Override
    public void getAvailableProducts() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetUberProductsUseCase.PARAM_LATITUDE, "latitude");
        requestParams.putString(GetUberProductsUseCase.PARAM_LONGITUDE, "longitude");
        mGetUberProductsUseCase.execute(requestParams, new Subscriber<List<Product>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Product> products) {

            }
        });
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
        requestParams.putString("origin", String.format("%s,%s",
                sourceLat,
                sourceLng
        ));
        requestParams.putString("destination", String.format("%s,%s",
                destinationLat,
                destinationLng
        ));
        requestParams.putString("sensor", "false");
//        requestParams.putString("key", getView().getActivity().getString(R.string.GOOGLE_API_KEY));
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
        /*getOverviewPolylineUseCase.execute(requestParams, new Subscriber<List<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(List<String> strings) {
                if (isViewAttached() && !isUnsubscribed()) {
                    List<List<LatLng>> routes = new ArrayList<>();
                    for (String route : strings) {
                        routes.add(PolyUtil.decode(route));
                    }
                    getView().renderTripPolyline(routes);
                }
            }
        });*/
    }

    @Override
    public void actionMyLocation() {
        if (!getView().isAlreadySelectDestination()) {
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
        mGetUberProductsUseCase.unsubscribe();
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
}