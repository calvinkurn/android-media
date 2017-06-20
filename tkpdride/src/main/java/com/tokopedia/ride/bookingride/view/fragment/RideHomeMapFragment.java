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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.google.maps.android.PolyUtil;
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
import com.tokopedia.ride.common.place.domain.model.OverviewPolyline;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class RideHomeMapFragment extends BaseFragment implements RideHomeMapContract.View, OnMapReadyCallback, TouchableWrapperLayout.OnDragListener {
    private static final String EXTRA_IS_ALREADY_HAVE_LOC = "EXTRA_IS_ALREADY_HAVE_LOC";
    private static final String EXTRA_SOURCE = "EXTRA_SOURCE";
    private static final String EXTRA_DESTINATION = "EXTRA_DESTINATION";

    public static final int LOGIN_REQUEST_CODE = 1005;
    public static final int PLACE_AUTOCOMPLETE_SOURCE_REQUEST_CODE = 1006;
    public static final int PLACE_AUTOCOMPLETE_DESTINATION_REQUEST_CODE = 1007;
    public static final int REQUEST_CHECK_LOCATION_SETTING_REQUEST_CODE = 1008;

    private static final float DEFAULT_MAP_ZOOM = 14;
    private static final float SELECT_SOURCE_MAP_ZOOM = 16;
    private static final LatLng DEFAULT_LATLNG = new LatLng(-6.175794, 106.826457);
    private static final String DEFAULT_EMPTY_VALUE = "";
    private static final String DEFAULT_EMPTY_MARKER = "--";

    private RideHomeMapContract.Presenter presenter;

    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.mapview)
    MapView mapView;
    @BindView(R2.id.crux_cabs_source)
    TextView sourceTextView;
    @BindView(R2.id.crux_cabs_destination)
    TextView destinationTextView;
    @BindView(R2.id.layout_src_destination)
    View sourceDestinationPickerLayout;
    @BindView(R2.id.marker_time)
    ImageView markerTimeBackgroundImageView;
    @BindView(R2.id.tv_marker_pickup_eta)
    TextView markerTimeTextView;
    @BindView(R2.id.layout_marker_time)
    RelativeLayout markerTimeLayout;
    @BindView(R2.id.iv_android_center_marker)
    ImageView markerTimeImageView;
    @BindView(R2.id.iv_android_center_marker_cross)
    ImageView markerTimeCrossImageView;
    @BindView(R2.id.crux_cabs_source_container)
    RelativeLayout sourcePickerLayout;
    @BindView(R2.id.layout_destination)
    RelativeLayout destinationLayoout;
    @BindView(R2.id.tw_map_wrapper)
    TouchableWrapperLayout mapWrapperLayout;
    @BindView((R2.id.destination_clear))
    ImageView destinationClearImageView;

    private PlacePassViewModel source, destination;

    private boolean isAlreadySelectDestination;
    private boolean isDisableSelectLocation;

    private GoogleMap googleMap;
    private OnFragmentInteractionListener interactionListener;
    private int toolBarHeightinPx;

    public interface OnFragmentInteractionListener {
        void onSourceAndDestinationChanged(PlacePassViewModel source, PlacePassViewModel destination);

        void animateBottomPanelOnMapDragging();

        void animateBottomPanelOnMapStopped();

        void showMessageInBottomContainer(String message, String btnText);

        SlidingUpPanelLayout.PanelState getPanelState();

        int getBottomViewLocation();

        void collapseBottomPanel();
    }

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
        try {
            mapView.onCreate(savedInstanceState);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        mapView.getMapAsync(this);
        presenter.attachView(this);
        presenter.initialize();
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
        try {
            mapView.onCreate(savedInstanceState);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        mapView.getMapAsync(this);
        if (savedInstanceState != null) {
            source = savedInstanceState.getParcelable(EXTRA_SOURCE);
            destination = savedInstanceState.getParcelable(EXTRA_DESTINATION);
        }
    }

    private void setupToolbar() {
        if (toolbar != null) {
            toolbar.setTitle(getString(R.string.toolbar_title_booking));
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
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
            markerTimeBackgroundImageView.setImageResource(R.drawable.avd_ride_marker);
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
            interactionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            interactionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        interactionListener = null;
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
        this.googleMap = googleMap;
        setMapViewListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (googleMap != null && source != null) {
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
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_LATLNG, DEFAULT_MAP_ZOOM));
//        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
//            @Override
//            public void onCameraChange(CameraPosition cameraPosition) {
//
//            }
//        });
//        mapView.se
//
//        googleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
//            @Override
//            public void onCameraMoveStarted(int i) {
//                if (i == REASON_GESTURE) {
//                    presenter.onMapMoveCameraStarted();
//                }
//            }
//        });
//
//        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
//            @Override
//            public void onCameraIdle() {
//                presenter.onMapMoveCameraIdle();
//            }
//        });

        if (source != null && destination != null) {
            presenter.getOverviewPolyline(source.getLatitude(), source.getLongitude(),
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
            setSourceLocationText(getString(R.string.ride_home_map_go_to_pin));
        }
        markerTimeTextView.setText(DEFAULT_EMPTY_MARKER);

        //animate marker to lift up
        /*
        AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
        markerTimeLayout.animate().setInterpolator(interpolator).translationY(-toolBarHeightinPx).setDuration(300);
        markerTimeImageView.animate().setInterpolator(interpolator).translationY(-toolBarHeightinPx).setDuration(300);
        markerTimeCrossImageView.animate().setInterpolator(interpolator).scaleX(1).scaleY(1).setDuration(300);
        */
        //animate toolbar and source destination layout
        if (interactionListener.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            toolbar.animate().translationY(-toolBarHeightinPx).setDuration(300);
            sourceDestinationPickerLayout.animate().translationY(-toolBarHeightinPx).setDuration(300);
        }

        interactionListener.animateBottomPanelOnMapDragging();
    }

    @Override
    public void onMapDragStopped() {
        if (googleMap == null) return;
        //set address based on current address and refresh the product list
        //add validation if user already pick destination
        if (!isAlreadySelectDestination) {
            double latitude = googleMap.getCameraPosition().target.latitude;
            double longitude = googleMap.getCameraPosition().target.longitude;
            presenter.actionMapDragStopped(latitude, longitude);
        }
        //animate marker to lift down
        /*
        AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
        markerTimeLayout.animate().setInterpolator(interpolator).translationY(0).setDuration(300);
        markerTimeImageView.animate().setInterpolator(interpolator).translationY(0).setDuration(300);
        markerTimeCrossImageView.animate().setInterpolator(interpolator).scaleX(0).scaleY(0).setDuration(300);
        */

        //animate toolbar and source destination layout
        toolbar.animate().translationY(0).setDuration(300);
        sourceDestinationPickerLayout.animate().translationY(0).setDuration(300);

        interactionListener.animateBottomPanelOnMapStopped();
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
        presenter.handleEnableLocationDialogResult(resultCode);
    }

    @Override
    public void onLayoutDrag() {
        presenter.onMapMoveCameraStarted();
    }

    @Override
    public void onLayoutIdle() {
        presenter.onMapMoveCameraIdle();
    }

    private void setInitialVariable() {
        presenter = BookingRideDependencyInjection.createPresenter(
                getActivity().getApplicationContext()
        );

        toolBarHeightinPx = (int) getResources().getDimension(R.dimen.tooler_height);

        if (isLaunchedWithLocation()) {
            source = getArguments().getParcelable(EXTRA_SOURCE);
            destination = getArguments().getParcelable(EXTRA_DESTINATION);
            isAlreadySelectDestination = true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case LOGIN_REQUEST_CODE:
                    presenter.initialize();
                    break;
                case PLACE_AUTOCOMPLETE_SOURCE_REQUEST_CODE:
                    PlacePassViewModel sourceTemp = data.getParcelableExtra(GooglePlacePickerActivity.EXTRA_SELECTED_PLACE);
                    if (sourceTemp.getLatitude() == 0.0 || sourceTemp.getLongitude() == 0.0) {
                        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.ride_home_map_pickup_zero_error));
                    } else {
                        source = sourceTemp;
                        interactionListener.collapseBottomPanel();
                        presenter.setSourceSelectedFromAddress();
                        setSourceLocationText(String.valueOf(source.getTitle()));
                        proccessToRenderRideProduct();
                        if (!isAlreadySelectDestination) {
                            moveMapToLocation(source.getLatitude(), source.getLongitude());
                        }
                    }

                    break;
                case PLACE_AUTOCOMPLETE_DESTINATION_REQUEST_CODE:
                    PlacePassViewModel destinationTemp = data.getParcelableExtra(GooglePlacePickerActivity.EXTRA_SELECTED_PLACE);
                    if (destinationTemp.getLatitude() == source.getLatitude() && destinationTemp.getLongitude() == source.getLongitude()) {
                        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.ride_home_map_dest_same_source_error));
                    } else if (destinationTemp.getLatitude() == 0.0 || destinationTemp.getLongitude() == 0.0) {
                        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.ride_home_map_dest_zero_error));
                    } else {
                        destination = destinationTemp;
                        interactionListener.collapseBottomPanel();
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
        interactionListener.onSourceAndDestinationChanged(source, destination);
        if (source != null && destination != null) {
            presenter.getOverviewPolyline(source.getLatitude(), source.getLongitude(),
                    destination.getLatitude(), destination.getLongitude());
        }
    }

    @Override
    public void showMessage(String message, String btnText) {
        interactionListener.showMessageInBottomContainer(message, btnText);
    }

    @Override
    public void moveMapToLocation(double latitude, double longitude) {
        if (googleMap != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), SELECT_SOURCE_MAP_ZOOM));
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
        presenter.onResume();
        if (source != null && destination != null) {
            presenter.getOverviewPolyline(source.getLatitude(), source.getLongitude(),
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
        startActivityForResultWithClipReveal(intent, PLACE_AUTOCOMPLETE_SOURCE_REQUEST_CODE, sourcePickerLayout);
        //startActivityForResult(intent, PLACE_AUTOCOMPLETE_SOURCE_REQUEST_CODE);
    }

    @OnClick(R2.id.crux_cabs_destination)
    public void actionDestinationButtonClicked() {
        if (isDisableSelectLocation)
            return;

        Intent intent = GooglePlacePickerActivity.getCallingIntent(getActivity(), R.drawable.marker_red);
        intent.putExtra(GooglePlacePickerActivity.EXTRA_REQUEST_CODE, PLACE_AUTOCOMPLETE_DESTINATION_REQUEST_CODE);
        intent.putExtra(GooglePlacePickerActivity.EXTRA_SOURCE, source);
        startActivityForResultWithClipReveal(intent, PLACE_AUTOCOMPLETE_DESTINATION_REQUEST_CODE, destinationLayoout);
        //startActivityForResult(intent, PLACE_AUTOCOMPLETE_DESTINATION_REQUEST_CODE);
    }

    @OnClick(R2.id.destination_clear)
    public void actionDestinationClearIconClicked() {
        enablePickLocation();
        destination = null;
        isAlreadySelectDestination = false;

        //update map
        if (googleMap != null) {
            googleMap.clear();
            moveMapToLocation(source.getLatitude(), source.getLongitude());
        }
        showMarkerCenter();

        setDestinationLocationText(DEFAULT_EMPTY_VALUE);
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
        sourceTextView.setText(address);
    }

    @Override
    public void setDestinationLocationText(String address) {
        if (address != null && address.length() > 0) {
            destinationTextView.setText(address);
            destinationClearImageView.setVisibility(View.VISIBLE);
        } else {
            destinationTextView.setText(DEFAULT_EMPTY_VALUE);
            destinationClearImageView.setVisibility(View.GONE);
        }
    }

    private void startMarkerAnimation() {
        markerTimeTextView.setText(DEFAULT_EMPTY_MARKER);

        if (markerTimeBackgroundImageView.getDrawable() instanceof Animatable) {
            ((Animatable) markerTimeBackgroundImageView.getDrawable()).start();
        }
    }


    @Override
    public void renderTripPolyline(List<OverviewPolyline> overviewPolylines) {
        if (googleMap == null) {
            return;
        }

        List<List<LatLng>> routes = new ArrayList<>();
        for (OverviewPolyline route : overviewPolylines) {
            routes.add(PolyUtil.decode(route.getOverviewPolyline()));
        }

        googleMap.clear();

        for (List<LatLng> route : routes) {
            if (route.size() > 1) {
                RouteMapAnimator.getInstance().animateRoute(googleMap, route);
            }
//            googleMap.addPolyline(new PolylineOptions()
//                    .addAll(route)
//                    .width(10)
//                    .color(Color.BLACK)
//                    .geodesic(true));
        }


        //add markers on source and destination
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(source.getLatitude(), source.getLongitude()))
                .icon(getMarkerIcon(R.drawable.marker_green))
                .title(source.getAddress()));

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(destination.getLatitude(), destination.getLongitude()))
                .icon(getMarkerIcon(R.drawable.marker_red))
                .title(destination.getAddress()));

        //zoom map to fit both source and dest
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(source.getLatitude(), source.getLongitude()));
        builder.include(new LatLng(destination.getLatitude(), destination.getLongitude()));
        if (overviewPolylines.size() > 0) {
            builder.include(new LatLng(
                    overviewPolylines.get(0).getBounds().getNortheast().getLatitude(),
                    overviewPolylines.get(0).getBounds().getNortheast().getLongitude()
            ));

            builder.include(new LatLng(
                    overviewPolylines.get(0).getBounds().getSouthwest().getLatitude(),
                    overviewPolylines.get(0).getBounds().getSouthwest().getLongitude()
            ));
        }
        int widthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
        int topYAxis = sourceDestinationPickerLayout.getBottom();
        int bottomYAxis = interactionListener.getBottomViewLocation();
        int heightPixels = bottomYAxis - topYAxis;
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),
                widthPixels, heightPixels,
                getResources().getDimensionPixelSize(R.dimen.map_polyline_padding))
        );
    }

    public BitmapDescriptor getMarkerIcon(int resourceId) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), resourceId);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, getResources().getDimensionPixelSize(R.dimen.marker_width), getResources().getDimensionPixelSize(R.dimen.marker_height), false);
        return BitmapDescriptorFactory.fromBitmap(resizedBitmap);
    }

    @OnClick(R2.id.iv_my_location_button)
    public void actionMyLocationButtonClicked() {
        presenter.actionMyLocation();
    }

    @Override
    public void hideMarkerCenter() {
        markerTimeBackgroundImageView.setVisibility(View.GONE);
        markerTimeTextView.setVisibility(View.GONE);
        markerTimeLayout.setVisibility(View.GONE);
        markerTimeImageView.setVisibility(View.GONE);
        markerTimeCrossImageView.setVisibility(View.GONE);
    }

    @Override
    public void showMarkerCenter() {
        markerTimeBackgroundImageView.setVisibility(View.VISIBLE);
        markerTimeTextView.setVisibility(View.VISIBLE);
        markerTimeLayout.setVisibility(View.VISIBLE);
        markerTimeImageView.setVisibility(View.VISIBLE);
        markerTimeCrossImageView.setVisibility(View.GONE);
    }

    @Override
    public void showEnterDestError() {
        Animation shake = AnimationUtils.loadAnimation(getActivityContext(), R.anim.shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                destinationTextView.setHintTextColor(ContextCompat.getColor(getActivityContext(), R.color.red_500));
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                destinationTextView.setHintTextColor(ContextCompat.getColor(getActivityContext(), R.color.pdp_rating_color_new));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        destinationTextView.startAnimation(shake);
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
        markerTimeTextView.setText(timeEst);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }
}