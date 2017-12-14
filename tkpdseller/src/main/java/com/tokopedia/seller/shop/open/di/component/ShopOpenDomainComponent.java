package com.tokopedia.seller.shop.open.di.component;

import com.tokopedia.seller.shop.common.di.component.ShopComponent;
import com.tokopedia.seller.shop.open.di.module.ShopOpenDomainModule;
import com.tokopedia.seller.shop.open.di.scope.ShopOpenDomainScope;
import com.tokopedia.seller.shop.open.view.activity.ShopOpenDomainActivity;
import com.tokopedia.seller.shop.open.view.fragment.ShopOpenDomainFragment;
import com.tokopedia.seller.shop.open.view.presenter.ShopOpenDomainPresenter;

import dagger.Component;

/**
 * Created by sebastianuskh on 3/17/17.
 */
@ShopOpenDomainScope
@Component(modules = ShopOpenDomainModule.class, dependencies = ShopComponent.class)
public interface ShopOpenDomainComponent {
    void inject(ShopOpenDomainFragment shopOpenDomainFragment);
    void inject(ShopOpenDomainActivity shopOpenDomainActivity);
}
