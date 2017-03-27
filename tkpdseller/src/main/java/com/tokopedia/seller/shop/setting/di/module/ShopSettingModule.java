package com.tokopedia.seller.shop.setting.di.module;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.shop.MyShopService;
import com.tokopedia.core.network.apiservices.shop.apis.MyShopApi;
import com.tokopedia.seller.shop.setting.data.repository.DistrictDataRepositoryImpl;
import com.tokopedia.seller.shop.setting.data.source.DistrictDataSource;
import com.tokopedia.seller.shop.setting.di.scope.ShopSettingScope;
import com.tokopedia.seller.shop.setting.domain.DistrictDataRepository;
import com.tokopedia.seller.shop.setting.domain.interactor.FetchDistrictDataUseCase;
import com.tokopedia.seller.shop.setting.domain.interactor.GetRecomendationLocationDistrictUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sebastianuskh on 3/20/17.
 */
@ShopSettingScope
@Module
public class ShopSettingModule {

    @Provides
    @ShopSettingScope
    public FetchDistrictDataUseCase provideFetchDistrictDataUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            DistrictDataRepository districtDataRepository
    ) {
        return new FetchDistrictDataUseCase(threadExecutor, postExecutionThread, districtDataRepository);
    }

    @Provides
    @ShopSettingScope
    public GetRecomendationLocationDistrictUseCase provideGetRecomendationLocationDistrictUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            DistrictDataRepository districtDataRepository
    ){
        return new GetRecomendationLocationDistrictUseCase(
                threadExecutor,
                postExecutionThread,
                districtDataRepository
        );
    }

    @Provides
    @ShopSettingScope
    public DistrictDataRepository provideDistrictDataRepository(DistrictDataSource districtDataSource) {
        return new DistrictDataRepositoryImpl(districtDataSource);
    }

    @Provides
    @ShopSettingScope
    public MyShopApi provideMyShopApi() {
        return new MyShopService().getApi();
    }
}