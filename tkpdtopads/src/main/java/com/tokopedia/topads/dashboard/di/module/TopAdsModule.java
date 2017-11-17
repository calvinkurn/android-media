package com.tokopedia.topads.dashboard.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.seller.product.common.di.scope.ProductScope;
import com.tokopedia.seller.product.edit.data.repository.ShopInfoRepositoryImpl;
import com.tokopedia.seller.product.edit.data.source.ShopInfoDataSource;
import com.tokopedia.seller.product.edit.data.source.cloud.api.ShopApi;
import com.tokopedia.seller.product.edit.domain.ShopInfoRepository;
import com.tokopedia.topads.dashboard.data.repository.GetDepositTopAdsRepositoryImpl;
import com.tokopedia.topads.dashboard.data.source.GetDepositTopadsDataSource;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.api.TopAdsManagementApi;
import com.tokopedia.topads.dashboard.di.scope.TopAdsQualifier;
import com.tokopedia.topads.dashboard.di.scope.TopAdsScope;
import com.tokopedia.topads.dashboard.domain.GetDepositTopAdsRepository;
import com.tokopedia.topads.dashboard.domain.interactor.GetDepositTopAdsUseCase;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by zulfikarrahman on 9/18/17.
 */

@TopAdsScope
@Module
public class TopAdsModule {

    @TopAdsScope
    @Provides
    public GetDepositTopAdsUseCase provideGetDepositTopAdsUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                                                  GetDepositTopAdsRepository getDepositTopAdsRepository) {
        return new GetDepositTopAdsUseCase(threadExecutor, postExecutionThread, getDepositTopAdsRepository);
    }

    @TopAdsScope
    @Provides
    public GetDepositTopAdsRepository provideGetDepositTopAdsRepository(GetDepositTopadsDataSource getDepositTopadsDataSource){
        return new GetDepositTopAdsRepositoryImpl(getDepositTopadsDataSource);
    }

    @TopAdsScope
    @Provides
    public TopAdsManagementApi provideTopAdsManagementApi(@TopAdsQualifier Retrofit retrofit){
        return retrofit.create(TopAdsManagementApi.class);
    }

    @TopAdsScope
    @Provides
    public ShopInfoRepository provideShopInfoRepository(@ApplicationContext Context context, ShopInfoDataSource shopInfoDataSource){
        return new ShopInfoRepositoryImpl(context, shopInfoDataSource);
    }

    @TopAdsScope
    @Provides
    public ShopApi provideShopApi(@WsV4QualifierWithErrorHander Retrofit retrofit){
        return retrofit.create(ShopApi.class);
    }
}
