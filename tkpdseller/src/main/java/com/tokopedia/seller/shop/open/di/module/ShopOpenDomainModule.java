package com.tokopedia.seller.shop.open.di.module;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.seller.logistic.GetOpenShopLocationPassUseCase;
import com.tokopedia.seller.logistic.GetOpenShopTokenUseCase;
import com.tokopedia.seller.logistic.data.source.cloud.api.WSLogisticApi;
import com.tokopedia.seller.shop.open.data.repository.ShopOpenRepository;
import com.tokopedia.seller.shop.open.data.repository.ShopOpenRepositoryImpl;
import com.tokopedia.seller.shop.open.data.source.ShopOpenDataSource;
import com.tokopedia.seller.shop.open.di.scope.ShopOpenDomainScope;
import com.tokopedia.seller.shop.open.data.source.cloud.api.OpenShopApi;
import com.tokopedia.seller.logistic.data.repository.DistrictLogisticDataRepositoryImpl;
import com.tokopedia.seller.logistic.data.source.LogisticDataSource;
import com.tokopedia.seller.logistic.domain.DistrictLogisticDataRepository;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@ShopOpenDomainScope
@Module
public class ShopOpenDomainModule {

    @ShopOpenDomainScope
    @Provides
    public ShopOpenRepository provideShopOpenRepository(ShopOpenDataSource shopOpenDataSource) {
        return new ShopOpenRepositoryImpl(shopOpenDataSource);
    }

    @ShopOpenDomainScope
    @Provides
    public DistrictLogisticDataRepository provideDistrictLogisticDataRepository(LogisticDataSource logisticDataSource) {
        return new DistrictLogisticDataRepositoryImpl(logisticDataSource);
    }

    @Provides
    @ShopOpenDomainScope
    public OpenShopApi provideOpenShopApi(@WsV4Qualifier Retrofit retrofit) {
        return retrofit.create(OpenShopApi.class);
    }

    @Provides
    @ShopOpenDomainScope
    public WSLogisticApi provideWSLogisticApi(@WsV4Qualifier Retrofit retrofit) {
        return retrofit.create(WSLogisticApi.class);
    }

    @Provides
    @ShopOpenDomainScope
    public GetOpenShopTokenUseCase provideGetOpenShopDataUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread){
        return new GetOpenShopTokenUseCase(threadExecutor,postExecutionThread);
    }


    @Provides
    @ShopOpenDomainScope
    public GetOpenShopLocationPassUseCase provideGetOpenShopLocationPassUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread){
        return new GetOpenShopLocationPassUseCase(threadExecutor,postExecutionThread);
    }
}