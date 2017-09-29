
package com.tokopedia.inbox.inboxchat.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.inbox.inboxchat.ChatTimeConverter;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomTypeFactory;

import java.util.Calendar;

public abstract class ListReplyViewModel implements Visitable<ChatRoomTypeFactory>{

    public ListReplyViewModel(int replyId, String senderId, String msg, String replyTime, int fraudStatus, String readTime, int attachmentId, int oldMsgId) {
        this.replyId = replyId;
        this.senderId = senderId;
        this.msg = msg;
        this.replyTime = replyTime;
        this.fraudStatus = fraudStatus;
        this.readTime = readTime;
        this.attachmentId = attachmentId;
        this.oldMsgId = oldMsgId;
        this.showTime = true;
    }

    private int replyId;
    private String senderId;
    private String msg;
    private String replyTime;
    private int fraudStatus;
    private String readTime;
    private int attachmentId;
    private int oldMsgId;
    private boolean showTime;

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

    public void setShowTime(boolean b) {
        showTime = b;
    }

    public boolean isShowTime() {
        return showTime;
    }
}
