package com.tokopedia.session.activation.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nisie on 4/17/17.
 */

public class ResendActivationData {
    @SerializedName("is_success")
    @Expose
    private int isSuccess;

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }
}
