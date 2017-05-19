package com.tokopedia.ride.maplocationpicker;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.base.presentation.BaseFragment;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapLocationPickerFragment extends BaseFragment implements OnMapReadyCallback, MapLocationPickerContract.View {
    private static final LatLng DEFAULT_LATLNG = new LatLng(-6.175794, 106.826457);
    private static final float DEFAULT_MAP_ZOOM = 16;
    private GoogleMap googleMap;

    @BindView(R2.id.mapview)
    MapView mapView;

    MapLocationPickerContract.Presenter presenter;

    public static MapLocationPickerFragment newInstance(){
        return new MapLocationPickerFragment();
    }

    public MapLocationPickerFragment() {
        // Required empty public constructor
    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_map_location_picker;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        presenter = new MapLocationPickerPresenter();
        presenter.attachView(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        MapsInitializer.initialize(this.getActivity());
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        googleMap.setMyLocationEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_LATLNG, DEFAULT_MAP_ZOOM));
    }
}