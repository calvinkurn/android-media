package com.tokopedia.otp.domain.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nisie on 3/7/17.
 */
public class ValidateOtpPojo {
    @SerializedName("is_success")
    @Expose
    int isSuccess;

    @SerializedName("uuid")
    @Expose
    String uuid;

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess == 1;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
