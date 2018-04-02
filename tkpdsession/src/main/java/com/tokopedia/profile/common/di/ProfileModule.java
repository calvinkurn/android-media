package com.tokopedia.profile.common.di;

import android.content.Context;

import com.tokopedia.SessionRouter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;
import com.tokopedia.profile.data.network.ProfileApi;
import com.tokopedia.profile.data.network.ProfileUrl;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
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
                                            TkpdAuthInterceptor tkpdAuthInterceptor,
                                            @ProfileQualifier OkHttpRetryPolicy retryPolicy,
                                            @ProfileChuckQualifier Interceptor chuckInterceptor) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(retryPolicy.connectTimeout, TimeUnit.SECONDS)
                .readTimeout(retryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(retryPolicy.writeTimeout, TimeUnit.SECONDS)
                .addInterceptor(tkpdAuthInterceptor)
                .addInterceptor(new FingerprintInterceptor());

        if (GlobalConfig.isAllowDebuggingTools()) {
            clientBuilder.addInterceptor(httpLoggingInterceptor);
            clientBuilder.addInterceptor(chuckInterceptor);
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

    @ProfileScope
    @Provides
    @ProfileChuckQualifier
    public Interceptor provideChuckInterceptory(@ApplicationContext Context context) {
        if (context instanceof SessionRouter) {
            return ((SessionRouter) context).getChuckInterceptor();
        }
        throw new RuntimeException("App should implement " + SessionRouter.class.getSimpleName());
    }
}
