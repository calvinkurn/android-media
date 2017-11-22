package com.tokopedia.inbox.inboxchat.data.source;

import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.inboxchat.data.mapper.ReplyMessageMapper;
import com.tokopedia.inbox.inboxchat.domain.model.reply.ReplyData;
import com.tokopedia.inbox.inboxchat.domain.model.replyaction.ReplyActionData;

import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class CloudReplyActionDataSource {

    private ReplyMessageMapper replyMessageMapper;
    private ChatService chatService;

    public CloudReplyActionDataSource(ChatService chatService, ReplyMessageMapper replyMessageMapper) {
        this.chatService = chatService;
        this.replyMessageMapper = replyMessageMapper;
    }

    public Observable<ReplyActionData> replyMessage(TKPDMapParam<String, Object> requestParams) {
        return chatService.getApi().reply(requestParams).map(replyMessageMapper);
    }
}
