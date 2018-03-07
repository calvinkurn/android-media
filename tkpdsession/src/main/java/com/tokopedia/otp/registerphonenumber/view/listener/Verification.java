package com.tokopedia.otp.registerphonenumber.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.otp.registerphonenumber.view.viewmodel.VerifyOtpViewModel;

/**
 * @author by yfsx on 5/3/18.
 */

public interface Verification {
    interface View extends CustomerView {
        void onSuccessGetOTP();

        void onSuccessVerifyOTP(VerifyOtpViewModel verifyOtpViewModel);

        void onErrorGetOTP(String errorMessage);

        void onErrorVerifyOtp(String errorMessage);

        void showLoadingProgress();

        void dismissLoadingProgress();

        void onErrorVerifyOtpCode(String error);

        boolean isCountdownFinished();

        void dropKeyboard();

        Context getContext();
    }

    interface Presenter extends CustomerPresenter<Verification.View> {

        void requestOTP(com.tokopedia.otp.registerphonenumber.view.viewmodel.VerificationViewModel bundle);

        void verifyOtp(String phoneNumber, String otpCode);
    }
}
