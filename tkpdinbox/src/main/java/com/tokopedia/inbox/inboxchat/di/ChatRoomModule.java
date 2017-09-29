package com.tokopedia.inbox.inboxchat.di;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.inbox.inboxchat.data.factory.MessageFactory;
import com.tokopedia.inbox.inboxchat.data.factory.ReplyFactory;
import com.tokopedia.inbox.inboxchat.data.mapper.GetMessageMapper;
import com.tokopedia.inbox.inboxchat.data.mapper.GetReplyMapper;
import com.tokopedia.inbox.inboxchat.data.mapper.ReplyMessageMapper;
import com.tokopedia.inbox.inboxchat.data.repository.MessageRepository;
import com.tokopedia.inbox.inboxchat.data.repository.MessageRepositoryImpl;
import com.tokopedia.inbox.inboxchat.data.repository.ReplyRepository;
import com.tokopedia.inbox.inboxchat.data.repository.ReplyRepositoryImpl;
import com.tokopedia.inbox.inboxchat.domain.usecase.GetMessageListUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.GetReplyListUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.ReplyMessageUseCase;

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
            GetMessageMapper getMessageMapper){
        return new MessageFactory(chatService, getMessageMapper);
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
    MessageRepository provideMessageRepository(MessageFactory messageFactory){
        return new MessageRepositoryImpl(messageFactory);
    }

    @InboxChatScope
    @Provides
    ReplyRepository provideReplyRepository(ReplyFactory replyFactory){
        return new ReplyRepositoryImpl(replyFactory);
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
    ChatService provideChatService(){
        return new ChatService();
    }
}
