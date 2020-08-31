package com.tokopedia.seller.manageitem.common.util;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * @author normansyahputa on 4/25/17.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({CurrencyTypeDef.TYPE_IDR, CurrencyTypeDef.TYPE_USD})
public @interface CurrencyTypeDef {
    int TYPE_IDR = 1;
    int TYPE_USD = 2;
}
