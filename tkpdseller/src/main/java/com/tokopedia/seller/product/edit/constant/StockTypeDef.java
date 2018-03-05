package com.tokopedia.seller.product.edit.constant;

import android.support.annotation.IntDef;

import static com.tokopedia.seller.product.edit.constant.StockTypeDef.TYPE_ACTIVE;
import static com.tokopedia.seller.product.edit.constant.StockTypeDef.TYPE_ACTIVE_LIMITED;
import static com.tokopedia.seller.product.edit.constant.StockTypeDef.TYPE_WAREHOUSE;

/**
 * @author normansyahputa on 4/25/17.
 */
@IntDef({TYPE_ACTIVE, TYPE_ACTIVE_LIMITED, TYPE_WAREHOUSE})
public @interface StockTypeDef {
    int TYPE_ACTIVE = 1; // from api
    int TYPE_ACTIVE_LIMITED = 2; // only for view
    int TYPE_WAREHOUSE = 3; // from api
}