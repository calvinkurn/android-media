package com.tokopedia.gm.featured.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.di.qualifier.GoldMerchantQualifier;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.gm.featured.data.FeaturedProductDataSource;
import com.tokopedia.gm.featured.data.cloud.api.FeaturedProductApi;
import com.tokopedia.gm.featured.di.scope.FeaturedProductScope;
import com.tokopedia.gm.featured.domain.mapper.FeaturedProductMapper;
import com.tokopedia.gm.featured.domain.mapper.FeaturedProductPOSTMapper;
import com.tokopedia.gm.featured.repository.FeaturedProductRepository;
import com.tokopedia.gm.featured.repository.FeaturedProductRepositoryImpl;
import com.tokopedia.seller.product.edit.data.repository.ShopInfoRepositoryImpl;
import com.tokopedia.seller.product.edit.data.source.ShopInfoDataSource;
import com.tokopedia.seller.product.edit.data.source.cloud.api.ShopApi;
import com.tokopedia.seller.product.edit.domain.ShopInfoRepository;

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

    @FeaturedProductScope
    @Provides
    FeaturedProductRepository provideFeaturedProductRepository(
            FeaturedProductDataSource featuredProductDataSource,
            ShopInfoRepository shopInfoRepository,
            FeaturedProductMapper featuredProductMapper,
            FeaturedProductPOSTMapper featuredProductPOSTMapper) {
        return new FeaturedProductRepositoryImpl(featuredProductDataSource, shopInfoRepository, featuredProductMapper, featuredProductPOSTMapper);
    }

    // FOR SHOP_INFO
    @FeaturedProductScope
    @Provides
    ShopInfoRepository provideShopInfoRepository(@ApplicationContext Context context, ShopInfoDataSource shopInfoDataSource) {
        return new ShopInfoRepositoryImpl(context, shopInfoDataSource);
    }

    @FeaturedProductScope
    @Provides
    ShopApi provideShopApi(@WsV4Qualifier Retrofit retrofit) {
        return retrofit.create(ShopApi.class);
    }
}
