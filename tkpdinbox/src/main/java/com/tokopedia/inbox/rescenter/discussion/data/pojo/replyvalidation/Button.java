
package com.tokopedia.inbox.rescenter.discussion.data.pojo.replyvalidation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Button {

    @SerializedName("hide_no_reply")
    @Expose
    private int hideNoReply;

    public int getHideNoReply() {
        return hideNoReply;
    }

    public void setHideNoReply(int hideNoReply) {
        this.hideNoReply = hideNoReply;
    }

}
