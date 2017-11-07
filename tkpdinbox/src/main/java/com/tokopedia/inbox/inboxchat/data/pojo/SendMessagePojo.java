package com.tokopedia.inbox.inboxchat.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by nisie on 11/6/17.
 */

public class SendMessagePojo {

    @SerializedName("is_success")
    @Expose
    private boolean isSuccess;

    public boolean isSuccess() {
        return isSuccess;
    }
}
