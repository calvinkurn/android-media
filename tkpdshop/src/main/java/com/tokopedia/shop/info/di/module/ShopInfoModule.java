package com.tokopedia.shop.info.di.module;

import android.content.Context;
import android.support.annotation.Nullable;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.reputation.speed.SpeedReputation;
import com.tokopedia.shop.ShopModuleRouter;
import com.tokopedia.shop.common.di.module.ShopModule;
import com.tokopedia.shop.info.data.repository.ShopInfoRepositoryImpl;
import com.tokopedia.shop.info.data.source.ShopInfoDataSource;
import com.tokopedia.shop.info.di.scope.ShopInfoScope;
import com.tokopedia.shop.info.domain.repository.ShopInfoRepository;
import com.tokopedia.shop.note.view.model.ShopNoteViewModel;

import dagger.Module;
import dagger.Provides;
import rx.Observable;

@ShopInfoScope
@Module
public class ShopInfoModule {

    @ShopInfoScope
    @Provides
    public ShopInfoRepository provideShopInfoRepository(ShopInfoDataSource shopInfoDataSource){
        return new ShopInfoRepositoryImpl(shopInfoDataSource);
    }

    @Nullable
    @ShopInfoScope
    @Provides
    public Observable<SpeedReputation> provideSpeedReputation(@ApplicationContext Context context){
        if(context != null && context instanceof ShopModuleRouter){
            return ((ShopModuleRouter)context).getSpeedReputationUseCase();
        }
        return null;
    }
}

