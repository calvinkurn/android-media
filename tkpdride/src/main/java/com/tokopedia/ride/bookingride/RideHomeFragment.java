package com.tokopedia.ride.bookingride;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.base.presentation.BaseFragment;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

public class RideHomeFragment extends BaseFragment implements BookingRideContract.View, OnMapReadyCallback {
    public static final int REQUEST_CODE_LOGIN = 1005;
    BookingRideContract.Presenter mPresenter;

    @BindView(R2.id.layout_view_flipper)
    ViewFlipper viewFlipper;
    @BindView(R2.id.toolbar)
    Toolbar mToolbar;
    @BindView(R2.id.mapview)
    MapView mapView;

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
        mPresenter = new BookingRidePresenter();
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
        startActivityForResult(intent, REQUEST_CODE_LOGIN);
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
        mPresenter = new BookingRidePresenter();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_LOGIN:
                    Toast.makeText(getActivity(), "User Logged In", Toast.LENGTH_SHORT).show();
                    mPresenter.initialize();
                    break;
            }
        }

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void hideMessage(String message) {

    }

    @Override
    public void moveToCurrentLocation(double latitude, double longitude) {
        LatLng currentLocation = new LatLng(latitude, longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation, 12);
        mGoogleMap.animateCamera(cameraUpdate);
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

    @Override
    public void renderUberProductView() {
        Fragment fragment = UberProductFragment.newInstance();

        Slide slideTransition = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            slideTransition = new Slide(Gravity.END);
            ChangeBounds changeBoundsTransition = new ChangeBounds();
            changeBoundsTransition.setDuration(getResources().getInteger(R.integer.anim_duration_medium));
            fragment.setEnterTransition(slideTransition);
            fragment.setAllowEnterTransitionOverlap(true);
            fragment.setAllowReturnTransitionOverlap(true);
            fragment.setSharedElementEnterTransition(changeBoundsTransition);
        }
        getFragmentManager().beginTransaction()
                .replace(R.id.option_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
