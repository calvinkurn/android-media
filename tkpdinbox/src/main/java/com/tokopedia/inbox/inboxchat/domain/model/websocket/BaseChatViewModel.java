package com.tokopedia.inbox.inboxchat.domain.model.websocket;

/**
 * @author by yfsx on 08/05/18.
 */

public class BaseChatViewModel {
    private String msgId;
    private String fromUid;
    private String from;
    private String fromRole;
    private MessageViewModel message;
    private String attachmentId;
    private String attachmentType;
    private FallbackAttachmentViewModel fallbackAttachment;

    public BaseChatViewModel(String msgId,
                             String fromUid,
                             String from,
                             String fromRole,
                             MessageViewModel message,
                             String attachmentId,
                             String attachmentType,
                             FallbackAttachmentViewModel fallbackAttachment) {
        this.msgId = msgId;
        this.fromUid = fromUid;
        this.from = from;
        this.fromRole = fromRole;
        this.message = message;
        this.attachmentId = attachmentId;
        this.attachmentType = attachmentType;
        this.fallbackAttachment = fallbackAttachment;
    }

    public String getMsgId() {
        return msgId;
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

    public MessageViewModel getMessage() {
        return message;
    }

    public String getAttachmentId() {
        return attachmentId;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public FallbackAttachmentViewModel getFallbackAttachment() {
        return fallbackAttachment;
    }

}
