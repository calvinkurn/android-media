package com.tokopedia.session.register.view.subscriber.registerinitial;

import com.tokopedia.core.network.retrofit.response.ErrorCode;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.profile.model.GetUserInfoDomainModel;
import com.tokopedia.session.data.viewmodel.login.MakeLoginDomain;
import com.tokopedia.session.register.domain.model.RegisterSosmedDomain;
import com.tokopedia.session.register.view.viewlistener.RegisterInitial;

import rx.Subscriber;

/**
 * @author by nisie on 10/12/17.
 */

public class RegisterSosmedSubscriber extends Subscriber<RegisterSosmedDomain> {
    private final RegisterInitial.View viewListener;

    public RegisterSosmedSubscriber(RegisterInitial.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.dismissProgressBar();

        viewListener.onErrorRegisterSosmed(ErrorHandler.getErrorMessage(e));
        viewListener.clearToken();
    }

    @Override
    public void onNext(RegisterSosmedDomain registerSosmedDomain) {
        viewListener.dismissProgressBar();
        if (!registerSosmedDomain.getInfo().getGetUserInfoDomainData().isCreatedPassword()) {
            viewListener.onGoToCreatePasswordPage(registerSosmedDomain.getInfo()
                    .getGetUserInfoDomainData());
        } else if (registerSosmedDomain.getMakeLoginModel() != null
                && !isGoToSecurityQuestion(registerSosmedDomain.getMakeLoginModel())
                && isMsisdnVerified(registerSosmedDomain.getInfo())) {
            viewListener.onSuccessLogin();
        } else if (!isGoToSecurityQuestion(registerSosmedDomain.getMakeLoginModel())
                && !isMsisdnVerified(registerSosmedDomain.getInfo())) {
            viewListener.onGoToPhoneVerification();
        } else if (isGoToSecurityQuestion(registerSosmedDomain.getMakeLoginModel())) {
            viewListener.onGoToSecurityQuestion(
                    registerSosmedDomain.getMakeLoginModel().getSecurityDomain(),
                    registerSosmedDomain.getMakeLoginModel().getFullName(),
                    registerSosmedDomain.getInfo().getGetUserInfoDomainData().getEmail(),
                    registerSosmedDomain.getInfo().getGetUserInfoDomainData().getPhone());
        } else {
            viewListener.onErrorRegisterSosmed(ErrorHandler.getDefaultErrorCodeMessage(ErrorCode.UNSUPPORTED_FLOW));
            viewListener.clearToken();
        }
    }

    private boolean isMsisdnVerified(GetUserInfoDomainModel info) {
        return info.getGetUserInfoDomainData().isPhoneVerified();
    }

    private boolean isGoToSecurityQuestion(MakeLoginDomain makeLoginModel) {
        return !makeLoginModel.isLogin() && makeLoginModel.getSecurityDomain() != null;
    }
}
