package com.tokopedia.ride.bookingride.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.tokopedia.core.geolocation.utils.GeoLocationUtils;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.bookingride.di.BookingRideDependencyInjection;
import com.tokopedia.ride.bookingride.view.BookingRideContract;
import com.tokopedia.ride.bookingride.view.activity.GooglePlacePickerActivity;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;

import java.util.List;

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
    @BindView(R2.id.layout_src_destination)
    View mSrcDestLayout;
    @BindView(R2.id.iv_my_location_button)
    ImageView myLocationButton;
    @BindView(R2.id.marker_time)
    ImageView mMarkerTimeBackgroundImage;
    @BindView(R2.id.tv_marker_pickup_eta)
    TextView mMarkerTimeTextView;
    @BindView(R2.id.layout_marker_time)
    RelativeLayout mMarkerTimeLayout;
    @BindView(R2.id.iv_android_center_marker)
    ImageView mMarkerImageView;
    @BindView(R2.id.iv_android_center_marker_cross)
    ImageView mMarkerCenterCrossImage;

    private PlacePassViewModel mSource, mDestination;

    boolean isAlreadySelectDestination;
    boolean isDisableSelectLocation;

    GoogleMap mGoogleMap;

    private OnFragmentInteractionListener mListener;
    private int mToolBarHeightinPx;

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
        MapsInitializer.initialize(this.getActivity());

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        mGoogleMap.setMyLocationEnabled(false);

        mGoogleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                if (i == REASON_GESTURE) {
                    mPresenter.onMapMoveCameraStarted();
                }
            }
        });

        mGoogleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                mPresenter.onMapMoveCameraIdle();
            }
        });
    }

    @Override
    public void onMapDragStarted() {

        //set source as go to pin
        //add validation if user already pick destination
        if (!isAlreadySelectDestination) {
            setSourceLocationText("GO TO PIN");
        }

        //animate marker to lift up
        AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
        mMarkerTimeLayout.animate().setInterpolator(interpolator).translationY(-mToolBarHeightinPx).setDuration(300);
        mMarkerImageView.animate().setInterpolator(interpolator).translationY(-mToolBarHeightinPx).setDuration(300);
        mMarkerCenterCrossImage.animate().setInterpolator(interpolator).scaleX(1).scaleY(1).setDuration(300);

        //animate toolbar and source destination layout
        mToolbar.animate().translationY(-mToolBarHeightinPx).setDuration(300);
        mSrcDestLayout.animate().translationY(-mToolBarHeightinPx).setDuration(300);

        mListener.animateBottomPanelOnMapDragging();
    }

    @Override
    public void onMapDragStopped() {
        //set address based on current address and refresh the product list
        //add validation if user already pick destination
        if (!isAlreadySelectDestination) {
            double latitude = mGoogleMap.getCameraPosition().target.latitude;
            double longitude = mGoogleMap.getCameraPosition().target.longitude;
            String sourceAddress = GeoLocationUtils.reverseGeoCodeToShortAdd(getActivity(), latitude, longitude);
            renderDefaultPickupLocation(latitude, longitude, sourceAddress);
        }
        //animate marker to lift down
        AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
        mMarkerTimeLayout.animate().setInterpolator(interpolator).translationY(0).setDuration(300);
        mMarkerImageView.animate().setInterpolator(interpolator).translationY(0).setDuration(300);
        mMarkerCenterCrossImage.animate().setInterpolator(interpolator).scaleX(0).scaleY(0).setDuration(300);

        //animate toolbar and source destination layout
        mToolbar.animate().translationY(0).setDuration(300);
        mSrcDestLayout.animate().translationY(0).setDuration(300);

        mListener.animateBottomPanelOnMapStopped();
    }

    public void disablePickLocation() {
        isDisableSelectLocation = true;
    }

    public interface OnFragmentInteractionListener {
        void onSourceAndDestinationChanged(PlacePassViewModel source, PlacePassViewModel destination);

        void animateBottomPanelOnMapDragging();

        void animateBottomPanelOnMapStopped();
    }

    private void setInitialVariable() {
        mPresenter = BookingRideDependencyInjection.createPresenter(
                getActivity().getApplicationContext()
        );

        mToolBarHeightinPx = (int) getResources().getDimension(R.dimen.tooler_height);
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
                    mSource = data.getParcelableExtra(GooglePlacePickerActivity.EXTRA_SELECTED_PLACE);
                    setSourceLocationText(String.valueOf(mSource.getTitle()));
                    proccessToRenderRideProduct();
                    break;
                case PLACE_AUTOCOMPLETE_DESTINATION_REQUEST_CODE:
                    mDestination = data.getParcelableExtra(GooglePlacePickerActivity.EXTRA_SELECTED_PLACE);
                    setDestinationLocationText(String.valueOf(mDestination.getTitle()));
                    proccessToRenderRideProduct();
                    isAlreadySelectDestination = true;
                    hideMarkerCenter();
                    break;
            }
        }

    }

    private void proccessToRenderRideProduct() {
        startMarkerAnimation();
        mListener.onSourceAndDestinationChanged(mSource, mDestination);
        if (mSource != null && mDestination != null) {
            mPresenter.getOverviewPolyline(mSource.getLatitude(), mSource.getLongitude(),
                    mDestination.getLatitude(), mDestination.getLongitude());
        }
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void moveToCurrentLocation(double latitude, double longitude) {
        LatLng currentLocation = new LatLng(latitude, longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation, 18);
        mGoogleMap.animateCamera(cameraUpdate);
    }

    @Override
    public void renderDefaultPickupLocation(double latitude, double longitude, String sourceAddress) {
        mSource = new PlacePassViewModel();
        mSource.setAddress(sourceAddress);
        mSource.setTitle(sourceAddress);
        mSource.setLatitude(latitude);
        mSource.setLongitude(longitude);
        proccessToRenderRideProduct();
        setSourceLocationText(sourceAddress);
    }

    @Override
    public void setSourceLocation(PlacePassViewModel location) {
        mSource = location;
        proccessToRenderRideProduct();
        setSourceLocationText(location.getTitle());
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
        if (isDisableSelectLocation)
            return;
        Intent intent = GooglePlacePickerActivity.getCallingIntent(getActivity());
        intent.putExtra(GooglePlacePickerActivity.EXTRA_REQUEST_CODE, PLACE_AUTOCOMPLETE_SOURCE_REQUEST_CODE);
        startActivityForResult(intent, PLACE_AUTOCOMPLETE_SOURCE_REQUEST_CODE);
    }

    @OnClick(R2.id.crux_cabs_destination)
    public void actionDestinationButtonClicked() {
        if (isDisableSelectLocation)
            return;
        Intent intent = GooglePlacePickerActivity.getCallingIntent(getActivity());
        intent.putExtra(GooglePlacePickerActivity.EXTRA_REQUEST_CODE, PLACE_AUTOCOMPLETE_DESTINATION_REQUEST_CODE);
        startActivityForResult(intent, PLACE_AUTOCOMPLETE_DESTINATION_REQUEST_CODE);
    }

    @Override
    public void setSourceLocationText(String address) {
        tvSource.setText(address);
    }

    @Override
    public void setDestinationLocationText(String address) {
        tvDestination.setText(address);
    }

    private void startMarkerAnimation() {
        mMarkerTimeTextView.setText("--");

        if (mMarkerTimeBackgroundImage.getDrawable() instanceof Animatable) {
            ((Animatable) mMarkerTimeBackgroundImage.getDrawable()).start();
        }
    }


    @Override
    public void renderTripRoute(List<List<LatLng>> routes) {
        mGoogleMap.clear();
        for (List<LatLng> route : routes) {
            mGoogleMap.addPolyline(new PolylineOptions()
                    .addAll(route)
                    .width(10)
                    .color(Color.BLUE)
                    .geodesic(true));
        }
    }

    @OnClick(R2.id.iv_my_location_button)
    public void actionMyLocationButtonClicked() {
        mPresenter.actionMyLocation();
    }

    @Override
    public void hideMarkerCenter() {
        mMarkerTimeBackgroundImage.setVisibility(View.GONE);
        mMarkerTimeTextView.setVisibility(View.GONE);
        mMarkerTimeLayout.setVisibility(View.GONE);
        mMarkerImageView.setVisibility(View.GONE);
        mMarkerCenterCrossImage.setVisibility(View.GONE);
    }

    public void setMarkerText(String timeEst) {
        mMarkerTimeTextView.setText(timeEst);
    }
}
