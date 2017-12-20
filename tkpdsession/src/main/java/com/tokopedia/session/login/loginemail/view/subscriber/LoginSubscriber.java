package com.tokopedia.session.login.loginemail.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorCode;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.session.data.viewmodel.login.MakeLoginDomain;
import com.tokopedia.session.login.loginemail.domain.model.LoginEmailDomain;
import com.tokopedia.session.login.loginemail.view.viewlistener.Login;

import rx.Subscriber;

/**
 * @author by nisie on 12/19/17.
 */

public class LoginSubscriber extends Subscriber<LoginEmailDomain> {
    private static final String NOT_ACTIVATED = "belum diaktivasi";
    private final Login.View view;

    public LoginSubscriber(Login.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.dismissLoadingLogin();
        if (e.getLocalizedMessage().toLowerCase().contains(NOT_ACTIVATED)) {
            view.onGoToActivationPage();
        } else {
            view.onErrorLogin(ErrorHandler.getErrorMessage(e));
        }
    }

    @Override
    public void onNext(LoginEmailDomain loginEmailDomain) {
        view.dismissLoadingLogin();
        if (goToSecurityQuestion(loginEmailDomain.getLoginResult())) {
            view.setSmartLock();
            view.onGoToSecurityQuestion(loginEmailDomain.getLoginResult().getSecurityDomain(),
                    loginEmailDomain.getInfo().getGetUserInfoDomainData().getFullName(),
                    loginEmailDomain.getInfo().getGetUserInfoDomainData().getEmail(),
                    loginEmailDomain.getInfo().getGetUserInfoDomainData().getPhone());
        } else if (loginEmailDomain.getLoginResult().isLogin()) {
            view.setSmartLock();
            view.onSuccessLogin();
        } else {
            view.resetToken();
            view.onErrorLogin(ErrorHandler.getDefaultErrorCodeMessage(ErrorCode.UNSUPPORTED_FLOW));
        }
    }


    private boolean goToSecurityQuestion(MakeLoginDomain makeLoginDomain) {
        return makeLoginDomain.getSecurityDomain().getAllowLogin() == 0
                && !makeLoginDomain.isLogin();
    }
}
