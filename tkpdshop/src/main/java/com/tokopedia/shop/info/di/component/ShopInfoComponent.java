package com.tokopedia.shop.info.di.component;

import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.info.di.module.ShopInfoModule;
import com.tokopedia.shop.info.di.scope.ShopInfoScope;
import com.tokopedia.shop.info.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.shop.info.view.activity.ShopInfoActivity;
import com.tokopedia.shop.info.view.fragment.ShopInfoDetailFragment;

import dagger.Component;

/**
 * Created by hendry on 18/01/18.
 */
@ShopInfoScope
@Component(modules = ShopInfoModule.class, dependencies = ShopComponent.class)
public interface ShopInfoComponent {

    void inject(ShopInfoActivity shopInfoActivity);

    void inject(ShopInfoDetailFragment shopInfoDetailFragment);

    GetShopInfoUseCase getShopInfoUseCase();
}
