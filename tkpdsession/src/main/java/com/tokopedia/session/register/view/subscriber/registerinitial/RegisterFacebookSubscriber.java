package com.tokopedia.session.register.view.subscriber.registerinitial;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.session.register.domain.model.RegisterFacebookDomain;
import com.tokopedia.session.register.view.viewlistener.RegisterInitial;

import rx.Subscriber;

/**
 * @author by nisie on 10/12/17.
 */

public class RegisterFacebookSubscriber extends Subscriber<RegisterFacebookDomain> {
    private final RegisterInitial.View viewListener;

    public RegisterFacebookSubscriber(RegisterInitial.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorRegisterFacebook(ErrorHandler.getErrorMessage(e));
        viewListener.clearToken();
    }

    @Override
    public void onNext(RegisterFacebookDomain registerFacebookDomain) {
        if (registerFacebookDomain.getInfo().getGetUserInfoDomainData().isCreatedPassword()) {
            viewListener.onGoToLogin();
        } else {
            viewListener.onGoToCreatePasswordPage(registerFacebookDomain.getInfo()
                    .getGetUserInfoDomainData());
        }
    }
}
