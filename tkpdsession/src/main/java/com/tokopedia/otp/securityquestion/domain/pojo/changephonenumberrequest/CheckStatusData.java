package com.tokopedia.otp.securityquestion.domain.pojo.changephonenumberrequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nisie on 3/10/17.
 */

public class CheckStatusData {
    @SerializedName("is_success")
    @Expose
    int isSuccess;

    @SerializedName("is_pending")
    @Expose
    int isPending;

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    public int getIsPending() {
        return isPending;
    }

    public void setIsPending(int isPending) {
        this.isPending = isPending;
    }

    public boolean isPending() {
        return isPending == 1;
    }

    public boolean isSuccess() {
        return isSuccess == 1;
    }
}
