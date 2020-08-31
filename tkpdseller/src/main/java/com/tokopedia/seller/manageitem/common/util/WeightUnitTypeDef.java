package com.tokopedia.seller.manageitem.common.util;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author normansyahputa on 4/25/17.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({WeightUnitTypeDef.TYPE_GRAM, WeightUnitTypeDef.TYPE_KG})
public @interface WeightUnitTypeDef {
    int TYPE_GRAM = 1;
    int TYPE_KG = 2;
}
