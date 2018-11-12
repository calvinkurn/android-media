package com.tokopedia.discovery.newdiscovery.di.module.net;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.network.interceptor.DebugInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.cacheapi.util.CacheApiResponseValidator;
import com.tokopedia.discovery.newdiscovery.di.qualifier.NoAuth;
import com.tokopedia.discovery.newdiscovery.di.scope.DiscoveryScope;
import com.tokopedia.network.interceptor.TkpdBaseInterceptor;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module(includes = DiscoveryInterceptorModule.class)
public class DiscoveryOkHttpClientModule {
    @DiscoveryScope
    @NoAuth
    @Provides
    public OkHttpClient provideOkHttpClientNoAuth(OkHttpRetryPolicy okHttpRetryPolicy,
                                                  ChuckInterceptor chuckInterceptor,
                                                  DebugInterceptor debugInterceptor,
                                                  CacheApiInterceptor cacheApiInterceptor,
                                                  TkpdBaseInterceptor tkpdBaseInterceptor) {

        cacheApiInterceptor.setResponseValidator(new CacheApiResponseValidator());
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .addInterceptor(tkpdBaseInterceptor)
                .addInterceptor(cacheApiInterceptor)
                .readTimeout(okHttpRetryPolicy.readTimeout, TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout, TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout, TimeUnit.SECONDS);

        if (GlobalConfig.isAllowDebuggingTools()) {
            clientBuilder.addInterceptor(debugInterceptor);
            clientBuilder.addInterceptor(chuckInterceptor);
        }
        return clientBuilder.build();
    }

    @DiscoveryScope
    @Provides
    OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy();
    }
}
