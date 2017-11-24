package com.tokopedia.seller.shop.setting.view.viewholder;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.seller.R;
import com.tokopedia.seller.shop.setting.view.listener.ShopSettingLocationPickupViewHolderListener;

/**
 * Created by sebastianuskh on 3/23/17.
 */

public class ShopSettingLocationPickupViewHolder {

    private final Context context;
    private final TextInputLayout postalCodeInputLayout;
    private final TextView locationPickupTextView;
    private final EditText postalCode;
    private LocationPass locationPass;

    public ShopSettingLocationPickupViewHolder(Context context, View view, final ShopSettingLocationPickupViewHolderListener listener) {
        this.context = context;
        view.findViewById(R.id.action_shop_setting_location_pickup_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.goToPickupLocationPicker(locationPass);
            }
        });
        locationPass = generateEmptyLocationPass();
        locationPickupTextView = (TextView) view.findViewById(R.id.text_view_shop_setting_location_pickup);
        postalCode = (EditText) view.findViewById(R.id.edit_text_shop_setting_location_postal_code);
        postalCodeInputLayout = (TextInputLayout) view.findViewById(R.id.input_layout_shop_setting_location_zip_code);
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

    public int getPostalCode() throws RuntimeException {
        try {
            String postalCode = this.postalCode.getText().toString();
            if (postalCode.isEmpty())
                throw new RuntimeException("Kode pos kosong");
            return Integer.parseInt(postalCode);
        } catch (Exception e) {
            postalCodeInputLayout.setError(context.getString(R.string.postal_code_not_filled));
            throw new RuntimeException("Kode pos kosong");
        }
    }
}
