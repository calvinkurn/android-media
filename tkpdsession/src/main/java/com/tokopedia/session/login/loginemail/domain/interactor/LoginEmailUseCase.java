package com.tokopedia.session.login.loginemail.domain.interactor;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.profile.model.GetUserInfoDomainModel;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;
import com.tokopedia.session.data.viewmodel.login.MakeLoginDomain;
import com.tokopedia.session.domain.interactor.GetTokenUseCase;
import com.tokopedia.session.domain.interactor.MakeLoginUseCase;
import com.tokopedia.session.domain.pojo.token.TokenViewModel;
import com.tokopedia.session.login.loginemail.domain.model.LoginEmailDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author by nisie on 12/19/17.
 */

public class LoginEmailUseCase extends UseCase<LoginEmailDomain> {

    private final SessionHandler sessionHandler;
    private final GetTokenUseCase getTokenUseCase;
    private final GetUserInfoUseCase getUserInfoUseCase;
    private final MakeLoginUseCase makeLoginUseCase;

    @Inject
    public LoginEmailUseCase(GetTokenUseCase getTokenUseCase,
                             GetUserInfoUseCase getUserInfoUseCase,
                             MakeLoginUseCase makeLoginUseCase,
                             SessionHandler sessionHandler) {
        this.getTokenUseCase = getTokenUseCase;
        this.getUserInfoUseCase = getUserInfoUseCase;
        this.makeLoginUseCase = makeLoginUseCase;
        this.sessionHandler = sessionHandler;
    }

    @Override
    public Observable<LoginEmailDomain> createObservable(RequestParams requestParams) {
        LoginEmailDomain domain = new LoginEmailDomain();
        return getToken(domain, requestParams)
                .flatMap(getInfo(domain))
                .flatMap(makeLogin(domain))
                .doOnError(resetToken());
    }

    private Action1<Throwable> resetToken() {
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                sessionHandler.clearToken();
            }
        };
    }

    private Func1<LoginEmailDomain, Observable<LoginEmailDomain>> makeLogin(final LoginEmailDomain domain) {
        return new Func1<LoginEmailDomain, Observable<LoginEmailDomain>>() {
            @Override
            public Observable<LoginEmailDomain> call(LoginEmailDomain loginEmailDomain) {
                return makeLoginUseCase.createObservable(MakeLoginUseCase.getParam
                        (String.valueOf(loginEmailDomain.getInfo().getGetUserInfoDomainData().getUserId())))
                        .flatMap(new Func1<MakeLoginDomain, Observable<LoginEmailDomain>>() {
                            @Override
                            public Observable<LoginEmailDomain> call(MakeLoginDomain makeLoginDomain) {
                                domain.setLoginResult(makeLoginDomain);
                                return Observable.just(domain);
                            }
                        });
            }
        };
    }

    private Func1<LoginEmailDomain, Observable<LoginEmailDomain>> getInfo(final LoginEmailDomain domain) {
        return new Func1<LoginEmailDomain, Observable<LoginEmailDomain>>() {
            @Override
            public Observable<LoginEmailDomain> call(LoginEmailDomain loginEmailDomain) {
                return getUserInfoUseCase.createObservable(GetUserInfoUseCase.generateParam())
                        .flatMap(new Func1<GetUserInfoDomainModel, Observable<LoginEmailDomain>>() {
                            @Override
                            public Observable<LoginEmailDomain> call(GetUserInfoDomainModel
                                                                             getUserInfoDomainModel) {
                                domain.setInfo(getUserInfoDomainModel);
                                return Observable.just(domain);
                            }
                        });
            }
        };
    }

    private Observable<LoginEmailDomain> getToken(final LoginEmailDomain domain, RequestParams requestParams) {
        return getTokenUseCase.createObservable(getTokenParam(requestParams))
                .flatMap(new Func1<TokenViewModel, Observable<LoginEmailDomain>>() {
                    @Override
                    public Observable<LoginEmailDomain> call(TokenViewModel tokenViewModel) {
                        domain.setToken(tokenViewModel);
                        return Observable.just(domain);
                    }
                });
    }

    private RequestParams getTokenParam(RequestParams requestParams) {
        return GetTokenUseCase.getParamLogin(
                requestParams.getString(GetTokenUseCase.USER_NAME, ""),
                requestParams.getString(GetTokenUseCase.PASSWORD, ""));
    }

    public static RequestParams getParam(String email, String password) {
        RequestParams params = RequestParams.create();
        params.putAll(GetTokenUseCase.getParamLogin(email, password).getParameters());
        return params;
    }
}
