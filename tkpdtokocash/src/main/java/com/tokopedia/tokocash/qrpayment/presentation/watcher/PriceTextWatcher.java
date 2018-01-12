package com.tokopedia.tokocash.qrpayment.presentation.watcher;

import android.widget.EditText;

import com.tokopedia.design.text.watcher.NumberTextWatcher;
import com.tokopedia.design.utils.CurrencyFormatHelper;

/**
 * Created by nabillasabbaha on 1/12/18.
 */

public class PriceTextWatcher extends NumberTextWatcher {

    public PriceTextWatcher(EditText editText) {
        super(editText);
    }

    @Override
    protected void applyFormatter() {
        CurrencyFormatHelper.SetToDollar(editText);
    }
}
