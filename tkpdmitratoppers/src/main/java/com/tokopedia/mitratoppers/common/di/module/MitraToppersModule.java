package com.tokopedia.mitratoppers.common.di.module;

import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.mitratoppers.common.constant.MitraToppersBaseURL;
import com.tokopedia.mitratoppers.preapprove.data.source.cloud.api.MitraToppersApi;
import com.tokopedia.mitratoppers.common.di.MitraToppersQualifier;
import com.tokopedia.mitratoppers.common.di.scope.MitraToppersScope;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

@MitraToppersScope
@Module
public class MitraToppersModule {

    @MitraToppersScope
    @Provides
    MitraToppersApi provideMitraToppersApi(@MitraToppersQualifier Retrofit retrofit){
        return retrofit.create(MitraToppersApi.class);
    }

    @MitraToppersQualifier
    @MitraToppersScope
    @Provides
    public Retrofit provideRetrofit(@MitraToppersQualifier OkHttpClient okHttpClient,
                                    Retrofit.Builder retrofitBuilder){
        return retrofitBuilder.baseUrl(MitraToppersBaseURL.WEB_DOMAIN + MitraToppersBaseURL.PATH_MITRA_TOPPERS)
                .client(okHttpClient).build();
    }

    //TODO add api cache interceptor to cache the response
    @MitraToppersQualifier
    @Provides
    public OkHttpClient provideOkHttpClient(TkpdAuthInterceptor tkpdAuthInterceptor,
                                            @ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                            HeaderErrorResponseInterceptor errorResponseInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(tkpdAuthInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

}

