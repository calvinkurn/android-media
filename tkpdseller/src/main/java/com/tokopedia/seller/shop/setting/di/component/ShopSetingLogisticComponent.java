package com.tokopedia.seller.shop.setting.di.component;

import com.tokopedia.seller.shop.setting.di.module.ShopSetingLogisticModule;
import com.tokopedia.seller.shop.setting.di.scope.ShopSettingLogisticScope;
import com.tokopedia.seller.shop.setting.view.fragment.ShopSettingLogisticFragment;

import dagger.Component;

/**
 * Created by sebastianuskh on 3/23/17.
 */
@ShopSettingLogisticScope
@Component(modules = ShopSetingLogisticModule.class, dependencies = ShopSettingComponent.class)
public interface ShopSetingLogisticComponent {
    void inject(ShopSettingLogisticFragment shopSettingLogisticFragment);
}
