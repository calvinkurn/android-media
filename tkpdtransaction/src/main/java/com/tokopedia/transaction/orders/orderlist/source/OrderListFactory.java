package com.tokopedia.transaction.orders.orderlist.source;


import com.google.gson.Gson;
import com.tokopedia.transaction.orders.orderlist.source.api.OrderListDataApi;

import javax.inject.Inject;

/**
 * Created by baghira on 19/03/18.
 */

public class OrderListFactory {
    private final Gson gson;
    private OrderListDataApi orderListApi;

    @Inject
    public OrderListFactory(OrderListDataApi service, Gson gson){
        this.orderListApi = service;
        this.gson = gson;
    }

    public CloudOrderListDataSource createCloudAttrDataSource(){
        return new CloudOrderListDataSource(orderListApi,gson);
    }

}
