package com.tokopedia.session.login.loginphonenumber.view.subscriber;

import com.tokopedia.network.ErrorHandler;
import com.tokopedia.session.R;
import com.tokopedia.session.login.loginphonenumber.view.viewlistener.LoginPhoneNumber;
import com.tokopedia.session.login.loginphonenumber.view.viewmodel.CheckMsisdnTokoCashViewModel;

import rx.Subscriber;

/**
 * @author by nisie on 12/6/17.
 */

public class CheckMsisdnTokoCashSubscriber extends Subscriber<CheckMsisdnTokoCashViewModel> {
    private final LoginPhoneNumber.View view;
    private final String phoneNumber;

    public CheckMsisdnTokoCashSubscriber(LoginPhoneNumber.View view, String phoneNumber) {
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
        if (checkMsisdnTokoCashViewModel.isTokopediaAccountExist()) {
            view.goToVerifyAccountPage(phoneNumber);
        } else {
            view.showErrorPhoneNumber(R.string.phone_number_not_registered);
        }
    }
}
