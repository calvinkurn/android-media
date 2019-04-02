package com.tokopedia.transaction.opportunity.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by nisie on 7/20/17.
 */

public class CancelReplacementPojo {

    @SerializedName("is_success")
    @Expose
    private int isSuccess;
    @SerializedName("message_status")
    @Expose
    private String messageStatus;

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }
}
