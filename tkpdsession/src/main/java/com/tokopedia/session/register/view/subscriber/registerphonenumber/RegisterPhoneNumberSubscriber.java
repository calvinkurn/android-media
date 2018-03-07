package com.tokopedia.session.register.view.subscriber.registerphonenumber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.session.register.data.model.RegisterPhoneNumberModel;
import com.tokopedia.session.register.view.viewlistener.RegisterPhoneNumber;

import rx.Subscriber;

/**
 * @author by yfsx on 07/03/18.
 */

public class RegisterPhoneNumberSubscriber extends Subscriber<RegisterPhoneNumberModel> {
    private RegisterPhoneNumber.View mainView;

    public RegisterPhoneNumberSubscriber(RegisterPhoneNumber.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable throwable) {
        mainView.dismissLoading();
        mainView.showErrorRegisterPhoneNumber(ErrorHandler.getErrorMessage(mainView.getContext(), throwable));

    }

    @Override
    public void onNext(RegisterPhoneNumberModel registerPhoneNumberModel) {
        mainView.dismissLoading();
        mainView.showSuccessRegisterPhoneNumber(registerPhoneNumberModel);
    }
}
