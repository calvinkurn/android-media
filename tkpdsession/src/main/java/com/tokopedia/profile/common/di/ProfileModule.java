package com.tokopedia.profile.common.di;

import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;
import com.tokopedia.profile.data.network.ProfileApi;
import com.tokopedia.profile.data.network.ProfileUrl;

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
    public OkHttpClient provideOkHttpClient(@ApplicationScope HttpLoggingInterceptor
                                                        httpLoggingInterceptor,
                                            TkpdAuthInterceptor tkpdAuthInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(tkpdAuthInterceptor)
                .addInterceptor(new FingerprintInterceptor())
                .build();
    }

    @ProfileScope
    @Provides
    @ProfileQualifier
    public Retrofit provideProfileRetrofit(OkHttpClient okHttpClient,
                                           Retrofit.Builder retrofitBuilder){
        return retrofitBuilder.baseUrl(ProfileUrl.BASE_URL).client(okHttpClient).build();
    }

    @ProfileScope
    @Provides
    public ProfileApi provideProfileApi(@ProfileQualifier Retrofit retrofit){
        return retrofit.create(ProfileApi.class);
    }
}
