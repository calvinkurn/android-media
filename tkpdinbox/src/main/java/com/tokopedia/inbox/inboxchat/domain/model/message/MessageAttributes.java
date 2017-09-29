
package com.tokopedia.inbox.inboxchat.domain.model.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageAttributes {

    @SerializedName("contact")
    @Expose
    private Contact contact;
    @SerializedName("last_reply_msg")
    @Expose
    private String lastReplyMsg;
    @SerializedName("last_reply_time")
    @Expose
    private String lastReplyTime;
    @SerializedName("read_status")
    @Expose
    private int readStatus;
    @SerializedName("unreads")
    @Expose
    private int unreads;
    @SerializedName("fraud_status")
    @Expose
    private int fraudStatus;

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getLastReplyMsg() {
        return lastReplyMsg;
    }

    public void setLastReplyMsg(String lastReplyMsg) {
        this.lastReplyMsg = lastReplyMsg;
    }

    public String getLastReplyTime() {
        return lastReplyTime;
    }

    public void setLastReplyTime(String lastReplyTime) {
        this.lastReplyTime = lastReplyTime;
    }

    public int getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(int readStatus) {
        this.readStatus = readStatus;
    }

    public int getUnreads() {
        return unreads;
    }

    public void setUnreads(int unreads) {
        this.unreads = unreads;
    }

    public int getFraudStatus() {
        return fraudStatus;
    }

    public void setFraudStatus(int fraudStatus) {
        this.fraudStatus = fraudStatus;
    }

}
