package com.tokopedia.inbox.inboxchat;

import com.google.gson.GsonBuilder;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.inbox.inboxchat.domain.WebSocketMapper;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.BaseChatViewModel;
import com.tokopedia.inbox.inboxchat.domain.pojo.WebSocketResponse;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Created by stevenfredian on 9/20/17.
 */

public class ChatWebSocketListenerImpl extends WebSocketListener {
    private static final int NORMAL_CLOSURE_STATUS = 1000;
    private final WebSocketInterface listener;
    private WebSocketMapper webSocketMapper;

    public ChatWebSocketListenerImpl(WebSocketInterface webSocketInterface, WebSocketMapper webSocketMapper) {
        listener = webSocketInterface;
        this.webSocketMapper = webSocketMapper;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        listener.onOpenWebSocket();
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        CommonUtils.dumper("WS Message: " + text);

        if (text.contains("NISEH")) {
            text = "{\"code\":103,\"data\":{\"from\":\"Cincin Buyer\",\"from_role\":\"User\"," +
                    "\"from_uid\":3045173,\"message\":{\"censored_reply\":\"AYAM\",\"original_reply\":\"AYAM\",\"timestamp\":\"2018-05-14T09:00:23.253222446+07:00\",\"timestamp_fmt\":\"14 May 2018, 09:00 WIB\",\"timestamp_unix\":1526263223253,\"timestamp_unix_nano\":1526263223253222000},\"msg_id\":8695843,\"show_rating\":false,\"start_time\":\"2018-05-14T02:00:22.958Z\",\"thumbnail\":\"https://imagerouter.tokopedia.com/image/v1/u/3045173/user_thumbnail/desktop\",\"to_uid\":2590134,\"attachment_id\":1,\"attachment\":{\"id\":1,\"type\":8,\"attributes\":{\"quick_replies\":[{\"message\":\"Selamat pagi juga kak\"},{\"message\":\"pagi juga kak\"},{\"message\":\"pagi\"}]},\"fallback_attachment\":{\"message\":\"halo selamat fallback\",\"html\":\"halo selamat fallback\"}}}}";
        } else if (text.contains("AYAM")) {
            text = "{\"code\":103,\"data\":{\"from\":\"Cincin Buyer\",\"from_role\":\"User\"," +
                    "\"from_uid\":3045173,\"message\":{\"censored_reply\":\"AYAM\",\"original_reply\":\"AYAM\",\"timestamp\":\"2018-05-14T09:00:23.253222446+07:00\",\"timestamp_fmt\":\"14 May 2018, 09:00 WIB\",\"timestamp_unix\":1526263223253,\"timestamp_unix_nano\":1526263223253222000},\"msg_id\":8695843,\"show_rating\":false,\"start_time\":\"2018-05-14T02:00:22.958Z\",\"thumbnail\":\"https://imagerouter.tokopedia.com/image/v1/u/3045173/user_thumbnail/desktop\",\"to_uid\":2590134,\"attachment_id\":1,\"attachment\":{\"id\":1,\"type\":1,\"attributes\":{\"quick_replies\":[{\"message\":\"Selamat pagi juga kak\"},{\"message\":\"pagi juga kak\"},{\"message\":\"pagi\"}]},\"fallback_attachment\":{\"message\":\"halo selamat fallback\",\"html\":\"halo selamat fallback\"}}}}";
        }

        BaseChatViewModel message = webSocketMapper.map(text);
        if (message != null) {
            listener.onReceiveMessage(message);
        } else {
            WebSocketResponse response = process(text);
            listener.onIncomingEvent(response);
        }
    }

    private WebSocketResponse process(String text) {
        WebSocketResponse data = new GsonBuilder().create().fromJson(text, WebSocketResponse.class);
        return data;
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        CommonUtils.dumper("WS Message: " + bytes.hex());
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null);
        webSocket.request();
        CommonUtils.dumper("WS Closing : " + code + " / " + reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        listener.onErrorWebSocket();
    }
}