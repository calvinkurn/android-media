package com.tokopedia.inbox.inboxchat.data.repository;


import com.google.gson.JsonObject;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.inboxchat.data.factory.MessageFactory;
import com.tokopedia.inbox.inboxchat.viewmodel.DeleteChatListViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.InboxChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.SendMessageViewModel;

import org.json.JSONObject;

import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class MessageRepositoryImpl implements MessageRepository{

    private final SendMessageSource sendMessageSource;
    private MessageFactory messageFactory;

    public MessageRepositoryImpl(MessageFactory messageFactory,
                                 SendMessageSource sendMessageSource){
        this.messageFactory = messageFactory;
        this.sendMessageSource = sendMessageSource;
    }

    @Override
    public Observable<InboxChatViewModel> getMessage(TKPDMapParam<String, Object> mapParam) {
        return messageFactory.createCloudMessageDataSource().getMessage(mapParam);
    }

    @Override
    public Observable<DeleteChatListViewModel> deleteMessage(JsonObject parameters) {
        return messageFactory.createCloudMessageDataSource().deleteMessage(parameters);
    }

    @Override
    public Observable<SendMessageViewModel> sendMessage(TKPDMapParam<String, Object> requestParams) {
        return sendMessageSource.sendMessage(requestParams);
    }
}
