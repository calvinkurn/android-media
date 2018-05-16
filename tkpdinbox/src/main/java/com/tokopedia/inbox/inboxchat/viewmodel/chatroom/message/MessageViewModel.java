package com.tokopedia.inbox.inboxchat.viewmodel.chatroom.message;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomTypeFactory;
import com.tokopedia.inbox.inboxchat.viewmodel.DummyChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.SendableViewModel;

/**
 * @author by nisie on 5/16/18.
 */
public class MessageViewModel extends SendableViewModel implements Visitable<ChatRoomTypeFactory> {

    /**
     * Constructor for WebSocketResponse / API Response
     * {@link com.tokopedia.inbox.inboxchat.ChatWebSocketListenerImpl}
     * {@link com.tokopedia.inbox.inboxchat.domain.usecase.GetReplyListUseCase}
     *
     * @param messageId      messageId
     * @param fromUid        userId of sender
     * @param from           name of sender
     * @param fromRole       role of sender
     * @param attachmentId   attachment id
     * @param attachmentType attachment type. Please refer to
     *                       {@link com.tokopedia.inbox.inboxchat.domain.WebSocketMapper} types
     * @param replyTime      replytime in unixtime
     * @param startTime      date time when sending / uploading data. Used to validate temporary
     * @param message        censored reply
     * @param isRead         is message already read by opponent
     * @param isSender       is own sender
     */
    public MessageViewModel(String messageId, String fromUid, String from, String fromRole,
                            String attachmentId, String attachmentType, String replyTime,
                            String startTime, String message, boolean isRead, boolean isDummy,
                            boolean isSender) {
        super(messageId, fromUid, from, fromRole, attachmentId, attachmentType, replyTime,
                startTime, isRead, isDummy, isSender, message);
    }

    /**
     * Constructor for send message
     *
     * @param messageId messageId
     * @param fromUid   userId of sender
     * @param from      name of sender
     * @param startTime date time when sending / uploading data. Used to validate temporary
     *                  message
     * @param message   censored reply
     */
    public MessageViewModel(String messageId, String fromUid, String from, String startTime,
                            String message) {
        super(messageId, fromUid, from, "", "", "",
                DummyChatViewModel.SENDING_TEXT, startTime,
                false, true, true, message);
    }

    @Override
    public int type(ChatRoomTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
