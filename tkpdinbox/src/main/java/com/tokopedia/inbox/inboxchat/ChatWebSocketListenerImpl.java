package com.tokopedia.inbox.inboxchat;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.inboxchat.domain.model.reply.ReplyData;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.WebSocketResponse;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.WebSocketResponseData;
import com.tokopedia.inbox.inboxmessage.presenter.InboxMessageDetailFragmentPresenterImpl;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Created by stevenfredian on 9/20/17.
 */

public class ChatWebSocketListenerImpl extends WebSocketListener{
    private static final int NORMAL_CLOSURE_STATUS = 1000;
    private final WebSocketInterface listener;

    public ChatWebSocketListenerImpl(WebSocketInterface webSocketInterface) {
        listener = webSocketInterface;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        Log.i("Receiving : " , text);
        listener.onIncomingEvent(process(text));
    }

    private WebSocketResponse process(String text) {
        WebSocketResponse data = new GsonBuilder().create().fromJson(text, WebSocketResponse.class);
        return data;
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        Log.i("Receiving : " , bytes.hex());
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null);
        webSocket.request();
        Log.i("Closing : ", code + " / " + reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        Log.i("Error : " , "");
        listener.newWebSocket();
    }
}