package com.tokopedia.seller.shop.setting.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.shop.setting.di.module.ShopSettingModule;
import com.tokopedia.seller.shop.setting.di.scope.ShopSettingScope;
import com.tokopedia.seller.shop.setting.domain.ShopSettingSaveInfoRepository;
import com.tokopedia.seller.shop.setting.domain.interactor.FetchDistrictDataUseCase;
import com.tokopedia.seller.shop.setting.domain.interactor.GetRecomendationLocationDistrictUseCase;

import dagger.Component;

/**
 * Created by sebastianuskh on 3/17/17.
 */
@ShopSettingScope
@Component(modules = ShopSettingModule.class, dependencies = AppComponent.class)
public interface ShopSettingComponent {

    FetchDistrictDataUseCase getFetchDistrictDataUseCase();

    GetRecomendationLocationDistrictUseCase getGetRecomendationLocationDistrictUseCase();

    ThreadExecutor getThreadExecutor();

    PostExecutionThread getPostExecutionThread();

    ShopSettingSaveInfoRepository getSaveInfoRepository();
}
