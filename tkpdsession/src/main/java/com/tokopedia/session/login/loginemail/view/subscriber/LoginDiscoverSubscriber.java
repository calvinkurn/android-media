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
        ErrorHandler.getErrorMessage(new ErrorHandler.ErrorForbiddenListener() {
            @Override
            public void onForbidden() {
                view.onForbidden();
            }

            @Override
            public void onError(String errorMessage) {
                view.onErrorDiscoverLogin(errorMessage);
            }
        }, e, view.getContext());
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
