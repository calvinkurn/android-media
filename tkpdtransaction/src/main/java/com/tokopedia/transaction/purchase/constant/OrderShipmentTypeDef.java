package com.tokopedia.transaction.purchase.constant;

import android.support.annotation.IntDef;

import static com.tokopedia.transaction.purchase.constant.OrderShipmentTypeDef.ORDER_DELIVERED;
import static com.tokopedia.transaction.purchase.constant.OrderShipmentTypeDef.ORDER_WAITING;

/**
 * Created by kris on 2/2/18. Tokopedia
 */

@IntDef({ORDER_WAITING, ORDER_DELIVERED})
public @interface OrderShipmentTypeDef {
    int ORDER_WAITING = 500;
    int ORDER_DELIVERED = 600;
}
