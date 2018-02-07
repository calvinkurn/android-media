package com.tokopedia.otp.tokocashotp.view.viewmodel;

/**
 * @author by nisie on 12/4/17.
 */

public class RequestOtpTokoCashViewModel {
    private final int attemptLeft;
    private final boolean isSuccess;

    public RequestOtpTokoCashViewModel(int otpAttemptLeft, boolean sent) {
        this.attemptLeft = otpAttemptLeft;
        this.isSuccess = sent;
    }
}
