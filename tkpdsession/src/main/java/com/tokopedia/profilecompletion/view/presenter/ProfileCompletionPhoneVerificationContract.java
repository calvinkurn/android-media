package com.tokopedia.profilecompletion.view.presenter;

import android.app.Activity;

import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by stevenfredian on 7/10/17.
 */

public interface ProfileCompletionPhoneVerificationContract {

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

    interface Presenter{
        void verifyPhoneNumber();

        void requestOtp();

        void requestOtpWithCall();

        void onDestroyView();
    }
}
