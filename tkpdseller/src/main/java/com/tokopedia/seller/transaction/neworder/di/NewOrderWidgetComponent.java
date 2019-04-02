package com.tokopedia.seller.transaction.neworder.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.transaction.neworder.view.presenter.GetOrderService;
import com.tokopedia.seller.transaction.neworder.view.presenter.OrderWidgetJobService;

import dagger.Component;

/**
 * Created by zulfikarrahman on 7/12/17.
 */

@NewOrderWidgetScope
@Component(modules = NewOrderWidgetModule.class, dependencies = AppComponent.class)
public interface NewOrderWidgetComponent {
    void inject(GetOrderService getOrderService);
    void inject(OrderWidgetJobService orderWidgetJobService);
}
