
package com.tokopedia.inbox.inboxchat.domain.model.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
