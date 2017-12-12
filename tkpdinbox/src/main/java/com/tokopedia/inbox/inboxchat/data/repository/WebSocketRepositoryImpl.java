package com.tokopedia.inbox.inboxchat.data.repository;


import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.inboxchat.data.factory.ReplyFactory;
import com.tokopedia.inbox.inboxchat.data.factory.WebSocketFactory;
import com.tokopedia.inbox.inboxchat.domain.model.reply.ReplyData;
import com.tokopedia.inbox.inboxchat.domain.model.replyaction.ReplyActionData;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.ChatSocketData;

import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class WebSocketRepositoryImpl implements WebSocketRepository{

    private ReplyFactory replyFactory;
    private WebSocketFactory webSocketFactory;

    public WebSocketRepositoryImpl(WebSocketFactory webSocketFactory){
        this.webSocketFactory = webSocketFactory;
    }

    @Override
    public Observable<ChatSocketData> listen(TKPDMapParam<String, Object> requestParams) {
        return webSocketFactory.createCloudListWebSocketSource().listen(requestParams);
    }
}
