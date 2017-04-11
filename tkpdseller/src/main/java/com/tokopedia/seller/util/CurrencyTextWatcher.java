package com.tokopedia.seller.util;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.tkpd.library.utils.CurrencyFormatHelper;

/**
 * Created by Nathaniel on 3/3/2017.
 */

public class CurrencyTextWatcher implements TextWatcher {

    private EditText editText;
    private String defaultValue;

    public CurrencyTextWatcher(EditText editText, String defaultValue) {
        this.editText = editText;
        this.defaultValue = defaultValue;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        CurrencyFormatHelper.setToRupiahCheckPrefix(editText);
        String valueString = CurrencyFormatHelper.removeCurrencyPrefix(charSequence.toString());
        valueString = CurrencyFormatHelper.RemoveNonNumeric (valueString);
        if (TextUtils.isEmpty(valueString)) {
            editText.setText(defaultValue);
            return;
        }
        float value = Float.parseFloat(valueString);
        onCurrencyChanged(value);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void onCurrencyChanged(float currencyValue) {

    }
}
