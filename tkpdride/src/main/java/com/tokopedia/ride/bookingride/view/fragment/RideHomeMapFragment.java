package com.tokopedia.ride.bookingride.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.bookingride.di.BookingRideDependencyInjection;
import com.tokopedia.ride.bookingride.view.RideHomeMapContract;
import com.tokopedia.ride.bookingride.view.TouchableWrapperLayout;
import com.tokopedia.ride.bookingride.view.activity.GooglePlacePickerActivity;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;
import com.tokopedia.ride.common.animator.RouteMapAnimator;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class RideHomeMapFragment extends BaseFragment implements RideHomeMapContract.View, OnMapReadyCallback, TouchableWrapperLayout.OnDragListener {
    public static final String EXTRA_IS_ALREADY_HAVE_LOC = "EXTRA_IS_ALREADY_HAVE_LOC";
    private static final String EXTRA_SOURCE = "EXTRA_SOURCE";
    private static final String EXTRA_DESTINATION = "EXTRA_DESTINATION";

    public static final int LOGIN_REQUEST_CODE = 1005;
    public static final int PLACE_AUTOCOMPLETE_SOURCE_REQUEST_CODE = 1006;
    public static final int PLACE_AUTOCOMPLETE_DESTINATION_REQUEST_CODE = 1007;
    public static final int REQUEST_CHECK_LOCATION_SETTINGS = 1008;

    private static final float DEFAUL_MAP_ZOOM = 14;
    private static final float SELECT_SOURCE_MAP_ZOOM = 18;
    private static final LatLng DEFAULT_LATLNG = new LatLng(-6.175794, 106.826457);

    RideHomeMapContract.Presenter mPresenter;

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
    @BindView(R2.id.crux_cabs_source_container)
    RelativeLayout mSourceLayout;
    @BindView(R2.id.layout_destination)
    RelativeLayout mDestinationLayout;
    @BindView(R2.id.tw_map_wrapper)
    TouchableWrapperLayout mapWrapperLayout;
    @BindView((R2.id.destination_clear))
    ImageView mDestinationClearIcon;

    private PlacePassViewModel source, destination;

    boolean isAlreadySelectDestination;
    boolean isDisableSelectLocation;

    GoogleMap mGoogleMap;

    private OnFragmentInteractionListener mListener;
    private int mToolBarHeightinPx;

    public RideHomeMapFragment() {
    }

    /**
     * This singleton instance used if apps got pressed back when in confirm booking
     * or on trip screen
     *
     * @param source
     * @param destination
     * @return
     */
    public static RideHomeMapFragment newInstance(PlacePassViewModel source, PlacePassViewModel destination) {
        RideHomeMapFragment fragment = new RideHomeMapFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_IS_ALREADY_HAVE_LOC, true);
        bundle.putParcelable(EXTRA_SOURCE, source);
        bundle.putParcelable(EXTRA_DESTINATION, destination);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static RideHomeMapFragment newInstance() {
        return new RideHomeMapFragment();
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_SOURCE, source);
        outState.putParcelable(EXTRA_DESTINATION, destination);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        if (savedInstanceState != null) {
            source = savedInstanceState.getParcelable(EXTRA_SOURCE);
            destination = savedInstanceState.getParcelable(EXTRA_DESTINATION);
        }
    }

    private void setupToolbar() {
        if (mToolbar != null) {
            mToolbar.setTitle(getString(R.string.toolbar_title_booking));
            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().invalidateOptionsMenu();
        }
    }

    private void setViewListener() {
        mapWrapperLayout.setListener(this);
        if (isLaunchedWithLocation() && source != null && destination != null) {
            setSourceLocationText(source.getTitle());
            setDestinationLocationText(destination.getTitle());
            proccessToRenderRideProduct();
            hideMarkerCenter();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mMarkerTimeBackgroundImage.setImageResource(R.drawable.avd_ride_marker);
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

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleMap != null && source != null) {
            moveMapToLocation(source.getLatitude(), source.getLongitude());
        }
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
//        mGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
//            @Override
//            public void onCameraChange(CameraPosition cameraPosition) {
//
//            }
//        });
//        mapView.se
//
//        mGoogleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
//            @Override
//            public void onCameraMoveStarted(int i) {
//                if (i == REASON_GESTURE) {
//                    mPresenter.onMapMoveCameraStarted();
//                }
//            }
//        });
//
//        mGoogleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
//            @Override
//            public void onCameraIdle() {
//                mPresenter.onMapMoveCameraIdle();
//            }
//        });

        if (source != null && destination != null) {
            mPresenter.getOverviewPolyline(source.getLatitude(), source.getLongitude(),
                    destination.getLatitude(), destination.getLongitude());
        } else if (source != null) {
            moveMapToLocation(source.getLatitude(), source.getLongitude());
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
        /*
        AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
        mMarkerTimeLayout.animate().setInterpolator(interpolator).translationY(-mToolBarHeightinPx).setDuration(300);
        mMarkerImageView.animate().setInterpolator(interpolator).translationY(-mToolBarHeightinPx).setDuration(300);
        mMarkerCenterCrossImage.animate().setInterpolator(interpolator).scaleX(1).scaleY(1).setDuration(300);
        */
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
        /*
        AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
        mMarkerTimeLayout.animate().setInterpolator(interpolator).translationY(0).setDuration(300);
        mMarkerImageView.animate().setInterpolator(interpolator).translationY(0).setDuration(300);
        mMarkerCenterCrossImage.animate().setInterpolator(interpolator).scaleX(0).scaleY(0).setDuration(300);
        */

        //animate toolbar and source destination layout
        mToolbar.animate().translationY(0).setDuration(300);
        mSrcDestLayout.animate().translationY(0).setDuration(300);

        mListener.animateBottomPanelOnMapStopped();
    }

    public void disablePickLocation() {
        isDisableSelectLocation = true;
    }

    public void enablePickLocation() {
        isDisableSelectLocation = false;
    }

    /**
     * This function handles location alert result, initiated from Activity class
     *
     * @param resultCode
     */
    public void handleLocationAlertResult(int resultCode) {
        mPresenter.handleEnableLocationDialogResult(resultCode);
    }

    @Override
    public void onLayoutDrag() {
        mPresenter.onMapMoveCameraStarted();
    }

    @Override
    public void onLayoutIdle() {
        mPresenter.onMapMoveCameraIdle();
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

        if (isLaunchedWithLocation()) {
            source = getArguments().getParcelable(EXTRA_SOURCE);
            destination = getArguments().getParcelable(EXTRA_DESTINATION);
            isAlreadySelectDestination = true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("Vishal RideHomeMapFragment onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case LOGIN_REQUEST_CODE:
                    Toast.makeText(getActivity(), "User Logged In", Toast.LENGTH_SHORT).show();
                    mPresenter.initialize();
                    break;
                case PLACE_AUTOCOMPLETE_SOURCE_REQUEST_CODE:
                    source = data.getParcelableExtra(GooglePlacePickerActivity.EXTRA_SELECTED_PLACE);
                    setSourceLocationText(String.valueOf(source.getTitle()));
                    proccessToRenderRideProduct();
                    if (!isAlreadySelectDestination) {
                        moveMapToLocation(source.getLatitude(), source.getLongitude());
                    }
                    break;
                case PLACE_AUTOCOMPLETE_DESTINATION_REQUEST_CODE:
                    PlacePassViewModel destinationTemp = data.getParcelableExtra(GooglePlacePickerActivity.EXTRA_SELECTED_PLACE);
                    if (destinationTemp.getLatitude() == source.getLatitude() && destinationTemp.getLongitude() == source.getLongitude()) {
                        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.ride_home_map_dest_same_source_error));
                    } else {
                        destination = destinationTemp;
                        setDestinationLocationText(String.valueOf(destination.getTitle()));
                        proccessToRenderRideProduct();
                        isAlreadySelectDestination = true;
                        hideMarkerCenter();
                    }
                    break;
            }
        }
    }

    private void proccessToRenderRideProduct() {
        startMarkerAnimation();
        mListener.onSourceAndDestinationChanged(source, destination);
        if (source != null && destination != null) {
            mPresenter.getOverviewPolyline(source.getLatitude(), source.getLongitude(),
                    destination.getLatitude(), destination.getLongitude());
        }
    }

    @Override
    public void showMessage(String message, String btnText) {
        mListener.showMessageInBottomContainer(message, btnText);
    }

    @Override
    public void moveMapToLocation(double latitude, double longitude) {
        if (mGoogleMap != null) {
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), SELECT_SOURCE_MAP_ZOOM));
        }
    }

    @Override
    public void renderDefaultPickupLocation(double latitude, double longitude, String sourceAddress) {
        source = new PlacePassViewModel();
        source.setAddress(sourceAddress);
        source.setTitle(sourceAddress);
        source.setAndFormatLatitude(latitude);
        source.setAndFormatLongitude(longitude);
        proccessToRenderRideProduct();
        setSourceLocationText(sourceAddress);
    }

    @Override
    public void setSourceLocation(PlacePassViewModel location) {
        source = location;
        proccessToRenderRideProduct();
        setSourceLocationText(location.getTitle());
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
        if (source != null && destination != null) {
            mPresenter.getOverviewPolyline(source.getLatitude(), source.getLongitude(),
                    destination.getLatitude(), destination.getLongitude());
        }
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
        Intent intent = GooglePlacePickerActivity.getCallingIntent(getActivity(), R.drawable.marker_green);
        intent.putExtra(GooglePlacePickerActivity.EXTRA_REQUEST_CODE, PLACE_AUTOCOMPLETE_SOURCE_REQUEST_CODE);
        startActivityForResultWithClipReveal(intent, PLACE_AUTOCOMPLETE_SOURCE_REQUEST_CODE, mDestinationLayout);
        //startActivityForResult(intent, PLACE_AUTOCOMPLETE_SOURCE_REQUEST_CODE);
    }

    @OnClick(R2.id.crux_cabs_destination)
    public void actionDestinationButtonClicked() {
        if (isDisableSelectLocation)
            return;
        Intent intent = GooglePlacePickerActivity.getCallingIntent(getActivity(), R.drawable.marker_red);
        intent.putExtra(GooglePlacePickerActivity.EXTRA_REQUEST_CODE, PLACE_AUTOCOMPLETE_DESTINATION_REQUEST_CODE);
        intent.putExtra(GooglePlacePickerActivity.EXTRA_SOURCE, source);
        startActivityForResultWithClipReveal(intent, PLACE_AUTOCOMPLETE_DESTINATION_REQUEST_CODE, mDestinationLayout);

        //startActivityForResult(intent, PLACE_AUTOCOMPLETE_DESTINATION_REQUEST_CODE);
    }

    @OnClick(R2.id.destination_clear)
    public void actionDestinationClearIconClicked() {
        enablePickLocation();
        destination = null;
        isAlreadySelectDestination = false;

        //update map
        if (mGoogleMap != null) {
            mGoogleMap.clear();
            moveMapToLocation(source.getLatitude(), source.getLongitude());
        }
        showMarkerCenter();

        setDestinationLocationText("");
        proccessToRenderRideProduct();

    }

    private void startActivityForResultWithClipReveal(Intent intent, int requestCode, View view) {
        ActivityOptions options = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            options = ActivityOptions.makeClipRevealAnimation(view, 0, 0,
                    view.getWidth(), view.getHeight());
            startActivityForResult(intent, requestCode, options.toBundle());
        } else {
            startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void setSourceLocationText(String address) {
        tvSource.setText(address);
    }

    @Override
    public void setDestinationLocationText(String address) {
        if (address != null && address.length() > 0) {
            tvDestination.setText(address);
            mDestinationClearIcon.setVisibility(View.VISIBLE);
        } else {
            tvDestination.setText("");
            mDestinationClearIcon.setVisibility(View.GONE);
        }
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
            if (route.size() > 1) {
                RouteMapAnimator.getInstance().animateRoute(mGoogleMap, route);
            }
//            mGoogleMap.addPolyline(new PolylineOptions()
//                    .addAll(route)
//                    .width(10)
//                    .color(Color.BLACK)
//                    .geodesic(true));
        }


        //add markers on source and destination
        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(source.getLatitude(), source.getLongitude()))
                .icon(getMarkerIcon(R.drawable.marker_green))
                .title(source.getAddress()));

        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(destination.getLatitude(), destination.getLongitude()))
                .icon(getMarkerIcon(R.drawable.marker_red))
                .title(destination.getAddress()));

        //zoom map to fit both source and dest
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(source.getLatitude(), source.getLongitude()));
        builder.include(new LatLng(destination.getLatitude(), destination.getLongitude()));
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), getResources().getDimensionPixelSize(R.dimen.map_polyline_padding)));
    }

    public BitmapDescriptor getMarkerIcon(int resourceId) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), resourceId);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, getResources().getDimensionPixelSize(R.dimen.marker_width), getResources().getDimensionPixelSize(R.dimen.marker_height), false);
        return BitmapDescriptorFactory.fromBitmap(resizedBitmap);
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
    public void showMarkerCenter() {
        mMarkerTimeBackgroundImage.setVisibility(View.VISIBLE);
        mMarkerTimeTextView.setVisibility(View.VISIBLE);
        mMarkerTimeLayout.setVisibility(View.VISIBLE);
        mMarkerImageView.setVisibility(View.VISIBLE);
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

    @Override
    public boolean isLaunchedWithLocation() {
        if (getArguments() != null && getArguments().getBoolean(EXTRA_IS_ALREADY_HAVE_LOC, false)) {
            return true;
        }

        return false;
    }

    public void setMarkerText(String timeEst) {
        mMarkerTimeTextView.setText(timeEst);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
    }

    public static float convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }
}