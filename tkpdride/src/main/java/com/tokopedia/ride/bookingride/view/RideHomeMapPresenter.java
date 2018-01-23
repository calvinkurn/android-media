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
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.domain.GetLocationAddressUseCase;
import com.tokopedia.ride.bookingride.domain.GetNearbyRoadsUseCase;
import com.tokopedia.ride.bookingride.domain.GetOverviewPolylineUseCase;
import com.tokopedia.ride.bookingride.domain.GetPeopleAddressesUseCase;
import com.tokopedia.ride.bookingride.domain.GetUserAddressUseCase;
import com.tokopedia.ride.bookingride.view.fragment.RideHomeMapFragment;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;
import com.tokopedia.ride.common.configuration.MapConfiguration;
import com.tokopedia.ride.common.place.data.entity.NearbyRoads;
import com.tokopedia.ride.common.place.data.entity.ReverseGeoCodeAddress;
import com.tokopedia.ride.common.place.domain.model.OverviewPolyline;
import com.tokopedia.ride.common.ride.domain.model.RideAddress;
import com.tokopedia.ride.common.ride.utils.GoogleAPIClientObservable;
import com.tokopedia.ride.common.ride.utils.PendingResultObservable;
import com.tokopedia.ride.common.ride.utils.RideUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;
import static com.tokopedia.core.network.retrofit.utils.AuthUtil.md5;

/**
 * Created by alvarisi on 3/13/17.
 */

public class RideHomeMapPresenter extends BaseDaggerPresenter<RideHomeMapContract.View>
        implements RideHomeMapContract.Presenter {

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private final GetOverviewPolylineUseCase getOverviewPolylineUseCase;
    private GetUserAddressUseCase getUserAddressUseCase;
    private GetNearbyRoadsUseCase getNearbyRoadsUseCase;
    private boolean isMapDragging = false;
    private Location mCurrentLocation;
    private boolean mRenderProductListBasedOnLocationUpdates;
    private boolean mSourceIsCurrentLocation;
    private MapConfiguration mapConfiguration;
    private boolean isDestinationPrefilledOnce;
    private GetLocationAddressUseCase getLocationAddressUseCase;


    @Inject
    public RideHomeMapPresenter(GetOverviewPolylineUseCase getOverviewPolylineUseCase, GetUserAddressUseCase getUserAddressUseCase, GetNearbyRoadsUseCase getNearbyRoadsUseCase, GetLocationAddressUseCase getLocationAddressUseCase) {
        this.getOverviewPolylineUseCase = getOverviewPolylineUseCase;
        this.getUserAddressUseCase = getUserAddressUseCase;
        this.getLocationAddressUseCase = getLocationAddressUseCase;
        this.getNearbyRoadsUseCase = getNearbyRoadsUseCase;
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
                            if (isViewAttached()) {
                                if (getFuzedLocation() != null) {
                                    mCurrentLocation = getFuzedLocation();
                                    if (!getView().isLaunchedWithLocation()) {
                                        setSourceAsCurrentLocation();
                                        mRenderProductListBasedOnLocationUpdates = true;
                                    } else {
                                        mRenderProductListBasedOnLocationUpdates = false;
                                    }
                                }

                                if (!RideUtils.isLocationEnabled(getView().getActivity())) {
                                    checkLocationSettings();
                                } else {
                                    startLocationUpdates();
                                }
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
     * This functions checks if locations is not enabled shows a dialog to enable to location
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
                        if (isViewAttached()) {
                            mCurrentLocation = getFuzedLocation();
                            startLocationUpdates();
                            if (!getView().isLaunchedWithLocation()) {
                                setSourceAsCurrentLocation();
                            }

                            if (mapConfiguration != null && mCurrentLocation != null) {
                                mapConfiguration.setDefaultLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                            }
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

                        if (getView().getSource() == null) {
                            getView().showMessage(getView().getActivity().getString(R.string.msg_enter_location), getView().getActivity().getString(R.string.btn_enter_location));
                        }
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

        getView().moveMapToLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetLocationAddressUseCase.PARAM_LATITUDE, String.valueOf(mCurrentLocation.getLatitude()));
        requestParams.putString(GetLocationAddressUseCase.PARAM_LONGITUDE, String.valueOf(mCurrentLocation.getLongitude()));
        requestParams.putString(GetLocationAddressUseCase.PARAM_KEY, getView().getActivity().getString(R.string.GOOGLE_API_KEY));

        //set source as current location
        getLocationAddressUseCase.execute(requestParams, new Subscriber<ReverseGeoCodeAddress>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                String sourceAddress = String.valueOf(mCurrentLocation.getLatitude()) + "," + String.valueOf(mCurrentLocation.getLongitude());
                handleAddressResult(sourceAddress, sourceAddress);
            }

            @Override
            public void onNext(ReverseGeoCodeAddress reverseGeoCodeAddres) {
                String title = RideUtils.getShortAddress(reverseGeoCodeAddres);
                String address = reverseGeoCodeAddres.getFormattedAddress();
                handleAddressResult(title, address);
            }

            private void handleAddressResult(String title, String address) {
                if (isViewAttached()) {
                    PlacePassViewModel placeVm = new PlacePassViewModel();
                    placeVm.setAddress(address);
                    placeVm.setAndFormatLatitude(mCurrentLocation.getLatitude());
                    placeVm.setAndFormatLongitude(mCurrentLocation.getLongitude());
                    placeVm.setTitle(title);
                    getView().setSourceLocation(placeVm);

                    if (!isDestinationPrefilledOnce) {
                        prefillDestinationFromRecentAddressList();
                    }
                }
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
        if (getView() == null || getView().getActivity() == null) {
            return;
        }

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
                if (isViewAttached() && !isUnsubscribed() && getView().isAlreadySelectDestination()) {
                    getView().renderTripPolyline(overviewPolylines);
                }
            }
        });
    }

    @Override
    public void getNearbyRoadsData(ArrayList<com.tokopedia.ride.common.ride.domain.model.Location> locationArrayList) {
        if (getView() == null || getView().getActivity() == null) {
            return;
        }

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject("Coordinates", locationArrayList);

        requestParams.putString(GetNearbyRoadsUseCase.PARAM_KEY, "AIzaSyCRkgwGBe8ZxjcK07Cnl3Auf72BpgA6lLo");

        getNearbyRoadsUseCase.execute(requestParams, new Subscriber<NearbyRoads>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(NearbyRoads nearbyRoads) {
                if (isViewAttached() && !isUnsubscribed()) {
                    getView().renderNearbyCabs(nearbyRoads);
                }
            }
        });
    }

    @Override
    public void actionMyLocation() {
        if (!getView().isAlreadySelectDestination()) {

            //if current location is null, ask permission to enable location
            if (mCurrentLocation == null || !RideUtils.isLocationEnabled(getView().getActivity())) {
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
        if (isViewAttached()) {
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
                if (getView().getSource() == null) {
                    getView().showMessage(getView().getActivity().getString(R.string.msg_enter_location), getView().getActivity().getString(R.string.btn_enter_location));
                }
            }
        }
    }

    @Override
    public void actionMapDragStopped(final double latitude, final double longitude) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetLocationAddressUseCase.PARAM_LATITUDE, String.valueOf(latitude));
        requestParams.putString(GetLocationAddressUseCase.PARAM_LONGITUDE, String.valueOf(longitude));
        requestParams.putString(GetLocationAddressUseCase.PARAM_KEY, getView().getActivity().getString(R.string.GOOGLE_API_KEY));

        //find address using latitude, longitude
        getLocationAddressUseCase.execute(requestParams, new Subscriber<ReverseGeoCodeAddress>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached() && !isUnsubscribed()) {
                    String sourceAddress = String.valueOf(mCurrentLocation.getLatitude()) + "," + String.valueOf(mCurrentLocation.getLongitude());

                    if (isViewAttached() && !isUnsubscribed()) {
                        getView().renderDefaultPickupLocation(latitude, longitude, sourceAddress, sourceAddress);
                    }
                }
            }

            @Override
            public void onNext(ReverseGeoCodeAddress reverseGeoCodeAddres) {
                String title = RideUtils.getShortAddress(reverseGeoCodeAddres);
                String address = reverseGeoCodeAddres.getFormattedAddress();


                if (isViewAttached() && !isUnsubscribed()) {
                    getView().renderDefaultPickupLocation(latitude, longitude, title, address);
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

    private void prefillDestinationFromRecentAddressList() {
        if (getView().isAlreadySelectDestination()) {
            return;
        }

        String deviceId = GCMHandler.getRegistrationId(getView().getActivity());
        String userId = SessionHandler.getLoginID(getView().getActivity());
        String hash = md5(userId + "~" + deviceId);
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetPeopleAddressesUseCase.PARAM_USER_ID, userId);
        requestParams.putString(GetPeopleAddressesUseCase.PARAM_DEVICE_ID, deviceId);
        requestParams.putString(GetPeopleAddressesUseCase.PARAM_HASH, hash);
        requestParams.putString(GetPeopleAddressesUseCase.PARAM_OS_TYPE, "1");
        requestParams.putString(GetPeopleAddressesUseCase.PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));

        getUserAddressUseCase.execute(requestParams,
                new Subscriber<List<RideAddress>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<RideAddress> rideAddresses) {
                        if (isViewAttached() && !isUnsubscribed() && rideAddresses != null && rideAddresses.size() > 0) {

                            RideAddress address = rideAddresses.get(0);
                            if (address.isPrefill() && getView().getSource() != null) {
                                //set the destination if not already set
                                if (!getView().isAlreadySelectDestination() && !getView().getSource().getTitle().equalsIgnoreCase(address.getAddressName())) {
                                    //set destination
                                    PlacePassViewModel destination = new PlacePassViewModel();
                                    destination.setAddress(address.getAddressDescription());
                                    destination.setTitle(address.getAddressName());
                                    destination.setAndFormatLongitude(Double.parseDouble(address.getLongitude()));
                                    destination.setAndFormatLatitude(Double.parseDouble(address.getLatitude()));

                                    getView().setDestinationAndProcessList(destination);
                                    isDestinationPrefilledOnce = true;
                                }
                            }
                        }
                    }
                });
    }
}