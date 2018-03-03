package com.tokopedia.shop.favourite.di.module;

import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.shop.common.constant.ShopUrl;
import com.tokopedia.shop.common.data.source.cloud.api.ShopWS4Api;
import com.tokopedia.shop.favourite.data.repository.ShopFavouriteRepositoryImpl;
import com.tokopedia.shop.favourite.data.source.ShopFavouriteDataSource;
import com.tokopedia.shop.favourite.data.source.cloud.ShopFavouriteCloudDataSource;
import com.tokopedia.shop.favourite.di.scope.ShopFavouriteScope;
import com.tokopedia.shop.favourite.domain.repository.ShopFavouriteRepository;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

@ShopFavouriteScope
@Module
public class ShopFavouriteModule {

    @ShopFavouriteScope
    @Provides
    public OkHttpClient provideOkHttpClient(TkpdAuthInterceptor tkpdAuthInterceptor,
                                            @ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                            CacheApiInterceptor cacheApiInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(cacheApiInterceptor)
                .addInterceptor(tkpdAuthInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @ShopFavouriteScope
    @Provides
    public Retrofit provideRetrofit(OkHttpClient okHttpClient,
                                    Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(ShopUrl.BASE_WS_URL).client(okHttpClient).build();
    }

    @ShopFavouriteScope
    @Provides
    public ShopWS4Api provideShopWs4Api(Retrofit retrofit) {
        return retrofit.create(ShopWS4Api.class);
    }

    @ShopFavouriteScope
    @Provides
    public ShopFavouriteCloudDataSource provideShopFavouriteCloudDataSource(ShopWS4Api shopWS4Api) {
        return new ShopFavouriteCloudDataSource(shopWS4Api);
    }

    @ShopFavouriteScope
    @Provides
    public ShopFavouriteDataSource provideShopFavouriteDataSource(ShopFavouriteCloudDataSource ShopFavouriteCloudDataSource) {
        return new ShopFavouriteDataSource(ShopFavouriteCloudDataSource);
    }

    @ShopFavouriteScope
    @Provides
    public ShopFavouriteRepository provideShopFavouriteRepository(ShopFavouriteDataSource ShopFavouriteDataSource) {
        return new ShopFavouriteRepositoryImpl(ShopFavouriteDataSource);
    }
}

