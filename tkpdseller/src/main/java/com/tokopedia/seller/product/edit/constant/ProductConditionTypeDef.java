package com.tokopedia.seller.product.edit.constant;

import android.support.annotation.IntDef;

import static com.tokopedia.seller.product.edit.constant.ProductConditionTypeDef.TYPE_NEW;
import static com.tokopedia.seller.product.edit.constant.ProductConditionTypeDef.TYPE_RECON;

/**
 * Created by User on 8/11/2017.
 */

@IntDef({TYPE_NEW, TYPE_RECON})
public @interface ProductConditionTypeDef {
    int TYPE_NEW = 1;
    int TYPE_RECON = 2;
}

