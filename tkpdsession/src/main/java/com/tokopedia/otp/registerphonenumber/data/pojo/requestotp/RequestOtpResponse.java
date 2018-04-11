
package com.tokopedia.otp.registerphonenumber.data.pojo.requestotp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 5/3/18.
 */
public class RequestOtpResponse {

    @SerializedName("is_success")
    @Expose
    int isSuccess;

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess == 1;
    }

}
