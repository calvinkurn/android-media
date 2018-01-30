package com.tokopedia.otp.phoneverification.view.listener;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by nisie on 2/23/17.
 */
public interface PhoneVerification {

    interface View extends CustomerView {

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

    interface Presenter extends CustomerPresenter<View> {
        void verifyPhoneNumber(String otpCode, String phoneNumber);

        void requestOtp();

        void requestOtpWithCall();

    }
}
