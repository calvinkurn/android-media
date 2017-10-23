package com.tokopedia.topads.common.di.module;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.cache.interceptor.ApiCacheInterceptor;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.topads.common.util.TopAdsOkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.retrofit.interceptors.DebugInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdErrorResponseInterceptor;
import com.tokopedia.topads.common.util.TopAdsAuthInterceptor;
import com.tokopedia.core.network.retrofit.response.TopAdsResponseError;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.topads.dashboard.di.scope.TopAdsQualifier;
import com.tokopedia.topads.dashboard.di.scope.TopAdsScope;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by normansyahputa on 10/20/17.
 */
@TopAdsScope
@Module
public class TopAdsNetworkModule {

    @TopAdsQualifier
    @TopAdsScope
    @Provides
    TkpdErrorResponseInterceptor provideTopAdsErrorResponseInterceptor() {
        return new TkpdErrorResponseInterceptor(TopAdsResponseError.class);
    }


    @TopAdsScope
    @Provides
    public TopAdsAuthInterceptor provideTopAdsAuthInterceptor(
            SessionHandler sessionHandler,
            @ApplicationContext Context context) {
        String oAuthString = "Bearer " + sessionHandler.getAccessToken(context);
        return new TopAdsAuthInterceptor(oAuthString);
    }

    @TopAdsScope
    @Provides
    public FingerprintInterceptor provideFingerprintInterceptor(){
        return new FingerprintInterceptor();
    }

    @TopAdsQualifier
    @TopAdsScope
    @Provides
    public OkHttpClient provideOkHttpClientTopAdsAuth(FingerprintInterceptor fingerprintInterceptor,
                                                      TopAdsAuthInterceptor topAdsAuthInterceptor,
                                                      OkHttpRetryPolicy okHttpRetryPolicy,
                                                      @TopAdsQualifier TkpdErrorResponseInterceptor errorResponseInterceptor,
                                                      ApiCacheInterceptor apiCacheInterceptor) {

        return TopAdsOkHttpFactory.create().buildDaggerClientBearerTopAdsAuth(fingerprintInterceptor,
                topAdsAuthInterceptor,
                okHttpRetryPolicy,
                errorResponseInterceptor,
                apiCacheInterceptor);
    }

    @TopAdsQualifier
    @TopAdsScope
    @Provides
    public Retrofit provideTopAdsRetrofit(@TopAdsQualifier OkHttpClient okHttpClient,
                                          Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TkpdBaseURL.TOPADS_DOMAIN).client(okHttpClient).build();

    }


    @TopAdsScope
    @Provides
    public ApiCacheInterceptor provideApiCacheInterceptor(){
        return new ApiCacheInterceptor();
    }
}
