package com.tokopedia.topads.keyword.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.widget.EditText;

import com.tokopedia.design.utils.CurrencyFormatHelper;
import com.tokopedia.seller.util.CurrencyIdrTextWatcher;

/**
 * Created by normansyahputa on 10/12/17.
 */

public class EmptyCurrencyIdrTextWatcher extends CurrencyIdrTextWatcher {

    public static final String DEFAULT_VALUE = "";
    private boolean isAllowEmpty = true;
    private double avoidMessageErrorValue;

    public EmptyCurrencyIdrTextWatcher(EditText editText) {
        this(editText, DEFAULT_VALUE);
    }

    public EmptyCurrencyIdrTextWatcher(EditText editText, String defaultValue) {
        super(editText, DEFAULT_VALUE);
    }

    public double getAvoidMessageErrorValue() {
        return avoidMessageErrorValue;
    }

    public void setAvoidMessageErrorValue(double avoidMessageErrorValue) {
        this.avoidMessageErrorValue = avoidMessageErrorValue;
    }

    @Override
    public void afterTextChanged(Editable s) {
        if(isAllowEmpty){
            isAllowEmpty = false;
            return;
        }
        String valueString = CurrencyFormatHelper.removeCurrencyPrefix(s.toString());
        valueString = CurrencyFormatHelper.RemoveNonNumeric(valueString);
        if(valueString.isEmpty()){
            isAllowEmpty = true;
            this.editText.setText(DEFAULT_VALUE);
            this.onNumberChanged(avoidMessageErrorValue);
            return;
        }
        super.afterTextChanged(s);
    }
}
