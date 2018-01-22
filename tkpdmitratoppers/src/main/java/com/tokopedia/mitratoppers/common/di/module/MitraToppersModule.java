package com.tokopedia.mitratoppers.common.di.module;

import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.mitratoppers.common.constant.MitraToppersBaseURL;
import com.tokopedia.mitratoppers.common.data.source.cloud.api.MitraToppersApi;
import com.tokopedia.mitratoppers.common.di.MitraToppersQualifier;
import com.tokopedia.mitratoppers.common.di.scope.MitraToppersScope;
import com.tokopedia.mitratoppers.common.exception.model.HeaderErrorResponse;
import com.tokopedia.mitratoppers.common.interceptor.HeaderErrorResponseInterceptor;

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

    @MitraToppersQualifier
    @Provides
    public OkHttpClient provideOkHttpClient(TkpdAuthInterceptor tkpdAuthInterceptor,
                                            @ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                            @MitraToppersQualifier HeaderErrorResponseInterceptor errorResponseInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(tkpdAuthInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @MitraToppersQualifier
    @Provides
    public HeaderErrorResponseInterceptor provideTkpdErrorResponseInterceptor() {
        return new HeaderErrorResponseInterceptor(HeaderErrorResponse.class);
    }

}

