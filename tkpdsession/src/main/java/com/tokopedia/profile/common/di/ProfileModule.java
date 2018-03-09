package com.tokopedia.profile.common.di;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.core.DeveloperOptions;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;
import com.tokopedia.profile.data.network.ProfileApi;
import com.tokopedia.profile.data.network.ProfileUrl;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author by alvinatin on 20/02/18.
 */

@ProfileScope
@Module
public class ProfileModule {
    private static final int NET_READ_TIMEOUT = 60;
    private static final int NET_WRITE_TIMEOUT = 60;
    private static final int NET_CONNECT_TIMEOUT = 60;
    private static final int NET_RETRY = 1;

    @ProfileScope
    @Provides
    public OkHttpClient provideOkHttpClient(@ApplicationScope HttpLoggingInterceptor
                                                    httpLoggingInterceptor,
                                            @ApplicationContext Context context,
                                            TkpdAuthInterceptor tkpdAuthInterceptor,
                                            OkHttpRetryPolicy retryPolicy) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(retryPolicy.connectTimeout, TimeUnit.SECONDS)
                .readTimeout(retryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(retryPolicy.writeTimeout, TimeUnit.SECONDS)
                .addInterceptor(tkpdAuthInterceptor)
                .addInterceptor(new FingerprintInterceptor());

        if (GlobalConfig.isAllowDebuggingTools()) {
            LocalCacheHandler localCacheHandler =
                    new LocalCacheHandler(context, DeveloperOptions.CHUCK_ENABLED);
            clientBuilder.addInterceptor(
                    new ChuckInterceptor(context).showNotification(
                            localCacheHandler.getBoolean(
                                    DeveloperOptions.IS_CHUCK_ENABLED,
                                    false
                            )
                    )
            );
            clientBuilder.addInterceptor(httpLoggingInterceptor);
        }

        return clientBuilder.build();
    }

    @ProfileScope
    @Provides
    @ProfileQualifier
    public Retrofit provideProfileRetrofit(OkHttpClient okHttpClient,
                                           Retrofit.Builder retrofitBuilder){
        return retrofitBuilder.baseUrl(ProfileUrl.BASE_URL)
                .client(okHttpClient)
                .build();
    }

    @ProfileScope
    @Provides
    public ProfileApi provideProfileApi(@ProfileQualifier Retrofit retrofit){
        return retrofit.create(ProfileApi.class);
    }

    @ProfileScope
    @ProfileQualifier
    @Provides
    public OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return new OkHttpRetryPolicy(NET_READ_TIMEOUT,
                NET_WRITE_TIMEOUT,
                NET_CONNECT_TIMEOUT,
                NET_RETRY);
    }
}
