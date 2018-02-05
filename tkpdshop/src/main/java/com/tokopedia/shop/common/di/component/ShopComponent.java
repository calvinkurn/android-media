package com.tokopedia.shop.common.di.component;

import android.content.Context;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.shop.common.di.module.ShopModule;
import com.tokopedia.shop.common.di.scope.ShopScope;
import com.tokopedia.shop.info.view.activity.ShopInfoActivity;

import dagger.Component;

/**
 * Created by hendry on 18/01/18.
 */
@ShopScope
@Component(modules = ShopModule.class, dependencies = BaseAppComponent.class)
public interface ShopComponent {

    @ApplicationContext
    Context context();

    void inject(ShopInfoActivity shopInfoActivity);
}
