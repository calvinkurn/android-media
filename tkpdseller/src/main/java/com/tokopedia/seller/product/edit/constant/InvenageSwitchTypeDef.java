package com.tokopedia.seller.product.edit.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.seller.product.edit.constant.InvenageSwitchTypeDef.TYPE_ACTIVE;
import static com.tokopedia.seller.product.edit.constant.InvenageSwitchTypeDef.TYPE_NOT_ACTIVE;

/**
 * @author normansyahputa on 4/25/17.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({TYPE_ACTIVE, TYPE_NOT_ACTIVE})
public @interface InvenageSwitchTypeDef {
    int TYPE_ACTIVE = 1;
    int TYPE_NOT_ACTIVE = 0;
}