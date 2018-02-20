package com.tokopedia.seller.shop.open.view.holder;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.seller.R;
import com.tokopedia.seller.shop.open.view.model.GoogleLocationViewModel;
import com.tokopedia.seller.shop.open.view.watcher.AfterTextWatcher;

/**
 * Created by normansyahputa on 12/21/17.
 */

public class LocationMapViewHolder implements OnMapReadyCallback {

    private static final String TAG = "LocationMapViewHolder";
    private final TextView generatedLocationOpenShop;
    private final MapView mapView;
    private final FrameLayout mapViewContainer;
    private final FrameLayout emptyMapView;
    private final TextView generateLocationOpenShopCopy;
    private final Button deletePinPoint;
    private View root;
    private ViewHolderListener viewHolderListener;
    private final AppCompatEditText shopAddressEditText;
    private GoogleMap googleMap;

    private boolean isFromReserveDomain;
    private final TkpdHintTextInputLayout tilShopAddress;

    public void setFromReserveDomain(boolean fromReserveDomain) {
        isFromReserveDomain = fromReserveDomain;
    }

    private GoogleLocationViewModel googleLocationViewModel;

    public LocationMapViewHolder(View root, final ViewHolderListener viewHolderListener) {
        this.root = root;

        tilShopAddress = root.findViewById(R.id.shop_desc_input_layout);
        shopAddressEditText = root.findViewById(R.id.open_shop_address_edittext);
        shopAddressEditText.addTextChangedListener(new AfterTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                tilShopAddress.disableSuccessError();
            }
        });
        // Currently hide it since new Logistic API does not support user to choose express courier
        /*root.findViewById(R.id.map_info_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetView bottomSheetView = new BottomSheetView(LocationMapViewHolder.this.root.getContext());
                bottomSheetView.renderBottomSheet(new BottomSheetView.BottomSheetField
                        .BottomSheetFieldBuilder()
                        .setTitle(LocationMapViewHolder.this.root.getContext().getString(R.string.shipping_location_pinpoint_label))
                        .setBody(LocationMapViewHolder.this.root.getContext().getString(R.string.shipping_location_pinpoint_detail))
                        .build());

                bottomSheetView.show();
            }
        });*/

        generatedLocationOpenShop = root.findViewById(R.id.generated_location_open_shop);
        mapViewContainer = root.findViewById(R.id.mapview_container);
        emptyMapView = root.findViewById(R.id.empty_map_view);
        deletePinPoint = root.findViewById(R.id.delete_pinpoint);
        deletePinPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mapViewContainer.getVisibility() == View.VISIBLE) {
                    shopAddressEditText.setText("");

                    if (googleLocationViewModel != null) {
                        googleLocationViewModel.clearData();
                    }

                    generatedLocationOpenShop.setText("");
                    generatedLocationOpenShop.setVisibility(View.GONE);
                    generateLocationOpenShopCopy.setVisibility(View.GONE);

                    emptyMapView.setVisibility(View.VISIBLE);
                    mapViewContainer.setVisibility(View.GONE);
                    if (viewHolderListener != null) {
                        viewHolderListener.onPinPointDeleted();
                    }
                }
            }
        });

        generateLocationOpenShopCopy = root.findViewById(R.id.generated_location_open_shop_copy);


        this.viewHolderListener = viewHolderListener;
        mapViewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolderListener != null) {
                    viewHolderListener.navigateToGeoLocationActivityRequest(shopAddressEditText.getText().toString());
                }
            }
        });

        root.findViewById(R.id.pin_pickup_location_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolderListener != null) {
                    viewHolderListener.navigateToGeoLocationActivityRequest(shopAddressEditText.getText().toString());
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

    public void onDestroy() {
        mapView.onDestroy();
    }

    public void onCreate(Bundle saveInstanceState) {
        mapView.onCreate(saveInstanceState);
    }

    public void onLowMemory() {
        mapView.onLowMemory();
    }

    public void setLocationText(GoogleLocationViewModel googleLocationViewModel) {
        this.googleLocationViewModel = googleLocationViewModel;

        if (googleLocationViewModel != null && !googleLocationViewModel.isLatLongEmpty()) {
            emptyMapView.setVisibility(View.GONE);
            mapViewContainer.setVisibility(View.VISIBLE);
        }

        if (googleLocationViewModel != null && !TextUtils.isEmpty(googleLocationViewModel.getGeneratedAddress())) {
            if (googleMap != null) {
                googleMap.clear();
            }

            generatedLocationOpenShop.setVisibility(View.VISIBLE);
            generateLocationOpenShopCopy.setVisibility(View.VISIBLE);
            generatedLocationOpenShop.setText(getReverseGeocode(googleLocationViewModel));
            generateLocationOpenShopCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shopAddressEditText.setText(generatedLocationOpenShop.getText().toString());
                    if (viewHolderListener != null) {
                        viewHolderListener.onPinPointSelected();
                    }
                }
            });
            shopAddressEditText.setText(generatedLocationOpenShop.getText().toString());
        } else {
            generatedLocationOpenShop.setVisibility(View.GONE);
            generateLocationOpenShopCopy.setVisibility(View.GONE);
        }

        if (googleLocationViewModel != null && !TextUtils.isEmpty(googleLocationViewModel.getManualAddress())) {
            shopAddressEditText.setText(googleLocationViewModel.getManualAddress());
        }

        if (!isFromReserveDomain)
            setGoogleMap(googleMap);
    }

    public boolean isDataInputValid() {
        if (!TextUtils.isEmpty(generatedLocationOpenShop.getText().toString())) {
            if (TextUtils.isEmpty(shopAddressEditText.getText().toString())) {
                tilShopAddress.setError(root.getContext().getString(R.string.shop_open_shop_location_must_be_filled));
                shopAddressEditText.requestFocus();
                CommonUtils.hideSoftKeyboard(root);
                return false;
            }
        }
        return true;
    }


    public String getManualAddress() {
        return shopAddressEditText.getText().toString();
    }

    public GoogleLocationViewModel getGoogleLocationViewModel() {
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

        if (isFromReserveDomain) {
            setGoogleMap(googleMap);

            isFromReserveDomain = false;
        }
    }

    private void setGoogleMap(GoogleMap googleMap) {
        if (googleMap != null && !googleLocationViewModel.isLatLongEmpty()) {

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
                    if (viewHolderListener != null) {
                        viewHolderListener.navigateToGeoLocationActivityRequest(shopAddressEditText.getText().toString());
                    }
                }
            });
        }
    }

    public interface ViewHolderListener {
        void navigateToGeoLocationActivityRequest(String generatedMap);

        void onPinPointSelected();

        void onPinPointDeleted();
    }
}
