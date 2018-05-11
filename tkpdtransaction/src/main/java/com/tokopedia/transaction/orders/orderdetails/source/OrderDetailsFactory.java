package com.tokopedia.transaction.orders.orderdetails.source;


import com.google.gson.Gson;
import com.tokopedia.transaction.orders.orderlist.source.CloudOrderListDataSource;
import com.tokopedia.transaction.orders.orderlist.source.api.OrderListDataApi;

import javax.inject.Inject;

/**
 * Created by baghira on 19/03/18.
 */

public class OrderDetailsFactory {
    private final Gson gson;
    private OrderDetailsDataApi orderDetailsApi;

    @Inject
    public OrderDetailsFactory(OrderDetailsDataApi service, Gson gson){
        this.orderDetailsApi = service;
        this.gson = gson;
    }

    public CloudOrderDetailsDataSource createCloudAttrDataSource(){
        return new CloudOrderDetailsDataSource(orderDetailsApi,gson);
    }

}
