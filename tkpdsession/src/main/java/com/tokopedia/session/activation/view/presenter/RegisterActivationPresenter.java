package com.tokopedia.session.activation.view.presenter;

/**
 * Created by nisie on 1/31/17.
 */
public interface RegisterActivationPresenter {
    void resendActivation();

    void activateAccount();

    void unsubscribeObservable();
}
