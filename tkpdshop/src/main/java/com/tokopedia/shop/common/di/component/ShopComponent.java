package com.tokopedia.shop.common.di.component;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.shop.common.di.module.ShopModule;
import com.tokopedia.shop.common.di.scope.ShopScope;
import com.tokopedia.shop.common.data.source.cloud.api.WS4ShopApi;

import dagger.Component;

/**
 * @author sebastianuskh on 4/13/17.
 */
@ShopScope
@Component(modules = ShopModule.class, dependencies = BaseAppComponent.class)
public interface ShopComponent {

    WS4ShopApi shopApi();

    UserSession userSession();
}