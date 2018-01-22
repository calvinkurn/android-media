package com.tokopedia.otp.cotp.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorCode;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.otp.cotp.view.viewlistener.Verification;
import com.tokopedia.otp.data.model.ValidateOtpLoginDomain;
import com.tokopedia.session.R;

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
        view.onErrorVerifyLogin(ErrorHandler.getErrorMessageWithErrorCode(e));
    }

    @Override
    public void onNext(ValidateOtpLoginDomain validateOTPLoginDomain) {
        view.dismissLoadingProgress();
        if (!validateOTPLoginDomain.getValidateOtpDomain().isSuccess()) {
            view.onErrorVerifyOtpCode(R.string.default_request_error_unknown);
        } else if (validateOTPLoginDomain.getValidateOtpDomain().isSuccess()
                && validateOTPLoginDomain.getMakeLoginDomain().isLogin()) {
            if (!validateOTPLoginDomain.getMakeLoginDomain().isMsisdnVerified()) {
                view.onGoToPhoneVerification();
            } else {
                view.onSuccessVerifyOTP();
            }
        } else {
            view.onErrorVerifyLogin(ErrorHandler.getDefaultErrorCodeMessage(ErrorCode.UNSUPPORTED_FLOW));
        }
    }
}
