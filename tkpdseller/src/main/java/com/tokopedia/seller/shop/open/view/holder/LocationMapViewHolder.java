package com.tokopedia.seller.shop.open.view.holder;

import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.R;
import com.tokopedia.seller.shop.open.view.model.GoogleLocationViewModel;

/**
 * Created by normansyahputa on 12/21/17.
 */

public class LocationMapViewHolder implements OnMapReadyCallback {

    private final TextView pinPickupLocation;
    private final TextView generatedLocationOpenShop;
    private final MapView mapView;
    private final LinearLayout mapViewContainer;
    private final FrameLayout emptyMapView;
    private View root;
    private final EditText shopAddressEdittext;
    private GoogleMap googleMap;

    private GoogleLocationViewModel googleLocationViewModel;

    public LocationMapViewHolder(View root, final ViewHolderListener3 viewHolderListener3) {
        shopAddressEdittext = root.findViewById(R.id.open_shop_address_edittext);

        generatedLocationOpenShop = root.findViewById(R.id.generated_location_open_shop);
        mapViewContainer = root.findViewById(R.id.mapview_container);
        emptyMapView = root.findViewById(R.id.empty_map_view);

        pinPickupLocation = root.findViewById(R.id.pin_pickup_location);
        this.root = root;
        pinPickupLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewHolderListener3 != null){
                    viewHolderListener3.navigateToGeoLocationActivityRequest(shopAddressEdittext.getText().toString());
                }
            }
        });

        mapView = root.findViewById(R.id.mapview);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

    }

    public void setLocationText(GoogleLocationViewModel googleLocationViewModel){
        this.googleLocationViewModel = googleLocationViewModel;

        emptyMapView.setVisibility(View.GONE);
        mapViewContainer.setVisibility(View.VISIBLE);
        generatedLocationOpenShop.setVisibility(View.VISIBLE);
        generatedLocationOpenShop.setText(getReverseGeocode(googleLocationViewModel));
        generatedLocationOpenShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopAddressEdittext.setText(generatedLocationOpenShop.getText().toString());
            }
        });

        setGoogleMap(googleMap);
    }

    public String getManualAddress(){
        return shopAddressEdittext.getText().toString();
    }

    public GoogleLocationViewModel getGoogleLocationViewModel(){
        return googleLocationViewModel;
    }

    private String getReverseGeocode(GoogleLocationViewModel locationPass) {
        if (locationPass.getGeneratedAddress().equals(root.getContext().getString(R.string.choose_this_location))) {
            return locationPass.getLatitude() + ", " + locationPass.getLongitude();
        } else {
            return locationPass.getGeneratedAddress();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    private void setGoogleMap(GoogleMap googleMap) {
        if (googleMap != null) {

            double latitude = Double.parseDouble(googleLocationViewModel.getLatitude());
            double longitude = Double.parseDouble(googleLocationViewModel.getLongitude());
            LatLng latLng = new LatLng(latitude, longitude);

            googleMap.getUiSettings().setMapToolbarEnabled(false);
            googleMap.addMarker(
                    new MarkerOptions().position(latLng)
                            .icon(BitmapDescriptorFactory.fromResource(com.tokopedia.core.R.drawable.ic_icon_pointer_toped))
            ).setDraggable(false);
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

    public interface ViewHolderListener3{
        void navigateToGeoLocationActivityRequest(String generatedMap);
    }
}
