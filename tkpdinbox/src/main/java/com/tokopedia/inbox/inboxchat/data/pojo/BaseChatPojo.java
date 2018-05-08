package com.tokopedia.inbox.inboxchat.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 08/05/18.
 */

public class BaseChatPojo {
    @SerializedName("msg_id")
    @Expose
    private int msgId;
    @SerializedName("from_uid")
    @Expose
    private int fromUid;
    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("from_role")
    @Expose
    private String fromRole;
    @SerializedName("to_uid")
    @Expose
    private int toUid;
    @SerializedName("message")
    @Expose
    private MessagePojo message;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("thumbnail")
    @Expose
    private String imageUri;
    @SerializedName("show_rating")
    @Expose
    private boolean showRating;
    @SerializedName("rating_status")
    @Expose
    private int ratingStatus;

    @SerializedName("id")
    @Expose
    private String attachmentId;
    @SerializedName("type")
    @Expose
    private String attachmentType;
    @SerializedName("attributes")
    @Expose
    private FallbackAttachmentPojo fallbackAttachment;

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

    public int getToUid() {
        return toUid;
    }

    public void setToUid(int toUid) {
        this.toUid = toUid;
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

    public String getFromRole() {
        return fromRole;
    }

    public void setFromRole(String fromRole) {
        this.fromRole = fromRole;
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

    public MessagePojo getMessage() {
        return message;
    }

    public void setMessage(MessagePojo message) {
        this.message = message;
    }

    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public FallbackAttachmentPojo getFallbackAttachment() {
        return fallbackAttachment;
    }

    public void setFallbackAttachment(FallbackAttachmentPojo fallbackAttachment) {
        this.fallbackAttachment = fallbackAttachment;
    }
}
