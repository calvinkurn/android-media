package com.tokopedia.inbox.inboxchat.viewmodel.chatroom;

import com.tokopedia.inbox.inboxchat.ChatWebSocketListenerImpl;
import com.tokopedia.inbox.inboxchat.domain.WebSocketMapper;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.BaseChatViewModel;
import com.tokopedia.inbox.inboxchat.domain.usecase.GetReplyListUseCase;

/**
 * @author by nisie on 5/16/18.
 */
public class SendableViewModel extends BaseChatViewModel {

    public static final String START_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    protected String startTime;

    /**
     * Constructor for WebSocketResponse / API Response
     * {@link ChatWebSocketListenerImpl}
     * {@link GetReplyListUseCase}
     *
     * @param messageId      messageId
     * @param fromUid        userId of sender
     * @param from           name of sender
     * @param fromRole       role of sender
     * @param attachmentId   attachment id
     * @param attachmentType attachment type. Please refer to
     *                       {@link WebSocketMapper} types
     * @param replyTime      replytime in unixtime
     * @param startTime      date time when sending / uploading data. Used to validate temporary
     *                       sending messages
     */
    public SendableViewModel(String messageId, String fromUid, String from, String fromRole,
                             String attachmentId, String attachmentType, String replyTime,
                             String startTime) {
        super(messageId, fromUid, from, fromRole, attachmentId, attachmentType, replyTime);
        this.startTime = startTime;
    }

    public String getStartTime() {
        return startTime;
    }

}
