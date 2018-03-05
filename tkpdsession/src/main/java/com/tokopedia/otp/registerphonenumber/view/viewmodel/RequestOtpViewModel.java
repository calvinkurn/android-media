package com.tokopedia.otp.registerphonenumber.view.viewmodel;

/**
 * @author by yfsx on 5/3/17.
 */

public class RequestOtpViewModel {
    private final int attemptLeft;
    private final boolean isSuccess;

    public RequestOtpViewModel(int otpAttemptLeft, boolean sent) {
        this.attemptLeft = otpAttemptLeft;
        this.isSuccess = sent;
    }
}
