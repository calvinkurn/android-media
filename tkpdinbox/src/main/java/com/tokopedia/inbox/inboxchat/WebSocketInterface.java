package com.tokopedia.inbox.inboxchat;

import com.tokopedia.inbox.inboxchat.domain.model.websocket.WebSocketResponse;

/**
 * Created by stevenfredian on 9/22/17.
 */

public interface WebSocketInterface {
    void onIncomingEvent(WebSocketResponse response);

    void onErrorWebSocket();

    void onOpenWebSocket();
}
