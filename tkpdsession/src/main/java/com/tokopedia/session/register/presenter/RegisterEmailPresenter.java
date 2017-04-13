package com.tokopedia.session.register.presenter;

/**
 * Created by nisie on 1/27/17.
 */
public interface RegisterEmailPresenter {
    void onRegisterClicked();

    void unsubscribeObservable();

    void startAction(int action);
}
