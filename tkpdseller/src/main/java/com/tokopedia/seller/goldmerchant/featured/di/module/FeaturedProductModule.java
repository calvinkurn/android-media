package com.tokopedia.seller.goldmerchant.featured.di.module;

import com.tokopedia.core.network.di.qualifier.GoldMerchantQualifier;
import com.tokopedia.seller.goldmerchant.featured.data.cloud.api.FeaturedProductApi;
import com.tokopedia.seller.goldmerchant.featured.di.scope.FeaturedProductScope;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by normansyahputa on 9/7/17.
 */
@Module
public class FeaturedProductModule {
    @FeaturedProductScope
    @Provides
    FeaturedProductApi provideFeaturedProductApi(@GoldMerchantQualifier Retrofit retrofit) {
        return retrofit.create(FeaturedProductApi.class);
    }
}
