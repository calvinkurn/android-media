package com.tokopedia.session.login.loginphonenumber.view.presenter;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.session.login.loginphonenumber.domain.interactor.LoginPhoneNumberUseCase;
import com.tokopedia.session.login.loginphonenumber.view.subscriber.LoginTokoCashSubscriber;
import com.tokopedia.session.login.loginphonenumber.view.viewlistener.ChooseTokocashAccount;
import com.tokopedia.session.login.loginphonenumber.view.viewmodel.AccountTokocash;

import javax.inject.Inject;

/**
 * @author by nisie on 12/4/17.
 */

public class ChooseTokocashAccountPresenter extends BaseDaggerPresenter<ChooseTokocashAccount.View>
        implements ChooseTokocashAccount.Presenter {

    private final LoginPhoneNumberUseCase loginTokoCashUseCase;
    private final SessionHandler sessionHandler;

    @Inject
    public ChooseTokocashAccountPresenter(LoginPhoneNumberUseCase loginTokoCashUseCase,
                                          SessionHandler sessionHandler) {
        this.loginTokoCashUseCase = loginTokoCashUseCase;
        this.sessionHandler = sessionHandler;
    }

    @Override
    public void detachView() {
        super.detachView();
        loginTokoCashUseCase.unsubscribe();
    }

    @Override
    public void loginWithTokocash(String key, AccountTokocash accountTokocash) {
        getView().showLoadingProgress();
        loginTokoCashUseCase.execute(LoginPhoneNumberUseCase.getParam(
                key,
                accountTokocash.getEmail(),
                accountTokocash.getUserId()
        ), new LoginTokoCashSubscriber
                (getView(), accountTokocash));
    }

    @Override
    public void clearToken() {
        sessionHandler.clearToken();
    }
}
