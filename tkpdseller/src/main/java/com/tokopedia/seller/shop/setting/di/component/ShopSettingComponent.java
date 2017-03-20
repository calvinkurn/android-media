package com.tokopedia.seller.shop.setting.di.component;

import com.tokopedia.seller.shop.setting.di.module.ShopSettingModule;
import com.tokopedia.seller.shop.setting.di.scope.ShopSettingScope;

import dagger.Component;

/**
 * Created by sebastianuskh on 3/17/17.
 */
@ShopSettingScope
@Component(modules = ShopSettingModule.class)
public interface ShopSettingComponent {


}
