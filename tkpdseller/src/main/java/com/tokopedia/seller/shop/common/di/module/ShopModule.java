package com.tokopedia.seller.shop.common.di.module;

import com.tokopedia.core.base.di.scope.ApplicationScope;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.interceptors.BearerInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdErrorResponseInterceptor;
import com.tokopedia.seller.shop.common.exception.model.ShopErrorResponse;
import com.tokopedia.seller.shop.common.di.ShopQualifier;
import com.tokopedia.seller.shop.common.di.ShopScope;
import com.tokopedia.seller.shop.open.data.source.cloud.api.TomeApi;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by zulfikarrahman on 10/20/17.
 */

@ShopScope
@Module
public class ShopModule {
    @ShopQualifier
    @ShopScope
    @Provides
    public TomeApi provideTomeApi(@ShopQualifier Retrofit retrofit) {
        return retrofit.create(TomeApi.class);
    }

    @ShopQualifier
    @ShopScope
    @Provides
    public Retrofit provideRetrofit(@ShopQualifier OkHttpClient okHttpClient,
                                    Retrofit.Builder retrofitBuilder){
        return retrofitBuilder.baseUrl(TkpdBaseURL.TOME_DOMAIN).client(okHttpClient).build();
    }

    @ShopQualifier
    @ShopScope
    @Provides
    public OkHttpClient provideOkHttpClientTomeBearerAuth(OkHttpClient.Builder okHttpClientBuilder,
                                                          HttpLoggingInterceptor httpLoggingInterceptor,
                                                          BearerInterceptor bearerInterceptor,
                                                          @ShopQualifier TkpdErrorResponseInterceptor tkpdErrorResponseInterceptor
                                                          ) {
        return okHttpClientBuilder
                .addInterceptor(bearerInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(tkpdErrorResponseInterceptor)
                .build();
    }

    @ShopQualifier
    @ShopScope
    @Provides
    public TkpdErrorResponseInterceptor provideTkpdErrorResponseInterceptor() {
        return new TkpdErrorResponseInterceptor(ShopErrorResponse.class);
    }
}

