package com.tokopedia.shop.product.di.module;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.gm.common.constant.GMCommonUrl;
import com.tokopedia.gm.common.data.interceptor.GMAuthInterceptor;
import com.tokopedia.gm.common.data.repository.GMCommonRepositoryImpl;
import com.tokopedia.gm.common.data.source.GMCommonDataSource;
import com.tokopedia.gm.common.data.source.cloud.GMCommonCloudDataSource;
import com.tokopedia.gm.common.data.source.cloud.api.GMCommonApi;
import com.tokopedia.gm.common.domain.interactor.GetFeatureProductListUseCase;
import com.tokopedia.gm.common.domain.repository.GMCommonRepository;
import com.tokopedia.shop.product.data.repository.ShopProductRepositoryImpl;
import com.tokopedia.shop.product.data.source.cloud.ShopFilterCloudDataSource;
import com.tokopedia.shop.product.data.source.cloud.ShopProductCloudDataSource;
import com.tokopedia.shop.product.di.ShopProductGMFeaturedQualifier;
import com.tokopedia.shop.product.di.ShopProductWishListFeaturedQualifier;
import com.tokopedia.shop.product.di.scope.ShopProductScope;
import com.tokopedia.shop.product.domain.repository.ShopProductRepository;
import com.tokopedia.shop.product.view.mapper.ShopProductMapper;
import com.tokopedia.wishlist.common.constant.WishListCommonUrl;
import com.tokopedia.wishlist.common.data.interceptor.WishListAuthInterceptor;
import com.tokopedia.wishlist.common.data.repository.WishListCommonRepositoryImpl;
import com.tokopedia.wishlist.common.data.source.WishListCommonDataSource;
import com.tokopedia.wishlist.common.data.source.cloud.WishListCommonCloudDataSource;
import com.tokopedia.wishlist.common.data.source.cloud.api.WishListCommonApi;
import com.tokopedia.wishlist.common.data.source.cloud.mapper.WishListProductListMapper;
import com.tokopedia.wishlist.common.domain.interactor.GetWishListUseCase;
import com.tokopedia.wishlist.common.domain.repository.WishListCommonRepository;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

@ShopProductScope
@Module
public class ShopProductModule {

    @Provides
    public GMAuthInterceptor provideGMAuthInterceptor(@ApplicationContext Context context,
                                                      AbstractionRouter abstractionRouter,
                                                      UserSession userSession) {
        return new GMAuthInterceptor(context, abstractionRouter, userSession);
    }

    @ShopProductGMFeaturedQualifier
    @Provides
    public OkHttpClient provideGMOkHttpClient(GMAuthInterceptor gmAuthInterceptor,
                                            @ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                            HeaderErrorResponseInterceptor errorResponseInterceptor,
                                            CacheApiInterceptor cacheApiInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(cacheApiInterceptor)
                .addInterceptor(gmAuthInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @ShopProductGMFeaturedQualifier
    @ShopProductScope
    @Provides
    public Retrofit provideGMRetrofit(@ShopProductGMFeaturedQualifier OkHttpClient okHttpClient,
                                    Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(GMCommonUrl.BASE_URL).client(okHttpClient).build();
    }

    @ShopProductScope
    @Provides
    public GMCommonApi provideGMCommonApi(@ShopProductGMFeaturedQualifier Retrofit retrofit) {
        return retrofit.create(GMCommonApi.class);
    }

    @ShopProductScope
    @Provides
    public GMCommonCloudDataSource provideGMCommonCloudDataSource(GMCommonApi gmCommonApi) {
        return new GMCommonCloudDataSource(gmCommonApi);
    }

    @ShopProductScope
    @Provides
    public GMCommonDataSource provideGMCommonDataSource(GMCommonCloudDataSource gmCommonCloudDataSource) {
        return new GMCommonDataSource(gmCommonCloudDataSource);
    }

    @ShopProductScope
    @Provides
    public GMCommonRepository provideGMCommonRepository(GMCommonDataSource gmCommonDataSource) {
        return new GMCommonRepositoryImpl(gmCommonDataSource);
    }

    @ShopProductScope
    @Provides
    public GetFeatureProductListUseCase provideGetFeatureProductListUseCase(GMCommonRepository gmCommonRepository) {
        return new GetFeatureProductListUseCase(gmCommonRepository);
    }

    // WishList
    @Provides
    public WishListAuthInterceptor provideWishListAuthInterceptor(@ApplicationContext Context context,
                                                                  AbstractionRouter abstractionRouter,
                                                                  UserSession userSession) {
        return new WishListAuthInterceptor(context, abstractionRouter, userSession);
    }

    @ShopProductWishListFeaturedQualifier
    @Provides
    public OkHttpClient provideWishListOkHttpClient(WishListAuthInterceptor wishListAuthInterceptor,
                                            @ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                            HeaderErrorResponseInterceptor errorResponseInterceptor,
                                            CacheApiInterceptor cacheApiInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(cacheApiInterceptor)
                .addInterceptor(wishListAuthInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @ShopProductWishListFeaturedQualifier
    @ShopProductScope
    @Provides
    public Retrofit provideWishListRetrofit(@ShopProductWishListFeaturedQualifier OkHttpClient okHttpClient,
                                            Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(WishListCommonUrl.BASE_URL).client(okHttpClient).build();
    }

    @ShopProductScope
    @Provides
    public WishListCommonApi provideWishListCommonApi(@ShopProductWishListFeaturedQualifier Retrofit retrofit) {
        return retrofit.create(WishListCommonApi.class);
    }

    @ShopProductScope
    @Provides
    public WishListProductListMapper provideWishListProductListMapper() {
        return new WishListProductListMapper();
    }

    @ShopProductScope
    @Provides
    public WishListCommonCloudDataSource provideWishListCommonCloudDataSource(
            WishListCommonApi wishListCommonApi,
            WishListProductListMapper wishListProductListMapper) {
        return new WishListCommonCloudDataSource(wishListCommonApi, wishListProductListMapper);
    }

    @ShopProductScope
    @Provides
    public WishListCommonDataSource provideWishListCommonDataSource(WishListCommonCloudDataSource wishListCommonCloudDataSource) {
        return new WishListCommonDataSource(wishListCommonCloudDataSource);
    }

    @ShopProductScope
    @Provides
    public WishListCommonRepository provideWishListCommonRepository(WishListCommonDataSource wishListCommonDataSource) {
        return new WishListCommonRepositoryImpl(wishListCommonDataSource);
    }

    @ShopProductScope
    @Provides
    public GetWishListUseCase provideGetWishListUseCase(WishListCommonRepository wishListCommonRepository) {
        return new GetWishListUseCase(wishListCommonRepository);
    }

    // Filter
    @ShopProductScope
    @Provides
    public ShopProductRepository provideShopProductRepository(
            ShopProductCloudDataSource shopProductDataSource,
            ShopFilterCloudDataSource shopFilterCloudDataSource) {
        return new ShopProductRepositoryImpl(shopProductDataSource, shopFilterCloudDataSource);
    }

    @ShopProductScope
    @Provides
    public ShopProductMapper provideShopProductMapper() {
        return new ShopProductMapper();
    }
}