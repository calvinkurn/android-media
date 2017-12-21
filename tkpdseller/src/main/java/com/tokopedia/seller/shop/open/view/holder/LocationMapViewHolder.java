package com.tokopedia.seller.shop.open.view.holder;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.shop.open.view.model.GoogleLocationViewModel;

/**
 * Created by normansyahputa on 12/21/17.
 */

public class LocationMapViewHolder {

    private final TextView pinPickupLocation;
    private final TextView generatedLocationOpenShop;
    private View root;
    private final EditText shopAddressEdittext;
    private GoogleLocationViewModel googleLocationViewModel;

    public LocationMapViewHolder(View root, final ViewHolderListener3 viewHolderListener3) {
        shopAddressEdittext = root.findViewById(R.id.open_shop_address_edittext);

        generatedLocationOpenShop = root.findViewById(R.id.generated_location_open_shop);

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

    }

    public void setLocationText(GoogleLocationViewModel googleLocationViewModel){
        generatedLocationOpenShop.setText(getReverseGeocode(googleLocationViewModel));
    }

    private String getReverseGeocode(GoogleLocationViewModel locationPass) {
        if (locationPass.getGeneratedAddress().equals(root.getContext().getString(R.string.choose_this_location))) {
            return locationPass.getLatitude() + ", " + locationPass.getLongitude();
        } else {
            return locationPass.getGeneratedAddress();
        }
    }

    public interface ViewHolderListener3{
        void navigateToGeoLocationActivityRequest(String generatedMap);
    }
}
