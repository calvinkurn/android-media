package com.tokopedia.seller.product.edit.view.model.upload.intdef;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author sebastianuskh on 4/25/17.
 */

@Retention(RetentionPolicy.SOURCE)
@IntDef({ProductStatus.ADD, ProductStatus.EDIT})
public @interface ProductStatus {
    int ADD = 0;
    int EDIT = 1;
}
