package com.tokopedia.transaction.orders.orderlist.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.flight.common.di.module.FlightModule;
import com.tokopedia.flight.orderlist.di.FlightOrderComponent;
import com.tokopedia.transaction.orders.orderlist.view.activity.OrderListActivity;
import com.tokopedia.transaction.orders.orderlist.view.fragment.OrderListFragment;

import dagger.Component;

/**
 * Created by baghira on 07/05/18.
 */

@OrderListModuleScope
@Component(dependencies = {BaseAppComponent.class}, modules = {FlightModule.class})
public interface OrderListComponent {
    void inject(OrderListFragment orderListFragment);
    void inject(OrderListActivity orderListActivity);
}
