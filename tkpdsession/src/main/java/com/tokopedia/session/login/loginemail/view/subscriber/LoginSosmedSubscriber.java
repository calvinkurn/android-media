package com.tokopedia.session.login.loginemail.view.subscriber;

import android.text.TextUtils;

import com.tokopedia.core.network.retrofit.response.ResponseStatus;
import com.tokopedia.core.profile.model.GetUserInfoDomainModel;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.network.ErrorCode;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.session.data.viewmodel.login.MakeLoginDomain;
import com.tokopedia.session.login.loginemail.view.viewlistener.Login;
import com.tokopedia.session.register.domain.model.LoginSosmedDomain;

import rx.Subscriber;

/**
 * @author by nisie on 12/20/17.
 */

public class LoginSosmedSubscriber extends Subscriber<LoginSosmedDomain> {
    private static final String NOT_ACTIVATED = "belum diaktivasi";
    private final Login.View view;
    private final String email;
    private final String loginMethodName;

    public LoginSosmedSubscriber(String loginMethodName, Login.View view, String email) {
        this.view = view;
        this.email = email;
        this.loginMethodName = loginMethodName;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e.getLocalizedMessage() != null
                && e.getLocalizedMessage().toLowerCase().contains(NOT_ACTIVATED)
                && !TextUtils.isEmpty(email)) {
            view.onGoToActivationPage(email);
        } else if (e instanceof RuntimeException && e.getLocalizedMessage() != null && !e.getLocalizedMessage().isEmpty()) {
            int code = Integer.parseInt(e.getLocalizedMessage());
            if (code == ResponseStatus.SC_FORBIDDEN) {
                view.onForbidden();
            } else {
                view.dismissLoadingLogin();
                view.onErrorLogin(ErrorHandler.getErrorMessageWithErrorCode(view.getContext(), e));
            }
        } else {
            view.dismissLoadingLogin();
            view.onErrorLogin(ErrorHandler.getErrorMessageWithErrorCode(view.getContext(), e));
        }
    }

    @Override
    public void onNext(LoginSosmedDomain loginSosmedDomain) {
        if (!loginSosmedDomain.getInfo().getGetUserInfoDomainData().isCreatedPassword()) {
            view.onGoToCreatePasswordPage(loginSosmedDomain.getInfo()
                    .getGetUserInfoDomainData());
        } else if (loginSosmedDomain.getMakeLoginModel() != null
                && !isGoToSecurityQuestion(loginSosmedDomain.getMakeLoginModel())
                && (isMsisdnVerified(loginSosmedDomain.getInfo()) || GlobalConfig.isSellerApp())) {
            view.dismissLoadingLogin();
            view.setSmartLock();
            view.onSuccessLoginSosmed(loginMethodName);
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
            view.dismissLoadingLogin();
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
