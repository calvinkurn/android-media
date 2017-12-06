package com.tokopedia.otp.securityquestion.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorCode;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.otp.data.model.ValidateOTPLoginDomain;
import com.tokopedia.otp.securityquestion.view.listener.SecurityQuestion;

import rx.Subscriber;

/**
 * @author by nisie on 10/21/17.
 */

public class ValidateOtpLoginSubscriber extends Subscriber<ValidateOTPLoginDomain> {
    private final SecurityQuestion.View viewListener;

    public ValidateOtpLoginSubscriber(SecurityQuestion.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {
        viewListener.dismissLoadingProgress();
    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorValidateOtp(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(ValidateOTPLoginDomain validateOTPLoginDomain) {
        if (validateOTPLoginDomain.getValidateOTPDomain().isSuccess()
                && validateOTPLoginDomain.getMakeLoginDomain().isLogin()
                && !validateOTPLoginDomain.getMakeLoginDomain().isMsisdnVerified()) {
            viewListener.onGoToPhoneVerification();
        } else if (validateOTPLoginDomain.getValidateOTPDomain().isSuccess()
                && validateOTPLoginDomain.getMakeLoginDomain().isLogin()
                && validateOTPLoginDomain.getMakeLoginDomain().isMsisdnVerified())
            viewListener.onSuccessValidateOtp();
        else {
            viewListener.onErrorValidateOtp(
                    ErrorHandler.getDefaultErrorCodeMessage(ErrorCode.UNSUPPORTED_FLOW));
        }
    }
}
