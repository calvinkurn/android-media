package com.tokopedia.tkpdstream.common.di.module;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.AuthKeyQualifier;
import com.tokopedia.abstraction.common.di.qualifier.FreshAccessTokenQualifier;
import com.tokopedia.abstraction.common.di.qualifier.OkHttpClientBuilderNonBaseQualifier;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
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
    public StreamErrorInterceptor provideStreamErrorInterceptor() {
        return new StreamErrorInterceptor(StreamErrorResponse.class);
    }

    @StreamScope
    @Provides
    public OkHttpClient provideOkHttpClient(HttpLoggingInterceptor httpLoggingInterceptor,
                                            StreamErrorInterceptor streamErrorInterceptor,
                                            TkpdAuthInterceptor tkpdAuthInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(streamErrorInterceptor)
                .addInterceptor(tkpdAuthInterceptor)
                .build();
    }


}
