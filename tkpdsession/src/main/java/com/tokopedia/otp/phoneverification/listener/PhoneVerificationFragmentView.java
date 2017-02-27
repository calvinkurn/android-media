package com.tokopedia.otp.phoneverification.listener;

import android.app.Activity;
import android.content.Context;

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

    void onSuccessVerifyOTP();

    void showErrorPhoneNumber(String errorMessage);

    String getOTPCode();

    void onErrorVerifyOTP(String errorMessage);
}
