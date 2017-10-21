package com.tokopedia.seller.shop.setting.di.component;

import com.tokopedia.core.base.di.scope.ActivityScope;
import com.tokopedia.seller.shop.common.di.component.ShopComponent;
import com.tokopedia.seller.shop.setting.di.module.ShopOpenDomainModule;
import com.tokopedia.seller.shop.open.view.presenter.ShopOpenDomainPresenter;

import dagger.Component;

/**
 * Created by sebastianuskh on 3/17/17.
 */
@ActivityScope
@Component(modules = ShopOpenDomainModule.class, dependencies = ShopComponent.class)
public interface ShopOpenDomainComponent {
    ShopOpenDomainPresenter getPresenter();
}
