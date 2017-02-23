package com.tokopedia.otp.phoneverification.presenter;

/**
 * Created by nisie on 2/22/17.
 */

public interface PhoneVerificationPresenter {
    void verifyOtp();

    void requestOtp();

    void requestOtpWithCall();
}
