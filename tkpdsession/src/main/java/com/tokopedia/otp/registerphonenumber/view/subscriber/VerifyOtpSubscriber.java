package com.tokopedia.otp.registerphonenumber.view.subscriber;

import com.tokopedia.network.ErrorHandler;
import com.tokopedia.otp.registerphonenumber.view.listener.Verification;
import com.tokopedia.otp.registerphonenumber.view.viewmodel.VerifyOtpViewModel;

import rx.Subscriber;

/**
 * @author by yfsx on 5/3/18.
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
        view.onErrorVerifyOtp(ErrorHandler.getErrorMessageWithErrorCode(view.getContext(), e));
    }

    @Override
    public void onNext(VerifyOtpViewModel verifyOtpViewModel) {
        view.dismissLoadingProgress();
        if (verifyOtpViewModel.isVerified()
                && !verifyOtpViewModel.getList().isEmpty())
            view.onSuccessVerifyOTP(verifyOtpViewModel);
        else {
            view.onErrorNoAccountTokoCash();
        }
    }
}
