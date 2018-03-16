package com.tokopedia.session.register.view.subscriber.registerphonenumber;

import com.tokopedia.network.ErrorHandler;
import com.tokopedia.session.register.view.viewlistener.RegisterPhoneNumber;
import com.tokopedia.session.register.view.viewmodel.LoginRegisterPhoneNumberModel;

import rx.Subscriber;

/**
 * @author by yfsx on 07/03/18.
 */

public class RegisterPhoneNumberSubscriber extends Subscriber<LoginRegisterPhoneNumberModel> {
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
        mainView.showErrorRegisterPhoneNumber(ErrorHandler.getErrorMessage(throwable));

    }

    @Override
    public void onNext(LoginRegisterPhoneNumberModel registerPhoneNumberModel) {
        mainView.dismissLoading();
        mainView.showSuccessRegisterPhoneNumber(registerPhoneNumberModel);
    }
}
