package com.tokopedia.session.login.loginphonenumber.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorCode;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.otp.tokocashotp.view.viewmodel.LoginTokoCashViewModel;
import com.tokopedia.session.data.viewmodel.login.MakeLoginDomain;
import com.tokopedia.session.login.loginphonenumber.view.viewlistener.ChooseTokocashAccount;

import rx.Subscriber;

/**
 * @author by nisie on 12/5/17.
 */

public class LoginTokoCashSubscriber extends Subscriber<LoginTokoCashViewModel> {

    private final ChooseTokocashAccount.View view;

    public LoginTokoCashSubscriber(ChooseTokocashAccount.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.dismissLoadingProgress();
        view.onErrorLoginTokoCash(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(LoginTokoCashViewModel loginTokoCashViewModel) {
        view.dismissLoadingProgress();
        if (goToSecurityQuestion(loginTokoCashViewModel.getMakeLoginDomain())) {
            view.goToSecurityQuestion(loginTokoCashViewModel.getMakeLoginDomain());
        } else if (loginTokoCashViewModel.getMakeLoginDomain().isLogin()) {
            view.onSuccessLogin();
        } else {
            view.onErrorLoginTokoCash(ErrorHandler.getDefaultErrorCodeMessage(ErrorCode.UNSUPPORTED_FLOW));
        }

    }

    private boolean goToSecurityQuestion(MakeLoginDomain makeLoginDomain) {
        return makeLoginDomain.getSecurityDomain().getAllowLogin() == 0
                && !makeLoginDomain.isLogin();
    }
}
