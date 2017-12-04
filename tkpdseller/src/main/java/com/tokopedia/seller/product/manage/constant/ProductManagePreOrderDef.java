package com.tokopedia.seller.product.manage.constant;

import android.support.annotation.IntDef;

import static com.tokopedia.seller.product.manage.constant.ProductManagePreOrderDef.NOTE_PRE_ORDER;
import static com.tokopedia.seller.product.manage.constant.ProductManagePreOrderDef.PRE_ORDER;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

@IntDef({NOTE_PRE_ORDER, PRE_ORDER})
public @interface ProductManagePreOrderDef {
    int NOTE_PRE_ORDER = 0;
    int PRE_ORDER = 1;
}
