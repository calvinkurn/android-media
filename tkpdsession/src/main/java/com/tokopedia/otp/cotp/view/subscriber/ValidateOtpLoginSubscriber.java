package com.tokopedia.otp.cotp.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorCode;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.otp.cotp.view.viewlistener.Verification;
import com.tokopedia.otp.data.model.ValidateOtpLoginDomain;

import rx.Subscriber;

/**
 * @author by nisie on 12/28/17.
 */

public class ValidateOtpLoginSubscriber extends Subscriber<ValidateOtpLoginDomain> {
    private final Verification.View view;

    public ValidateOtpLoginSubscriber(Verification.View view) {
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
    public void onNext(ValidateOtpLoginDomain validateOTPLoginDomain) {
        view.dismissLoadingProgress();
        if (validateOTPLoginDomain.getValidateOtpDomain().isSuccess()
                && validateOTPLoginDomain.getMakeLoginDomain().isLogin()
                && !validateOTPLoginDomain.getMakeLoginDomain().isMsisdnVerified()) {
            view.onGoToPhoneVerification();
        } else if (validateOTPLoginDomain.getValidateOtpDomain().isSuccess()
                && validateOTPLoginDomain.getMakeLoginDomain().isLogin()
                && validateOTPLoginDomain.getMakeLoginDomain().isMsisdnVerified())
            view.onSuccessVerifyOTP();
        else {
            view.onErrorVerifyOtp(
                    ErrorHandler.getDefaultErrorCodeMessage(ErrorCode.UNSUPPORTED_FLOW));
        }
    }
}
