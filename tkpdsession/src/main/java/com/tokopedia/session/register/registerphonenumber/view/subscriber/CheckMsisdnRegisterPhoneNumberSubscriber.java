package com.tokopedia.session.register.registerphonenumber.view.subscriber;

import com.tokopedia.network.ErrorHandler;
import com.tokopedia.session.register.registerphonenumber.domain.model.CheckMsisdnDomain;
import com.tokopedia.session.register.registerphonenumber.view.listener.RegisterPhoneNumber;

import rx.Subscriber;

/**
 * @author by yfsx on 26/2/18.
 */

public class CheckMsisdnRegisterPhoneNumberSubscriber extends Subscriber<CheckMsisdnDomain> {
    private final RegisterPhoneNumber.View view;
    private final String phoneNumber;

    public CheckMsisdnRegisterPhoneNumberSubscriber(RegisterPhoneNumber.View view, String phoneNumber) {
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
    public void onNext(CheckMsisdnDomain model) {
        view.dismissLoading();
        if (!model.isExist()) {
            view.showConfirmationPhoneNumber(phoneNumber);
        } else {
            view.showAlreadyRegisteredDialog(phoneNumber);
        }
    }
}
