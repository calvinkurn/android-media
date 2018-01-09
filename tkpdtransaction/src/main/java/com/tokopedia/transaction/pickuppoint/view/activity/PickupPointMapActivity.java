package com.tokopedia.transaction.pickuppoint.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.pickuppoint.domain.model.Store;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.tokopedia.transaction.pickuppoint.view.contract.PickupPointContract.Constant.INTENT_DATA_STORE;

public class PickupPointMapActivity extends BasePresenterActivity implements OnMapReadyCallback {

    @BindView(R2.id.btn_choose_pickup_booth)
    Button btnChoosePickupBooth;
    @BindView(R2.id.mapview)
    MapView mapView;

    private Store store;

    public static Intent createInstance(Activity activity, Store store) {
        Intent intent = new Intent(activity, PickupPointMapActivity.class);
        intent.putExtra(INTENT_DATA_STORE, store);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pickup_booth_map;
    }

    @Override
    protected void initView() {
        if (getIntent().getParcelableExtra(INTENT_DATA_STORE) != null) {
            store = getIntent().getParcelableExtra(INTENT_DATA_STORE);
            setupMapView();
        }
    }

    private void setupMapView() {
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    private void setGoogleMap(GoogleMap googleMap) {
        if (googleMap != null) {
            List<String> geolocation = Arrays.asList(store.getGeolocation().split(","));
            double latitude = Double.parseDouble(geolocation.get(0));
            double longitude = Double.parseDouble(geolocation.get(1));
            LatLng latLng = new LatLng(latitude, longitude);

            googleMap.getUiSettings().setMapToolbarEnabled(false);
            googleMap.addMarker(new MarkerOptions().position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_icon_pointer_toped))
            ).setDraggable(true);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    // need this even it's not used
                    // it's used to override default function of OnMapClickListener
                    // which is navigate to default Google Map Apps
                }
            });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        setGoogleMap(googleMap);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        toolbar.setNavigationIcon(com.tokopedia.core.R.drawable.ic_clear_24dp);
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @OnClick(R2.id.btn_choose_pickup_booth)
    public void onChoosePickupBooth() {
        setResult(Activity.RESULT_OK, getIntent());
        finish();
    }

}