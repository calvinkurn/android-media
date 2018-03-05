
package com.tokopedia.otp.registerphonenumber.data.pojo.requestotp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestOtpResponse {

    @SerializedName("otp_attempt_left")
    @Expose
    private int otpAttemptLeft;
    @SerializedName("sent")
    @Expose
    private boolean sent;

    public int getOtpAttemptLeft() {
        return otpAttemptLeft;
    }

    public void setOtpAttemptLeft(int otpAttemptLeft) {
        this.otpAttemptLeft = otpAttemptLeft;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

}
