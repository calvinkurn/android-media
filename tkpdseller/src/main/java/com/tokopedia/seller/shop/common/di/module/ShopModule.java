package com.tokopedia.seller.shop.common.di.module;

import com.tokopedia.core.network.di.qualifier.TomeQualifier;
import com.tokopedia.seller.shop.common.di.ShopScope;
import com.tokopedia.seller.shop.open.data.source.cloud.api.TomeApi;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by zulfikarrahman on 10/20/17.
 */

@ShopScope
@Module
public class ShopModule {
    @ShopScope
    @Provides
    public TomeApi provideTomeApi(@TomeQualifier Retrofit retrofit) {
        return retrofit.create(TomeApi.class);
    }
}
