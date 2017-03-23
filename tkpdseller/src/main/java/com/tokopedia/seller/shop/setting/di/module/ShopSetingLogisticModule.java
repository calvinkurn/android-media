package com.tokopedia.seller.shop.setting.di.module;

import com.tokopedia.seller.app.BaseFragmentModule;
import com.tokopedia.seller.shop.setting.di.scope.ShopSettingLogisticScope;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingLogisticPresenter;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingLogisticPresenterImpl;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingLogisticView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sebastianuskh on 3/23/17.
 */
@ShopSettingLogisticScope
@Module
public class ShopSetingLogisticModule extends BaseFragmentModule<ShopSettingLogisticView>{
    public ShopSetingLogisticModule(ShopSettingLogisticView view) {
        super(view);
    }

    @ShopSettingLogisticScope
    @Provides
    ShopSettingLogisticPresenter provideShopSettingLogisticPresenter(ShopSettingLogisticView view){
        return new ShopSettingLogisticPresenterImpl(view);
    }


}
