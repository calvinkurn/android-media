
package com.tokopedia.inbox.inboxchat.domain.model.reply;

import android.text.Spanned;
import android.text.SpannedString;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.inbox.inboxchat.ChatTimeConverter;

import java.util.Calendar;

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


    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    public int getFraudStatus() {
        return fraudStatus;
    }

    public void setFraudStatus(int fraudStatus) {
        this.fraudStatus = fraudStatus;
    }

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

    public int getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(int attachmentId) {
        this.attachmentId = attachmentId;
    }

    public int getOldMsgId() {
        return oldMsgId;
    }

    public void setOldMsgId(int oldMsgId) {
        this.oldMsgId = oldMsgId;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isMessageIsRead() {
        return messageIsRead;
    }

    public void setMessageIsRead(boolean messageIsRead) {
        this.messageIsRead = messageIsRead;
    }

    public boolean isOpposite() {
        return isOpposite;
    }

    public void setOpposite(boolean opposite) {
        isOpposite = opposite;
    }

    public boolean isHighlight() {
        return isHighlight;
    }

    public void setHighlight(boolean highlight) {
        isHighlight = highlight;
    }

    public String getOldMessageTitle() {
        return oldMessageTitle;
    }

    public void setOldMessageTitle(String oldMessageTitle) {
        this.oldMessageTitle = oldMessageTitle;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }
}
