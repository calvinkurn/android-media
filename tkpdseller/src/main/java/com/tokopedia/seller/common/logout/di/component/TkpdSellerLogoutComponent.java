package com.tokopedia.seller.common.logout.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.common.logout.di.module.TkpdSellerLogoutModule;
import com.tokopedia.seller.common.logout.di.scope.TkpdSellerLogoutScope;
import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatClearCacheUseCase;
import com.tokopedia.seller.product.edit.domain.interactor.categorypicker.ClearCategoryCacheUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.ClearAllDraftProductUseCase;

import dagger.Component;

/**
 * @author sebastianuskh on 5/8/17.
 */
@TkpdSellerLogoutScope
@Component(modules = TkpdSellerLogoutModule.class, dependencies = AppComponent.class)
public interface TkpdSellerLogoutComponent {

    ClearAllDraftProductUseCase getClearAllDraftProductUseCase();

    ClearCategoryCacheUseCase getClearCategoryCacheUseCase();

    GMStatClearCacheUseCase getGmStatClearUseCase();
}
