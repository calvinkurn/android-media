package com.tokopedia.otp.registerphonenumber.view.viewmodel;

/**
 * @author by yfsx on 5/3/18.
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
