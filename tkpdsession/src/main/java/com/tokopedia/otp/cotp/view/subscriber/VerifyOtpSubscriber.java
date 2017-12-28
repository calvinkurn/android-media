package com.tokopedia.otp.cotp.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.otp.cotp.view.viewlistener.Verification;
import com.tokopedia.otp.cotp.view.viewmodel.VerifyOtpViewModel;

import rx.Subscriber;

/**
 * @author by nisie on 12/4/17.
 */

public class VerifyOtpSubscriber extends Subscriber<VerifyOtpViewModel> {
    private final Verification.View view;

    public VerifyOtpSubscriber(Verification.View view) {
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
    public void onNext(VerifyOtpViewModel verifyOtpTokoCashViewModel) {
        view.dismissLoadingProgress();
        if (verifyOtpTokoCashViewModel.isVerified()
                && !verifyOtpTokoCashViewModel.getList().isEmpty())
            view.onSuccessVerifyOTP(verifyOtpTokoCashViewModel);
        else {
            view.onErrorNoAccountTokoCash();
        }
    }
}
