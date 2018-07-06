package com.tokopedia.transaction.orders.orderlist.source;


import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.transaction.orders.orderlist.source.api.OrderListDataApi;

import javax.inject.Inject;

/**
 * Created by baghira on 19/03/18.
 */

public class OrderListFactory {
    private final Context context;
    private OrderListDataApi orderListApi;

    @Inject
    public OrderListFactory(OrderListDataApi service, Context context){
        this.orderListApi = service;
        this.context = context;
    }

    public CloudOrderListDataSource createCloudAttrDataSource(){
        return new CloudOrderListDataSource(orderListApi,context);
    }

}
