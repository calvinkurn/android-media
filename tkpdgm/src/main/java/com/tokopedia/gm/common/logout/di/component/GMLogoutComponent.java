package com.tokopedia.gm.common.logout.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.common.category.domain.interactor.ClearCategoryCacheUseCase;
import com.tokopedia.gm.common.logout.di.module.GMLogoutModule;
import com.tokopedia.gm.common.logout.di.scope.GMLogoutScope;
import com.tokopedia.gm.statistic.domain.interactor.GMStatClearCacheUseCase;

import dagger.Component;

/**
 * @author sebastianuskh on 5/8/17.
 */
@GMLogoutScope
@Component(modules = GMLogoutModule.class, dependencies = AppComponent.class)
public interface GMLogoutComponent {

    GMStatClearCacheUseCase getClearStatisticCacheUseCase();
}
