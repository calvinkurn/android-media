package com.tokopedia.otp.phoneverification.domain.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nisie on 3/7/17.
 */

public class VerifyPhoneNumberPojo {

    @SerializedName("is_success")
    @Expose
    int isSuccess;

    public boolean isSuccess() {
        return isSuccess == 1;
    }

    public void setSuccess(int success) {
        isSuccess = success;
    }
}
