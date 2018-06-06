package com.tokopedia.transaction.orders.orderdetails.data;

import com.tokopedia.transaction.orders.orderlist.data.OrderCategory;

/**
 * Created by baghira on 10/05/18.
 */

public class Variable {
    OrderCategory orderCategory;
    String orderId;

    public Variable(OrderCategory orderCategory, String orderId) {
        this.orderCategory = orderCategory;
        this.orderId = orderId;
    }
}
