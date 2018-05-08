package com.tokopedia.inbox.inboxchat.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 08/05/18.
 */

public class WebSocketPojo {

    @SerializedName("code")
    @Expose
    private int code;
    @SerializedName("data")
    @Expose
    private BaseChatPojo data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public BaseChatPojo getData() {
        return data;
    }

    public void setData(BaseChatPojo data) {
        this.data = data;
    }
}
