
package com.tokopedia.inbox.rescenter.discussion.data.pojo.replysubmit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Button {

    @SerializedName("hide_no_reply")
    @Expose
    private Integer hideNoReply;

    public Integer getHideNoReply() {
        return hideNoReply;
    }

    public void setHideNoReply(Integer hideNoReply) {
        this.hideNoReply = hideNoReply;
    }

}
