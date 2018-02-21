package com.tokopedia.tkpdstream.chatroom.domain;

import android.util.Log;

import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;

/**
 * @author by nisie on 2/7/18.
 */

public class ConnectionManager {

    public static final String CONNECTION_HANDLER_ID = "CONNECTION_HANDLER_OPEN_CHAT";
    public static final String CHANNEL_HANDLER_ID = "CHANNEL_HANDLER_OPEN_CHAT";

    public static void login(String userId, final SendBird.ConnectHandler handler) {
        SendBird.connect(userId, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                if (handler != null) {
                    handler.onConnected(user, e);
                }
            }
        });
    }

    public static void logout(final SendBird.DisconnectHandler handler) {
        SendBird.disconnect(new SendBird.DisconnectHandler() {
            @Override
            public void onDisconnected() {
                if (handler != null) {
                    handler.onDisconnected();
                }
            }
        });
    }

    public static void addConnectionManagementHandler(String userId, String handlerId, final ConnectionManagementHandler handler) {
        SendBird.addConnectionHandler(handlerId, new SendBird.ConnectionHandler() {
            @Override
            public void onReconnectStarted() {
            }

            @Override
            public void onReconnectSucceeded() {
                if (handler != null) {
                    handler.onConnected(true);
                }
            }

            @Override
            public void onReconnectFailed() {
            }
        });

        if (SendBird.getConnectionState() == SendBird.ConnectionState.OPEN) {
            if (handler != null) {
                handler.onConnected(false);
            }
        } else if (SendBird.getConnectionState() == SendBird.ConnectionState.CLOSED) { // push notification or system kill
            SendBird.connect(userId, new SendBird.ConnectHandler() {
                @Override
                public void onConnected(User user, SendBirdException e) {
                    if (e != null) {
                        return;
                    }

                    if (handler != null) {
                        handler.onConnected(false);
                    }
                }
            });
        }
    }

    public static void removeConnectionManagementHandler(String handlerId) {
        SendBird.removeConnectionHandler(handlerId);
    }

    public interface ConnectionManagementHandler {
        /**
         * A callback for when connected or reconnected to refresh.
         *
         * @param reconnect Set false if connected, true if reconnected.
         */
        void onConnected(boolean reconnect);
    }
}
