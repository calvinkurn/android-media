package com.tokopedia.transaction.orders.orderlist.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.transaction.orders.orderlist.view.fragment.OrderListFragment;

import dagger.Component;

/**
 * Created by baghira on 07/05/18.
 */

@OrderListModuleScope
@Component(modules = {OrderListModule.class}, dependencies = {BaseAppComponent.class})
public interface OrderListComponent {
    void inject(OrderListFragment orderListFragment);
}
