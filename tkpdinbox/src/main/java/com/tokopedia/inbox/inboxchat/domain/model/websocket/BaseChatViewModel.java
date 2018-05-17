package com.tokopedia.inbox.inboxchat.domain.model.websocket;

/**
 * @author by yfsx on 08/05/18.
 */

public class BaseChatViewModel {
    private String messageId;
    private String fromUid;
    private String from;
    private String fromRole;
    private String attachmentId;
    private String attachmentType;
    private String replyTime;
    private boolean showTime = false;

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
     * @param replyTime replytime in unixtime
     */
    public BaseChatViewModel(String messageId,
                             String fromUid,
                             String from,
                             String fromRole,
                             String attachmentId,
                             String attachmentType,
                             String replyTime) {
        this.messageId = messageId;
        this.fromUid = fromUid;
        this.from = from;
        this.fromRole = fromRole;
        this.attachmentId = attachmentId;
        this.attachmentType = attachmentType;
        this.replyTime = replyTime;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getFromUid() {
        return fromUid;
    }

    public String getFrom() {
        return from;
    }

    public String getFromRole() {
        return fromRole;
    }

    public String getAttachmentId() {
        return attachmentId;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public boolean isShowTime() {
        return showTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    public void setFromUid(String fromUid) {
        this.fromUid = fromUid;
    }

    /**
     * Set in {@link com.tokopedia.inbox.inboxchat.adapter.ChatRoomAdapter}
     *
     * @param showTime set true to show time in header of chat
     */
    public void setShowTime(boolean showTime) {
        this.showTime = showTime;
    }
}
