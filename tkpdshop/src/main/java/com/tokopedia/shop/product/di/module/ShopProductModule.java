package com.tokopedia.shop.product.di.module;

import com.tokopedia.shop.info.data.repository.ShopInfoRepositoryImpl;
import com.tokopedia.shop.info.data.source.ShopInfoDataSource;
import com.tokopedia.shop.info.domain.repository.ShopInfoRepository;
import com.tokopedia.shop.product.data.repository.ShopProductRepositoryImpl;
import com.tokopedia.shop.product.data.source.cloud.ShopProductCloudDataSource;
import com.tokopedia.shop.product.di.scope.ShopProductScope;
import com.tokopedia.shop.product.domain.repository.ShopProductRepository;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

import dagger.Module;
import dagger.Provides;

@ShopProductScope
@Module
public class ShopProductModule {

    @ShopProductScope
    @Provides
    public ShopProductRepository provideShopProductRepository(ShopProductCloudDataSource shopProductDataSource){
        return new ShopProductRepositoryImpl(shopProductDataSource);
    }

    @ShopProductScope
    @Provides
    public ShopProductViewModel provideShopProductViewModel(){
        return new ShopProductViewModel();
    }

    @ShopProductScope
    @Provides
    public ShopInfoRepository provideShopInfoRepository(ShopInfoDataSource shopInfoDataSource){
        return new ShopInfoRepositoryImpl(shopInfoDataSource);
    }
}

