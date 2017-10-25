package com.tokopedia.topads.common.util;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.core.cache.interceptor.ApiCacheInterceptor;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.core.TkpdOkHttpBuilder;
import com.tokopedia.core.network.retrofit.interceptors.DebugInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.StandardizedInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdErrorResponseInterceptor;
import com.tokopedia.core.network.retrofit.response.TopAdsResponseError;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.topads.dashboard.di.scope.TopAdsQualifier;

import okhttp3.OkHttpClient;

/**
 * Created by normansyahputa on 10/23/17.
 */

public class TopAdsOkHttpFactory extends OkHttpFactory {

    public static TopAdsOkHttpFactory create(){
        return new TopAdsOkHttpFactory();
    }

    public OkHttpClient buildDaggerClientBearerTopAdsAuth(FingerprintInterceptor fingerprintInterceptor,
                                                          TopAdsAuthInterceptor topAdsAuthInterceptor,
                                                          OkHttpRetryPolicy okHttpRetryPolicy,
                                                          @TopAdsQualifier TkpdErrorResponseInterceptor errorResponseInterceptor,
                                                          ApiCacheInterceptor apiCacheInterceptor) {

        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(apiCacheInterceptor)
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(topAdsAuthInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .setOkHttpRetryPolicy(okHttpRetryPolicy)
                .addDebugInterceptor()
                .build();
    }

    public OkHttpClient buildClientTopAdsAuth(String authorizationString) {
        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(new ApiCacheInterceptor())
                .addInterceptor(new FingerprintInterceptor())
                .addInterceptor(new TopAdsAuthInterceptor(authorizationString))
                .addInterceptor(new TkpdErrorResponseInterceptor(TopAdsResponseError.class))
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .addDebugInterceptor()
                .build();
    }

    public TopAdsOkHttpFactory addOkHttpRetryPolicy(OkHttpRetryPolicy okHttpRetryPolicy) {
        this.okHttpRetryPolicy = okHttpRetryPolicy;
        return this;
    }
}
