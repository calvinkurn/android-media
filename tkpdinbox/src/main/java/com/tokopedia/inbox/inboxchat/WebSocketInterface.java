package com.tokopedia.inbox.inboxchat;

import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.BaseChatViewModel;
import com.tokopedia.inbox.inboxchat.domain.model.reply.WebSocketResponse;

/**
 * Created by stevenfredian on 9/22/17.
 */

public interface WebSocketInterface {
    void onIncomingEvent(WebSocketResponse response);

    void onErrorWebSocket();

    void onOpenWebSocket();

    void onReceiveMessage(BaseChatViewModel message);
}
