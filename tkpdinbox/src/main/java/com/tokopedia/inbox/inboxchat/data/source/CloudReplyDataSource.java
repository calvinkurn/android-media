package com.tokopedia.inbox.inboxchat.data.source;

import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.inboxchat.data.mapper.GetReplyMapper;
import com.tokopedia.inbox.inboxchat.domain.model.reply.ReplyData;
import com.tokopedia.inbox.inboxchat.viewmodel.ChatRoomViewModel;

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

    public Observable<ChatRoomViewModel> getReply(String id, TKPDMapParam<String, Object> requestParams) {
        return chatService.getApi().getReply(id, requestParams).map(getReplyMapper);
    }


}
