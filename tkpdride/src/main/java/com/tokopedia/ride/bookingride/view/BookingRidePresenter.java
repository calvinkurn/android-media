package com.tokopedia.ride.bookingride.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.domain.GetOverviewPolylineUseCase;
import com.tokopedia.ride.common.ride.data.BookingRideDataStoreFactory;
import com.tokopedia.ride.common.ride.data.BookingRideRepositoryData;
import com.tokopedia.ride.common.ride.data.ProductEntityMapper;
import com.tokopedia.ride.common.ride.domain.model.Product;
import com.tokopedia.ride.bookingride.domain.GetUberProductsUseCase;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by alvarisi on 3/13/17.
 */

public class BookingRidePresenter extends BaseDaggerPresenter<BookingRideContract.View>
        implements BookingRideContract.Presenter {
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private final GetUberProductsUseCase mGetUberProductsUseCase;
    private final GetOverviewPolylineUseCase getOverviewPolylineUseCase;
    private boolean isMapDragging = false;
    private Location mCurrentLocation;

    public BookingRidePresenter(GetUberProductsUseCase getUberProductsUseCase,
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
                                Location location = getFuzedLocation();
                                mCurrentLocation = location;
                                startLocationUpdates();
                                getView().moveToCurrentLocation(location.getLatitude(), location.getLongitude());
                                getView().renderDefaultPickupLocation(location.getLatitude(), location.getLongitude());
                                getView().setSourceLocation(location);
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
    public void actionMyLocation() {
        getView().moveToCurrentLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
    }
}