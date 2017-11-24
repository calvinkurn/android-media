
package com.tokopedia.inbox.inboxchat.domain.model.reply;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Reply {

    @SerializedName("data")
    @Expose
    private ReplyData data;

    public ReplyData getData() {
        return data;
    }

    public void setData(ReplyData data) {
        this.data = data;
    }

}
