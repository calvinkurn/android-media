package com.tokopedia.otp.cotp.view.viewmodel;

/**
 * @author by nisie on 12/4/17.
 */

public class RequestOtpViewModel {
    private final int attemptLeft;
    private final boolean isSuccess;

    public RequestOtpViewModel(int otpAttemptLeft, boolean sent) {
        this.attemptLeft = otpAttemptLeft;
        this.isSuccess = sent;
    }
}
