package com.tokopedia.seller.shopsettings.shipping.customview;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.core.geolocation.activity.GeolocationActivity;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.core.geolocation.utils.GeoLocationUtils;
import com.tokopedia.seller.R;
import com.tokopedia.seller.shopsettings.shipping.fragment.EditShippingViewListener;
import com.tokopedia.seller.shopsettings.shipping.model.editshipping.ShopShipping;
import com.tokopedia.seller.shopsettings.shipping.presenter.EditShippingPresenter;

/**
 * Created by Kris on 6/9/2016.
 * TOKOPEDIA
 */
public class ShippingAddressLayout extends EditShippingCustomView<ShopShipping,
        EditShippingPresenter,
        EditShippingViewListener>{

    EditText addressArea;
    EditText chooseLocation;
    TextView phoneNumber;
    TextView phoneNumberTitle;
    TextView phoneNumberButton;

    private EditShippingPresenter presenter;
    private EditShippingViewListener mainView;

    public ShippingAddressLayout(Context context) {
        super(context);
    }

    public ShippingAddressLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShippingAddressLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void bindView(View view) {
        addressArea = (EditText) view.findViewById(R.id.address_text_field);

        chooseLocation = (EditText) view.findViewById(R.id.value_location);

        phoneNumber = (TextView) view.findViewById(R.id.shop_phone_number);
        phoneNumberTitle = (TextView) view.findViewById(R.id.shop_phone_number_title);

        phoneNumberButton = (TextView) view.findViewById(R.id.change_phone_number_button);

        chooseLocation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mainView.openGeoLocation();
            }
        });
        addressArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                presenter.saveAddressArea(s.toString());
            }
        });

    }

    @Override
    protected int getLayoutView() {
        return R.layout.edit_shipping_address_layout;
    }

    @Override
    public void renderData(@NonNull ShopShipping data) {
        addressArea.setText(data.addrStreet);
        setGoogleMapAddress(data.getShopLongitude(), data.getShopLatitude());
    }

    @Override
    public void setListener(EditShippingPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setViewListener(EditShippingViewListener mainView) {
        this.mainView = mainView;
    }

    private void setGoogleMapAddress(String longitude, String latitude) {
        try {
            chooseLocation.setText(GeoLocationUtils.reverseGeoCode(getContext(), Double.parseDouble(latitude), Double.parseDouble(longitude)));
        } catch (NumberFormatException e){
            chooseLocation.setText("");
            e.printStackTrace();
        } catch (Exception e) {
            chooseLocation.setText(getContext().getString(R.string.shop_coordinate)
                    .replace("LAT", latitude)
                    .replace("LONG", longitude));
            e.printStackTrace();
        }
    }

    public void setGoogleMapData(Intent data){
        LocationPass locationPass = data.getParcelableExtra(GeolocationActivity.EXTRA_EXISTING_LOCATION);
        if(locationPass != null && locationPass.getLatitude() != null) {
            presenter.getShopInformation().setShopLatitude(locationPass.getLatitude());
            presenter.getShopInformation().setShopLongitude(locationPass.getLongitude());
            chooseLocation.setText(getReverseGeocode(locationPass));
        }
    }

    private String getReverseGeocode(LocationPass locationPass) {
        if (locationPass.getGeneratedAddress().equals(getContext().getString(R.string.choose_this_location))) {
            return locationPass.getLatitude() + ", " + locationPass.getLongitude();
        } else {
            return locationPass.getGeneratedAddress();
        }
    }

    public String getGoogleMapAddressString(){
        return chooseLocation.getText().toString();
    }

    public String getAddressData(){
        return addressArea.getText().toString();
    }
}
