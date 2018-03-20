package com.tokopedia.seller.product.edit.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.seller.product.edit.constant.ProductInsuranceValueTypeDef.TYPE_OPTIONAL;
import static com.tokopedia.seller.product.edit.constant.ProductInsuranceValueTypeDef.TYPE_YES;

/**
 * Created by User on 8/11/2017.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({TYPE_OPTIONAL, TYPE_YES})
public @interface ProductInsuranceValueTypeDef {
    int TYPE_OPTIONAL = 0;
    int TYPE_YES = 1;
}
