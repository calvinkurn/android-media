package com.tokopedia.inbox.inboxchat.chatroom.presenter;

import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.BaseChatViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.domain.pojo.reply.WebSocketResponse;

/**
 * Created by stevenfredian on 9/22/17.
 */

public interface WebSocketInterface {
    void onIncomingEvent(WebSocketResponse response);

    void onErrorWebSocket();

    void onOpenWebSocket();

    void onReceiveMessage(BaseChatViewModel message);
}
