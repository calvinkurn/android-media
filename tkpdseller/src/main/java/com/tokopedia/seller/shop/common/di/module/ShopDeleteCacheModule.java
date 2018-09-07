package com.tokopedia.seller.shop.common.di.module;

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
    DeleteShopInfoTomeUseCase provideDeleteShopInfoTomeUseCase() {
        return new DeleteShopInfoTomeUseCase();
    }

    @DeleteCacheScope
    @Provides
    DeleteShopInfoUseCase provideDeleteShopInfoUseCase(DeleteShopInfoTomeUseCase deleteShopInfoTomeUseCase) {
        return new DeleteShopInfoUseCase(deleteShopInfoTomeUseCase);
    }
}

