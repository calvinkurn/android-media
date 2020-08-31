package com.tokopedia.seller.manageitem.common.util;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by User on 8/11/2017.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({ProductConditionTypeDef.TYPE_NEW, ProductConditionTypeDef.TYPE_RECON})
public @interface ProductConditionTypeDef {
    int TYPE_NEW = 1;
    int TYPE_RECON = 2;
}

