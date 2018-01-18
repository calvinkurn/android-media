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
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.domain.GetLocationAddressUseCase;
import com.tokopedia.ride.bookingride.view.fragment.RideHomeMapFragment;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;
import com.tokopedia.ride.common.place.data.entity.ReverseGeoCodeAddress;
import com.tokopedia.ride.common.ride.utils.RideUtils;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by vishal.gupta on 5/19/17.
 */

public class SelectLocationOnMapPresenter extends BaseDaggerPresenter<SelectLocationOnMapContract.View> implements SelectLocationOnMapContract.Presenter {
    private final GetLocationAddressUseCase getLocationAddressUseCase;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;


    @Inject
    public SelectLocationOnMapPresenter(GetLocationAddressUseCase getLocationAddressUseCase) {
        this.getLocationAddressUseCase = getLocationAddressUseCase;
    }

    @Override
    public void initialize() {
        if (checkPlayServices()) {
            createLocationRequest();
            initializeLocationService();
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
                                if (getView().getDefaultLocation() == null) {
                                    getView().moveMapToLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                                    actionMapDragStopped(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
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
                            status.startResolutionForResult(getView().getActivity(), RideHomeMapFragment.REQUEST_CHECK_LOCATION_SETTING_REQUEST_CODE);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.

                        getView().showMessage(getView().getActivity().getString(R.string.location_permission_required));
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

    @Override
    public void onDestroy() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onDone() {

    }

    @Override
    public void onMapMoveCameraStarted() {
        CommonUtils.dumper("onMapMoveCameraStarted ");
        getView().disableDoneButton();
        getView().showCrossLoading();
        getView().setDestinationLocationText(null);
    }

    @Override
    public void onMapMoveCameraIdle() {
        CommonUtils.dumper("onMapMoveCameraIdle = ");
        if (getView() != null) {
            getView().onMapDraggedStopped();
        }
    }

    @Override
    public void actionMapDragStopped(final double latitude, final double longitude) {
        getView().showCrossLoading();
        getView().disableDoneButton();
        CommonUtils.dumper("actionMapDragStopped = " + latitude + " , " + longitude);

        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetLocationAddressUseCase.PARAM_LATITUDE, String.valueOf(latitude));
        requestParams.putString(GetLocationAddressUseCase.PARAM_LONGITUDE, String.valueOf(longitude));
        requestParams.putString(GetLocationAddressUseCase.PARAM_KEY, getView().getActivity().getString(R.string.GOOGLE_API_KEY));

        //find address using latitude and longitude
        getLocationAddressUseCase.execute(requestParams, new Subscriber<ReverseGeoCodeAddress>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                String sourceAddress = String.valueOf(latitude) + "," + String.valueOf(longitude);
                handleAddressResult(sourceAddress, sourceAddress);
            }

            @Override
            public void onNext(ReverseGeoCodeAddress reverseGeoCodeAddres) {
                String title = RideUtils.getShortAddress(reverseGeoCodeAddres);
                String address = reverseGeoCodeAddres.getFormattedAddress();
                handleAddressResult(title, address);
            }

            private void handleAddressResult(String title, String address) {
                if (isViewAttached() && !isUnsubscribed()) {
                    CommonUtils.dumper("actionMapDragStopped onNext 1 = " + address);
                    PlacePassViewModel destination = new PlacePassViewModel();
                    destination.setAndFormatLatitude(latitude);
                    destination.setAndFormatLongitude(longitude);
                    destination.setAddress(address);
                    destination.setTitle(title);

                    getView().setDestination(destination);
                    getView().setDestinationLocationText(title);
                    getView().hideCrossLoading();
                    getView().enableDoneButton();
                }
            }
        });
    }
}
