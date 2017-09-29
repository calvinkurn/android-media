package com.tokopedia.inbox.inboxchat.data.repository;


import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.inboxchat.data.factory.MessageFactory;
import com.tokopedia.inbox.inboxchat.domain.model.message.MessageData;

import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class MessageRepositoryImpl implements MessageRepository{

    private MessageFactory messageFactory;

    public MessageRepositoryImpl(MessageFactory messageFactory){
        this.messageFactory = messageFactory;
    }

    @Override
    public Observable<MessageData> getMessage(TKPDMapParam<String, Object> mapParam) {
        return messageFactory.createCloudMessageDataSource().getMessage(mapParam);
    }
}
