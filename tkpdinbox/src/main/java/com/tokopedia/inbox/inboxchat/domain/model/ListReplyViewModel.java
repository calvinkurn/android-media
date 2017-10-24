
package com.tokopedia.inbox.inboxchat.domain.model;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.inbox.inboxchat.ChatTimeConverter;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomTypeFactory;

import java.util.Calendar;

public abstract class ListReplyViewModel implements Visitable<ChatRoomTypeFactory>{

    private int msgId;
    private int userId;
    private int replyId;
    private String senderId;
    private String senderName;
    private String role;
    private String msg;
    private String replyTime;
    private int fraudStatus;
    private String readTime;
    private int attachmentId;
    private int oldMsgId;
    private boolean showTime;
    private boolean messageIsRead;
    private boolean isOpposite;

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
}
