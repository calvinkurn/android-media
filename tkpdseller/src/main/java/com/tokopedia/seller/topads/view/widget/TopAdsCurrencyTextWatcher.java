package com.tokopedia.seller.topads.view.widget;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.tkpd.library.utils.CurrencyFormatHelper;

/**
 * Created by Nathaniel on 3/3/2017.
 */

public class TopAdsCurrencyTextWatcher implements TextWatcher {

    private EditText editText;
    private String defaultValue;

    public TopAdsCurrencyTextWatcher(EditText editText, String defaultValue) {
        this.editText = editText;
        this.defaultValue = defaultValue;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        CurrencyFormatHelper.SetToRupiah(editText);
        String valueString = CurrencyFormatHelper.RemoveNonNumeric(charSequence.toString());
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
