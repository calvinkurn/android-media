package com.tokopedia.seller.product.edit.constant;

import android.support.annotation.IntDef;

import static com.tokopedia.seller.product.edit.constant.WeightUnitTypeDef.TYPE_GRAM;
import static com.tokopedia.seller.product.edit.constant.WeightUnitTypeDef.TYPE_KG;

/**
 * @author normansyahputa on 4/25/17.
 */
@IntDef({TYPE_GRAM, TYPE_KG})
public @interface WeightUnitTypeDef {
    int TYPE_GRAM = 1;
    int TYPE_KG = 2;
}
