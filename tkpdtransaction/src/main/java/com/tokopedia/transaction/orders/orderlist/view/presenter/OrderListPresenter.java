package com.tokopedia.transaction.orders.orderlist.view.presenter;

import android.content.Context;

import com.tokopedia.transaction.orders.orderlist.data.OrderCategory;

public interface OrderListPresenter {

    void getAllOrderData(Context context, OrderCategory orderCategory, int typeRequest, int page);
}
