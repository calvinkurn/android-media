package com.tokopedia.otp.phoneverification.data;

/**
 * Created by nisie on 3/7/17.
 */
public class VerifyPhoneNumberDomain {
    private boolean isSuccess;
    private String statusMessage;
    private String phoneNumber;

    public VerifyPhoneNumberDomain(boolean isSuccess, String statusMessage) {
        this.isSuccess = isSuccess;
        this.statusMessage = statusMessage;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
