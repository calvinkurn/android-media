package com.tokopedia.transaction.orders.orderlist.source;


import com.tokopedia.transaction.orders.orderlist.data.Data;
import com.tokopedia.transaction.orders.orderlist.domain.OrderListRepository;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

public class OrderListRepositoryImpl implements OrderListRepository {

    private OrderListFactory orderFactory;

    public OrderListRepositoryImpl(OrderListFactory orderListFactory){
        orderFactory = orderListFactory;
    }

    @Override
    public Observable<Data> getOrderList(RequestParams parameters) {
        return orderFactory.createCloudAttrDataSource().getOrderList(parameters);
    }
}