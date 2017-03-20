package com.tokopedia.seller.shop.setting.di.module;

import android.app.Activity;

import com.tokopedia.seller.app.BaseActivityModule;
import com.tokopedia.seller.shop.setting.di.scope.ShopSettingScope;

import dagger.Module;

/**
 * Created by sebastianuskh on 3/20/17.
 */
@ShopSettingScope
@Module
public class ShopSettingModule extends BaseActivityModule {
    public ShopSettingModule(Activity activity) {
        super(activity);
    }
}
