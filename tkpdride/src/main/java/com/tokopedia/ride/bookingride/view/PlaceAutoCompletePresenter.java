package com.tokopedia.ride.bookingride.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.geolocation.utils.GeoLocationUtils;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.domain.GetPeopleAddressesUseCase;
import com.tokopedia.ride.bookingride.domain.GetUserAddressCacheUseCase;
import com.tokopedia.ride.bookingride.domain.GetUserAddressUseCase;
import com.tokopedia.ride.bookingride.domain.model.PeopleAddress;
import com.tokopedia.ride.bookingride.domain.model.PeopleAddressWrapper;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.LabelViewModel;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.PlaceAutoCompeleteViewModel;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;
import com.tokopedia.ride.common.ride.domain.model.RideAddress;
import com.tokopedia.ride.common.ride.utils.GoogleAPIClientObservable;
import com.tokopedia.ride.common.ride.utils.PendingResultObservable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by alvarisi on 3/15/17.
 */

public class PlaceAutoCompletePresenter extends BaseDaggerPresenter<PlaceAutoCompleteContract.View> implements PlaceAutoCompleteContract.Presenter {
    private static final String TAG = "addressautocomplete";
    private GoogleApiClient mGoogleApiClient;
    private CompositeSubscription mCompositeSubscription;
    private AutocompleteFilter mAutocompleteFilter;
    private Location mCurrentLocation;
    private boolean isLoadingPlaces;

    private LocationRequest mLocationRequest;
    private OnQueryListener mOnQueryListener;
    private GetPeopleAddressesUseCase mGetPeopleAddressesUseCase;
    private CompositeSubscription compositeSubscription;
    private GetUserAddressUseCase getUserAddressUseCase;
    private GetUserAddressCacheUseCase getUserAddressCacheUseCase;

    private interface OnQueryListener {
        void onQuerySubmit(String query);
    }

    public PlaceAutoCompletePresenter(GetPeopleAddressesUseCase getPeopleAddressesUseCase,
                                      GetUserAddressUseCase getUserAddressUseCase,
                                      GetUserAddressCacheUseCase getUserAddressCacheUseCase) {
        mGetPeopleAddressesUseCase = getPeopleAddressesUseCase;
        compositeSubscription = new CompositeSubscription();
        this.getUserAddressUseCase = getUserAddressUseCase;
        this.getUserAddressCacheUseCase = getUserAddressCacheUseCase;
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
            isLoadingPlaces = false;
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
                    }).debounce(150, TimeUnit.MILLISECONDS)
                            .subscribeOn(Schedulers.from(new JobExecutor()))
                            .observeOn(new UIThread().getScheduler())
                            .subscribe(new AutoCompletePlaceTextChanged())
            );
            getView().hideGoogleLabel();
            getView().setActiveMarketplaceSource();
            actionGetUserAddressesFromCache();
            actionGetUserAddresses(false);
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

    @Override
    public void actionGetPeopleAddresses(final boolean isLoadMore) {
        getView().showAutoDetectLocationButton();
        mGetPeopleAddressesUseCase.execute(getView().getPeopleAddressParam(), new Subscriber<PeopleAddressWrapper>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(PeopleAddressWrapper wrapper) {
                if (isViewAttached() && !isUnsubscribed()) {
                    if (!getView().isActiveMarketPlaceSource()) return;
                    compositeSubscription.clear();
                    ArrayList<Visitable> addresses = new ArrayList<>();
                    for (PeopleAddress peopleAddress : wrapper.getAddresses()) {
                        if (!TextUtils.isEmpty(peopleAddress.getLongitude()) && !TextUtils.isEmpty(peopleAddress.getLatitude())) {
                            PlaceAutoCompeleteViewModel address = new PlaceAutoCompeleteViewModel();
                            address.setAddress(String.valueOf(peopleAddress.getAddressStreet()));
                            address.setTitle(String.valueOf(peopleAddress.getAddressName()));
                            address.setAddressId(peopleAddress.getAddressId());
                            address.setLatitude(Double.parseDouble(peopleAddress.getLatitude()));
                            address.setLongitude(Double.parseDouble(peopleAddress.getLongitude()));
                            address.setType(PlaceAutoCompeleteViewModel.TYPE.MARKETPLACE_PLACE);
                            addresses.add(address);
                        }
                    }
                    getView().showListPlaces();
                    getView().setPagingConfiguration(wrapper.getPaging());
                    if (!isLoadMore) {
                        getView().renderPlacesList(addresses);
                    } else {
                        getView().renderMorePlacesList(addresses);
                    }
                    getView().hideAutoCompleteLoadingCross();
                }
            }
        });
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
    public boolean isLocationPermissionGranted() {
        if ((ActivityCompat.checkSelfPermission(getView().getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
                && (ActivityCompat.checkSelfPermission(getView().getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)) {
            return false;
        }

        return true;
    }

    @Override
    public void actionQueryPlacesByKeyword(final String keyword) {
//        if (isLoadingPlaces) return;
//        isLoadingPlaces = true;
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
        /*mCompositeSubscription.add(getAutocompleteObservable(keyword).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ArrayList<AutocompletePrediction>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ArrayList<AutocompletePrediction> results) {
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
                })
        );*/


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
//            if (!isLoadingPlaces) {
                getPlacesAndRenderViewByKeyword(keyword);
//            }
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
        isLoadingPlaces = true;
        LatLngBounds bounds = null;
        if (mCurrentLocation != null) {
            bounds = GeoLocationUtils.generateBoundary(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        }
        compositeSubscription.add(
                getPlaceAutocompletePredictions(keyword, bounds, mAutocompleteFilter)
                        .map(new Func1<AutocompletePredictionBuffer, ArrayList<AutocompletePrediction>>() {
                                 @Override
                                 public ArrayList<AutocompletePrediction> call(AutocompletePredictionBuffer autocompletePredictions) {
                                     if (autocompletePredictions.getStatus().isSuccess()) {
                                         isLoadingPlaces = false;
                                         return DataBufferUtils.freezeAndClose(autocompletePredictions);
                                     }
                                     autocompletePredictions.release();
                                     isLoadingPlaces = false;
                                     return new ArrayList<>();
                                 }
                             }
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<ArrayList<AutocompletePrediction>>() {
                                       @Override
                                       public void onCompleted() {

                                       }

                                       @Override
                                       public void onError(Throwable e) {
                                           e.printStackTrace();
                                       }

                                       @Override
                                       public void onNext(ArrayList<AutocompletePrediction> results) {
                                           if (isViewAttached() && !isUnsubscribed()) {
                                               if (!getView().isActiveGooglePlaceSource()) return;
                                               ArrayList<Visitable> addresses = new ArrayList<>();
                                               for (AutocompletePrediction autocompletePrediction : results) {
                                                   PlaceAutoCompeleteViewModel address = new PlaceAutoCompeleteViewModel();
                                                   address.setAddress(String.valueOf(autocompletePrediction.getSecondaryText(null)));
                                                   address.setTitle(String.valueOf(autocompletePrediction.getPrimaryText(null)));
                                                   address.setAddressId(autocompletePrediction.getPlaceId());
                                                   address.setType(PlaceAutoCompeleteViewModel.TYPE.GOOGLE_PLACE);
                                                   addresses.add(address);
                                               }
                                               getView().showGoogleLabel();
                                               getView().renderPlacesList(addresses);
                                               getView().hideAutoCompleteLoadingCross();
                                               getView().showClearButton();
                                           }
                                       }
                                   }
                        )
        );
    }

    public Observable<GoogleApiClient> getGoogleApiClientObservable(Api... apis) {
        //noinspection unchecked
        return GoogleAPIClientObservable.create(getView().getActivity(), apis);
    }

    public Observable<AutocompletePredictionBuffer> getPlaceAutocompletePredictions(final String query, final LatLngBounds bounds, final AutocompleteFilter filter) {
        return getGoogleApiClientObservable(Places.PLACE_DETECTION_API, Places.GEO_DATA_API)
                .flatMap(new Func1<GoogleApiClient, Observable<AutocompletePredictionBuffer>>() {
                    @Override
                    public Observable<AutocompletePredictionBuffer> call(GoogleApiClient api) {
                        return fromPendingResult(Places.GeoDataApi.getAutocompletePredictions(api, query, bounds, filter));
                    }
                });
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
            Places.GeoDataApi.getPlaceById(mGoogleApiClient, adress.getAddressId())
                    .setResultCallback(new ResultCallback<PlaceBuffer>() {
                        @Override
                        public void onResult(PlaceBuffer places) {
                            if (places.getStatus().isSuccess() && places.getCount() > 0) {
                                final Place myPlace = places.get(0);
                                PlacePassViewModel placePassViewModel = new PlacePassViewModel();
                                placePassViewModel.setAndFormatLatitude(myPlace.getLatLng().latitude);
                                placePassViewModel.setAndFormatLongitude(myPlace.getLatLng().longitude);
                                placePassViewModel.setTitle((String) myPlace.getName());
                                placePassViewModel.setPlaceId(myPlace.getId());
//                                placePassViewModel.setType(PlacePassViewModel.TYPE.OTHER);
                                placePassViewModel.setAddress(String.valueOf(myPlace.getAddress()));
                                getView().onPlaceSelectedFound(placePassViewModel);
                            } else {
                                getView().showMessage("Place not Found");
                                Log.e(TAG, "Place not found");
                            }
                            places.release();
                        }
                    });
        } else {
            PlacePassViewModel placePassViewModel = new PlacePassViewModel();
            placePassViewModel.setAndFormatLatitude(adress.getLatitude());
            placePassViewModel.setAndFormatLongitude(adress.getLongitude());
            placePassViewModel.setTitle(adress.getTitle());
            placePassViewModel.setPlaceId(adress.getAddressId());
//            placePassViewModel.setType(PlacePassViewModel.TYPE.OTHER);
            placePassViewModel.setAddress(adress.getAddress());
            getView().onPlaceSelectedFound(placePassViewModel);
        }
    }

    @Override
    public void actionAutoDetectLocation() {
        if (mCurrentLocation != null) {
            compositeSubscription.add(Observable.create(new Observable.OnSubscribe<String>() {
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
                    }
                }
            }).onErrorReturn(new Func1<Throwable, String>() {
                @Override
                public String call(Throwable throwable) {
                    return String.valueOf(mCurrentLocation.getLatitude()) + ", " + String.valueOf(mCurrentLocation.getLongitude());
                }
            }).subscribe(new Subscriber<String>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(String currentAddress) {
                    if (isViewAttached() && !isUnsubscribed()) {
                        PlacePassViewModel placePassViewModel = new PlacePassViewModel();
                        placePassViewModel.setAndFormatLatitude(mCurrentLocation.getLatitude());
                        placePassViewModel.setAndFormatLongitude(mCurrentLocation.getLongitude());
                        placePassViewModel.setTitle(currentAddress);
                        //placePassViewModel.setPlaceId(mCurrentLocation);
//                        placePassViewModel.setType(PlacePassViewModel.TYPE.OTHER);
                        placePassViewModel.setAddress(currentAddress);
                        getView().onPlaceSelectedFound(placePassViewModel);
                    }
                }
            }));
        } else {
            getView().showMessage(getView().getActivity().getString(R.string.invalid_current_location));
        }
    }

    @Override
    public void actionHomeLocation() {
        PlacePassViewModel placePassViewModel = new PlacePassViewModel();
//        placePassViewModel.setAndFormatLatitude(myPlace.getLatLng().latitude);
//        placePassViewModel.setAndFormatLongitude(myPlace.getLatLng().longitude);
        placePassViewModel.setTitle("Home");
//        placePassViewModel.setPlaceId(myPlace.getId());
//        placePassViewModel.setType(PlacePassViewModel.TYPE.HOME);
        placePassViewModel.setAddress("Home");
        getView().onPlaceSelectedFound(placePassViewModel);
    }

    @Override
    public void actionWorkLocation() {
        PlacePassViewModel placePassViewModel = new PlacePassViewModel();
//        placePassViewModel.setAndFormatLatitude(myPlace.getLatLng().latitude);
//        placePassViewModel.setAndFormatLongitude(myPlace.getLatLng().longitude);
        placePassViewModel.setTitle("Work");
//        placePassViewModel.setPlaceId(myPlace.getId());
//        placePassViewModel.setType(PlacePassViewModel.TYPE.WORK);
        placePassViewModel.setAddress("Work");
        getView().onPlaceSelectedFound(placePassViewModel);
    }

    @Override
    public void detachView() {
        mGetPeopleAddressesUseCase.unsubscribe();
        super.detachView();
    }
}
