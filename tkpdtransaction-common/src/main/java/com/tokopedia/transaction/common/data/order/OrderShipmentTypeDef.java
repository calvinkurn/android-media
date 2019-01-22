package com.tokopedia.transaction.common.data.order;

import android.support.annotation.IntDef;

import static com.tokopedia.transaction.common.data.order.OrderShipmentTypeDef.ACCEPT_FULL;
import static com.tokopedia.transaction.common.data.order.OrderShipmentTypeDef.ACCEPT_PARTIAL;
import static com.tokopedia.transaction.common.data.order.OrderShipmentTypeDef.ORDER_DELIVERED;
import static com.tokopedia.transaction.common.data.order.OrderShipmentTypeDef.ORDER_WAITING;

/**
 * Created by kris on 2/2/18. Tokopedia
 */

@IntDef({ORDER_WAITING, ORDER_DELIVERED, ACCEPT_FULL, ACCEPT_PARTIAL})
public @interface OrderShipmentTypeDef {
    int ORDER_WAITING = 500;
    int ORDER_DELIVERED = 600;
    int ORDER_DELIVERED_DUE_LIMIT = 699;
    int ACCEPT_FULL = 400;
    int ACCEPT_PARTIAL = 401;
}
