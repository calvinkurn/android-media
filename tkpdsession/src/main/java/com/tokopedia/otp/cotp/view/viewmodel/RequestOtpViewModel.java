package com.tokopedia.otp.cotp.view.viewmodel;

/**
 * @author by nisie on 12/4/17.
 */

public class RequestOtpViewModel {
    private final boolean isSuccess;

    public RequestOtpViewModel(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
