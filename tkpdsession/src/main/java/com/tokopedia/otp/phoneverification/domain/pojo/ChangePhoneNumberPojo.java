package com.tokopedia.otp.phoneverification.domain.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nisie on 5/10/17.
 */

public class ChangePhoneNumberPojo {

    @SerializedName("is_success")
    @Expose
    int isSuccess;

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }
}
