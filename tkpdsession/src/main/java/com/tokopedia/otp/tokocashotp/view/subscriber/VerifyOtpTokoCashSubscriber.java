package com.tokopedia.otp.tokocashotp.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.otp.tokocashotp.view.viewlistener.Verification;
import com.tokopedia.otp.tokocashotp.view.viewmodel.VerifyOtpTokoCashViewModel;

import rx.Subscriber;

/**
 * @author by nisie on 12/4/17.
 */

public class VerifyOtpTokoCashSubscriber extends Subscriber<VerifyOtpTokoCashViewModel> {
    private final Verification.View view;

    public VerifyOtpTokoCashSubscriber(Verification.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.dismissLoadingProgress();
        view.onErrorVerifyOtp(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(VerifyOtpTokoCashViewModel verifyOtpTokoCashViewModel) {
        view.dismissLoadingProgress();
        if (verifyOtpTokoCashViewModel.isVerified()
                && verifyOtpTokoCashViewModel.isHasTokocashAccount()
                && !verifyOtpTokoCashViewModel.getList().isEmpty())
            view.onSuccessVerifyOTP(verifyOtpTokoCashViewModel);
        else {
            view.onErrorNoAccountTokoCash();
        }
    }
}
