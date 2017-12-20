package com.tokopedia.session.login.loginemail.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorCode;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.profile.model.GetUserInfoDomainModel;
import com.tokopedia.session.data.viewmodel.login.MakeLoginDomain;
import com.tokopedia.session.login.loginemail.view.viewlistener.Login;
import com.tokopedia.session.register.domain.model.LoginSosmedDomain;

import rx.Subscriber;

/**
 * @author by nisie on 12/20/17.
 */

public class LoginSosmedSubscriber extends Subscriber<LoginSosmedDomain> {
    private final Login.View view;

    public LoginSosmedSubscriber(Login.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {
        
    }

    @Override
    public void onError(Throwable e) {
        view.dismissLoadingLogin();
        view.onErrorLogin(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(LoginSosmedDomain loginSosmedDomain) {
        view.dismissLoadingLogin();
        if (!loginSosmedDomain.getInfo().getGetUserInfoDomainData().isCreatedPassword()) {
            view.onGoToCreatePasswordPage(loginSosmedDomain.getInfo()
                    .getGetUserInfoDomainData());
        } else if (loginSosmedDomain.getMakeLoginModel() != null
                && !isGoToSecurityQuestion(loginSosmedDomain.getMakeLoginModel())
                && isMsisdnVerified(loginSosmedDomain.getInfo())) {
            view.setSmartLock();
            view.onSuccessLogin();
        } else if (!isGoToSecurityQuestion(loginSosmedDomain.getMakeLoginModel())
                && !isMsisdnVerified(loginSosmedDomain.getInfo())) {
            view.setSmartLock();
            view.onGoToPhoneVerification();
        } else if (isGoToSecurityQuestion(loginSosmedDomain.getMakeLoginModel())) {
            view.setSmartLock();
            view.onGoToSecurityQuestion(
                    loginSosmedDomain.getMakeLoginModel().getSecurityDomain(),
                    loginSosmedDomain.getMakeLoginModel().getFullName(),
                    loginSosmedDomain.getInfo().getGetUserInfoDomainData().getEmail(),
                    loginSosmedDomain.getInfo().getGetUserInfoDomainData().getPhone());
        } else {
            view.resetToken();
            view.onErrorLogin(ErrorHandler.getDefaultErrorCodeMessage(ErrorCode.UNSUPPORTED_FLOW));
        }
    }

    private boolean isMsisdnVerified(GetUserInfoDomainModel info) {
        return info.getGetUserInfoDomainData().isPhoneVerified();
    }

    private boolean isGoToSecurityQuestion(MakeLoginDomain makeLoginModel) {
        return !makeLoginModel.isLogin() && makeLoginModel.getSecurityDomain() != null;
    }
}
