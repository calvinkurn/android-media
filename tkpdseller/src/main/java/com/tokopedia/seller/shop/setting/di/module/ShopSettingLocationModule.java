package com.tokopedia.seller.shop.setting.di.module;

import com.tokopedia.seller.shop.setting.di.scope.ShopSettingLocationScope;
import com.tokopedia.seller.shop.setting.domain.interactor.FetchDistrictDataUseCase;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingLocationPresenter;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingLocationPresenterImpl;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingLocationView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sebastianuskh on 3/17/17.
 */

@ShopSettingLocationScope
@Module
public class ShopSettingLocationModule {

    private final ShopSettingLocationView view;

    public ShopSettingLocationModule(ShopSettingLocationView view) {
        this.view = view;
    }

    @ShopSettingLocationScope
    @Provides
    ShopSettingLocationPresenter providePresenter(FetchDistrictDataUseCase fetchDistrictDataUseCase) {
        return new ShopSettingLocationPresenterImpl(view, fetchDistrictDataUseCase);
    }

}
