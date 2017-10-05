
package com.tokopedia.inbox.rescenter.discussion.data.pojo.replyvalidation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class By {

    @SerializedName("is_sender")
    @Expose
    private String isSender;
    @SerializedName("is_receiver")
    @Expose
    private String isReceiver;

    public String getIsSender() {
        return isSender;
    }

    public void setIsSender(String isSender) {
        this.isSender = isSender;
    }

    public String getIsReceiver() {
        return isReceiver;
    }

    public void setIsReceiver(String isReceiver) {
        this.isReceiver = isReceiver;
    }

}
