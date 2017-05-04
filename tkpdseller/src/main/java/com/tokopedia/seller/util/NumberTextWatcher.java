package com.tokopedia.seller.util;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.tkpd.library.utils.CurrencyFormatHelper;

/**
 * Created by Nathaniel on 3/3/2017.
 */

public class NumberTextWatcher implements TextWatcher {

    private static final String DEFAULT_VALUE = "0";

    protected EditText editText;

    private String defaultValue;

    public NumberTextWatcher(EditText editText) {
        this.editText = editText;
        this.defaultValue = DEFAULT_VALUE;
    }

    public NumberTextWatcher(EditText editText, String defaultValue) {
        this.editText = editText;
        this.defaultValue = defaultValue;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

    }

    protected void applyFormatter() {
        CurrencyFormatHelper.setToRupiahCheckPrefix(editText);
    }

    @Override
    public void afterTextChanged(Editable s) {
        applyFormatter();
        String valueString = CurrencyFormatHelper.removeCurrencyPrefix(s.toString());
        valueString = CurrencyFormatHelper.RemoveNonNumeric (valueString);
        if (TextUtils.isEmpty(valueString)) {
            editText.setText(defaultValue);
            editText.setSelection(defaultValue.length());
            return;
        }
        float value = Float.parseFloat(valueString);
        onNumberChanged(value);
    }

    public void onNumberChanged(float number) {

    }
}
