
package com.tokopedia.inbox.inboxchat.domain.pojo.quickreply;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuickReplyWebSocketResponse {

    @SerializedName("code")
    @Expose
    private int code;
    @SerializedName("data")
    @Expose
    private QuickReplyWebSocketResponseData data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public QuickReplyWebSocketResponseData getData() {
        return data;
    }

    public void setData(QuickReplyWebSocketResponseData data) {
        this.data = data;
    }

}
