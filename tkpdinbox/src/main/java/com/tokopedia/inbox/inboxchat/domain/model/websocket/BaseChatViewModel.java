package com.tokopedia.inbox.inboxchat.domain.model.websocket;

/**
 * @author by yfsx on 08/05/18.
 */

public class BaseChatViewModel {
    private int msgId;
    private int fromUid;
    private String from;
    private String fromRole;
    private int toUid;
    private MessageViewModel message;
    private String startTime;
    private String imageUri;
    private boolean showRating;
    private int ratingStatus;
    private int attachmentId;
    private int attachmentType;
    private FallbackAttachmentViewModel fallbackAttachment;

    public BaseChatViewModel() {
    }

    public BaseChatViewModel(int msgId,
                             int fromUid,
                             String from,
                             String fromRole,
                             int toUid,
                             MessageViewModel message,
                             String startTime,
                             String imageUri,
                             boolean showRating,
                             int ratingStatus,
                             int attachmentId,
                             int attachmentType,
                             FallbackAttachmentViewModel fallbackAttachment) {
        this.msgId = msgId;
        this.fromUid = fromUid;
        this.from = from;
        this.fromRole = fromRole;
        this.toUid = toUid;
        this.message = message;
        this.startTime = startTime;
        this.imageUri = imageUri;
        this.showRating = showRating;
        this.ratingStatus = ratingStatus;
        this.attachmentId = attachmentId;
        this.attachmentType = attachmentType;
        this.fallbackAttachment = fallbackAttachment;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public int getFromUid() {
        return fromUid;
    }

    public void setFromUid(int fromUid) {
        this.fromUid = fromUid;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFromRole() {
        return fromRole;
    }

    public void setFromRole(String fromRole) {
        this.fromRole = fromRole;
    }

    public int getToUid() {
        return toUid;
    }

    public void setToUid(int toUid) {
        this.toUid = toUid;
    }

    public MessageViewModel getMessage() {
        return message;
    }

    public void setMessage(MessageViewModel message) {
        this.message = message;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public boolean isShowRating() {
        return showRating;
    }

    public void setShowRating(boolean showRating) {
        this.showRating = showRating;
    }

    public int getRatingStatus() {
        return ratingStatus;
    }

    public void setRatingStatus(int ratingStatus) {
        this.ratingStatus = ratingStatus;
    }

    public int getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(int attachmentId) {
        this.attachmentId = attachmentId;
    }

    public int getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(int attachmentType) {
        this.attachmentType = attachmentType;
    }

    public FallbackAttachmentViewModel getFallbackAttachment() {
        return fallbackAttachment;
    }

    public void setFallbackAttachment(FallbackAttachmentViewModel fallbackAttachment) {
        this.fallbackAttachment = fallbackAttachment;
    }
}
