package com.tokopedia.session.changephonenumber.view.subscriber;

import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberInputFragmentListener;

import rx.Subscriber;

/**
 * Created by milhamj on 03/01/18.
 */

public class ValidateNumberSubscriber extends Subscriber<Boolean> {
    private final ChangePhoneNumberInputFragmentListener.View view;

    public ValidateNumberSubscriber(ChangePhoneNumberInputFragmentListener.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.dismissLoading();
        if (e != null) {
            view.onValidateNumberError(e.getMessage());
        } else {
            view.onValidateNumberFailed();
        }
    }

    @Override
    public void onNext(Boolean aBoolean) {
        view.dismissLoading();
        view.onValidateNumberSuccess(aBoolean);
    }
}
