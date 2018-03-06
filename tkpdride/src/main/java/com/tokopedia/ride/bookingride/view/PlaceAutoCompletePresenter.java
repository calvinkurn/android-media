package com.tokopedia.ride.bookingride.view;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

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
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.geolocation.utils.GeoLocationUtils;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.domain.AutoCompletePredictionUseCase;
import com.tokopedia.ride.bookingride.domain.GetDistanceMatrixUseCase;
import com.tokopedia.ride.bookingride.domain.GetLocationAddressUseCase;
import com.tokopedia.ride.bookingride.domain.GetPlaceDetailUseCase;
import com.tokopedia.ride.bookingride.domain.GetUserAddressCacheUseCase;
import com.tokopedia.ride.bookingride.domain.GetUserAddressUseCase;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.LabelViewModel;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.PlaceAutoCompeleteViewModel;
import com.tokopedia.ride.bookingride.view.fragment.PlaceAutocompleteFragment;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;
import com.tokopedia.ride.common.place.data.entity.DistanceMatrixEntity;
import com.tokopedia.ride.common.place.data.entity.Element;
import com.tokopedia.ride.common.place.data.entity.ReverseGeoCodeAddress;
import com.tokopedia.ride.common.ride.domain.model.RideAddress;
import com.tokopedia.ride.common.ride.utils.GoogleAPIClientObservable;
import com.tokopedia.ride.common.ride.utils.PendingResultObservable;
import com.tokopedia.ride.common.ride.utils.RideUtils;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static android.app.Activity.RESULT_OK;

/**
 * Created by alvarisi on 3/15/17.
 */

public class PlaceAutoCompletePresenter extends BaseDaggerPresenter<PlaceAutoCompleteContract.View> implements PlaceAutoCompleteContract.Presenter {
    private static final String TAG = "addressautocomplete";

    private GoogleApiClient mGoogleApiClient;
    private CompositeSubscription mCompositeSubscription;
    private AutocompleteFilter mAutocompleteFilter;
    private Location mCurrentLocation;
    private LocationRequest mLocationRequest;
    private OnQueryListener mOnQueryListener;
    private CompositeSubscription compositeSubscription;
    private GetUserAddressUseCase getUserAddressUseCase;
    private GetUserAddressCacheUseCase getUserAddressCacheUseCase;
    private GetDistanceMatrixUseCase getDistanceMatrixUseCase;
    private AutoCompletePredictionUseCase autoCompletePredictionUseCase;
    private GetPlaceDetailUseCase placeDetailUseCase;
    private final GetLocationAddressUseCase getLocationAddressUseCase;
    private boolean mAutoDetectLocation;

    private interface OnQueryListener {
        void onQuerySubmit(String query);
    }

    @Inject
    public PlaceAutoCompletePresenter(GetUserAddressUseCase getUserAddressUseCase,
                                      GetUserAddressCacheUseCase getUserAddressCacheUseCase,
                                      GetDistanceMatrixUseCase getDistanceMatrixUseCase,
                                      AutoCompletePredictionUseCase autoCompletePredictionUseCase,
                                      GetPlaceDetailUseCase placeDetailUseCase,
                                      GetLocationAddressUseCase getLocationAddressUseCase) {
        compositeSubscription = new CompositeSubscription();
        this.getUserAddressUseCase = getUserAddressUseCase;
        this.getUserAddressCacheUseCase = getUserAddressCacheUseCase;
        this.getDistanceMatrixUseCase = getDistanceMatrixUseCase;
        this.autoCompletePredictionUseCase = autoCompletePredictionUseCase;
        this.placeDetailUseCase = placeDetailUseCase;
        this.getLocationAddressUseCase = getLocationAddressUseCase;
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
            mCompositeSubscription.add(
                    Observable.create(new Observable.OnSubscribe<String>() {
                        @Override
                        public void call(final Subscriber<? super String> subscriber) {
                            mOnQueryListener = new OnQueryListener() {
                                @Override
                                public void onQuerySubmit(String query) {
                                    CommonUtils.dumper("onQuerySubmit : " + query);
                                    subscriber.onNext(String.valueOf(query));
                                }
                            };
                        }
                    }).debounce(250, TimeUnit.MILLISECONDS)
                            .subscribeOn(Schedulers.from(new JobExecutor()))
                            .observeOn(new UIThread().getScheduler())
                            .subscribe(new AutoCompletePlaceTextChanged())
            );
            getView().hideGoogleLabel();

            if (getView().isShowNearbyPlaces()) {
                //show nearby places if location is enabled else show recent address
                LocationManager manager = (LocationManager) getView().getActivity().getSystemService(Context.LOCATION_SERVICE);

                if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && ActivityCompat.checkSelfPermission(getView().getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getView().setActiveGooglePlaceSource();
                    showNearbyPlaces();
                } else {
                    getView().setActiveMarketplaceSource();
                    actionGetUserAddressesFromCache();
                    actionGetUserAddresses(false);
                }
            } else {
                getView().setActiveMarketplaceSource();
                actionGetUserAddressesFromCache();
                actionGetUserAddresses(false);
            }
        }
    }

    @Override
    public void actionGetUserAddressesFromCache() {
        getView().showAutoDetectLocationButton();
        getUserAddressCacheUseCase.execute(RequestParams.EMPTY, new Subscriber<List<RideAddress>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(List<RideAddress> rideAddresses) {
                if (isViewAttached() && !isUnsubscribed()) {
                    if (!getView().isActiveMarketPlaceSource()) return;
                    compositeSubscription.clear();
                    ArrayList<Visitable> addresses = new ArrayList<>();
                    for (RideAddress rideAddress : rideAddresses) {
                        if (!TextUtils.isEmpty(rideAddress.getLongitude()) && !TextUtils.isEmpty(rideAddress.getLatitude())) {
                            PlaceAutoCompeleteViewModel address = new PlaceAutoCompeleteViewModel();
                            address.setAddress(String.valueOf(rideAddress.getAddressDescription()));
                            address.setTitle(String.valueOf(rideAddress.getAddressName()));
                            address.setAddressId("0");
                            address.setLatitude(Double.parseDouble(rideAddress.getLatitude()));
                            address.setLongitude(Double.parseDouble(rideAddress.getLongitude()));
                            address.setType(PlaceAutoCompeleteViewModel.TYPE.MARKETPLACE_PLACE);
                            addresses.add(address);
                        }
                    }
                    getView().showListPlaces();
                    getView().setPagingConfiguration(null);
                    if (addresses.size() > 0) {
                        addresses.add(0, new LabelViewModel());
                    }
                    getView().renderPlacesList(addresses);
                    getView().hideAutoCompleteLoadingCross();
                    getView().hideGoogleLabel();
                }
            }
        });
    }

    @Override
    public void actionGetUserAddresses(final boolean isLoadMore) {
        getView().showAutoDetectLocationButton();
        getUserAddressUseCase.execute(getView().getUserAddressParam(),
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
                        if (isViewAttached() && !isUnsubscribed()) {
                            if (!getView().isActiveMarketPlaceSource()) return;
                            compositeSubscription.clear();
                            ArrayList<Visitable> addresses = new ArrayList<>();
                            for (RideAddress rideAddress : rideAddresses) {
                                if (isValidStringLatLng(rideAddress)) {
                                    PlaceAutoCompeleteViewModel address = new PlaceAutoCompeleteViewModel();
                                    address.setAddress(String.valueOf(rideAddress.getAddressDescription()));
                                    address.setTitle(String.valueOf(rideAddress.getAddressName()));
                                    address.setAddressId("0");
                                    address.setLatitude(Double.parseDouble(rideAddress.getLatitude()));
                                    address.setLongitude(Double.parseDouble(rideAddress.getLongitude()));
                                    address.setType(PlaceAutoCompeleteViewModel.TYPE.MARKETPLACE_PLACE);
                                    addresses.add(address);
                                }
                            }
                            getView().showListPlaces();
                            getView().setPagingConfiguration(null);
                            if (!isLoadMore) {
                                if (addresses.size() > 0) {
                                    addresses.add(0, new LabelViewModel());
                                }
                                getView().renderPlacesList(addresses);
                            } else {
                                getView().renderMorePlacesList(addresses);
                            }
                            getView().hideAutoCompleteLoadingCross();
                            getView().hideGoogleLabel();
                        }
                    }
                });
    }

    private boolean isValidStringLatLng(RideAddress rideAddress) {
        return !TextUtils.isEmpty(rideAddress.getLongitude())
                && !TextUtils.isEmpty(rideAddress.getLatitude())
                && !rideAddress.getLatitude().equalsIgnoreCase("0")
                && !rideAddress.getLongitude().equalsIgnoreCase("0");
    }

    private void prepareInitialView() {
        getView().hideAutoCompleteLoadingCross();
        getView().showAutoDetectLocationButton();
        getView().hideHomeLocationButton();
        getView().hideWorkLocationButton();
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
                                //do not do anything
                                //getView().showMessage(getView().getActivity().getString(R.string.location_permission_required));
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
                        onLocationSuccess(mCurrentLocation);
                        startLocationUpdates();

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(getView().getActivity(), PlaceAutocompleteFragment.REQUEST_CHECK_LOCATION_SETTING_REQUEST_CODE);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.

                        //getView().showMessage(getView().getActivity().getString(R.string.msg_enter_location));
                        break;
                }
            }
        });
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
                onLocationSuccess(location);
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
    public boolean isLocationPermissionGranted() {
        return !((ActivityCompat.checkSelfPermission(getView().getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
                && (ActivityCompat.checkSelfPermission(getView().getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED));

    }

    @Override
    public void handleEnableLocationDialogResult(int resultCode) {
        if (resultCode == RESULT_OK) {
            if (getFuzedLocation() != null) {
                onLocationSuccess(mCurrentLocation);
                startLocationUpdates();
            } else {
                startLocationUpdates();
            }
        }
    }

    private void onLocationSuccess(Location currentLocation) {
        mCurrentLocation = currentLocation;

        //get current place if auto detect location was clicked
        if (mCurrentLocation != null && mAutoDetectLocation) {
            getCurrentPlace();
        }
    }

    @Override
    public void actionQueryPlacesByKeyword(final String keyword) {
        getView().showAutoCompleteLoadingCross();
        getView().hideClearButton();
        getView().hideAutoDetectLocationButton();
        getView().hideHomeLocationButton();
        getView().hideWorkLocationButton();
        getView().resetMarketplacePaging();
        if (TextUtils.isEmpty(keyword)) return;
        if (mOnQueryListener != null) {
            mOnQueryListener.onQuerySubmit(keyword);
        }
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
            if (isViewAttached() && !isUnsubscribed()) {
                getView().showListPlaces();
                getPlacesAndRenderViewByKeyword(keyword);
            }
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
        LatLngBounds bounds = null;
        if (mCurrentLocation != null) {
            bounds = GeoLocationUtils.generateBoundary(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        }

        TKPDMapParam<String, String> temp = new TKPDMapParam<>();
        temp = AuthUtil.generateParamsNetwork(getView().getActivity(), temp);
        TKPDMapParam<String, Object> params = new TKPDMapParam<>();
        params.putAll(temp);

        RequestParams requestParams = RequestParams.create();
        requestParams.putString(AutoCompletePredictionUseCase.PARAM_KEYWORD, keyword);
        requestParams.putAll(params);
        autoCompletePredictionUseCase.execute(requestParams, new Subscriber<List<PlaceAutoCompeleteViewModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached() && !isUnsubscribed()) {
                    if (e instanceof UnknownHostException) {
                        getView().showErrorNoInternetConnectionMessage(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL);
                    }
                    renderPlaceList(new ArrayList<PlaceAutoCompeleteViewModel>(), false);
                }
            }

            @Override
            public void onNext(List<PlaceAutoCompeleteViewModel> placeAutoCompeleteViewModels) {
                if (isViewAttached() && !isUnsubscribed()) {
                    if (!getView().isActiveGooglePlaceSource()) return;

                    if (mCurrentLocation == null || placeAutoCompeleteViewModels == null || placeAutoCompeleteViewModels.size() == 0) {
                        renderPlaceList(placeAutoCompeleteViewModels, false);
                    } else {
                        renderPlaceList(placeAutoCompeleteViewModels, false);
                        getDistanceMatrixFromOrigin(placeAutoCompeleteViewModels, false);
                    }
                }
            }
        });
    }

    public void renderPlaceList(List<PlaceAutoCompeleteViewModel> addresses, boolean isNearbyPlaces) {
        getView().showGoogleLabel();
        ArrayList<Visitable> addr = new ArrayList<>();

        if (isNearbyPlaces) {
            Collections.sort(addresses, new Comparator<PlaceAutoCompeleteViewModel>() {
                @Override
                public int compare(PlaceAutoCompeleteViewModel placeAutoCompeleteViewModel, PlaceAutoCompeleteViewModel t1) {

                    if (placeAutoCompeleteViewModel != null &&
                            placeAutoCompeleteViewModel.getDistance() != null &&
                            t1 != null &&
                            t1.getDistance() != null) {
                        return placeAutoCompeleteViewModel.getDistanceValue() - t1.getDistanceValue();
                    } else
                        return 0;
                }
            });
        }

        addr.addAll(addresses);
        getView().showListPlaces();
        getView().renderPlacesList(addr);
        getView().hideAutoCompleteLoadingCross();
        if (!isNearbyPlaces) {
            getView().showClearButton();
        }
    }

    public Observable<GoogleApiClient> getGoogleApiClientObservable(Api... apis) {
        //noinspection unchecked
        return GoogleAPIClientObservable.create(getView().getActivity(), apis);
    }


    public <T extends Result> Observable<T> fromPendingResult(PendingResult<T> result) {
        return Observable.create(new PendingResultObservable<>(result));
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
    public void onPlaceSelected(PlaceAutoCompeleteViewModel adress) {
        if (adress.getType() == PlaceAutoCompeleteViewModel.TYPE.GOOGLE_PLACE) {
            RequestParams requestParams = RequestParams.create();
            requestParams.putString("placeid", adress.getAddressId());
            placeDetailUseCase.execute(requestParams, new Subscriber<PlacePassViewModel>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(PlacePassViewModel placePassViewModel) {
                    getView().onPlaceSelectedFound(placePassViewModel);
                }
            });
        } else {
            PlacePassViewModel placePassViewModel = new PlacePassViewModel();
            placePassViewModel.setAndFormatLatitude(adress.getLatitude());
            placePassViewModel.setAndFormatLongitude(adress.getLongitude());
            placePassViewModel.setTitle(adress.getTitle());
            placePassViewModel.setPlaceId(adress.getAddressId());
            placePassViewModel.setAddress(adress.getAddress());
            getView().onPlaceSelectedFound(placePassViewModel);
        }
    }

    @Override
    public void actionAutoDetectLocation() {
        mAutoDetectLocation = true;
        if (mCurrentLocation != null || ActivityCompat.checkSelfPermission(getView().getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            getCurrentPlace();
        }

        if (mGoogleApiClient != null && !RideUtils.isLocationEnabled(getView().getActivity())) {
            checkLocationSettings();
        }
    }

    public void getDistanceMatrixFromOrigin(final List<PlaceAutoCompeleteViewModel> addresses, final boolean isNearbyPlaces) {
        if (mCurrentLocation == null || addresses == null || addresses.size() == 0) return;

        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetDistanceMatrixUseCase.PARAM_ORIGINS, String.format("%s,%s",
                mCurrentLocation.getLatitude(),
                mCurrentLocation.getLongitude()
        ));

        String destinations = "";
        for (PlaceAutoCompeleteViewModel prediction : addresses) {
            if (isNearbyPlaces) {
                destinations += String.format("%s,%s", prediction.getLatitude(), prediction.getLongitude()) + "|";
            } else {
                destinations += prediction.getAddress() + "|";
            }
        }

        requestParams.putString(GetDistanceMatrixUseCase.PARAM_DESTINATIONS, destinations);
        requestParams.putString(GetDistanceMatrixUseCase.PARAM_KEY, getView().getActivity().getString(R.string.GOOGLE_API_KEY));

        getDistanceMatrixUseCase.execute(requestParams, new Subscriber<DistanceMatrixEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    renderPlaceList(addresses, isNearbyPlaces);
                }
            }

            @Override
            public void onNext(DistanceMatrixEntity distanceMatrixEntity) {
                if (isViewAttached()) {
                    //add distance to the addresses
                    if (distanceMatrixEntity != null && distanceMatrixEntity.getRows().size() > 0 && distanceMatrixEntity.getRows().get(0).getElements() != null) {
                        int index = 0;
                        for (Element element : distanceMatrixEntity.getRows().get(0).getElements()) {
                            if (element != null && element.getStatus().equalsIgnoreCase("OK")) {
                                String distance = element.getDistance().getText();
                                Integer value = element.getDistance().getValue();

                                if (addresses.size() > index) {
                                    addresses.get(index).setDistance(distance);
                                    addresses.get(index).setDistanceValue(value);
                                }
                            }
                            index++;
                        }
                    }

                    renderPlaceList(addresses, isNearbyPlaces);
                }
            }
        });
    }

    @Override
    public void actionHomeLocation() {
        PlacePassViewModel placePassViewModel = new PlacePassViewModel();
        placePassViewModel.setTitle("Home");
        placePassViewModel.setAddress("Home");
        getView().onPlaceSelectedFound(placePassViewModel);
    }

    @Override
    public void actionWorkLocation() {
        PlacePassViewModel placePassViewModel = new PlacePassViewModel();
        placePassViewModel.setTitle("Work");
        placePassViewModel.setAddress("Work");
        getView().onPlaceSelectedFound(placePassViewModel);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    private void getCurrentPlace() {

        if (mCurrentLocation == null) {
            return;
        }

        final double latitude = mCurrentLocation.getLatitude();
        final double longitude = mCurrentLocation.getLongitude();

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
                    PlacePassViewModel placeVm = new PlacePassViewModel();
                    placeVm.setAddress(String.valueOf(address));
                    placeVm.setAndFormatLatitude(latitude);
                    placeVm.setAndFormatLongitude(longitude);
                    placeVm.setTitle(title);
                    if (mAutoDetectLocation) {
                        mAutoDetectLocation = false;
                        getView().sendAutoDetectGAEvent(placeVm);
                    }
                    getView().onPlaceSelectedFound(placeVm);
                }
            }
        });
    }

    @Override
    public void showNearbyPlaces() {
        getNearbyPlacesObservable(new PlaceFilter())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PlaceLikelihoodBuffer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(PlaceLikelihoodBuffer placeLikelihoodBuffer) {
                        if (isViewAttached() && !isUnsubscribed() && placeLikelihoodBuffer != null) {
                            if (!getView().isActiveGooglePlaceSource()) return;
                            compositeSubscription.clear();

                            CommonUtils.dumper("Vishal showNearbyPlaces Status Code :: " + placeLikelihoodBuffer.getStatus().getStatusCode());
                            CommonUtils.dumper("Vishal showNearbyPlaces Status Code :: " + placeLikelihoodBuffer.getStatus().getStatusMessage());

                            ArrayList<PlaceAutoCompeleteViewModel> addresses = new ArrayList<>();
                            for (PlaceLikelihood placeLikelihood : placeLikelihoodBuffer) {
                                PlaceAutoCompeleteViewModel address = new PlaceAutoCompeleteViewModel();

                                Place place = placeLikelihood.getPlace();

                                address.setAddress(String.valueOf(place.getAddress()));
                                address.setTitle(String.valueOf(place.getName()));
                                address.setAddressId(place.getId());
                                address.setLatitude(place.getLatLng().latitude);
                                address.setLongitude(place.getLatLng().longitude);
                                address.setType(PlaceAutoCompeleteViewModel.TYPE.NEARBY_PLACE);
                                addresses.add(address);
                            }

                            placeLikelihoodBuffer.release();

                            //find distances and render
                            if (addresses == null || addresses.size() == 0) {
                                renderPlaceList(addresses, true);
                            } else {
                                renderPlaceList(addresses, true);
                                getDistanceMatrixFromOrigin(addresses, true);
                            }
                        }
                    }
                });
    }

    private Observable<PlaceLikelihoodBuffer> getNearbyPlacesObservable(@javax.annotation.Nullable final PlaceFilter placeFilter) {
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
}
