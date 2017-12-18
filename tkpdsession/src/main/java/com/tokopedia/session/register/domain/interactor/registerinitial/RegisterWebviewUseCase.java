package com.tokopedia.session.register.domain.interactor.registerinitial;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;
import com.tokopedia.session.domain.interactor.GetTokenUseCase;
import com.tokopedia.session.domain.interactor.MakeLoginUseCase;
import com.tokopedia.session.register.domain.model.RegisterSosmedDomain;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by nisie on 10/16/17.
 */

public class RegisterWebviewUseCase extends RegisterWithSosmedUseCase {

    @Inject
    public RegisterWebviewUseCase(ThreadExecutor threadExecutor,
                                  PostExecutionThread postExecutionThread,
                                  GetTokenUseCase getTokenUseCase,
                                  GetUserInfoUseCase getUserInfoUseCase,
                                  MakeLoginUseCase makeLoginUseCase) {
        super(threadExecutor, postExecutionThread, getTokenUseCase, getUserInfoUseCase, makeLoginUseCase);
    }

    @Override
    public Observable<RegisterSosmedDomain> createObservable(final RequestParams requestParams) {
        RegisterSosmedDomain registerSosmedDomain = new RegisterSosmedDomain();
        return getToken(registerSosmedDomain,
                GetTokenUseCase.getParamRegisterWebview(
                        requestParams.getString(GetTokenUseCase.CODE, ""),
                        requestParams.getString(GetTokenUseCase.REDIRECT_URI, "")
                ))
                .flatMap(new Func1<RegisterSosmedDomain, Observable<RegisterSosmedDomain>>() {
                    @Override
                    public Observable<RegisterSosmedDomain> call(RegisterSosmedDomain registerSosmedDomain) {
                        return getInfo(registerSosmedDomain);
                    }
                })
                .flatMap(new Func1<RegisterSosmedDomain, Observable<RegisterSosmedDomain>>() {
                    @Override
                    public Observable<RegisterSosmedDomain> call(RegisterSosmedDomain registerSosmedDomain) {
                        if (registerSosmedDomain.getInfo().getGetUserInfoDomainData().isCreatedPassword()) {
                            return makeLogin(registerSosmedDomain, requestParams);
                        } else {
                            return Observable.just(registerSosmedDomain);
                        }
                    }
                });
    }

    public static RequestParams getParamWebview(String code, String redirectUri, String tempLoginSession) {
        RequestParams params = RequestParams.create();
        params.putAll(GetTokenUseCase.getParamRegisterWebview(code, redirectUri).getParameters());
        params.putString(MakeLoginUseCase.PARAM_USER_ID, tempLoginSession);
        return params;
    }
}
