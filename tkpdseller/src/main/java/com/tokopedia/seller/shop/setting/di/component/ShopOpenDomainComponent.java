package com.tokopedia.seller.shop.setting.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.scope.ActivityScope;
import com.tokopedia.seller.app.BaseFragmentComponent;
import com.tokopedia.seller.shop.open.view.fragment.ShopOpenDomainFragment;
import com.tokopedia.seller.shop.setting.di.module.ShopOpenDomainModule;
import com.tokopedia.seller.shop.setting.view.presenter.ShopOpenDomainPresenter;

import dagger.Component;

/**
 * Created by sebastianuskh on 3/17/17.
 */
@ActivityScope
@Component(modules = ShopOpenDomainModule.class, dependencies = AppComponent.class)
public interface ShopOpenDomainComponent {
    ShopOpenDomainPresenter getPresenter();
}
