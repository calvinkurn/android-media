package com.tokopedia.otp.cotp.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorCode;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.otp.cotp.view.viewlistener.Verification;
import com.tokopedia.otp.data.model.ValidateOtpDomain;

import rx.Subscriber;

/**
 * @author by nisie on 12/4/17.
 */

public class VerifyOtpSubscriber extends Subscriber<ValidateOtpDomain> {
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
    public void onNext(ValidateOtpDomain validateOtpDomain) {
        view.dismissLoadingProgress();
        if (validateOtpDomain.isSuccess())
            view.onSuccessVerifyOTP();
        else {
            view.onErrorVerifyOtp(ErrorHandler.getDefaultErrorCodeMessage(ErrorCode.UNSUPPORTED_FLOW));
        }
    }
}
