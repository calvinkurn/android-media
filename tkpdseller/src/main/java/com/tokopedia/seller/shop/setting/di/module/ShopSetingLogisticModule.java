package com.tokopedia.seller.shop.setting.di.module;

import com.tokopedia.seller.shop.setting.di.scope.ShopSettingLogisticScope;
import com.tokopedia.seller.shop.setting.domain.interactor.GetLogisticAvailableUseCase;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingLogisticPresenter;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingLogisticPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sebastianuskh on 3/23/17.
 */
@ShopSettingLogisticScope
@Module
public class ShopSetingLogisticModule {

    @ShopSettingLogisticScope
    @Provides
    ShopSettingLogisticPresenter provideShopSettingLogisticPresenter(GetLogisticAvailableUseCase getLogisticAvailableUseCase) {
        return new ShopSettingLogisticPresenterImpl(getLogisticAvailableUseCase);
    }


}
