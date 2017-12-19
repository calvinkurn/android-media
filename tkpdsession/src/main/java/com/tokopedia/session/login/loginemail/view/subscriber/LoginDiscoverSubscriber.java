package com.tokopedia.session.login.loginemail.view.subscriber;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.response.ErrorCode;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.session.R;
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
        view.onErrorDiscoverLogin(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(DiscoverViewModel discoverViewModel) {
        view.dismissLoadingDiscover();
        if (!discoverViewModel.getProviders().isEmpty()) {
            view.onSuccessDiscoverLogin(discoverViewModel.getProviders());
        } else {
            view.onErrorDiscoverLogin(MainApplication.getAppContext().getString(R
                    .string.error_empty_provider) + " " + MainApplication.getAppContext().getString(R
                    .string.code_error) + " " + ErrorCode.UNSUPPORTED_FLOW);
        }
    }
}
