package com.tokopedia.seller.product.edit.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.seller.product.edit.constant.CurrencyTypeDef.TYPE_IDR;
import static com.tokopedia.seller.product.edit.constant.CurrencyTypeDef.TYPE_USD;

/**
 * @author normansyahputa on 4/25/17.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({TYPE_IDR, TYPE_USD})
public @interface CurrencyTypeDef {
    int TYPE_IDR = 1;
    int TYPE_USD = 2;
}
