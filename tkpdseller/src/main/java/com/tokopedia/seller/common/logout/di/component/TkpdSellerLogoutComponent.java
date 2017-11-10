package com.tokopedia.seller.common.logout.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.common.category.domain.interactor.ClearCategoryCacheUseCase;
import com.tokopedia.seller.common.logout.di.module.TkpdSellerLogoutModule;
import com.tokopedia.seller.common.logout.di.scope.TkpdSellerLogoutScope;

import dagger.Component;

/**
 * @author sebastianuskh on 5/8/17.
 */
@TkpdSellerLogoutScope
@Component(modules = TkpdSellerLogoutModule.class, dependencies = AppComponent.class)
public interface TkpdSellerLogoutComponent {

    ClearCategoryCacheUseCase getClearCategoryCacheUseCase();
}
