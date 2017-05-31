package com.tokopedia.ride.bookingride.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.bookingride.view.SelectLocationOnMapContract;
import com.tokopedia.ride.bookingride.view.SelectLocationOnMapPresenter;
import com.tokopedia.ride.bookingride.view.TouchableWrapperLayout;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;

import butterknife.BindView;
import butterknife.OnClick;

public class SelectLocationOnMapFragment extends BaseFragment implements SelectLocationOnMapContract.View, OnMapReadyCallback, TouchableWrapperLayout.OnDragListener {
    private static final String EXTRA_SOURCE = "EXTRA_SOURCE";
    public static String EXTRA_MARKER_ID = "EXTRA_MARKER_ID";

    private static final float DEFAULT_MAP_ZOOM = 18;
    private static final LatLng DEFAULT_LATLNG = new LatLng(-6.175794, 106.826457);


    @BindView(R2.id.mapview)
    MapView mapView;
    @BindView(R2.id.tv_cabs_autocomplete)
    TextView addressNameTextView;
    @BindView(R2.id.tw_map_wrapper)
    TouchableWrapperLayout mapWrapperLayout;
    @BindView(R2.id.iv_android_center_marker)
    ImageView centerMarker;
    @BindView(R2.id.pb_loader)
    ProgressBar progressBar;
    @BindView(R2.id.btn_done)
    TextView doneBtn;

    private SelectLocationOnMapContract.Presenter mPresenter;
    //    private PlacePassViewModel source, destination;
    private OnFragmentInteractionListener mListener;
    private GoogleMap mGoogleMap;
    private PlacePassViewModel locationDragged;

    @Override
    public void onLayoutDrag() {
        mPresenter.onMapMoveCameraStarted();
    }

    @Override
    public void onLayoutIdle() {
        mPresenter.onMapMoveCameraIdle();
    }

    public interface OnFragmentInteractionListener {
        void handleSelectDestinationOnMap(PlacePassViewModel destination);

        void backArrowClicked();
    }

    public SelectLocationOnMapFragment() {
    }


    public static SelectLocationOnMapFragment newInstance(PlacePassViewModel source) {
        SelectLocationOnMapFragment fragment = new SelectLocationOnMapFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_SOURCE, source);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static SelectLocationOnMapFragment newInstance(int markerId) {
        SelectLocationOnMapFragment fragment = new SelectLocationOnMapFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_MARKER_ID, markerId);
        fragment.setArguments(bundle);
        return fragment;
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
        mPresenter.attachView(this);
        mPresenter.initialize();
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putParcelable(EXTRA_SOURCE, source);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        if (savedInstanceState != null) {
            if (savedInstanceState.getInt(EXTRA_MARKER_ID) != 0) {
                centerMarker.setImageDrawable(getResources().getDrawable(savedInstanceState.getInt(EXTRA_MARKER_ID)));
            }
//            source = savedInstanceState.getParcelable(EXTRA_SOURCE);
        }
    }

    private void setViewListener() {
        mapWrapperLayout.setListener(this);/*
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(com.tokopedia.core.R.drawable.ic_arrow_back_24dp);
        }*/
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_select_location_on_map;
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
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        setMapViewListener();
    }

    @Override
    public void onStart() {
        super.onStart();/*
        if (mGoogleMap != null && source != null) {
            moveMapToLocation(source.getLatitude(), source.getLongitude());
        }*/
    }

    private void setInitialVariable() {
        mPresenter = new SelectLocationOnMapPresenter();
        /*source = getArguments().getParcelable(EXTRA_SOURCE);
        if (source == null) {
            source = new PlacePassViewModel();
            source.setAddress("Source");
            source.setAddress("Source");
            source.setAndFormatLatitude(DEFAULT_LATLNG.latitude);
            source.setAndFormatLongitude(DEFAULT_LATLNG.longitude);
        }*/
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
    }


    @Override
    public void showMessage(String message) {

    }


    @Override
    public void moveMapToLocation(double latitude, double longitude) {
        if (mGoogleMap != null) {
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), DEFAULT_MAP_ZOOM));
        }
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
    public void setSourceLocationText(String address) {

    }

    @Override
    public void setDestinationLocationText(String address) {
        addressNameTextView.setText(address);
    }

    @Override
    public void setDestination(PlacePassViewModel destination) {
        locationDragged = destination;
    }

    @Override
    public void onMapDraggedStopped() {
        double latitude = mGoogleMap.getCameraPosition().target.latitude;
        double longitude = mGoogleMap.getCameraPosition().target.longitude;
        mPresenter.actionMapDragStopped(latitude, longitude);
    }

    @Override
    public void showCrossLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void disableDoneButton() {
        doneBtn.setEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            doneBtn.setBackground(getResources().getDrawable(R.drawable.rounded_filled_theme_disable_bttn));
        } else {
            doneBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_filled_theme_disable_bttn));
        }
    }

    @Override
    public void hideCrossLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void enableDoneButton() {
        doneBtn.setEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            doneBtn.setBackground(getResources().getDrawable(R.drawable.rounded_filled_theme_bttn));
        } else {
            doneBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_filled_theme_bttn));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
    }

    @OnClick(R2.id.btn_done)
    public void actionOnDoneClicked() {
        mListener.handleSelectDestinationOnMap(locationDragged);
    }

    @OnClick(R2.id.cabs_autocomplete_back_icon)
    public void actionBackClicked() {
        mListener.backArrowClicked();
    }
}