package com.tokopedia.otp.phoneverification.view.presenter;

/**
 * Created by nisie on 2/22/17.
 */

public interface PhoneVerificationPresenter {
    void verifyPhoneNumber();

    void requestOtp();

    void requestOtpWithCall();

    void onDestroyView();
}
