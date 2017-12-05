
package com.tokopedia.inbox.inboxchat.domain.model.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.inbox.inboxchat.domain.model.message.Contact;

public class RepliesContent {

    @SerializedName("msg_id")
    @Expose
    private int msgId;
    @SerializedName("reply_id")
    @Expose
    private int replyId;
    @SerializedName("room_id")
    @Expose
    private String roomId;
    @SerializedName("opposite_id")
    @Expose
    private int oppositeId;
    @SerializedName("opposite_type")
    @Expose
    private int oppositeType;
    @SerializedName("opposite_name")
    @Expose
    private String oppositeName;
    @SerializedName("last_message")
    @Expose
    private String lastMessage;
    @SerializedName("create_time")
    @Expose
    private String createTime;
    @SerializedName("create_by")
    @Expose
    private int createBy;
    @SerializedName("contact")
    @Expose
    private Contact contact;

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public int getOppositeId() {
        return oppositeId;
    }

    public void setOppositeId(int oppositeId) {
        this.oppositeId = oppositeId;
    }

    public int getOppositeType() {
        return oppositeType;
    }

    public void setOppositeType(int oppositeType) {
        this.oppositeType = oppositeType;
    }

    public String getOppositeName() {
        return oppositeName;
    }

    public void setOppositeName(String oppositeName) {
        this.oppositeName = oppositeName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getCreateBy() {
        return createBy;
    }

    public void setCreateBy(int createBy) {
        this.createBy = createBy;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
