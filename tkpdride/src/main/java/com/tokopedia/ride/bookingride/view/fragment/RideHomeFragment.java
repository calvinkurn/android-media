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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
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
    public static final String EXTRA_IS_ALREADY_HAVE_LOC = "EXTRA_IS_ALREADY_HAVE_LOC";
    private static final String EXTRA_SOURCE = "EXTRA_SOURCE";
    private static final String EXTRA_DESTINATION = "EXTRA_DESTINATION";

    public static final int LOGIN_REQUEST_CODE = 1005;
    private static final int PLACE_AUTOCOMPLETE_SOURCE_REQUEST_CODE = 1006;
    private static final int PLACE_AUTOCOMPLETE_DESTINATION_REQUEST_CODE = 1007;
    public static final int REQUEST_CHECK_LOCATION_SETTINGS = 1008;

    private static final float DEFAUL_MAP_ZOOM = 16;
    private static final float SELECT_SOURCE_MAP_ZOOM = 18;
    private static final LatLng DEFAULT_LATLNG = new LatLng(-6.21462d, 106.84513d);

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

    /**
     * This singleton instance used if apps got pressed back when in confirm booking
     * or on trip screen
     *
     * @param source
     * @param destination
     * @return
     */
    public static RideHomeFragment newInstance(PlacePassViewModel source, PlacePassViewModel destination) {
        RideHomeFragment fragment = new RideHomeFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_IS_ALREADY_HAVE_LOC, true);
        bundle.putParcelable(EXTRA_SOURCE, source);
        bundle.putParcelable(EXTRA_DESTINATION, destination);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static RideHomeFragment newInstance() {
        return new RideHomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInitialVariable();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViewListener();
        setupToolbar();
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        mPresenter.attachView(this);
        mPresenter.initialize();
    }

    private void setupToolbar() {
        if (mToolbar != null) {
            mToolbar.setTitle(getString(R.string.toolbar_title_booking));
            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setViewListener() {
        if (getArguments() != null)
            if (getArguments().getBoolean(EXTRA_IS_ALREADY_HAVE_LOC, false)) {
                if (mSource != null && mDestination != null) {
                    setSourceLocationText(mSource.getTitle());
                    setDestinationLocationText(mDestination.getTitle());
                }
            }
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
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_LATLNG, DEFAUL_MAP_ZOOM));

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

        if (mSource != null && mDestination != null) {
            mPresenter.getOverviewPolyline(mSource.getLatitude(), mSource.getLongitude(),
                    mDestination.getLatitude(), mDestination.getLongitude());
        }
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
        if (mListener.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            mToolbar.animate().translationY(-mToolBarHeightinPx).setDuration(300);
            mSrcDestLayout.animate().translationY(-mToolBarHeightinPx).setDuration(300);
        }

        mListener.animateBottomPanelOnMapDragging();
    }

    @Override
    public void onMapDragStopped() {
        //set address based on current address and refresh the product list
        //add validation if user already pick destination
        if (!isAlreadySelectDestination) {
            double latitude = mGoogleMap.getCameraPosition().target.latitude;
            double longitude = mGoogleMap.getCameraPosition().target.longitude;
            mPresenter.actionMapDragStopped(latitude, longitude);
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

    /**
     * This function handles location alert result, initiated from Activity class
     *
     * @param resultCode
     */
    public void handleLocationAlertResult(int resultCode) {
        mPresenter.handleEnableLocationDialogResult(resultCode);
    }

    public interface OnFragmentInteractionListener {
        void onSourceAndDestinationChanged(PlacePassViewModel source, PlacePassViewModel destination);

        void animateBottomPanelOnMapDragging();

        void animateBottomPanelOnMapStopped();

        void showMessageInBottomContainer(String message, String btnText);

        SlidingUpPanelLayout.PanelState getPanelState();
    }

    private void setInitialVariable() {
        mPresenter = BookingRideDependencyInjection.createPresenter(
                getActivity().getApplicationContext()
        );

        mToolBarHeightinPx = (int) getResources().getDimension(R.dimen.tooler_height);

        if (getArguments() != null) {
            if (getArguments().getBoolean(EXTRA_IS_ALREADY_HAVE_LOC, false)) {
                mSource = getArguments().getParcelable(EXTRA_SOURCE);
                mDestination = getArguments().getParcelable(EXTRA_DESTINATION);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("Vishal RideHomeFragment onActivityResult");
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
                    moveMapToLocation(mSource.getLatitude(), mSource.getLongitude());
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
    public void showMessage(String message, String btnText) {
        mListener.showMessageInBottomContainer(message, btnText);
    }

    @Override
    public void moveMapToLocation(double latitude, double longitude) {
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), SELECT_SOURCE_MAP_ZOOM));
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
                    .color(Color.BLACK)
                    .geodesic(true));
        }

        //add markers on source and destination
        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(mSource.getLatitude(), mSource.getLongitude()))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(mSource.getAddress()));

        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(mDestination.getLatitude(), mDestination.getLongitude()))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .title(mDestination.getAddress()));

        //zoom map to fit both source and dest
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(mSource.getLatitude(), mSource.getLongitude()));
        builder.include(new LatLng(mDestination.getLatitude(), mDestination.getLongitude()));
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), getResources().getDimensionPixelSize(R.dimen.map_polyline_padding)));
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

    @Override
    public void showEnterDestError() {
        Animation shake = AnimationUtils.loadAnimation(getActivityContext(), R.anim.shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                tvDestination.setHintTextColor(ContextCompat.getColor(getActivityContext(), R.color.red_500));
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tvDestination.setHintTextColor(ContextCompat.getColor(getActivityContext(), R.color.pdp_rating_color_new));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        tvDestination.startAnimation(shake);
    }

    @Override
    public boolean isAlreadySelectDestination() {
        return isAlreadySelectDestination;
    }

    public void setMarkerText(String timeEst) {
        mMarkerTimeTextView.setText(timeEst);
    }
}