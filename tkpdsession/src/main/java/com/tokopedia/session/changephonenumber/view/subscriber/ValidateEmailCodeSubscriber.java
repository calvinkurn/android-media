package com.tokopedia.session.changephonenumber.view.subscriber;

import com.tokopedia.network.ErrorCode;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.session.changephonenumber.view.listener
        .ChangePhoneNumberEmailVerificationFragmentListener;

import rx.Subscriber;

/**
 * Created by milhamj on 04/01/18.
 */

public class ValidateEmailCodeSubscriber extends Subscriber<Boolean> {
    private final ChangePhoneNumberEmailVerificationFragmentListener.View view;

    public ValidateEmailCodeSubscriber(ChangePhoneNumberEmailVerificationFragmentListener.View
                                               view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.dismissLoading();
        view.onValidateOtpError(ErrorHandler.getErrorMessageWithErrorCode(view.getContext(), e));
    }

    @Override
    public void onNext(Boolean isSuccess) {
        view.dismissLoading();
        if (isSuccess)
            view.onValidateOtpSuccess();
        else
            view.onValidateOtpError(ErrorHandler.getDefaultErrorCodeMessage(
                    ErrorCode.UNSUPPORTED_FLOW));
    }
}
