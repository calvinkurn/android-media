package com.tokopedia.tkpdstream.common.di.module;

import com.tokopedia.abstraction.common.di.qualifier.OkHttpClientBuilderNonBaseQualifier;
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
    public OkHttpClient provideOkHttpClient(@OkHttpClientBuilderNonBaseQualifier OkHttpClient.Builder okHttpClientBuilder,
                                            HttpLoggingInterceptor httpLoggingInterceptor,
                                            TkpdAuthInterceptor tkpdAuthInterceptor,
                                            StreamErrorInterceptor streamErrorInterceptor) {
        return okHttpClientBuilder
                .addInterceptor(tkpdAuthInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(streamErrorInterceptor)
                .build();
    }

}
