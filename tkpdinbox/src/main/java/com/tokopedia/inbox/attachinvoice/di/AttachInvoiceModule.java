package com.tokopedia.inbox.attachinvoice.di;

import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.accounts.UploadImageService;
import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.core.network.apiservices.kunyit.KunyitService;
import com.tokopedia.core.network.apiservices.upload.GenerateHostActService;
import com.tokopedia.core.network.di.qualifier.InboxQualifier;
import com.tokopedia.core.network.retrofit.interceptors.DigitalHmacAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.inboxchat.data.factory.MessageFactory;
import com.tokopedia.inbox.inboxchat.data.factory.ReplyFactory;
import com.tokopedia.inbox.inboxchat.data.factory.SearchFactory;
import com.tokopedia.inbox.inboxchat.data.factory.template.TemplateChatFactory;
import com.tokopedia.inbox.inboxchat.data.mapper.DeleteMessageMapper;
import com.tokopedia.inbox.inboxchat.data.mapper.GetMessageMapper;
import com.tokopedia.inbox.inboxchat.data.mapper.GetReplyMapper;
import com.tokopedia.inbox.inboxchat.data.mapper.ReplyMessageMapper;
import com.tokopedia.inbox.inboxchat.data.mapper.SearchChatMapper;
import com.tokopedia.inbox.inboxchat.data.mapper.SendMessageMapper;
import com.tokopedia.inbox.inboxchat.data.mapper.template.TemplateChatMapper;
import com.tokopedia.inbox.inboxchat.data.network.ChatBotApi;
import com.tokopedia.inbox.inboxchat.data.network.ChatBotUrl;
import com.tokopedia.inbox.inboxchat.data.repository.MessageRepository;
import com.tokopedia.inbox.inboxchat.data.repository.MessageRepositoryImpl;
import com.tokopedia.inbox.inboxchat.data.repository.ReplyRepository;
import com.tokopedia.inbox.inboxchat.data.repository.ReplyRepositoryImpl;
import com.tokopedia.inbox.inboxchat.data.repository.SearchRepository;
import com.tokopedia.inbox.inboxchat.data.repository.SearchRepositoryImpl;
import com.tokopedia.inbox.inboxchat.data.repository.SendMessageSource;
import com.tokopedia.inbox.inboxchat.data.repository.template.TemplateRepository;
import com.tokopedia.inbox.inboxchat.data.repository.template.TemplateRepositoryImpl;
import com.tokopedia.inbox.inboxchat.domain.usecase.AttachImageUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.DeleteMessageListUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.GetMessageListUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.GetReplyListUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.ReplyMessageUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.SearchMessageUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.SendMessageUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.template.GetTemplateUseCase;
import com.tokopedia.inbox.inboxchat.uploadimage.data.factory.ImageUploadFactory;
import com.tokopedia.inbox.inboxchat.uploadimage.data.mapper.GenerateHostMapper;
import com.tokopedia.inbox.inboxchat.uploadimage.data.mapper.UploadImageMapper;
import com.tokopedia.inbox.inboxchat.uploadimage.data.repository.ImageUploadRepository;
import com.tokopedia.inbox.inboxchat.uploadimage.data.repository.ImageUploadRepositoryImpl;
import com.tokopedia.inbox.inboxchat.uploadimage.domain.interactor.GenerateHostUseCase;
import com.tokopedia.inbox.inboxchat.uploadimage.domain.interactor.UploadImageUseCase;

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
    OkHttpClient provideOkHttpClient(@InboxQualifier OkHttpRetryPolicy retryPolicy){
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
                                 Retrofit.Builder retrofitBuilder){
        return retrofitBuilder.baseUrl(ChatBotUrl.BASE_URL)
                .client(okHttpClient)
                .build();
    }

    @AttachInvoiceScope
    @Provides
    ChatBotApi provideChatRatingApi(@InboxQualifier Retrofit retrofit){
        return retrofit.create(ChatBotApi.class);
    }
}
