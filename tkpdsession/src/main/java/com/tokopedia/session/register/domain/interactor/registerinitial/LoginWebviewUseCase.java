package com.tokopedia.session.register.domain.interactor.registerinitial;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;
import com.tokopedia.session.domain.interactor.GetTokenUseCase;
import com.tokopedia.session.domain.interactor.MakeLoginUseCase;
import com.tokopedia.session.register.domain.model.LoginSosmedDomain;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by nisie on 10/16/17.
 */

public class LoginWebviewUseCase extends LoginWithSosmedUseCase {

    @Inject
    public LoginWebviewUseCase(SessionHandler sessionHandler,
                               GetTokenUseCase getTokenUseCase,
                               GetUserInfoUseCase getUserInfoUseCase,
                               MakeLoginUseCase makeLoginUseCase) {
        super(sessionHandler, getTokenUseCase,
                getUserInfoUseCase, makeLoginUseCase);
    }

    @Override
    public Observable<LoginSosmedDomain> createObservable(final RequestParams requestParams) {
        LoginSosmedDomain registerSosmedDomain = new LoginSosmedDomain();
        return getToken(registerSosmedDomain,
                GetTokenUseCase.getParamRegisterWebview(
                        requestParams.getString(GetTokenUseCase.CODE, ""),
                        requestParams.getString(GetTokenUseCase.REDIRECT_URI, "")
                ))
                .flatMap(new Func1<LoginSosmedDomain, Observable<LoginSosmedDomain>>() {
                    @Override
                    public Observable<LoginSosmedDomain> call(LoginSosmedDomain registerSosmedDomain) {
                        return getInfo(registerSosmedDomain);
                    }
                })
                .flatMap(new Func1<LoginSosmedDomain, Observable<LoginSosmedDomain>>() {
                    @Override
                    public Observable<LoginSosmedDomain> call(LoginSosmedDomain registerSosmedDomain) {
                        if (registerSosmedDomain.getInfo().getGetUserInfoDomainData().isCreatedPassword()) {
                            return makeLogin(registerSosmedDomain);
                        } else {
                            return Observable.just(registerSosmedDomain);
                        }
                    }
                });
    }

    public static RequestParams getParamWebview(String code, String redirectUri) {
        RequestParams params = RequestParams.create();
        params.putAll(GetTokenUseCase.getParamRegisterWebview(code, redirectUri).getParameters());
        return params;
    }
}
