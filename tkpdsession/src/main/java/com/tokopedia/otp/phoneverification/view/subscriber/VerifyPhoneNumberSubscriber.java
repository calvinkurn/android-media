package com.tokopedia.otp.phoneverification.view.subscriber;

import com.tokopedia.network.ErrorCode;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.otp.phoneverification.data.model.ValidateVerifyPhoneNumberDomain;
import com.tokopedia.otp.phoneverification.view.listener.PhoneVerification;

import rx.Subscriber;

/**
 * @author by nisie on 10/24/17.
 */

public class VerifyPhoneNumberSubscriber extends Subscriber<ValidateVerifyPhoneNumberDomain> {
    private final PhoneVerification.View viewListener;

    public VerifyPhoneNumberSubscriber(PhoneVerification.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorVerifyPhoneNumber(ErrorHandler.getErrorMessageWithErrorCode(viewListener.getActivity(), e));
    }

    @Override
    public void onNext(ValidateVerifyPhoneNumberDomain validateVerifyPhoneNumberDomain) {
        if (validateVerifyPhoneNumberDomain.getValidateOtpDomain().isSuccess()
                && validateVerifyPhoneNumberDomain.getVerifyPhoneDomain().isSuccess()) {
            viewListener.onSuccessVerifyPhoneNumber();
        } else {
            viewListener.onErrorVerifyPhoneNumber(ErrorHandler.getDefaultErrorCodeMessage
                    (ErrorCode.UNSUPPORTED_FLOW));
        }
    }
}
