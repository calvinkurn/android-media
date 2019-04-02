package com.tokopedia.transaction.orders.orderdetails.di;

/**
 * Created by baghira on 10/05/18.
 */

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.transaction.orders.orderdetails.view.fragment.MarketPlaceDetailFragment;
import com.tokopedia.transaction.orders.orderdetails.view.fragment.OmsDetailFragment;
import com.tokopedia.transaction.orders.orderdetails.view.fragment.OrderListDetailFragment;
import com.tokopedia.transaction.orders.orderlist.di.OrderListModuleScope;
import com.tokopedia.transaction.purchase.detail.di.OrderDetailModule;

import dagger.Component;


@OrderListModuleScope
@Component(dependencies = {BaseAppComponent.class}, modules = OrderListDetailModule.class)
public interface OrderDetailsComponent {
    void inject(OrderListDetailFragment orderListDetailFragment);

    void inject(OmsDetailFragment omsDetailFragment);


    void inject(MarketPlaceDetailFragment marketPlaceDetailFragment);
}