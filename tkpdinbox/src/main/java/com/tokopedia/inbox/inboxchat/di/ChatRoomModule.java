package com.tokopedia.inbox.inboxchat.di;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.accounts.UploadImageService;
import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.core.network.apiservices.kunyit.KunyitService;
import com.tokopedia.core.network.apiservices.upload.GenerateHostActService;
import com.tokopedia.inbox.inboxchat.data.factory.MessageFactory;
import com.tokopedia.inbox.inboxchat.data.factory.ReplyFactory;
import com.tokopedia.inbox.inboxchat.data.factory.template.TemplateChatFactory;
import com.tokopedia.inbox.inboxchat.data.mapper.DeleteMessageMapper;
import com.tokopedia.inbox.inboxchat.data.mapper.GetMessageMapper;
import com.tokopedia.inbox.inboxchat.data.mapper.GetReplyMapper;
import com.tokopedia.inbox.inboxchat.data.mapper.ReplyMessageMapper;
import com.tokopedia.inbox.inboxchat.data.mapper.SendMessageMapper;
import com.tokopedia.inbox.inboxchat.data.mapper.template.TemplateChatMapper;
import com.tokopedia.inbox.inboxchat.data.repository.MessageRepository;
import com.tokopedia.inbox.inboxchat.data.repository.MessageRepositoryImpl;
import com.tokopedia.inbox.inboxchat.data.repository.ReplyRepository;
import com.tokopedia.inbox.inboxchat.data.repository.ReplyRepositoryImpl;
import com.tokopedia.inbox.inboxchat.data.repository.SendMessageSource;
import com.tokopedia.inbox.inboxchat.data.repository.template.TemplateRepository;
import com.tokopedia.inbox.inboxchat.data.repository.template.TemplateRepositoryImpl;
import com.tokopedia.inbox.inboxchat.domain.usecase.AttachImageUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.GetMessageListUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.GetReplyListUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.SendMessageUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.template.GetTemplateUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.ReplyMessageUseCase;
import com.tokopedia.inbox.inboxchat.uploadimage.data.factory.ImageUploadFactory;
import com.tokopedia.inbox.inboxchat.uploadimage.data.mapper.GenerateHostMapper;
import com.tokopedia.inbox.inboxchat.uploadimage.data.mapper.UploadImageMapper;
import com.tokopedia.inbox.inboxchat.uploadimage.data.repository.ImageUploadRepository;
import com.tokopedia.inbox.inboxchat.uploadimage.data.repository.ImageUploadRepositoryImpl;
import com.tokopedia.inbox.inboxchat.uploadimage.domain.interactor.GenerateHostUseCase;
import com.tokopedia.inbox.inboxchat.uploadimage.domain.interactor.UploadImageUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by stevenfredian on 9/14/17.
 */

@Module
public class ChatRoomModule {

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
    TemplateChatFactory provideTemplateFactory(
            ChatService chatService,
            TemplateChatMapper templateChatMapper){
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
    TemplateChatMapper provideTemplateChatMapper(){
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

}
