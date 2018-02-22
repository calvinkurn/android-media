package com.tokopedia.transaction.purchase.detail.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.transaction.purchase.detail.activity.ConfirmShippingActivity;

import dagger.Component;

/**
 * Created by kris on 1/3/18. Tokopedia
 */

@OrderCourierScope
@Component(modules = OrderCourierModule.class, dependencies = AppComponent.class)
public interface OrderCourierComponent {
    void inject(ConfirmShippingActivity activity);
}
