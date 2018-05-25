package com.tokopedia.transaction.orders.orderdetails.source;


import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.transaction.orders.orderlist.source.CloudOrderListDataSource;
import com.tokopedia.transaction.orders.orderlist.source.api.OrderListDataApi;

import javax.inject.Inject;

/**
 * Created by baghira on 19/03/18.
 */

public class OrderDetailsFactory {
    private OrderDetailsDataApi orderDetailsApi;
    Context context;

    @Inject
    public OrderDetailsFactory(OrderDetailsDataApi service, @ApplicationContext Context context){
        this.orderDetailsApi = service;
        this.context = context;
    }

    public CloudOrderDetailsDataSource createCloudAttrDataSource(){
        return new CloudOrderDetailsDataSource(orderDetailsApi, context);
    }

}
