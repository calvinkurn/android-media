package com.tokopedia.seller.shopsettings.shipping.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.shopsettings.shipping.fragment.EditShippingViewListener;
import com.tokopedia.seller.shopsettings.shipping.fragment.ShippingLocationDialog;
import com.tokopedia.seller.shopsettings.shipping.model.editshipping.ShopShipping;
import com.tokopedia.seller.shopsettings.shipping.presenter.EditShippingPresenter;

/**
 * Created by Kris on 6/9/2016.
 * TOKOPEDIA
 */
public class ShippingHeaderLayout extends EditShippingCustomView<ShopShipping,
        EditShippingPresenter,
        EditShippingViewListener>{

    EditShippingPresenter presenter;

    EditShippingViewListener mainView;

    EditText zipCode;
    TextView editShippingProvinceCitiesDistrict;
    EditText shopProvince;
    EditText shopCity;
    EditText shopDistrict;
    TextInputLayout postalTextInputLayout;

    public ShippingHeaderLayout(Context context) {
        super(context);
    }

    public ShippingHeaderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShippingHeaderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void bindView(View view) {
        zipCode = (EditText) view.findViewById(R.id.postal_code);
        editShippingProvinceCitiesDistrict =
                (TextView) view.findViewById(R.id.title_edit_shipping_change_location);
        shopProvince = (EditText) view.findViewById(R.id.text_edit_shipping_province);
        shopCity = (EditText) view.findViewById(R.id.text_edit_shipping_city);
        shopDistrict = (EditText) view.findViewById(R.id.text_edit_shipping_district);
        postalTextInputLayout = (TextInputLayout) view.findViewById(R.id.text_input_layout_postal_code);
        zipCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                presenter.savePostalCode(s.toString());
            }
        });
        OnClickListener editAddressSpinnerClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                mainView.editAddressSpinner();
            }
        };
        findViewById(R.id.fragment_shipping_header).setOnClickListener(editAddressSpinnerClickListener);
        editShippingProvinceCitiesDistrict.setOnClickListener(editAddressSpinnerClickListener);
        shopProvince.setOnClickListener(editAddressSpinnerClickListener);
        shopCity.setOnClickListener(editAddressSpinnerClickListener);
        shopDistrict.setOnClickListener(editAddressSpinnerClickListener);
    }

    @Override
    protected int getLayoutView() {
        return R.layout.edit_shipping_location_layout;
    }

    @Override
    public void renderData(@NonNull ShopShipping shopData) {
        zipCode.setText(shopData.postalCode);
        setCityAndDistrictVisibility(shopData.districtId == ShippingLocationDialog.JAKARTA_ORIGIN_ID);
    }

    @Override
    public void setListener(EditShippingPresenter presenter) {
        this.presenter = presenter;
        zipCode.addTextChangedListener(postTextWatcher());
    }

    @Override
    public void setViewListener(EditShippingViewListener mainView) {
        this.mainView = mainView;
    }

    public void updateLocationData(String provinceName, String cityName, String districtName){
        shopProvince.setText(provinceName);
        shopCity.setText(cityName);
        shopDistrict.setText(districtName);
        setCityAndDistrictVisibility(cityName.isEmpty() && districtName.isEmpty());
    }

    private void setCityAndDistrictVisibility(boolean shouldNotBeVisible){
        if(shouldNotBeVisible){
            shopCity.setVisibility(View.GONE);
            shopDistrict.setVisibility(View.GONE);
        }
        else{
            shopCity.setVisibility(View.VISIBLE);
            shopDistrict.setVisibility(View.VISIBLE);
        }
    }

    public void setEditShippingLocationButtonTitle(String title) {
        editShippingProvinceCitiesDistrict
                .setText(title);
    }

    public void setZipCodeError(String error){
        postalTextInputLayout.setError(error);
        zipCode.requestFocus();
    }

    public String getZipCodeData(){
        return zipCode.getText().toString();
    }

    private TextWatcher postTextWatcher(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(zipCode.getText().length() < 1) postalTextInputLayout.setError(getContext().getString(R.string.error_field_required));
                else postalTextInputLayout.setError(null);
            }
        };
    }
}
