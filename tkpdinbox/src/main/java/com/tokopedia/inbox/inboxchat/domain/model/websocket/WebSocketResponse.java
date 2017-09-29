
package com.tokopedia.inbox.inboxchat.domain.model.websocket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WebSocketResponse {

    @SerializedName("code")
    @Expose
    private int code;
    @SerializedName("data")
    @Expose
    private WebSocketResponseData data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public WebSocketResponseData getData() {
        return data;
    }

    public void setData(WebSocketResponseData data) {
        this.data = data;
    }

}
