package com.tokopedia.shop.info.di.module;

import com.tokopedia.shop.info.data.repository.ShopInfoRepositoryImpl;
import com.tokopedia.shop.info.data.source.ShopInfoDataSource;
import com.tokopedia.shop.info.di.scope.ShopInfoScope;
import com.tokopedia.shop.info.domain.repository.ShopInfoRepository;
import com.tokopedia.shop.note.view.model.ShopNoteViewModel;

import dagger.Module;
import dagger.Provides;

@ShopInfoScope
@Module
public class ShopInfoModule {

    @ShopInfoScope
    @Provides
    public ShopInfoRepository provideShopInfoRepository(ShopInfoDataSource shopInfoDataSource){
        return new ShopInfoRepositoryImpl(shopInfoDataSource);
    }
}

