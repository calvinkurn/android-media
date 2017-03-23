package com.tokopedia.seller.shop.setting.di.component;

import com.tokopedia.seller.app.BaseFragmentComponent;
import com.tokopedia.seller.shop.setting.di.module.ShopSettingInfoModule;
import com.tokopedia.seller.shop.setting.di.scope.ShopSettingInfoScope;
import com.tokopedia.seller.shop.setting.di.scope.ShopSettingScope;
import com.tokopedia.seller.shop.setting.domain.ShopSettingSaveInfoRepository;
import com.tokopedia.seller.shop.setting.view.fragment.ShopSettingInfoFragment;

import dagger.Component;

/**
 * Created by zulfikarrahman on 3/23/17.
 */
@ShopSettingInfoScope
@Component(modules = ShopSettingInfoModule.class, dependencies = ShopSettingComponent.class)
public interface ShopSettingInfoComponent extends BaseFragmentComponent<ShopSettingInfoFragment> {
}
