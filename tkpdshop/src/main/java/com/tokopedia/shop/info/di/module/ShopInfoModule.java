package com.tokopedia.shop.info.di.module;

import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.shop.common.constant.ShopUrl;
import com.tokopedia.shop.common.data.interceptor.ShopAuthInterceptor;
import com.tokopedia.shop.info.di.ShopInfoQualifier;
import com.tokopedia.shop.info.di.scope.ShopInfoScope;
import com.tokopedia.shop.info.data.source.cloud.api.ShopApi;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

@ShopInfoScope
@Module
public class ShopInfoModule {

    @ShopInfoScope
    @Provides
    ShopApi provideShopApi(@ShopInfoQualifier Retrofit retrofit){
        return retrofit.create(ShopApi.class);
    }

    @ShopInfoQualifier
    @ShopInfoScope
    @Provides
    public Retrofit provideRetrofit(@ShopInfoQualifier OkHttpClient okHttpClient,
                                    Retrofit.Builder retrofitBuilder){
        return retrofitBuilder.baseUrl(ShopUrl.BASE_URL).client(okHttpClient).build();
    }

    @ShopInfoQualifier
    @Provides
    public OkHttpClient provideOkHttpClient(ShopAuthInterceptor ShopAuthInterceptor,
                                            @ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                            HeaderErrorResponseInterceptor errorResponseInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(ShopAuthInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

}

