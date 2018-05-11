package com.tokopedia.transaction.orders.orderdetails.di;

/**
 * Created by baghira on 10/05/18.
 */

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.transaction.orders.orderdetails.view.OrderListDetailFragment;
import com.tokopedia.transaction.orders.orderlist.di.OrderListComponent;
import com.tokopedia.transaction.orders.orderlist.di.OrderListModule;
import com.tokopedia.transaction.orders.orderlist.di.OrderListModuleScope;

import dagger.Component;


@OrderListModuleScope
@Component(modules = {OrderDetailsModule.class}, dependencies = {BaseAppComponent.class})
public interface  OrderDetailsComponent {
    void inject(OrderListDetailFragment orderListDetailFragment);
}