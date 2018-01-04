package com.tokopedia.session.changephonenumber.view.subscriber;

import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberEmailFragmentListener;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberEmailVerificationFragmentListener;

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
        if (e != null)
            view.onSendEmailError(e.getMessage());
        else
            view.onSendEmailFailed();
    }

    @Override
    public void onNext(Boolean isSuccess) {
        view.dismissLoading();
        view.onSendEmailSuccess(isSuccess);
    }
}
