package com.tokopedia.transaction.orders.orderlist.domain;

import com.tokopedia.transaction.orders.orderlist.data.Data;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

public interface OrderListRepository {

    Observable<Data> getOrderList(RequestParams parameters);

}