package com.tokopedia.transaction.checkout.di.module;

import com.tokopedia.transaction.apiservice.CartService;
import com.tokopedia.transaction.checkout.domain.CartRepository;
import com.tokopedia.transaction.checkout.domain.CartRepositoryDataDummy;
import com.tokopedia.transaction.checkout.domain.ICartRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author anggaprasetiyo on 29/01/18.
 */
@Module
public class DataModule {

    @Provides
    CartService provideCartService() {
        return new CartService();
    }

    @Provides
    ICartRepository provideICartRepository(CartService cartService) {
        return new CartRepositoryDataDummy();
    }
}
