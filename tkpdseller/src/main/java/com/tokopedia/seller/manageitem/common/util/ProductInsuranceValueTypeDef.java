package com.tokopedia.seller.manageitem.common.util;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by User on 8/11/2017.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({ProductInsuranceValueTypeDef.TYPE_OPTIONAL, ProductInsuranceValueTypeDef.TYPE_YES})
public @interface ProductInsuranceValueTypeDef {
    int TYPE_OPTIONAL = 0;
    int TYPE_YES = 1;
}
