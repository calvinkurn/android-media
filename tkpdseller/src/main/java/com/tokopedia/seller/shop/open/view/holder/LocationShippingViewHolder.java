package com.tokopedia.seller.shop.open.view.holder;

import android.text.Editable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.seller.R;
import com.tokopedia.seller.lib.widget.TkpdHintTextInputLayout;
import com.tokopedia.seller.shop.open.view.watcher.AfterTextWatcher;

import java.util.ArrayList;

/**
 * Created by normansyahputa on 12/21/17.
 */

public class LocationShippingViewHolder {
    private final TkpdHintTextInputLayout postalCodeTextInputLayout;
    private final AutoCompleteTextView textEditPostalCode;
    private View root;
    ViewHolderListener2 viewHolderListener2;
    TkpdHintTextInputLayout cityTextInputLayout;
    EditText textEditShippingCity;
    String hintTypePostalCode;
    private String districtName;
    private String districtId;

    public LocationShippingViewHolder(View root, final ViewHolderListener2 viewHolderListener2) {
        this.root = root;
        this.viewHolderListener2 = viewHolderListener2;
        cityTextInputLayout = root.findViewById(R.id.seller_city_text_input_layout);
        textEditShippingCity = root.findViewById(R.id.seller_text_edit_shipping_city);
        postalCodeTextInputLayout = root.findViewById(R.id.seller_text_input_layout_postal_code);
        textEditPostalCode = root.findViewById(R.id.seller_postal_code);

        textEditShippingCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolderListener2 != null) {
                    viewHolderListener2.navigateToEditAddressActivityRequest();
                }
            }
        });
        textEditShippingCity.addTextChangedListener(new AfterTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                cityTextInputLayout.disableSuccessError();
            }
        });
        textEditPostalCode.addTextChangedListener(new AfterTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                postalCodeTextInputLayout.disableSuccessError();
            }
        });

        hintTypePostalCode = root.getResources().getString(R.string.hint_type_postal_code);

        textEditPostalCode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!textEditPostalCode.isPopupShowing()) {
                    textEditPostalCode.showDropDown();
                }
                return false;
            }
        });

        textEditPostalCode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0 && !Character.isDigit(textEditPostalCode.getText().toString().charAt(0))) {
                    textEditPostalCode.setText("");
                }
            }
        });
    }

    private void setErrorEmptyCity() {
        cityTextInputLayout.setError(root.getContext().getString(R.string.shop_open_error_city_not_filled));
        cityTextInputLayout.requestFocus();
        CommonUtils.hideSoftKeyboard(root);
    }

    private void setErrorEmptyPostalCode() {
        postalCodeTextInputLayout.setError(root.getContext().getString(R.string.shop_open_postal_code_must_be_filled));
        postalCodeTextInputLayout.requestFocus();
        CommonUtils.hideSoftKeyboard(root);
    }

    private void setErrorLengthPostalCode() {
        postalCodeTextInputLayout.setError(root.getContext().getString(R.string.shop_open_postal_code_minimum_5_char));
        postalCodeTextInputLayout.requestFocus();
        CommonUtils.hideSoftKeyboard(root);
    }

    public boolean isDataInputValid(){
        if (TextUtils.isEmpty(getDistrictName())) {
            setErrorEmptyCity();
            return false;
        }
        if (TextUtils.isEmpty(getPostalCode())) {
            setErrorEmptyPostalCode();
            return false;
        } else if (getPostalCode().length()< 5) {
            setErrorLengthPostalCode();
            return false;
        }
        return true;
    }

    public String getLocationComplete() {
        return textEditShippingCity.getText().toString();
    }

    public String getPostalCode() {
        return textEditPostalCode.getText().toString();
    }

    public String getDistrictName() {
        return districtName;
    }

    public void updateLocationData(String completeLocation, String districtName) {
        this.districtName = districtName;
        textEditShippingCity.setText(completeLocation);
    }

    public void updateLocationData(String provinceName, String cityName, String districtName) {
        this.districtName = districtName;
        textEditShippingCity.setText(provinceName + ", " + cityName + ", " + districtName);
    }

    public void updateDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void initializeZipCodes(ArrayList<String> zipCodes) {
        textEditPostalCode.setText("");
        zipCodes.add(0, hintTypePostalCode);
        ArrayAdapter<String> zipCodeAdapter = new ArrayAdapter<>(root.getContext(), R.layout.item_autocomplete_text_double_row,
                R.id.item, zipCodes);
        textEditPostalCode.setAdapter(zipCodeAdapter);
    }

    public void updateZipCodes(String zipCode) {
        textEditPostalCode.setText(zipCode);
    }

    public interface ViewHolderListener2 {
        void navigateToEditAddressActivityRequest();
    }
}
