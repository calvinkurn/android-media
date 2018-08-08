package com.tokopedia.seller.shopsettings.shipping.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.tokopedia.seller.R;
import com.tokopedia.seller.shopsettings.shipping.fragment.EditShippingViewListener;
import com.tokopedia.seller.shopsettings.shipping.model.editshipping.ShopShipping;
import com.tokopedia.seller.shopsettings.shipping.presenter.EditShippingPresenter;

import java.util.ArrayList;

/**
 * Created by Kris on 6/9/2016.
 * TOKOPEDIA
 */
public class ShippingHeaderLayout extends EditShippingCustomView<ShopShipping,
        EditShippingPresenter,
        EditShippingViewListener> {

    private static final int POSTAL_CODE_LENGTH = 5;
    EditShippingPresenter presenter;
    EditShippingViewListener mainView;
    AutoCompleteTextView zipCode;
    EditText shopCity;
    TextInputLayout postalTextInputLayout;
    ArrayAdapter<String> zipCodeAdapter;

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
        zipCode = (AutoCompleteTextView) view.findViewById(R.id.postal_code);
        shopCity = (EditText) view.findViewById(R.id.text_edit_shipping_city);
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
                mainView.editAddress();
            }
        };
        findViewById(R.id.fragment_shipping_header).setOnClickListener(editAddressSpinnerClickListener);
        shopCity.setOnClickListener(editAddressSpinnerClickListener);

        zipCode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!zipCode.isPopupShowing()) {
                    zipCode.showDropDown();
                }
                return false;
            }
        });

        zipCode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0 && !Character.isDigit(zipCode.getText().toString().charAt(0))) {
                    zipCode.setText("");
                }
            }
        });
    }

    public void initializeZipCodes() {
        zipCode.setText("");
        String header = getResources().getString(R.string.hint_type_postal_code);
        ArrayList<String> zipCodes = presenter.getselectedAddress().getZipCodes();
        if (!zipCodes.contains(header)) {
            zipCodes.add(0, header);
        }
        zipCodeAdapter = new ArrayAdapter<>(getContext(), R.layout.item_autocomplete_text_double_row,
                R.id.item, presenter.getselectedAddress().getZipCodes());
        zipCode.setAdapter(zipCodeAdapter);
    }

    @Override
    protected int getLayoutView() {
        return R.layout.edit_shipping_location_layout;
    }

    @Override
    public void renderData(@NonNull ShopShipping shopData) {
        zipCode.setText(shopData.postalCode);
        if (!TextUtils.isEmpty(shopData.districtName) && !TextUtils.isEmpty(shopData.cityName) &&
                !TextUtils.isEmpty(shopData.provinceName)) {
            updateLocationData(shopData.provinceName, shopData.cityName, shopData.districtName);
        }
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

    public void updateLocationData(String provinceName, String cityName, String districtName) {
        shopCity.setText(provinceName + ", " + cityName + ", " + districtName);
    }

    public void updateLocationData(String location) {
        shopCity.setText(location);
    }

    public void setZipCodeError(String error) {
        postalTextInputLayout.setError(error);
        zipCode.requestFocus();
    }

    public String getZipCodeData() {
        return zipCode.getText().toString();
    }

    public String getDistrictAndCity() {
        return shopCity.getText().toString();
    }

    private TextWatcher postTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (zipCode.getText().length() < 1)
                    postalTextInputLayout.setError(getContext().getString(R.string.error_field_required));
                else postalTextInputLayout.setError(null);
            }
        };
    }
}
