package com.tokopedia.inbox.inboxchat.di;

import android.content.Context;


import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.core.network.apiservices.kunyit.KunyitService;
import com.tokopedia.inbox.inboxchat.data.factory.MessageFactory;
import com.tokopedia.inbox.inboxchat.data.factory.ReplyFactory;
import com.tokopedia.inbox.inboxchat.data.factory.SearchFactory;
import com.tokopedia.inbox.inboxchat.data.mapper.DeleteMessageMapper;
import com.tokopedia.inbox.inboxchat.data.mapper.GetMessageMapper;
import com.tokopedia.inbox.inboxchat.data.mapper.GetReplyMapper;
import com.tokopedia.inbox.inboxchat.data.mapper.ReplyMessageMapper;
import com.tokopedia.inbox.inboxchat.data.mapper.SearchChatMapper;
import com.tokopedia.inbox.inboxchat.data.mapper.SendMessageMapper;
import com.tokopedia.inbox.inboxchat.data.repository.MessageRepository;
import com.tokopedia.inbox.inboxchat.data.repository.MessageRepositoryImpl;
import com.tokopedia.inbox.inboxchat.data.repository.ReplyRepository;
import com.tokopedia.inbox.inboxchat.data.repository.ReplyRepositoryImpl;
import com.tokopedia.inbox.inboxchat.data.repository.SearchRepository;
import com.tokopedia.inbox.inboxchat.data.repository.SearchRepositoryImpl;
import com.tokopedia.inbox.inboxchat.data.repository.SendMessageSource;
import com.tokopedia.inbox.inboxchat.domain.usecase.DeleteMessageListUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.GetMessageListUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.GetReplyListUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.ReplyMessageUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.SearchMessageUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.SendMessageUseCase;

import dagger.Module;
import dagger.Provides;

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
            DeleteMessageMapper deleteMessageMapper){
        return new MessageFactory(chatService, getMessageMapper, deleteMessageMapper);
    }

    @InboxChatScope
    @Provides
    ReplyFactory provideReplyFactory(
            ChatService chatService,
            GetReplyMapper getReplyMapper,
            ReplyMessageMapper replyMessageMapper){
        return new ReplyFactory(chatService, getReplyMapper, replyMessageMapper);
    }

    @InboxChatScope
    @Provides
    SearchFactory provideSearchFactory(
            ChatService chatService,
            SearchChatMapper searchChatMapper){
        return new SearchFactory(chatService, searchChatMapper);
    }

    @InboxChatScope
    @Provides
    GetReplyMapper provideGetReplyMapper(){
        return new GetReplyMapper();
    }

    @InboxChatScope
    @Provides
    GetMessageMapper provideGetMessageMapper(){
        return new GetMessageMapper();
    }

    @InboxChatScope
    @Provides
    ReplyMessageMapper provideReplyMessageMapper(){
        return new ReplyMessageMapper();
    }

    @InboxChatScope
    @Provides
    DeleteMessageMapper provideDeleteMessageMapper(){
        return new DeleteMessageMapper();
    }


    @InboxChatScope
    @Provides
    SearchChatMapper provideSearchChatMapper(){
        return new SearchChatMapper();
    }

    @InboxChatScope
    @Provides
    MessageRepository provideMessageRepository(MessageFactory messageFactory,
                                               SendMessageSource sendMessageSource){
        return new MessageRepositoryImpl(messageFactory, sendMessageSource);
    }

    @InboxChatScope
    @Provides
    ReplyRepository provideReplyRepository(ReplyFactory replyFactory){
        return new ReplyRepositoryImpl(replyFactory);
    }

    @InboxChatScope
    @Provides
    SearchRepository provideSearchRepository(SearchFactory searchFactory){
        return new SearchRepositoryImpl(searchFactory);
    }

    @InboxChatScope
    @Provides
    GetMessageListUseCase provideGetMessageListUseCase(ThreadExecutor threadExecutor,
                                                       PostExecutionThread postExecutor,
                                                       MessageRepository messageRepository){
        return new GetMessageListUseCase(threadExecutor, postExecutor, messageRepository);
    }


    @InboxChatScope
    @Provides
    GetReplyListUseCase provideGetReplyListUseCase(ThreadExecutor threadExecutor,
                                 PostExecutionThread postExecutor,
                                 ReplyRepository replyRepository){
        return new GetReplyListUseCase(threadExecutor, postExecutor, replyRepository);
    }


    @InboxChatScope
    @Provides
    ReplyMessageUseCase provideReplyMessageUseCase(ThreadExecutor threadExecutor,
                                                   PostExecutionThread postExecutor,
                                                   ReplyRepository replyRepository){
        return new ReplyMessageUseCase(threadExecutor, postExecutor, replyRepository);
    }

    @InboxChatScope
    @Provides
    SearchMessageUseCase provideSearchChatUseCase(ThreadExecutor threadExecutor,
                                                   PostExecutionThread postExecutor,
                                                   SearchRepository searchRepository){
        return new SearchMessageUseCase(threadExecutor, postExecutor, searchRepository);
    }

    @InboxChatScope
    @Provides
    DeleteMessageListUseCase provideDeleteChatUseCase(ThreadExecutor threadExecutor,
                                                      PostExecutionThread postExecutor,
                                                      MessageRepository messageRepository){
        return new DeleteMessageListUseCase(threadExecutor, postExecutor, messageRepository);
    }

    @InboxChatScope
    @Provides
    ChatService provideChatService(){
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
    SendMessageSource provideSendMessageSource(KunyitService kunyitService,
                                               SendMessageMapper sendMessageMapper) {
        return new SendMessageSource(kunyitService, sendMessageMapper);
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
}
