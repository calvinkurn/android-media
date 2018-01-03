package com.tokopedia.session.changephonenumber.view.subscriber;

import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberInputFragmentListener;

import rx.Subscriber;

/**
 * Created by milhamj on 03/01/18.
 */

public class SubmitNumberSubscriber extends Subscriber<Boolean> {
    private final ChangePhoneNumberInputFragmentListener.View view;

    public SubmitNumberSubscriber(ChangePhoneNumberInputFragmentListener.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.dismissLoading();
        if (e != null) {
            view.onSubmitNumberError(e.getMessage());
        } else {
            view.onSubmitNumberFailed();
        }
    }

    @Override
    public void onNext(Boolean aBoolean) {
        view.dismissLoading();
        view.onSubmitNumberSuccess(aBoolean);
    }
}
