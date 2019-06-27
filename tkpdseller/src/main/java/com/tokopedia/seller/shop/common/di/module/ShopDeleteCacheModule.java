package com.tokopedia.seller.shop.common.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.seller.shop.common.di.scope.DeleteCacheScope;
import com.tokopedia.seller.shop.common.domain.interactor.DeleteShopInfoTomeUseCase;
import com.tokopedia.seller.shop.common.domain.interactor.DeleteShopInfoUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zulfikarrahman on 10/20/17.
 */

@DeleteCacheScope
@Module
public class ShopDeleteCacheModule {

    @DeleteCacheScope
    @Provides
    DeleteShopInfoTomeUseCase provideDeleteShopInfoTomeUseCase(@ApplicationContext Context context) {
        return new DeleteShopInfoTomeUseCase(context);
    }

    @DeleteCacheScope
    @Provides
    DeleteShopInfoUseCase provideDeleteShopInfoUseCase(@ApplicationContext Context context, DeleteShopInfoTomeUseCase deleteShopInfoTomeUseCase) {
        return new DeleteShopInfoUseCase(context, deleteShopInfoTomeUseCase);
    }
}

