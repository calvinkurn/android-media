package com.tokopedia.otp.securityquestion.domain.pojo.changephonenumberrequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nisie on 3/9/17.
 */

public class ValidateImageData {
    @SerializedName("is_success")
    @Expose
    int isSuccess;

    @SerializedName("token")
    @Expose
    String token;

    public boolean isSuccess() {
        return isSuccess == 1;
    }

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
