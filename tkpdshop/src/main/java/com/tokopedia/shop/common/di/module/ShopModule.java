package com.tokopedia.shop.common.di.module;

import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.shop.common.constant.ShopUrl;
import com.tokopedia.shop.common.data.interceptor.ShopAuthInterceptor;
import com.tokopedia.shop.common.di.ShopQualifier;
import com.tokopedia.shop.common.di.scope.ShopScope;
import com.tokopedia.shop.common.data.source.cloud.api.WS4ShopApi;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 4/13/17.
 */
@ShopScope
@Module
public class ShopModule {

    @ShopScope
    @Provides
    WS4ShopApi provideShopApi(@ShopQualifier Retrofit retrofit){
        return retrofit.create(WS4ShopApi.class);
    }

    @ShopQualifier
    @ShopScope
    @Provides
    public Retrofit provideRetrofit(@ShopQualifier OkHttpClient okHttpClient,
                                    Retrofit.Builder retrofitBuilder){
        return retrofitBuilder.baseUrl(ShopUrl.BASE_URL).client(okHttpClient).build();
    }

    @ShopQualifier
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