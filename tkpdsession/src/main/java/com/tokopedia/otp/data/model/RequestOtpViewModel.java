package com.tokopedia.otp.data.model;

/**
 * @author by nisie on 10/23/17.
 */

public class RequestOtpViewModel {
    boolean isSuccess;
    String messageStatus;

    public RequestOtpViewModel(boolean isSuccess, String messageStatus) {
        this.isSuccess = isSuccess;
        this.messageStatus = messageStatus;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getMessageStatus() {
        return messageStatus;
    }
}
