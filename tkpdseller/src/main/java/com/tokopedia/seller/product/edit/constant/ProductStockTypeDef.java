package com.tokopedia.seller.product.edit.constant;

import android.support.annotation.IntDef;

import static com.tokopedia.seller.product.edit.constant.ProductStockTypeDef.TYPE_ACTIVE;
import static com.tokopedia.seller.product.edit.constant.ProductStockTypeDef.TYPE_NOT_ACTIVE;

/**
 * @author normansyahputa on 4/25/17.
 */
@IntDef({TYPE_ACTIVE, TYPE_NOT_ACTIVE})
public @interface ProductStockTypeDef {
    int TYPE_ACTIVE = 1;
    int TYPE_NOT_ACTIVE = 0;
}