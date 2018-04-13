package com.tokopedia.session.changephonenumber.view.subscriber;

import com.tokopedia.network.ErrorCode;
import com.tokopedia.network.ErrorHandler;
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
        view.onSubmitNumberError(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(Boolean isSuccess) {
        view.dismissLoading();
        if (isSuccess)
            view.onSubmitNumberSuccess();
        else
            view.onSubmitNumberError(ErrorHandler.getDefaultErrorCodeMessage(
                    ErrorCode.UNSUPPORTED_FLOW));
    }
}
