package com.tokopedia.home.common;

import com.google.gson.Gson;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;
import com.tokopedia.core.network.retrofit.coverters.TkpdResponseConverter;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author by errysuprayogi on 11/28/17.
 */

@Module
public class ApiModule {

    @Provides
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logging;
    }

    @Provides
    HomeDataApi homeDataApi(@HomeGraphQLQualifier Retrofit retrofit){
        return retrofit.create(HomeDataApi.class);
    }

    @Provides
    public OkHttpClient provideOkHttpClient(OkHttpClient.Builder okHttpClientBuilder,
                                            HttpLoggingInterceptor httpLoggingInterceptor,
                                            HomeAuthInterceptor authInterceptor) {
        return okHttpClientBuilder
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(authInterceptor)
                .addInterceptor(new FingerprintInterceptor())
                .build();
    }

    @HomeGraphQLQualifier
    @Provides
    public Retrofit provideHomeGraphQlRetrofit(OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(TkpdBaseURL.HOME_DATA_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(new TkpdResponseConverter())
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

    }

}
