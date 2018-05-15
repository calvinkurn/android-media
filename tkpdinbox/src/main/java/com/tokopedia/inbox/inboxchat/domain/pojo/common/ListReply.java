
package com.tokopedia.inbox.inboxchat.domain.pojo.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListReply {

    @SerializedName("msg_id")
    @Expose
    private int msgId;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("reply_id")
    @Expose
    private int replyId;
    @SerializedName("sender_id")
    @Expose
    private String senderId;
    @SerializedName("sender_name")
    @Expose
    private String senderName;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("reply_time")
    @Expose
    private String replyTime;
    @SerializedName("reply_time_nano")
    @Expose
    private String replyTimeNano;
    @SerializedName("fraud_status")
    @Expose
    private int fraudStatus;
    @SerializedName("read_time")
    @Expose
    private String readTime;
    @SerializedName("attachment_id")
    @Expose
    private int attachmentId;
    @SerializedName("attachment")
    @Expose
    private Attachment attachment;
    @SerializedName("old_msg_id")
    @Expose
    private int oldMsgId;
    @SerializedName("message_is_read")
    @Expose
    private boolean messageIsRead;
    @SerializedName("is_opposite")
    @Expose
    private boolean isOpposite;

    @SerializedName("is_highlight")
    @Expose
    private boolean isHighlight;

    @SerializedName("old_msg_title")
    @Expose
    private String oldMessageTitle;

    @SerializedName("show_rating")
    @Expose
    private boolean showRating;

    @SerializedName("rating_status")
    @Expose
    private int ratingStatus;

    public int getReplyId() {
        return replyId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getMsg() {
        return msg;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public int getFraudStatus() {
        return fraudStatus;
    }

    public String getReadTime() {
        return readTime;
    }

    public int getAttachmentId() {
        return attachmentId;
    }

    public int getOldMsgId() {
        return oldMsgId;
    }

    public int getMsgId() {
        return msgId;
    }

    public int getUserId() {
        return userId;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getRole() {
        return role;
    }

    public boolean isMessageIsRead() {
        return messageIsRead;
    }

    public boolean isOpposite() {
        return isOpposite;
    }

    public boolean isHighlight() {
        return isHighlight;
    }

    public String getOldMessageTitle() {
        return oldMessageTitle;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public boolean isShowRating() {
        return showRating;
    }

    public int getRatingStatus() {
        return ratingStatus;
    }

    public String getReplyTimeNano() {
        return replyTimeNano;
    }


}
