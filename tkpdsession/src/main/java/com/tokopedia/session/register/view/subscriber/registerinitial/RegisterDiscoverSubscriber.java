package com.tokopedia.session.register.view.subscriber.registerinitial;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.response.ResponseStatus;
import com.tokopedia.network.ErrorCode;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.session.R;
import com.tokopedia.session.data.viewmodel.DiscoverViewModel;
import com.tokopedia.session.register.view.viewlistener.RegisterInitial;

import rx.Subscriber;

/**
 * @author by nisie on 10/10/17.
 */

public class RegisterDiscoverSubscriber extends Subscriber<DiscoverViewModel> {
    private final RegisterInitial.View viewListener;

    public RegisterDiscoverSubscriber(RegisterInitial.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.dismissLoadingDiscover();
        if (e instanceof RuntimeException && e.getLocalizedMessage() != null && !e.getLocalizedMessage().isEmpty()) {
            int code = Integer.parseInt(e.getLocalizedMessage());
            if (code == ResponseStatus.SC_FORBIDDEN) {
                viewListener.onForbidden();
            } else {
                viewListener.onErrorDiscoverRegister(ErrorHandler.getErrorMessage(e));
            }
        } else {
            viewListener.onErrorDiscoverRegister(ErrorHandler.getErrorMessage(e));
        }
    }

    @Override
    public void onNext(DiscoverViewModel discoverViewModel) {
        viewListener.dismissLoadingDiscover();
        if (!discoverViewModel.getProviders().isEmpty()) {
            viewListener.onSuccessDiscoverRegister(discoverViewModel.getProviders());
        } else {
            viewListener.onErrorDiscoverRegister(MainApplication.getAppContext().getString(R
                    .string.error_empty_provider) + " " + MainApplication.getAppContext().getString(R
                    .string.code_error) + " " + ErrorCode.UNSUPPORTED_FLOW);
        }
    }
}
