package com.tokopedia.inbox.inboxchat.domain.model.websocket;

import com.tokopedia.inbox.inboxchat.domain.model.reply.FallbackAttachment;

/**
 * @author by yfsx on 08/05/18.
 */

public class BaseChatViewModel {
    private String msgId;
    private String fromUid;
    private String from;
    private String fromRole;
    private String toUid;
    private Message message;
    private String startTime;
    private String imageUri;
    private String attachmentId;
    private String attachmentType;
    private FallbackAttachment fallbackAttachment;

    public BaseChatViewModel(String msgId,
                             String fromUid,
                             String from,
                             String fromRole,
                             String toUid,
                             Message message,
                             String startTime,
                             String imageUri,
                             String attachmentId,
                             String attachmentType,
                             FallbackAttachment fallbackAttachment) {
        this.msgId = msgId;
        this.fromUid = fromUid;
        this.from = from;
        this.fromRole = fromRole;
        this.toUid = toUid;
        this.message = message;
        this.startTime = startTime;
        this.imageUri = imageUri;
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

    public String getToUid() {
        return toUid;
    }

    public Message getMessage() {
        return message;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getAttachmentId() {
        return attachmentId;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public FallbackAttachment getFallbackAttachment() {
        return fallbackAttachment;
    }

}
