package com.tokopedia.transaction.purchase.detail.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.transaction.purchase.detail.activity.OrderHistoryActivity;

import dagger.Component;

/**
 * Created by kris on 11/17/17. Tokopedia
 */

@OrderHistoryScope
@Component(modules = OrderHistoryModule.class, dependencies = AppComponent.class)
public interface OrderHistoryComponent {
    void inject(OrderHistoryActivity activity);
}
