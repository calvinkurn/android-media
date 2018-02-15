package com.tokopedia.tkpdstream.common.di.module;

import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.tkpdstream.common.di.scope.StreamScope;
import com.tokopedia.tkpdstream.common.network.StreamErrorInterceptor;
import com.tokopedia.tkpdstream.common.network.StreamErrorResponse;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author by nisie on 2/1/18.
 */

@StreamScope
@Module
public class NetModule {

    @StreamScope
    @Provides
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logging;
    }

    @StreamScope
    @Provides
    public StreamErrorInterceptor provideStreamErrorInterceptor() {
        return new StreamErrorInterceptor(StreamErrorResponse.class);
    }

    @StreamScope
    @Provides
    public OkHttpClient provideOkHttpClient(OkHttpClient.Builder okHttpClientBuilder,
                                            HttpLoggingInterceptor httpLoggingInterceptor,
                                            StreamErrorInterceptor streamErrorInterceptor,
                                            TkpdAuthInterceptor tkpdAuthInterceptor) {
        return okHttpClientBuilder
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(streamErrorInterceptor)
                .addInterceptor(tkpdAuthInterceptor)
                .build();
    }


}
