package com.tokopedia.seller.shop.setting.di.module;

import com.tokopedia.seller.shop.setting.di.scope.ShopSettingLocationScope;
import com.tokopedia.seller.shop.setting.domain.interactor.FetchDistrictDataUseCase;
import com.tokopedia.seller.shop.setting.domain.interactor.GetRecomendationLocationDistrictUseCase;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingLocationPresenter;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingLocationPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sebastianuskh on 3/17/17.
 */

@ShopSettingLocationScope
@Module
public class ShopSettingLocationModule {

    @Provides
    ShopSettingLocationPresenter providePresenter(
            FetchDistrictDataUseCase fetchDistrictDataUseCase,
            GetRecomendationLocationDistrictUseCase getRecomendationLocationDistrictUseCase) {
        return new ShopSettingLocationPresenterImpl(
                fetchDistrictDataUseCase,
                getRecomendationLocationDistrictUseCase
        );
    }

}
