package com.tokopedia.inbox.inboxchat.di;


import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.accounts.UploadImageService;
import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.core.network.apiservices.kunyit.KunyitService;
import com.tokopedia.core.network.apiservices.rescenter.apis.ResCenterActApi;
import com.tokopedia.core.network.apiservices.upload.GenerateHostActService;
import com.tokopedia.core.network.apiservices.upload.apis.GeneratedHostActApi;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
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
import com.tokopedia.inbox.inboxchat.data.repository.AttachImageRepository;
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
import com.tokopedia.inbox.inboxchat.domain.usecase.template.GetTemplateUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.ReplyMessageUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.SearchMessageUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.SendMessageUseCase;
import com.tokopedia.inbox.inboxchat.uploadimage.data.factory.ImageUploadFactory;
import com.tokopedia.inbox.inboxchat.uploadimage.data.mapper.GenerateHostMapper;
import com.tokopedia.inbox.inboxchat.uploadimage.data.mapper.UploadImageMapper;
import com.tokopedia.inbox.inboxchat.uploadimage.data.repository.ImageUploadRepository;
import com.tokopedia.inbox.inboxchat.uploadimage.data.repository.ImageUploadRepositoryImpl;
import com.tokopedia.inbox.inboxchat.uploadimage.domain.interactor.GenerateHostUseCase;
import com.tokopedia.inbox.inboxchat.uploadimage.domain.interactor.UploadImageUseCase;
import com.tokopedia.inbox.rescenter.detailv2.di.scope.ResolutionDetailScope;
import com.tokopedia.inbox.rescenter.detailv2.domain.UploadImageRepository;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by stevenfredian on 9/14/17.
 */

@Module
public class InboxChatModule {

    @InboxChatScope
    @Provides
    MessageFactory provideMessageFactory(
            ChatService chatService,
            GetMessageMapper getMessageMapper,
            DeleteMessageMapper deleteMessageMapper) {
        return new MessageFactory(chatService, getMessageMapper, deleteMessageMapper);
    }

    @InboxChatScope
    @Provides
    ReplyFactory provideReplyFactory(
            ChatService chatService,
            GetReplyMapper getReplyMapper,
            ReplyMessageMapper replyMessageMapper) {
        return new ReplyFactory(chatService, getReplyMapper, replyMessageMapper);
    }

    @InboxChatScope
    @Provides
    SearchFactory provideSearchFactory(
            ChatService chatService,
            SearchChatMapper searchChatMapper) {
        return new SearchFactory(chatService, searchChatMapper);
    }


    @InboxChatScope
    @Provides
    TemplateChatFactory provideTemplateFactory(
            ChatService chatService,
            TemplateChatMapper templateChatMapper) {
        return new TemplateChatFactory(templateChatMapper, chatService);
    }


    @InboxChatScope
    @Provides
    GetReplyMapper provideGetReplyMapper() {
        return new GetReplyMapper();
    }

    @InboxChatScope
    @Provides
    GetMessageMapper provideGetMessageMapper() {
        return new GetMessageMapper();
    }

    @InboxChatScope
    @Provides
    ReplyMessageMapper provideReplyMessageMapper() {
        return new ReplyMessageMapper();
    }

    @InboxChatScope
    @Provides
    DeleteMessageMapper provideDeleteMessageMapper() {
        return new DeleteMessageMapper();
    }


    @InboxChatScope
    @Provides
    SearchChatMapper provideSearchChatMapper() {
        return new SearchChatMapper();
    }


    @InboxChatScope
    @Provides
    TemplateChatMapper provideTemplateChatMapper() {
        return new TemplateChatMapper();
    }


    @InboxChatScope
    @Provides
    MessageRepository provideMessageRepository(MessageFactory messageFactory,
                                               SendMessageSource sendMessageSource) {
        return new MessageRepositoryImpl(messageFactory, sendMessageSource);
    }

    @InboxChatScope
    @Provides
    ReplyRepository provideReplyRepository(ReplyFactory replyFactory) {
        return new ReplyRepositoryImpl(replyFactory);
    }

    @InboxChatScope
    @Provides
    SearchRepository provideSearchRepository(SearchFactory searchFactory) {
        return new SearchRepositoryImpl(searchFactory);
    }


    @InboxChatScope
    @Provides
    TemplateRepository provideTemplateRepository(TemplateChatFactory templateChatFactory) {
        return new TemplateRepositoryImpl(templateChatFactory);
    }

    @InboxChatScope
    @Provides
    GetMessageListUseCase provideGetMessageListUseCase(ThreadExecutor threadExecutor,
                                                       PostExecutionThread postExecutor,
                                                       MessageRepository messageRepository) {
        return new GetMessageListUseCase(threadExecutor, postExecutor, messageRepository);
    }


    @InboxChatScope
    @Provides
    GetReplyListUseCase provideGetReplyListUseCase(ThreadExecutor threadExecutor,
                                                   PostExecutionThread postExecutor,
                                                   ReplyRepository replyRepository) {
        return new GetReplyListUseCase(threadExecutor, postExecutor, replyRepository);
    }


    @InboxChatScope
    @Provides
    ReplyMessageUseCase provideReplyMessageUseCase(ThreadExecutor threadExecutor,
                                                   PostExecutionThread postExecutor,
                                                   ReplyRepository replyRepository) {
        return new ReplyMessageUseCase(threadExecutor, postExecutor, replyRepository);
    }

    @InboxChatScope
    @Provides
    SearchMessageUseCase provideSearchChatUseCase(ThreadExecutor threadExecutor,
                                                  PostExecutionThread postExecutor,
                                                  SearchRepository searchRepository) {
        return new SearchMessageUseCase(threadExecutor, postExecutor, searchRepository);
    }

    @InboxChatScope
    @Provides
    DeleteMessageListUseCase provideDeleteChatUseCase(ThreadExecutor threadExecutor,
                                                      PostExecutionThread postExecutor,
                                                      MessageRepository messageRepository) {
        return new DeleteMessageListUseCase(threadExecutor, postExecutor, messageRepository);
    }

    @InboxChatScope
    @Provides
    GetTemplateUseCase provideGetTemplateUseCase(ThreadExecutor threadExecutor,
                                                 PostExecutionThread postExecutor,
                                                 TemplateRepository templateRepository) {
        return new GetTemplateUseCase(threadExecutor, postExecutor, templateRepository);
    }


    @InboxChatScope
    @Provides
    ChatService provideChatService() {
        return new ChatService();
    }

    @InboxChatScope
    @Provides
    KunyitService provideKunyitService() {
        return new KunyitService();
    }

    @InboxChatScope
    @Provides
    SendMessageMapper provideSendMessageMapper() {
        return new SendMessageMapper();
    }


    @InboxChatScope
    @Provides
    SendMessageSource provideSendMessageSource(ChatService chatService,
                                               SendMessageMapper sendMessageMapper) {
        return new SendMessageSource(chatService, sendMessageMapper);
    }

    @InboxChatScope
    @Provides
    SendMessageUseCase provideSendMessageUseCase(ThreadExecutor threadExecutor,
                                                 PostExecutionThread postExecutor,
                                                 MessageRepository messageRepository) {
        return new SendMessageUseCase(
                threadExecutor,
                postExecutor,
                messageRepository
        );
    }

    @InboxChatScope
    @Provides
    AttachImageUseCase provideAttachImageUsecase(ThreadExecutor threadExecutor,
                                                 PostExecutionThread postExecutor,
                                                 GenerateHostUseCase generateHostUseCase,
                                                 UploadImageUseCase uploadImageUseCase,
                                                 ReplyMessageUseCase replyMessageUseCase) {
        return new AttachImageUseCase(threadExecutor, postExecutor, generateHostUseCase, uploadImageUseCase, replyMessageUseCase);
    }

    @InboxChatScope
    @Provides
    UploadImageUseCase
    provideUploadImageUseCase(ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread,
                              ImageUploadRepository imageUploadRepository) {
        return new UploadImageUseCase(
                threadExecutor,
                postExecutionThread,
                imageUploadRepository);
    }

    @InboxChatScope
    @Provides
    GenerateHostUseCase
    provideGenerateHostUseCase(ThreadExecutor threadExecutor,
                               PostExecutionThread postExecutionThread,
                               ImageUploadRepository imageUploadRepository) {
        return new GenerateHostUseCase(
                threadExecutor,
                postExecutionThread,
                imageUploadRepository);
    }

    @InboxChatScope
    @Provides
    ImageUploadRepository
    provideImageUploadRepository(ImageUploadFactory imageUploadFactory) {
        return new ImageUploadRepositoryImpl(imageUploadFactory);
    }

    @InboxChatScope
    @Provides
    ImageUploadFactory
    provideImageUploadFactory(GenerateHostActService generateHostActService,
                              UploadImageService uploadImageService,
                              GenerateHostMapper generateHostMapper,
                              UploadImageMapper uploadImageMapper) {
        return new ImageUploadFactory(generateHostActService,
                uploadImageService,
                generateHostMapper,
                uploadImageMapper);
    }

    @InboxChatScope
    @Provides
    GenerateHostActService
    provideGenerateHostActService() {
        return new GenerateHostActService();
    }

    @InboxChatScope
    @Provides
    UploadImageService
    provideUploadImageService() {
        return new UploadImageService();
    }

    @InboxChatScope
    @Provides
    GenerateHostMapper
    provideGenerateHostMapper() {
        return new GenerateHostMapper();
    }

    @InboxChatScope
    @Provides
    UploadImageMapper
    provideUploadImageMapper() {
        return new UploadImageMapper();
    }

}
