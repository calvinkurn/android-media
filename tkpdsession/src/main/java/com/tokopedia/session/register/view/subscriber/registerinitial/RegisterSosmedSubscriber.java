package com.tokopedia.session.register.view.subscriber.registerinitial;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.profile.model.GetUserInfoDomainModel;
import com.tokopedia.core.util.BranchSdkUtils;
import com.tokopedia.network.ErrorCode;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.session.data.viewmodel.login.MakeLoginDomain;
import com.tokopedia.session.register.domain.model.LoginSosmedDomain;
import com.tokopedia.session.register.view.viewlistener.RegisterInitial;

import rx.Subscriber;

/**
 * @author by nisie on 10/12/17.
 */

public class RegisterSosmedSubscriber extends Subscriber<LoginSosmedDomain> {
    private final RegisterInitial.View viewListener;
    private final String methodName;

    public RegisterSosmedSubscriber(String methodName, RegisterInitial.View viewListener) {
        this.viewListener = viewListener;
        this.methodName = methodName;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.dismissProgressBar();
        ErrorHandler.getErrorMessage(new ErrorHandler.ErrorForbiddenListener() {
            @Override
            public void onForbidden() {
                viewListener.onForbidden();
            }

            @Override
            public void onError(String errorMessage) {
                viewListener.onErrorRegisterSosmed(errorMessage);
            }
        }, e, MainApplication.getAppContext());
    }

    @Override
    public void onNext(LoginSosmedDomain registerSosmedDomain) {
        if (!registerSosmedDomain.getInfo().getGetUserInfoDomainData().isCreatedPassword()) {
            viewListener.onGoToCreatePasswordPage(registerSosmedDomain.getInfo()
                    .getGetUserInfoDomainData());
        } else if (registerSosmedDomain.getMakeLoginModel() != null
                && !isGoToSecurityQuestion(registerSosmedDomain.getMakeLoginModel())
                && isMsisdnVerified(registerSosmedDomain.getInfo())) {
            viewListener.onSuccessRegisterSosmed(methodName);
            sendRegisterEventToBranch(registerSosmedDomain.getInfo());
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
        }
    }

    private boolean isMsisdnVerified(GetUserInfoDomainModel info) {
        return info.getGetUserInfoDomainData().isPhoneVerified();
    }

    private boolean isGoToSecurityQuestion(MakeLoginDomain makeLoginModel) {
        return !makeLoginModel.isLogin() && makeLoginModel.getSecurityDomain() != null;
    }

    private void sendRegisterEventToBranch(GetUserInfoDomainModel userInfoDomainModel){
        if(userInfoDomainModel.getGetUserInfoDomainData()!=null) {
            BranchSdkUtils.sendRegisterEvent(userInfoDomainModel.getGetUserInfoDomainData().getEmail(),
                    userInfoDomainModel.getGetUserInfoDomainData().getPhone());
        }
    }
}
