
package com.tokopedia.inbox.inboxchat.domain.model.reply;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.inbox.inboxchat.ChatTimeConverter;

import java.util.Calendar;

public class ListReply {

    @SerializedName("reply_id")
    @Expose
    private int replyId;
    @SerializedName("sender_id")
    @Expose
    private String senderId;
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
    @SerializedName("old_msg_id")
    @Expose
    private int oldMsgId;

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

    public Calendar getReplyCalendar() {
        return ChatTimeConverter.unixToCalendar(Long.parseLong(replyTime));
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

}
