package com.tokopedia.otp.phoneverification.view.listener;

import android.app.Activity;

/**
 * Created by nisie on 2/23/17.
 */
public interface PhoneVerificationFragmentView {
    void onSuccessRequestOtp(String status);

    Activity getActivity();

    String getPhoneNumber();

    String getString(int resId);

    void onErrorRequestOTP(String errorMessage);

    void showProgressDialog();

    void onSuccessVerifyPhoneNumber();

    void showErrorPhoneNumber(String errorMessage);

    String getOTPCode();

    void onErrorVerifyPhoneNumber(String errorMessage);

    void setViewEnabled(boolean isEnabled);
}
