package com.tokopedia.seller.product.manage.constant;

import android.support.annotation.IntDef;

import static com.tokopedia.seller.product.manage.constant.ProductManageWholesaleDef.NOT_WHOLESALE;
import static com.tokopedia.seller.product.manage.constant.ProductManageWholesaleDef.WHOLESALE;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

@IntDef({NOT_WHOLESALE, WHOLESALE})
public @interface ProductManageWholesaleDef {
    int NOT_WHOLESALE = 0;
    int WHOLESALE = 1;
}
