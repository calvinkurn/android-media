package com.tokopedia.seller.shop.open.di.module;

import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.seller.shop.open.data.repository.ShopOpenRepository;
import com.tokopedia.seller.shop.open.data.repository.ShopOpenRepositoryImpl;
import com.tokopedia.seller.shop.open.data.source.ShopOpenDataSource;
import com.tokopedia.seller.shop.open.di.scope.ShopOpenDomainScope;
import com.tokopedia.seller.shop.setting.data.datasource.cloud.OpenShopApi;
import com.tokopedia.seller.shop.setting.di.scope.ShopSettingScope;

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

    @Provides
    @ShopOpenDomainScope
    public OpenShopApi provideOpenShopApi(@WsV4Qualifier Retrofit retrofit) {
        return retrofit.create(OpenShopApi.class);
    }

}