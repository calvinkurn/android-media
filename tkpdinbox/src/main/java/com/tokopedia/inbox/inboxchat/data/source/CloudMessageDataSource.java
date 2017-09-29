package com.tokopedia.inbox.inboxchat.data.source;

import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.inboxchat.data.mapper.GetMessageMapper;
import com.tokopedia.inbox.inboxchat.domain.model.message.MessageData;

import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class CloudMessageDataSource{

    private ChatService chatService;
    private GetMessageMapper getMessageMapper;

    public CloudMessageDataSource(ChatService chatService, GetMessageMapper getMessageMapper) {
        this.chatService = chatService;
        this.getMessageMapper = getMessageMapper;
    }

    public Observable<MessageData> getMessage(TKPDMapParam<String, Object> requestParams) {
        return chatService.getApi().getMessage(requestParams).map(getMessageMapper);
    }
}
