package com.tokopedia.transaction.orders.orderdetails.source;


import com.tokopedia.transaction.orders.orderdetails.data.DetailsData;
import com.tokopedia.transaction.orders.orderdetails.domain.OrderDetailsRepository;
import com.tokopedia.transaction.orders.orderlist.data.Data;
import com.tokopedia.transaction.orders.orderlist.domain.OrderListRepository;
import com.tokopedia.transaction.orders.orderlist.source.OrderListFactory;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

public class OrderDetailsRepositoryImpl implements OrderDetailsRepository {

    private OrderDetailsFactory orderFactory;

    public OrderDetailsRepositoryImpl(OrderDetailsFactory orderDetailsFactory){
        orderFactory = orderDetailsFactory;
    }

    @Override
    public Observable<DetailsData> getOrderDetails(RequestParams parameters) {
        return orderFactory.createCloudAttrDataSource().getOrderDetails(parameters);
    }
}