package com.tokopedia.profilecompletion.view.subscriber;

import com.tokopedia.network.ErrorCode;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.otp.phoneverification.data.model.ValidateVerifyPhoneNumberDomain;
import com.tokopedia.profilecompletion.view.presenter.ProfileCompletionPhoneVerificationContract;

import rx.Subscriber;

/**
 * @author by nisie on 10/24/17.
 */

public class ProfileCompletionVerifyPhoneNumberSubscriber extends Subscriber<ValidateVerifyPhoneNumberDomain> {

    private final ProfileCompletionPhoneVerificationContract.View view;

    public ProfileCompletionVerifyPhoneNumberSubscriber(ProfileCompletionPhoneVerificationContract.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.onErrorVerifyPhoneNumber(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(ValidateVerifyPhoneNumberDomain validateVerifyPhoneNumberDomain) {
        if (validateVerifyPhoneNumberDomain.getValidateOtpDomain().isSuccess()
                && validateVerifyPhoneNumberDomain.getVerifyPhoneDomain().isSuccess()) {
            view.onSuccessVerifyPhoneNumber();
        } else {
            view.onErrorVerifyPhoneNumber(ErrorHandler.getDefaultErrorCodeMessage(ErrorCode.UNSUPPORTED_FLOW));
        }
    }
}
