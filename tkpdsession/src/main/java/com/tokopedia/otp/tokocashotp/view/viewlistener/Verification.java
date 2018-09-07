package com.tokopedia.otp.tokocashotp.view.viewlistener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.otp.tokocashotp.view.viewmodel.VerificationViewModel;
import com.tokopedia.otp.tokocashotp.view.viewmodel.VerifyOtpTokoCashViewModel;
import com.tokopedia.session.login.loginphonenumber.view.viewlistener.ChooseTokocashAccount;

/**
 * @author by nisie on 11/30/17.
 */

public interface Verification {
    interface View extends CustomerView, ChooseTokocashAccount.View {
        void onSuccessGetOTP();

        void onSuccessVerifyOTP(VerifyOtpTokoCashViewModel verifyOtpTokoCashViewModel);

        void onErrorGetOTP(String errorMessage);

        void onErrorVerifyOtp(String errorMessage);

        void showLoadingProgress();

        void dismissLoadingProgress();

        void onErrorNoAccountTokoCash();

        boolean isCountdownFinished();

        void dropKeyboard();

        Context getContext();
    }

    interface Presenter extends CustomerPresenter<Verification.View> {

        void requestOTP(VerificationViewModel bundle);

        void verifyOtp(String phoneNumber, String otpCode);

        void autoLogin(String key, VerifyOtpTokoCashViewModel viewModel);
    }
}
