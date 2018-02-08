
package com.tokopedia.inbox.inboxchat.domain.model.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListMessage {

    @SerializedName("message_key")
    @Expose
    private String messageKey;
    @SerializedName("msg_id")
    @Expose
    private int msgId;
    @SerializedName("attributes")
    @Expose
    private MessageAttributes attributes;

    private boolean isChecked;
    private int position;

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public MessageAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(MessageAttributes attributes) {
        this.attributes = attributes;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean checked) {
        isChecked = checked;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
