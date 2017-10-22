package com.tokopedia.seller.shop.setting.view.activity;

import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.shop.setting.di.component.DaggerShopSettingComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopSettingComponent;
import com.tokopedia.seller.shop.setting.di.module.ShopSettingModule;
import com.tokopedia.seller.shop.setting.view.fragment.ShopSettingInfoFragment;

/**
 * Created by nathan on 10/22/17.
 */

public class ShopSettingInfoActivity extends BaseSimpleActivity implements HasComponent<ShopSettingComponent> {

    @Override
    protected Fragment getNewFragment() {
        return ShopSettingInfoFragment.createInstance();
    }

    @Override
    public ShopSettingComponent getComponent() {
        return DaggerShopSettingComponent
                .builder()
                .shopSettingModule(new ShopSettingModule())
                .shopComponent(((SellerModuleRouter) getApplication()).getShopComponent())
                .build();
    }
}