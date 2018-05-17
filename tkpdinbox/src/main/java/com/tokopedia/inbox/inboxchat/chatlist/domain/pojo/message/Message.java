
package com.tokopedia.inbox.inboxchat.chatlist.domain.pojo.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.inbox.inboxchat.chatlist.domain.pojo.message.MessageData;

public class Message {

    @SerializedName("data")
    @Expose
    private MessageData data;

    public MessageData getData() {
        return data;
    }

    public void setData(MessageData data) {
        this.data = data;
    }

}
