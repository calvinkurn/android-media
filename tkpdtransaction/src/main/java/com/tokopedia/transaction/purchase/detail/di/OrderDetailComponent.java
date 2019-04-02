package com.tokopedia.transaction.purchase.detail.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.transaction.purchase.detail.activity.OrderDetailActivity;

import dagger.Component;

/**
 * Created by kris on 11/14/17. Tokopedia
 */

@OrderDetailScope
@Component(modules = OrderDetailModule.class, dependencies = AppComponent.class)
public interface OrderDetailComponent {
    void inject(OrderDetailActivity activity);
}
