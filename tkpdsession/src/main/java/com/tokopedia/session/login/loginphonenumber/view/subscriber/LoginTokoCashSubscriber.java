package com.tokopedia.session.login.loginphonenumber.view.subscriber;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.response.ResponseStatus;
import com.tokopedia.network.ErrorCode;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.otp.tokocashotp.view.viewmodel.LoginTokoCashViewModel;
import com.tokopedia.session.data.viewmodel.login.MakeLoginDomain;
import com.tokopedia.session.login.loginphonenumber.view.viewlistener.ChooseTokocashAccount;
import com.tokopedia.session.login.loginphonenumber.view.viewmodel.AccountTokocash;

import rx.Subscriber;

/**
 * @author by nisie on 12/5/17.
 */

public class LoginTokoCashSubscriber extends Subscriber<LoginTokoCashViewModel> {

    private final ChooseTokocashAccount.View view;
    private final AccountTokocash accountTokocash;

    public LoginTokoCashSubscriber(ChooseTokocashAccount.View view,
                                   AccountTokocash accountTokocash) {
        this.view = view;
        this.accountTokocash = accountTokocash;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.dismissLoadingProgress();
        ErrorHandler.getErrorMessage(new ErrorHandler.ErrorForbiddenListener() {
            @Override
            public void onForbidden() {
                view.onForbidden();
            }

            @Override
            public void onError(String errorMessage) {
                view.onErrorLoginTokoCash(errorMessage);
            }
        }, e, view.getContext());
    }

    @Override
    public void onNext(LoginTokoCashViewModel loginTokoCashViewModel) {
        view.dismissLoadingProgress();
        if (canGoToSecurityQuestion(loginTokoCashViewModel.getMakeLoginDomain())) {
            view.goToSecurityQuestion(accountTokocash,
                    loginTokoCashViewModel);
        } else if (loginTokoCashViewModel.getMakeLoginDomain().isLogin()) {
            view.onSuccessLogin();
        } else {
            view.onErrorLoginTokoCash(ErrorHandler.getDefaultErrorCodeMessage(ErrorCode.UNSUPPORTED_FLOW));
        }

    }

    private boolean canGoToSecurityQuestion(MakeLoginDomain makeLoginDomain) {
        return makeLoginDomain.getSecurityDomain().getAllowLogin() == 0
                && !makeLoginDomain.isLogin();
    }
}
