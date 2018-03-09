package com.tokopedia.profile.common.di;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
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
    @ProfileScope
    @Provides
    public OkHttpClient provideOkHttpClient(@ApplicationContext Context context,
            @ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                            TkpdAuthInterceptor tkpdAuthInterceptor) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES)
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
}
