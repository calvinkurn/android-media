package com.tokopedia.shop.info.di.component;

import android.content.Context;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.shop.info.di.module.ShopInfoModule;
import com.tokopedia.shop.info.di.scope.ShopInfoScope;
import com.tokopedia.shop.info.view.activity.ShopInfoActivity;

import dagger.Component;

/**
 * Created by hendry on 18/01/18.
 */
@ShopInfoScope
@Component(modules = ShopInfoModule.class, dependencies = BaseAppComponent.class)
public interface ShopInfoComponent {

    @ApplicationContext
    Context context();

    void inject(ShopInfoActivity shopInfoActivity);
}
