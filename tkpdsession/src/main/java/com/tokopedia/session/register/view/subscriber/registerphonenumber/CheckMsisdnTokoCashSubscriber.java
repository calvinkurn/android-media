package com.tokopedia.session.register.view.subscriber.registerphonenumber;

import com.tokopedia.network.ErrorHandler;
import com.tokopedia.session.login.loginphonenumber.view.viewmodel.CheckMsisdnTokoCashViewModel;
import com.tokopedia.session.register.view.viewlistener.RegisterPhoneNumber;

import rx.Subscriber;

/**
 * @author by yfsx on 26/2/18.
 */

public class CheckMsisdnTokoCashSubscriber extends Subscriber<CheckMsisdnTokoCashViewModel> {
    private final RegisterPhoneNumber.View view;
    private final String phoneNumber;

    public CheckMsisdnTokoCashSubscriber(RegisterPhoneNumber.View view, String phoneNumber) {
        this.view = view;
        this.phoneNumber = phoneNumber;
    }


    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.dismissLoading();
        view.showErrorPhoneNumber(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(CheckMsisdnTokoCashViewModel checkMsisdnTokoCashViewModel) {
        view.dismissLoading();
        if (!checkMsisdnTokoCashViewModel.isTokopediaAccountExist()) {
            view.goToVerifyAccountPage(phoneNumber);
        } else {
            view.showAlreadyRegisteredDialog();
        }
    }
}
