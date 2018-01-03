package com.tokopedia.session.changephonenumber.view.subscriber;

import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberEmailFragmentListener;

import rx.Subscriber;

/**
 * Created by milhamj on 28/12/17.
 */

public class SendEmailSubscriber extends Subscriber<Boolean> {
    private final ChangePhoneNumberEmailFragmentListener.View view;

    public SendEmailSubscriber(ChangePhoneNumberEmailFragmentListener.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e != null)
            view.onSendEmailError(e.getMessage());
        else
            view.onSendEmailFailed();
    }

    @Override
    public void onNext(Boolean isSuccess) {
        view.onSendEmailSuccess(isSuccess);
    }
}
