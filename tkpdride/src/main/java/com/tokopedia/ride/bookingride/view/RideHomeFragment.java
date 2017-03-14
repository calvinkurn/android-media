package com.tokopedia.ride.bookingride.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.tokopedia.core.geolocation.utils.GeoLocationUtils;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.bookingride.di.BookingRideDependencyInjection;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class RideHomeFragment extends BaseFragment implements BookingRideContract.View, OnMapReadyCallback {
    public static final int LOGIN_REQUEST_CODE = 1005;
    private static final int PLACE_AUTOCOMPLETE_SOURCE_REQUEST_CODE = 1006;
    private static final int PLACE_AUTOCOMPLETE_DESTINATION_REQUEST_CODE = 1007;

    BookingRideContract.Presenter mPresenter;

    @BindView(R2.id.toolbar)
    Toolbar mToolbar;
    @BindView(R2.id.mapview)
    MapView mapView;
    @BindView(R2.id.crux_cabs_source)
    TextView tvSource;
    @BindView(R2.id.crux_cabs_destination)
    TextView tvDestination;

    GoogleMap mGoogleMap;

    private OnFragmentInteractionListener mListener;

    public RideHomeFragment() {
    }

    public static RideHomeFragment newInstance() {
        RideHomeFragment fragment = new RideHomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setInitialVariable();
        setViewListener();
        setupToolbar();
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        mPresenter.attachView(this);
        mPresenter.initialize();
    }

    private void setupToolbar() {
        if (mToolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setViewListener() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ride_home;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }

    @Override
    public boolean isUserLoggedIn() {
        return SessionHandler.isV4Login(getActivity());
    }

    @Override
    public void navigateToLoginPage() {
        Intent intent = SessionRouter.getLoginActivityIntent(getActivity());
        intent.putExtra(Session.WHICH_FRAGMENT_KEY,
                TkpdState.DrawerPosition.LOGIN);
        startActivityForResult(intent, LOGIN_REQUEST_CODE);
    }

    @Override
    public void showVerificationPhoneNumberPage() {

    }

    @Override
    public boolean isUserPhoneNumberVerified() {
        return SessionHandler.isMsisdnVerified();
    }

    @Override
    public void prepareMainView() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        setMapViewListener();
    }

    private void setMapViewListener() {
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        MapsInitializer.initialize(this.getActivity());

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.setMyLocationEnabled(true);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void setInitialVariable() {
        mPresenter = BookingRideDependencyInjection.createPresenter("TOKEN");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case LOGIN_REQUEST_CODE:
                    Toast.makeText(getActivity(), "User Logged In", Toast.LENGTH_SHORT).show();
                    mPresenter.initialize();
                    break;
                case PLACE_AUTOCOMPLETE_SOURCE_REQUEST_CODE:
                    Place source = PlaceAutocomplete.getPlace(getActivity(), data);
                    setSourceLocationText(String.valueOf(source.getAddress()));
                    break;
                case PLACE_AUTOCOMPLETE_DESTINATION_REQUEST_CODE:
                    Place destination = PlaceAutocomplete.getPlace(getActivity(), data);
                    setSourceLocationText(String.valueOf(destination.getAddress()));
                    break;
            }
        }

    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void moveToCurrentLocation(double latitude, double longitude) {
        LatLng currentLocation = new LatLng(latitude, longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation, 12);
        mGoogleMap.animateCamera(cameraUpdate);
    }

    @Override
    public void renderDefaultPickupLocation(double latitude, double longitude) {
        setSourceLocationText(GeoLocationUtils.reverseGeoCode(getActivity(), latitude, longitude));
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null)
            mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null)
            mapView.onLowMemory();
    }

    @OnClick(R2.id.crux_cabs_source)
    public void actionSourceButtonClicked() {
        Intent intent =
                null;
        try {
            intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(getActivity());

            startActivityForResult(intent, PLACE_AUTOCOMPLETE_SOURCE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R2.id.crux_cabs_destination)
    public void actionDestinationButtonClicked() {
        Intent intent =
                null;
        try {
            intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(getActivity());

            startActivityForResult(intent, PLACE_AUTOCOMPLETE_DESTINATION_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setSourceLocationText(String address) {
        tvSource.setText(address);
    }

    @Override
    public void setDestinationLocationText(String address) {
        tvDestination.setText(address);
    }
}
