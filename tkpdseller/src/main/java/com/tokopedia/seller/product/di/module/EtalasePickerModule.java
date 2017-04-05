package com.tokopedia.seller.product.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ActivityContext;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.product.data.repository.MyEtalaseRepositoryImpl;
import com.tokopedia.seller.product.data.source.MyEtalaseDataSource;
import com.tokopedia.seller.product.data.source.cloud.api.MyEtalaseApi;
import com.tokopedia.seller.product.di.scope.EtalasePickerScope;
import com.tokopedia.seller.product.domain.MyEtalaseRepository;
import com.tokopedia.seller.topads.data.factory.TopAdsEtalaseFactory;
import com.tokopedia.seller.topads.data.repository.TopAdsEtalaseListRepositoryImpl;
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
    MyEtalaseRepository provideMyEtalaseRepository(MyEtalaseDataSource myEtalaseDataSource){
        return new MyEtalaseRepositoryImpl(myEtalaseDataSource);
    }

    @EtalasePickerScope
    @Provides
    MyEtalaseApi provideMyEtalaseApi(@WsV4Qualifier Retrofit retrofit){
        return retrofit.create(MyEtalaseApi.class);
    }
}
