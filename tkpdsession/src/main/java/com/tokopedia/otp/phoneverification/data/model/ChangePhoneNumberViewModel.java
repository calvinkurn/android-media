package com.tokopedia.otp.phoneverification.data.model;

/**
 * Created by nisie on 5/10/17.
 */

public class ChangePhoneNumberViewModel {
    boolean isSuccess;
    private String statusMessage;
    private String phoneNumber;

    public ChangePhoneNumberViewModel(boolean isSuccess, String statusMessage) {
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
