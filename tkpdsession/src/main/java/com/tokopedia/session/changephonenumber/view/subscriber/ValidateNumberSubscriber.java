package com.tokopedia.session.changephonenumber.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorCode;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
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
        view.onValidateNumberError(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(Boolean isSuccess) {
        view.dismissLoading();
        if (isSuccess)
            view.onValidateNumberSuccess();
        else
            view.onValidateNumberError(ErrorHandler.getDefaultErrorCodeMessage(
                    ErrorCode.UNSUPPORTED_FLOW));
    }
}
