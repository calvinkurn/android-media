package com.tokopedia.inbox.inboxchat.data.source;

import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.inboxchat.data.mapper.GetReplyMapper;
import com.tokopedia.inbox.inboxchat.domain.model.reply.ReplyData;

import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class CloudReplyDataSource {

    private GetReplyMapper getReplyMapper;
    private ChatService chatService;

    public CloudReplyDataSource(ChatService chatService, GetReplyMapper getReplyMapper) {
        this.chatService = chatService;
        this.getReplyMapper = getReplyMapper;
    }

    public Observable<ReplyData> getReply(TKPDMapParam<String, Object> requestParams) {
        return chatService.getApi().getReply(String.valueOf(requestParams.get("msg_id")), requestParams).map(getReplyMapper);
    }


}
