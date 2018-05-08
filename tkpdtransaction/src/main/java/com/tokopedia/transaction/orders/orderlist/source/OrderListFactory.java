package com.tokopedia.transaction.orders.orderlist.source;


import com.tokopedia.transaction.orders.orderlist.source.api.OrderListDataApi;

/**
 * Created by baghira on 19/03/18.
 */

public class OrderListFactory {
    private OrderListDataApi orderListApi;

    public OrderListFactory(OrderListDataApi service){
        this.orderListApi = service;
    }

    public CloudOrderListDataSource createCloudAttrDataSource(){
        return new CloudOrderListDataSource(orderListApi);
    }

}
