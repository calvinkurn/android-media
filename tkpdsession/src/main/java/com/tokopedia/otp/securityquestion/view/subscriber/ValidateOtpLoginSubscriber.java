package com.tokopedia.otp.securityquestion.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorCode;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.otp.data.model.ValidateOtpLoginDomain;
import com.tokopedia.otp.securityquestion.view.listener.SecurityQuestion;

import rx.Subscriber;

/**
 * @author by nisie on 10/21/17.
 */

public class ValidateOtpLoginSubscriber extends Subscriber<ValidateOtpLoginDomain> {
    private final SecurityQuestion.View viewListener;

    public ValidateOtpLoginSubscriber(SecurityQuestion.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        viewListener.dismissLoadingProgress();
        viewListener.onErrorValidateOtp(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(ValidateOtpLoginDomain validateOTPLoginDomain) {
        viewListener.dismissLoadingProgress();
        if (validateOTPLoginDomain.getValidateOtpDomain().isSuccess()
                && validateOTPLoginDomain.getMakeLoginDomain().isLogin()) {
            if (!validateOTPLoginDomain.getMakeLoginDomain().isMsisdnVerified()) {
                viewListener.onGoToPhoneVerification();
            } else {
                viewListener.onSuccessValidateOtp();
            }
        } else {
            viewListener.onErrorValidateOtp(
                    ErrorHandler.getDefaultErrorCodeMessage(ErrorCode.UNSUPPORTED_FLOW));
        }
    }
}
