package com.tokopedia.seller.selling.appwidget.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.selling.appwidget.presenter.GetOrderService;

import dagger.Component;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by zulfikarrahman on 7/12/17.
 */

@NewOrderWidgetScope
@Component(modules = NewOrderWidgetModule.class, dependencies = AppComponent.class)
public interface NewOrderWidgetComponent {
    void inject(GetOrderService getOrderService);
}
