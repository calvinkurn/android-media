package com.tokopedia.inbox.inboxchat.domain.pojo.quickreply;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 09/05/18.
 */

public class QuickReplyPojo {

    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
