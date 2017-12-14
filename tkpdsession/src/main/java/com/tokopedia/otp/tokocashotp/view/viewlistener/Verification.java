package com.tokopedia.otp.tokocashotp.view.viewlistener;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.otp.tokocashotp.view.viewmodel.VerificationViewModel;
import com.tokopedia.otp.tokocashotp.view.viewmodel.VerifyOtpTokoCashViewModel;

/**
 * @author by nisie on 11/30/17.
 */

public interface Verification {
    interface View extends CustomerView {
        void onSuccessGetOTP();

        void onSuccessVerifyOTP(VerifyOtpTokoCashViewModel verifyOtpTokoCashViewModel);

        void onErrorGetOTP(String errorMessage);

        void onErrorVerifyOtp(String errorMessage);

        void showLoadingProgress();

        void dismissLoadingProgress();

        void onErrorNoAccountTokoCash();

        boolean isCountdownFinished();

        void dropKeyboard();
    }

    interface Presenter extends CustomerPresenter<Verification.View> {

        void requestOTP(VerificationViewModel bundle);

        void verifyOtp(String phoneNumber, String otpCode);
    }
}
