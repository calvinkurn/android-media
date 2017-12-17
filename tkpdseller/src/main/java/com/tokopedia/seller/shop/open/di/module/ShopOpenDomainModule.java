package com.tokopedia.seller.shop.open.di.module;

import com.tokopedia.seller.shop.open.data.repository.ShopOpenRepository;
import com.tokopedia.seller.shop.open.data.repository.ShopOpenRepositoryImpl;
import com.tokopedia.seller.shop.open.data.source.ShopOpenDataSource;
import com.tokopedia.seller.shop.open.di.scope.ShopOpenDomainScope;

import dagger.Module;
import dagger.Provides;

@ShopOpenDomainScope
@Module
public class ShopOpenDomainModule {

    @ShopOpenDomainScope
    @Provides
    public ShopOpenRepository provideShopOpenRepository(ShopOpenDataSource shopOpenDataSource) {
        return new ShopOpenRepositoryImpl(shopOpenDataSource);
    }

}