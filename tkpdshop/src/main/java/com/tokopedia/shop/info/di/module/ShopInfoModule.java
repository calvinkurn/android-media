package com.tokopedia.shop.info.di.module;

import android.content.Context;
import android.support.annotation.Nullable;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.reputation.speed.SpeedReputation;
import com.tokopedia.shop.ShopModuleRouter;
import com.tokopedia.shop.info.di.scope.ShopInfoScope;

import dagger.Module;
import dagger.Provides;
import rx.Observable;

@ShopInfoScope
@Module
public class ShopInfoModule {

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

