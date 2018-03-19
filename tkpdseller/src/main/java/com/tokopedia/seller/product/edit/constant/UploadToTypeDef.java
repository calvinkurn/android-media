package com.tokopedia.seller.product.edit.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.seller.product.edit.constant.UploadToTypeDef.TYPE_ACTIVE;
import static com.tokopedia.seller.product.edit.constant.UploadToTypeDef.TYPE_NOT_ACTIVE;
import static com.tokopedia.seller.product.edit.constant.UploadToTypeDef.TYPE_WAREHOUSE;

/**
 * @author normansyahputa on 4/25/17.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({TYPE_ACTIVE, TYPE_NOT_ACTIVE, TYPE_WAREHOUSE})
public @interface UploadToTypeDef {
    int TYPE_ACTIVE = 1;
    int TYPE_NOT_ACTIVE = 2;
    int TYPE_WAREHOUSE = 3;
}