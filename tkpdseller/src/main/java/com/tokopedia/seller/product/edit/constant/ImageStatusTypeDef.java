package com.tokopedia.seller.product.edit.constant;

import android.support.annotation.IntDef;

import static com.tokopedia.seller.product.edit.constant.ImageStatusTypeDef.ALREADY_DELETED;
import static com.tokopedia.seller.product.edit.constant.ImageStatusTypeDef.ALREADY_UPLOADED;
import static com.tokopedia.seller.product.edit.constant.ImageStatusTypeDef.DEFAULT;
import static com.tokopedia.seller.product.edit.constant.ImageStatusTypeDef.WILL_BE_DELETED;
import static com.tokopedia.seller.product.edit.constant.ImageStatusTypeDef.WILL_BE_UPLOADED;

/**
 * @author sebastianuskh on 5/16/17.
 */
@IntDef({DEFAULT, WILL_BE_DELETED, ALREADY_DELETED, WILL_BE_UPLOADED, ALREADY_UPLOADED})
public @interface ImageStatusTypeDef {
    int DEFAULT = 0;
    int WILL_BE_DELETED = 1;
    int ALREADY_DELETED = 2;
    int WILL_BE_UPLOADED = 3;
    int ALREADY_UPLOADED = 4;
}
