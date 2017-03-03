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

    private static final String DEFAULT_VALUE = "0";

    private EditText editText;

    public TopAdsCurrencyTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        CurrencyFormatHelper.SetToRupiah(editText);
        String valueString = CurrencyFormatHelper.RemoveNonNumeric(charSequence.toString());
        if (TextUtils.isEmpty(valueString)) {
            editText.setText(DEFAULT_VALUE);
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
