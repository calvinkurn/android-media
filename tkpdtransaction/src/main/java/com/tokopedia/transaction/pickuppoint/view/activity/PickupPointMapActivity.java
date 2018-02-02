package com.tokopedia.transaction.pickuppoint.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

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
import static com.tokopedia.transaction.pickuppoint.view.contract.PickupPointContract.Constant.INTENT_DISTRICT_NAME;

public class PickupPointMapActivity extends BasePresenterActivity implements OnMapReadyCallback {

    @BindView(R2.id.btn_choose_pickup_booth)
    Button btnChoosePickupBooth;
    @BindView(R2.id.mapview)
    MapView mapView;
    @BindView(R2.id.tv_pick_up_booth_name)
    TextView tvPickUpBoothName;
    @BindView(R2.id.tv_pick_up_booth_address)
    TextView tvPickUpBoothAddress;

    private Store store;

    public static Intent createInstance(Activity activity, String districtName, Store store) {
        Intent intent = new Intent(activity, PickupPointMapActivity.class);
        intent.putExtra(INTENT_DATA_STORE, store);
        intent.putExtra(INTENT_DISTRICT_NAME, districtName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
        }
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
            tvPickUpBoothName.setText(store.getStoreName());
            tvPickUpBoothAddress.setText(store.getAddress());
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
        toolbar.setTitle(String.format(getString(R.string.title_send_to_pick_up_booth),
                getIntent().getStringExtra(INTENT_DISTRICT_NAME)));
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