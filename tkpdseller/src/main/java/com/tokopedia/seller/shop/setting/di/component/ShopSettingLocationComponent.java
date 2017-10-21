package com.tokopedia.seller.shop.setting.di.component;

import com.tokopedia.seller.shop.setting.di.module.ShopSettingLocationModule;
import com.tokopedia.seller.shop.setting.di.scope.ShopSettingLocationScope;
import com.tokopedia.seller.shop.setting.view.fragment.ShopSettingShopSettingLocationFragment;

import dagger.Component;

/**
 * Created by sebastianuskh on 3/17/17.
 */
@ShopSettingLocationScope
@Component(modules = ShopSettingLocationModule.class, dependencies = ShopSettingComponent.class)
public interface ShopSettingLocationComponent {

    void inject(ShopSettingShopSettingLocationFragment fragment);
}
