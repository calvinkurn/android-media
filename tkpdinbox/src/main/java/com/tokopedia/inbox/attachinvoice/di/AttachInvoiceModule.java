package com.tokopedia.inbox.attachinvoice.di;

import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.core.network.di.qualifier.InboxQualifier;
import com.tokopedia.core.network.retrofit.interceptors.DigitalHmacAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.inbox.inboxchat.data.network.ChatBotApi;
import com.tokopedia.inbox.inboxchat.data.network.ChatBotUrl;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by Hendri on 05/04/18.
 */
@Module
public class AttachInvoiceModule {
    private static final int NET_READ_TIMEOUT = 60;
    private static final int NET_WRITE_TIMEOUT = 60;
    private static final int NET_CONNECT_TIMEOUT = 60;
    private static final int NET_RETRY = 1;

    @AttachInvoiceScope
    @Provides
    OkHttpClient provideOkHttpClient(@InboxQualifier OkHttpRetryPolicy retryPolicy) {
        return new OkHttpClient.Builder()
                .addInterceptor(new FingerprintInterceptor())
                .addInterceptor(new CacheApiInterceptor())
                .addInterceptor(new DigitalHmacAuthInterceptor(AuthUtil.KEY.KEY_WSV4))
                .connectTimeout(retryPolicy.connectTimeout, TimeUnit.SECONDS)
                .readTimeout(retryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(retryPolicy.writeTimeout, TimeUnit.SECONDS)
                .build();
    }

    @AttachInvoiceScope
    @InboxQualifier
    @Provides
    OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return new OkHttpRetryPolicy(NET_READ_TIMEOUT,
                NET_WRITE_TIMEOUT,
                NET_CONNECT_TIMEOUT,
                NET_RETRY);
    }

    @AttachInvoiceScope
    @InboxQualifier
    @Provides
    Retrofit provideChatRetrofit(OkHttpClient okHttpClient,
                                 Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(ChatBotUrl.BASE_URL)
                .client(okHttpClient)
                .build();
    }

    @AttachInvoiceScope
    @Provides
    ChatBotApi provideChatRatingApi(@InboxQualifier Retrofit retrofit) {
        return retrofit.create(ChatBotApi.class);
    }
}
