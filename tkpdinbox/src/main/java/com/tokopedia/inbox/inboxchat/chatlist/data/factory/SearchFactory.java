package com.tokopedia.inbox.inboxchat.chatlist.data.factory;

import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.inbox.inboxchat.chatlist.data.mapper.SearchChatMapper;
import com.tokopedia.inbox.inboxchat.chatlist.data.source.CloudSearchChatDataSource;

/**
 * Created by stevenfredian on 10/18/17.
 */

public class SearchFactory {

    private SearchChatMapper searchChatMapper;
    private ChatService chatService;

    public SearchFactory(ChatService chatService, SearchChatMapper searchChatMapper){
        this.chatService = chatService;
        this.searchChatMapper = searchChatMapper;
    }

    public CloudSearchChatDataSource createCloudSearchDataSource() {
        return new CloudSearchChatDataSource(chatService, searchChatMapper);
    }
}
