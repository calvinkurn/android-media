package com.tokopedia.seller.product.di.module;

import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.seller.product.di.scope.EtalasePickerScope;
import com.tokopedia.seller.topads.data.factory.TopAdsEtalaseFactory;
import com.tokopedia.seller.topads.data.repository.TopAdsEtalaseListRepositoryImpl;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.api.TopAdsShopApi;
import com.tokopedia.seller.topads.domain.TopAdsEtalaseListRepository;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 4/5/17.
 */
@EtalasePickerScope
@Module
public class EtalasePickerModule {
    @EtalasePickerScope
    @Provides
    TopAdsEtalaseListRepository provideTopAdsEtalaseListRepository(TopAdsEtalaseFactory topadsEtalaseFactory){
        return new TopAdsEtalaseListRepositoryImpl(topadsEtalaseFactory);
    }

    @EtalasePickerScope
    @Provides
    TopAdsShopApi provideTopAdsShopApi(@WsV4Qualifier Retrofit retrofit){
        return retrofit.create(TopAdsShopApi.class);
    }
}
