
package com.tokopedia.inbox.inboxchat.chatroom.domain.pojo.replyaction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.inbox.inboxchat.chatroom.domain.pojo.reply.Attachment;

public class Chat {

    @SerializedName("msg_id")
    @Expose
    private int msgId;
    @SerializedName("sender_id")
    @Expose
    private String senderId;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("reply_time")
    @Expose
    private String replyTime;
    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("role")
    @Expose
    private int role;
    @SerializedName("attachment")
    @Expose
    private Attachment attachment;

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }
}
