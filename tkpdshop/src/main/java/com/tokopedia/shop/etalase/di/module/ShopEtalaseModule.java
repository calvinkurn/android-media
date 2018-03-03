package com.tokopedia.shop.etalase.di.module;

import com.tokopedia.shop.common.data.source.cloud.api.ShopWSApi;
import com.tokopedia.shop.etalase.data.repository.ShopEtalaseRepositoryImpl;
import com.tokopedia.shop.etalase.data.source.cloud.ShopEtalaseCloudDataSource;
import com.tokopedia.shop.etalase.di.scope.ShopEtalaseScope;
import com.tokopedia.shop.etalase.domain.repository.ShopEtalaseRepository;

import dagger.Module;
import dagger.Provides;

@ShopEtalaseScope
@Module
public class ShopEtalaseModule {

    @ShopEtalaseScope
    @Provides
    public ShopEtalaseCloudDataSource provideShopEtalaseCloudDataSource(ShopWSApi shopWSApi) {
        return new ShopEtalaseCloudDataSource(shopWSApi);
    }

    @ShopEtalaseScope
    @Provides
    public ShopEtalaseRepository provideShopEtalaseRepository(ShopEtalaseCloudDataSource shopEtalaseDataSource) {
        return new ShopEtalaseRepositoryImpl(shopEtalaseDataSource);
    }
}

