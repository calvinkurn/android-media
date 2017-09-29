package com.tokopedia.inbox.inboxchat.data.factory;

import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.inbox.inboxchat.data.mapper.ChatEventMapper;
import com.tokopedia.inbox.inboxchat.data.source.CloudListenWebSocketDataSource;
import com.tokopedia.inbox.inboxchat.data.source.CloudReplyDataSource;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class WebSocketFactory {

    private ChatService chatService;
    private ChatEventMapper mapper;

    public WebSocketFactory(ChatService chatService, ChatEventMapper mapper){
        this.chatService = chatService;
        this.mapper = mapper;
    }

    public CloudListenWebSocketDataSource createCloudListWebSocketSource() {
        return new CloudListenWebSocketDataSource(chatService, mapper);
    }
}
