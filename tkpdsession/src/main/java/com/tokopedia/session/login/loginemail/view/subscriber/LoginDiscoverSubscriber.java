package com.tokopedia.session.login.loginemail.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ResponseStatus;
import com.tokopedia.network.ErrorCode;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.session.data.viewmodel.DiscoverViewModel;
import com.tokopedia.session.login.loginemail.view.viewlistener.Login;

import rx.Subscriber;

/**
 * @author by nisie on 12/19/17.
 */

public class LoginDiscoverSubscriber extends Subscriber<DiscoverViewModel> {

    private final Login.View view;

    public LoginDiscoverSubscriber(Login.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.dismissLoadingDiscover();
        if (e instanceof RuntimeException && e.getLocalizedMessage() != null && !e.getLocalizedMessage().isEmpty() && e.getLocalizedMessage().length() <= 3) {
            int code = Integer.parseInt(e.getLocalizedMessage());
            if (code == ResponseStatus.SC_FORBIDDEN) {
                view.onForbidden();
            } else {
                view.onErrorDiscoverLogin(ErrorHandler.getErrorMessageWithErrorCode(view.getContext(), e));
            }
        } else {
            view.onErrorDiscoverLogin(ErrorHandler.getErrorMessageWithErrorCode(view.getContext(), e));
        }
    }

    @Override
    public void onNext(DiscoverViewModel discoverViewModel) {
        view.dismissLoadingDiscover();
        if (!discoverViewModel.getProviders().isEmpty()) {
            view.onSuccessDiscoverLogin(discoverViewModel.getProviders());
        } else {
            view.onErrorDiscoverLogin(ErrorHandler.getDefaultErrorCodeMessage(ErrorCode.UNSUPPORTED_FLOW));
        }
    }
}
