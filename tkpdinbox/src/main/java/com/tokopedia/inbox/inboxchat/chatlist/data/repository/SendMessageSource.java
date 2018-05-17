package com.tokopedia.inbox.inboxchat.chatlist.data.repository;

import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.inboxchat.chatroom.data.mapper.SendMessageMapper;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.SendMessageViewModel;

import rx.Observable;

/**
 * @author by nisie on 10/25/17.
 */

public class SendMessageSource {
    private ChatService chatService;
    private SendMessageMapper sendMessageMapper;

    public SendMessageSource(ChatService chatService, SendMessageMapper sendMessageMapper) {
        this.chatService = chatService;
        this.sendMessageMapper = sendMessageMapper;
    }

    public Observable<SendMessageViewModel> sendMessage(TKPDMapParam<String, Object> requestParams) {
        return chatService.getApi()
                .sendMessage(requestParams)
                .map(sendMessageMapper);
    }
}
