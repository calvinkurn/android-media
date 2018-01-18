package com.tokopedia.mitratoppers.common.di.module;

import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.mitratoppers.common.constant.MitraToppersBaseURL;
import com.tokopedia.mitratoppers.common.data.source.cloud.api.MitraToppersApi;
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

    @MitraToppersQualifier
    @Provides
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (GlobalConfig.isAllowDebuggingTools()) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        return logging;
    }

    @MitraToppersQualifier
    @Provides
    public OkHttpClient provideOkHttpClient(OkHttpClient.Builder okHttpClientBuilder,
                                            TkpdAuthInterceptor tkpdAuthInterceptor,
                                            @MitraToppersQualifier HttpLoggingInterceptor httpLoggingInterceptor) {
        return okHttpClientBuilder
                .addInterceptor(tkpdAuthInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

}

