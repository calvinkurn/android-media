package com.tokopedia.session.login.loginphonenumber.domain.interactor;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.profile.model.GetUserInfoDomainModel;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.tokocashotp.view.viewmodel.LoginTokoCashViewModel;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;
import com.tokopedia.session.data.viewmodel.login.MakeLoginDomain;
import com.tokopedia.session.domain.interactor.GetTokenUseCase;
import com.tokopedia.session.domain.interactor.MakeLoginUseCase;
import com.tokopedia.session.domain.pojo.token.TokenViewModel;
import com.tokopedia.session.login.loginphonenumber.domain.model.CodeTokoCashDomain;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author by nisie on 12/5/17.
 */

public class LoginPhoneNumberUseCase extends UseCase<LoginTokoCashViewModel> {

    private GetCodeTokoCashUseCase getCodeTokoCashUseCase;
    private GetTokenUseCase getTokenUseCase;
    private MakeLoginUseCase makeLoginUseCase;
    private GetUserInfoUseCase getUserInfoUseCase;
    private SessionHandler sessionHandler;

    @Inject
    public LoginPhoneNumberUseCase(ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread,
                                   GetCodeTokoCashUseCase getCodeTokoCashUseCase,
                                   GetTokenUseCase getTokenUseCase,
                                   GetUserInfoUseCase getUserInfoUseCase,
                                   MakeLoginUseCase makeLoginUseCase,
                                   SessionHandler sessionHandler) {
        super(threadExecutor, postExecutionThread);
        this.getCodeTokoCashUseCase = getCodeTokoCashUseCase;
        this.getTokenUseCase = getTokenUseCase;
        this.getUserInfoUseCase = getUserInfoUseCase;
        this.makeLoginUseCase = makeLoginUseCase;
        this.sessionHandler = sessionHandler;
    }

    @Override
    public Observable<LoginTokoCashViewModel> createObservable(RequestParams requestParams) {
        final LoginTokoCashViewModel loginTokoCashViewModel = new LoginTokoCashViewModel();
        return Observable.just(loginTokoCashViewModel)
                .flatMap(getCodeTokoCash(requestParams))
                .flatMap(getTokenAccounts())
                .flatMap(getUserInfo())
                .flatMap(makeLogin())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        sessionHandler.clearToken();
                    }
                });
    }

    private Func1<LoginTokoCashViewModel, Observable<LoginTokoCashViewModel>> getUserInfo() {
        return new Func1<LoginTokoCashViewModel, Observable<LoginTokoCashViewModel>>() {
            @Override
            public Observable<LoginTokoCashViewModel> call(final LoginTokoCashViewModel loginTokoCashViewModel) {
                return getUserInfoUseCase.createObservable(GetUserInfoUseCase.generateParam())
                        .flatMap(new Func1<GetUserInfoDomainModel, Observable<LoginTokoCashViewModel>>() {
                            @Override
                            public Observable<LoginTokoCashViewModel> call(GetUserInfoDomainModel getUserInfoDomainModel) {
                                loginTokoCashViewModel.setUserInfoDomain(getUserInfoDomainModel);
                                return Observable.just(loginTokoCashViewModel);
                            }
                        });
            }
        };
    }

    private Func1<LoginTokoCashViewModel, Observable<LoginTokoCashViewModel>> makeLogin() {
        return new Func1<LoginTokoCashViewModel, Observable<LoginTokoCashViewModel>>() {
            @Override
            public Observable<LoginTokoCashViewModel> call(final LoginTokoCashViewModel loginTokoCashViewModel) {
                return makeLoginUseCase.createObservable(getMakeLoginParam(loginTokoCashViewModel))
                        .flatMap(new Func1<MakeLoginDomain, Observable<LoginTokoCashViewModel>>() {
                            @Override
                            public Observable<LoginTokoCashViewModel> call(MakeLoginDomain makeLoginDomain) {
                                loginTokoCashViewModel.setMakeLoginDomain(makeLoginDomain);
                                return Observable.just(loginTokoCashViewModel);
                            }
                        });
            }
        };
    }

    private RequestParams getMakeLoginParam(LoginTokoCashViewModel loginTokoCashViewModel) {
        return MakeLoginUseCase.getParam(String.valueOf(loginTokoCashViewModel.getUserInfoDomain()
                .getGetUserInfoDomainData().getUserId()));
    }

    private Func1<LoginTokoCashViewModel, Observable<LoginTokoCashViewModel>> getTokenAccounts() {
        return new Func1<LoginTokoCashViewModel, Observable<LoginTokoCashViewModel>>() {
            @Override
            public Observable<LoginTokoCashViewModel> call(final LoginTokoCashViewModel loginTokoCashViewModel) {
                return getTokenUseCase.createObservable(getTokenParam(loginTokoCashViewModel))
                        .flatMap(new Func1<TokenViewModel, Observable<LoginTokoCashViewModel>>() {
                            @Override
                            public Observable<LoginTokoCashViewModel> call(TokenViewModel tokenViewModel) {
                                loginTokoCashViewModel.setAccountsToken(tokenViewModel);
                                return Observable.just(loginTokoCashViewModel);
                            }
                        });
            }
        };
    }

    private RequestParams getTokenParam(LoginTokoCashViewModel loginTokoCashViewModel) {
        return GetTokenUseCase.getParamThirdParty(GetTokenUseCase.SOCIAL_TYPE_PHONE_NUMBER,
                loginTokoCashViewModel.getTokoCashCode().getCode());
    }

    private Func1<LoginTokoCashViewModel, Observable<LoginTokoCashViewModel>> getCodeTokoCash
            (final RequestParams requestParams) {
        return new Func1<LoginTokoCashViewModel, Observable<LoginTokoCashViewModel>>() {
            @Override
            public Observable<LoginTokoCashViewModel> call(final LoginTokoCashViewModel loginTokoCashViewModel) {
                return getCodeTokoCashUseCase.createObservable(getAccessTokenParams(requestParams))
                        .flatMap(new Func1<CodeTokoCashDomain, Observable<LoginTokoCashViewModel>>() {
                            @Override
                            public Observable<LoginTokoCashViewModel> call(CodeTokoCashDomain accessTokenTokoCashDomain) {
                                loginTokoCashViewModel.setTokoCashCode(accessTokenTokoCashDomain);
                                return Observable.just(loginTokoCashViewModel);
                            }
                        });
            }
        };
    }

    private RequestParams getAccessTokenParams(RequestParams requestParams) {
        return GetCodeTokoCashUseCase.getParam(
                requestParams.getString(GetCodeTokoCashUseCase.PARAM_KEY, ""),
                requestParams.getString(GetCodeTokoCashUseCase.PARAM_EMAIL, ""));
    }

    public static RequestParams getParam(String accessToken, String email, int userId) {
        RequestParams params = RequestParams.create();
        params.putAll(GetCodeTokoCashUseCase.getParam(accessToken, email).getParameters());
        params.putAll(MakeLoginUseCase.getParam(String.valueOf(userId)).getParameters());
        return params;
    }


}
