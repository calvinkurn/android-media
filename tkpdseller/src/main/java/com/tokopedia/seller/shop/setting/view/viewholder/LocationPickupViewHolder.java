package com.tokopedia.seller.shop.setting.view.viewholder;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.core.geolocation.model.LocationPass;
import com.tokopedia.seller.R;
import com.tokopedia.seller.shop.setting.view.fragment.ShopSettingLocationFragment;

/**
 * Created by sebastianuskh on 3/23/17.
 */

public class LocationPickupViewHolder {

    private LocationPass locationPass;
    private TextView locationPickupTextView;
    private EditText zipCodeEditText;

    public LocationPickupViewHolder(View view, final LocationPickupViewHolderListener listener) {
        view
                .findViewById(R.id.action_shop_setting_location_pickup_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(locationPass == null){
                            locationPass = generateEmptyLocationPass();
                        }
                        listener.goToPickupLocationPicker(locationPass);
                    }
                });
        locationPickupTextView = (TextView) view.findViewById(R.id.text_view_shop_setting_location_pickup);
        zipCodeEditText = (EditText) view.findViewById(R.id.edit_text_shop_setting_location_zip_code);
    }

    private LocationPass generateEmptyLocationPass() {
        LocationPass newLocationPass = new LocationPass();
        newLocationPass.setGeneratedAddress("");
        newLocationPass.setLatitude("0");
        newLocationPass.setLongitude("0");
        newLocationPass.setManualAddress("");
        return newLocationPass;
    }

    public void changePickupLocation(LocationPass locationPass) {
        this.locationPass = locationPass;
        locationPickupTextView.setText(locationPass.getGeneratedAddress());
    }
}
