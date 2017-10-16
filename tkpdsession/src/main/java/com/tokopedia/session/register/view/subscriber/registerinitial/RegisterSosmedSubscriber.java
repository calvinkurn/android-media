package com.tokopedia.session.register.view.subscriber.registerinitial;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.session.register.domain.model.RegisterSosmedDomain;
import com.tokopedia.session.register.view.viewlistener.RegisterInitial;

import rx.Subscriber;

/**
 * @author by nisie on 10/12/17.
 */

public class RegisterSosmedSubscriber extends Subscriber<RegisterSosmedDomain> {
    private final RegisterInitial.View viewListener;

    public RegisterSosmedSubscriber(RegisterInitial.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorRegisterSosmed(ErrorHandler.getErrorMessage(e));
        viewListener.clearToken();
    }

    @Override
    public void onNext(RegisterSosmedDomain registerSosmedDomain) {
        if (registerSosmedDomain.getInfo().getGetUserInfoDomainData().isCreatedPassword()) {
            viewListener.onGoToLogin();
        } else {
            viewListener.onGoToCreatePasswordPage(registerSosmedDomain.getInfo()
                    .getGetUserInfoDomainData());
        }
    }
}
