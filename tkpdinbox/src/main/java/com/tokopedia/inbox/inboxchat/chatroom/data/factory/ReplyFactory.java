package com.tokopedia.inbox.inboxchat.chatroom.data.factory;

import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.inbox.inboxchat.chatroom.data.mapper.GetReplyMapper;
import com.tokopedia.inbox.inboxchat.chatroom.data.mapper.ReplyMessageMapper;
import com.tokopedia.inbox.inboxchat.chatroom.data.source.CloudReplyActionDataSource;
import com.tokopedia.inbox.inboxchat.chatroom.data.source.CloudReplyDataSource;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class ReplyFactory {

    private GetReplyMapper getReplyMapper;
    private ReplyMessageMapper replyMessageMapper;
    private ChatService chatService;

    public ReplyFactory(ChatService chatService, GetReplyMapper getReplyMapper, ReplyMessageMapper replyMessageMapper){
        this.chatService = chatService;
        this.getReplyMapper = getReplyMapper;
        this.replyMessageMapper = replyMessageMapper;
    }

    public CloudReplyDataSource createCloudReplyDataSource() {
        return new CloudReplyDataSource(chatService, getReplyMapper);
    }

    public CloudReplyActionDataSource createCloudReplyActionDataSource(){
        return new CloudReplyActionDataSource(chatService, replyMessageMapper);
    }
}
