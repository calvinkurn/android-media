package com.tokopedia.inbox.inboxchat.data.factory;

import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.inbox.inboxchat.data.mapper.GetMessageMapper;
import com.tokopedia.inbox.inboxchat.data.mapper.GetReplyMapper;
import com.tokopedia.inbox.inboxchat.data.mapper.ReplyMessageMapper;
import com.tokopedia.inbox.inboxchat.data.repository.ReplyRepository;
import com.tokopedia.inbox.inboxchat.data.source.CloudMessageDataSource;
import com.tokopedia.inbox.inboxchat.data.source.CloudReplyActionDataSource;
import com.tokopedia.inbox.inboxchat.data.source.CloudReplyDataSource;

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
