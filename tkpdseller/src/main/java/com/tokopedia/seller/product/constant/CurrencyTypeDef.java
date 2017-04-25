package com.tokopedia.seller.product.constant;

import android.support.annotation.IntDef;

import static com.tokopedia.seller.product.constant.CurrencyTypeDef.TYPE_IDR;
import static com.tokopedia.seller.product.constant.CurrencyTypeDef.TYPE_USD;

/**
 * @author normansyahputa on 4/25/17.
 */
@IntDef({TYPE_IDR, TYPE_USD})
public @interface CurrencyTypeDef {
    int TYPE_IDR = 0;
    int TYPE_USD = 1;
}
