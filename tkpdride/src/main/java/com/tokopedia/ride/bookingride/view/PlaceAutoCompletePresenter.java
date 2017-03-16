package com.tokopedia.ride.bookingride.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataBufferUtils;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.geolocation.utils.GeoLocationUtils;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.PlaceAutoCompeleteViewModel;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by alvarisi on 3/15/17.
 */

public class PlaceAutoCompletePresenter extends BaseDaggerPresenter<PlaceAutoCompleteContract.View> implements PlaceAutoCompleteContract.Presenter {
    private static final String TAG = "addressautocomplete";
    GoogleApiClient mGoogleApiClient;
    CompositeSubscription mCompositeSubscription;
    AutocompleteFilter mAutocompleteFilter;
    Location mCurrentLocation;

    private LocationRequest mLocationRequest;

    public PlaceAutoCompletePresenter() {
    }

    @Override
    public void initialize() {
        if (checkPlayServices()) {
            createLocationRequest();
            initializeLocationService();
            prepareInitialView();
            mCompositeSubscription = new CompositeSubscription();
            AutocompleteFilter.Builder mbuilder = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE);
            mAutocompleteFilter = mbuilder.build();
        }
    }

    private void prepareInitialView() {
        getView().hideAutoCompleteLoadingCross();
        getView().showAutoDetectLocationButton();
        getView().showHomeLocationButton();
        getView().showWorkLocationButton();
        getView().hideListPlaces();
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

    private Location getFuzedLocation() {
        if ((ActivityCompat.checkSelfPermission(getView().getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
                && (ActivityCompat.checkSelfPermission(getView().getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)) {
            return null;
        }

        return LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    @Override
    public void actionQueryPlacesByKeyword(final String keyword) {
        getView().showAutoCompleteLoadingCross();
        getView().hideAutoDetectLocationButton();
        getView().hideHomeLocationButton();
        getView().hideWorkLocationButton();
        mCompositeSubscription.add(
                Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext(String.valueOf(keyword));
                    }
                }).debounce(500, TimeUnit.MILLISECONDS)
                        .subscribe(new AutoCompletePlaceTextChanged())
        );
    }

    private class AutoCompletePlaceTextChanged extends Subscriber<String> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(String keyword) {
            getView().showListPlaces();

            getPlacesAndRenderViewByKeyword(keyword);
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

    private void getPlacesAndRenderViewByKeyword(String keyword) {
        ArrayList<AutocompletePrediction> results = getAutocomplete(keyword);
        ArrayList<Visitable> addresses = new ArrayList<>();
        for (AutocompletePrediction autocompletePrediction : results) {
            PlaceAutoCompeleteViewModel address = new PlaceAutoCompeleteViewModel();
            address.setAddress(String.valueOf(autocompletePrediction.getSecondaryText(null)));
            address.setTitle(String.valueOf(autocompletePrediction.getPrimaryText(null)));
            address.setAddressId(autocompletePrediction.getPlaceId());
            addresses.add(address);
        }
        getView().renderPlacesList(addresses);
        getView().hideAutoCompleteLoadingCross();
    }

    private ArrayList<AutocompletePrediction> getAutocomplete(CharSequence constraint) {
        if (mGoogleApiClient.isConnected()) {
            Log.i(TAG, "Starting autocomplete query for: " + constraint);


            PendingResult<AutocompletePredictionBuffer> results =
                    Places.GeoDataApi
                            .getAutocompletePredictions(mGoogleApiClient, constraint.toString(),
                                    null, mAutocompleteFilter);

            AutocompletePredictionBuffer autocompletePredictions = results
                    .await(60, TimeUnit.SECONDS);
            final Status status = autocompletePredictions.getStatus();
            if (!status.isSuccess()) {
                getView().showMessage("Error contacting API: " + status.toString());
                Log.e(TAG, "Error getting autocomplete prediction API call: " + status.toString());
                autocompletePredictions.release();
                return new ArrayList<>();
            }

            Log.i(TAG, "Query completed. Received " + autocompletePredictions.getCount()
                    + " predictions.");

            return DataBufferUtils.freezeAndClose(autocompletePredictions);
        }
        Log.e(TAG, "Google API client is not connected for autocomplete query.");
        return new ArrayList<>();
    }

    @Override
    public void onDestroy() {
        RxUtils.unsubscribeIfNotNull(mCompositeSubscription);
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
    }

    @Override
    public void onStart() {
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    public void onPlaceSelected(String addressId) {
        Places.GeoDataApi.getPlaceById(mGoogleApiClient, addressId)
                .setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (places.getStatus().isSuccess() && places.getCount() > 0) {
                            final Place myPlace = places.get(0);
                            PlacePassViewModel placePassViewModel = new PlacePassViewModel();
                            placePassViewModel.setLatitude(myPlace.getLatLng().latitude);
                            placePassViewModel.setLongitude(myPlace.getLatLng().longitude);
                            placePassViewModel.setTitle((String) myPlace.getName());
                            placePassViewModel.setPlaceId(myPlace.getId());
                            placePassViewModel.setType(PlacePassViewModel.TYPE.OTHER);
                            placePassViewModel.setAddress(String.valueOf(myPlace.getAddress()));
                            getView().onPlaceSelectedFound(placePassViewModel);
                        } else {
                            getView().showMessage("Place not Found");
                            Log.e(TAG, "Place not found");
                        }
                        places.release();
                    }
                });
    }

    @Override
    public void actionAutoDetectLocation() {
        if (mCurrentLocation != null) {
            PlacePassViewModel placePassViewModel = new PlacePassViewModel();
            placePassViewModel.setLatitude(mCurrentLocation.getLatitude());
            placePassViewModel.setLongitude(mCurrentLocation.getLongitude());
            placePassViewModel.setTitle(
                    GeoLocationUtils.reverseGeoCode(getView().getActivity(),
                            mCurrentLocation.getLatitude(),
                            mCurrentLocation.getLongitude()
                    )
            );
//        placePassViewModel.setPlaceId(mCurrentLocation);
            placePassViewModel.setType(PlacePassViewModel.TYPE.OTHER);
            placePassViewModel.setAddress(
                    GeoLocationUtils.reverseGeoCode(getView().getActivity(),
                            mCurrentLocation.getLatitude(),
                            mCurrentLocation.getLongitude()
                    )
            );
            getView().onPlaceSelectedFound(placePassViewModel);
        } else {
            getView().showMessage("Location not setted");
        }
    }

    @Override
    public void actionHomeLocation() {
        PlacePassViewModel placePassViewModel = new PlacePassViewModel();
//        placePassViewModel.setLatitude(myPlace.getLatLng().latitude);
//        placePassViewModel.setLongitude(myPlace.getLatLng().longitude);
        placePassViewModel.setTitle("Home");
//        placePassViewModel.setPlaceId(myPlace.getId());
        placePassViewModel.setType(PlacePassViewModel.TYPE.HOME);
        placePassViewModel.setAddress("Home");
        getView().onPlaceSelectedFound(placePassViewModel);
    }

    @Override
    public void actionWorkLocation() {
        PlacePassViewModel placePassViewModel = new PlacePassViewModel();
//        placePassViewModel.setLatitude(myPlace.getLatLng().latitude);
//        placePassViewModel.setLongitude(myPlace.getLatLng().longitude);
        placePassViewModel.setTitle("Work");
//        placePassViewModel.setPlaceId(myPlace.getId());
        placePassViewModel.setType(PlacePassViewModel.TYPE.WORK);
        placePassViewModel.setAddress("Work");
        getView().onPlaceSelectedFound(placePassViewModel);
    }
}
