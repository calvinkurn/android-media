
package com.tokopedia.otp.registerphonenumber.data.pojo.verifyotp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 5/3/18.
 */

public class VerifyOtpResponse {
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
