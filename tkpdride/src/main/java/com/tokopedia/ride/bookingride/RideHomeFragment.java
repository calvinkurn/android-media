package com.tokopedia.ride.bookingride;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.base.presentation.BaseFragment;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

public class RideHomeFragment extends BaseFragment implements BookingRideContract.View, OnMapReadyCallback {
    public static final int REQUEST_CODE_LOGIN = 1005;
    BookingRideContract.Presenter mPresenter;

    @BindView(R2.id.cabs_crux_sliding_layout)
    SlidingUpPanelLayout mSlidingUpPanelLayout;
    @BindView(R2.id.layout_view_flipper)
    ViewFlipper viewFlipper;
    @BindView(R2.id.hidden_panel)
    RelativeLayout hiddenPanel;
    @BindView(R2.id.block_translucent_view)
    FrameLayout blockScreenView;
    @BindView(R2.id.toolbar)
    Toolbar mToolbar;
    @BindView(R2.id.mapview)
    MapView mapView;

    GoogleMap mGoogleMap;

    private boolean isPanelShown;
    private DisplayMetrics displayMetrics;

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
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        mPresenter.attachView(this);
        mPresenter.initialize();
    }

    private void setViewListener() {
        hiddenPanel.setVisibility(View.GONE);
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

    }

    @Override
    public void showVerificationPhoneNumberDialog() {

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
        isPanelShown = false;
        displayMetrics = getResources().getDisplayMetrics();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_LOGIN:
                    Toast.makeText(getActivity(), "User Logged In", Toast.LENGTH_SHORT).show();
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

    public void slideUpDown(final View view) {
        if (!isPanelShown) {
            showBlockingView(500);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Show the panel
                    Animation bottomUp = AnimationUtils.loadAnimation(getActivity(),
                            R.anim.bottom_up);

                    hiddenPanel.startAnimation(bottomUp);
                    hiddenPanel.setVisibility(View.VISIBLE);
                    isPanelShown = true;
                }
            }, 500);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Hide the Panel
                    Animation bottomDown = AnimationUtils.loadAnimation(getActivity(),
                            R.anim.bottom_down);

                    hiddenPanel.startAnimation(bottomDown);
                    hiddenPanel.setVisibility(View.INVISIBLE);
                    isPanelShown = false;

                    hideBlockingView();
                }
            }, 200);
        }
    }

    private void hideBlockingView() {
        blockScreenView.setVisibility(View.GONE);
    }

    private void showBlockingView(int delay) {
        blockScreenView.setVisibility(View.VISIBLE);

        final ObjectAnimator backgroundColorAnimator = ObjectAnimator.ofObject(blockScreenView,
                "backgroundColor",
                new ArgbEvaluator(),
                0x00000000,
                0xBB000000);
        backgroundColorAnimator.setDuration(delay);
        backgroundColorAnimator.start();
    }


}
