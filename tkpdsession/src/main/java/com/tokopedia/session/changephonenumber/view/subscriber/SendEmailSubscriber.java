package com.tokopedia.session.changephonenumber.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorCode;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.session.changephonenumber.view.listener
        .ChangePhoneNumberEmailVerificationFragmentListener;

import rx.Subscriber;

/**
 * Created by milhamj on 28/12/17.
 */

public class SendEmailSubscriber extends Subscriber<Boolean> {
    private final ChangePhoneNumberEmailVerificationFragmentListener.View view;

    public SendEmailSubscriber(ChangePhoneNumberEmailVerificationFragmentListener.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.dismissLoading();
        view.onSendEmailError(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(Boolean isSuccess) {
        view.dismissLoading();
        if (isSuccess)
            view.onSendEmailSuccess();
        else
            view.onSendEmailError(ErrorHandler.getDefaultErrorCodeMessage(
                    ErrorCode.UNSUPPORTED_FLOW));

    }
}
