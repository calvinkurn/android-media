package com.tokopedia.session.register.domain.interactor.registerinitial;

import com.facebook.AccessToken;
import com.tokopedia.core.profile.model.GetUserInfoDomainModel;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;
import com.tokopedia.session.data.viewmodel.login.MakeLoginDomain;
import com.tokopedia.session.domain.interactor.GetTokenUseCase;
import com.tokopedia.session.domain.interactor.MakeLoginUseCase;
import com.tokopedia.session.domain.pojo.token.TokenViewModel;
import com.tokopedia.session.register.domain.model.LoginSosmedDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author by nisie on 10/11/17.
 */

public class LoginWithSosmedUseCase extends UseCase<LoginSosmedDomain> {

    protected final GetTokenUseCase getTokenUseCase;
    protected final GetUserInfoUseCase getUserInfoUseCase;
    protected final MakeLoginUseCase makeLoginUseCase;
    private final SessionHandler sessionHandler;

    @Inject
    public LoginWithSosmedUseCase(SessionHandler sessionHandler,
                                  GetTokenUseCase getTokenUseCase,
                                  GetUserInfoUseCase getUserInfoUseCase,
                                  MakeLoginUseCase makeLoginUseCase) {
        this.sessionHandler = sessionHandler;
        this.getTokenUseCase = getTokenUseCase;
        this.getUserInfoUseCase = getUserInfoUseCase;
        this.makeLoginUseCase = makeLoginUseCase;
    }

    @Override
    public Observable<LoginSosmedDomain> createObservable(final RequestParams requestParams) {
        LoginSosmedDomain registerSosmedDomain = new LoginSosmedDomain();
        return getToken(registerSosmedDomain,
                GetTokenUseCase.getParamThirdParty(
                        requestParams.getInt(GetTokenUseCase.SOCIAL_TYPE, -1),
                        requestParams.getString(GetTokenUseCase.ACCESS_TOKEN, "")
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
                })
                .doOnError(clearToken());
    }

    private Action1<Throwable> clearToken() {
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                sessionHandler.clearToken();
            }
        };
    }

    protected Observable<LoginSosmedDomain> makeLogin(final LoginSosmedDomain
                                                                 registerSosmedDomain) {
        return makeLoginUseCase.getExecuteObservable(MakeLoginUseCase.getParam(
                String.valueOf(registerSosmedDomain.getInfo().getGetUserInfoDomainData().getUserId())
        ))
                .flatMap(new Func1<MakeLoginDomain, Observable<LoginSosmedDomain>>() {
                    @Override
                    public Observable<LoginSosmedDomain> call(MakeLoginDomain makeLoginDomain) {
                        registerSosmedDomain.setMakeLoginModel(makeLoginDomain);
                        return Observable.just(registerSosmedDomain);
                    }
                });
    }

    protected Observable<LoginSosmedDomain> getInfo(final LoginSosmedDomain
                                                               registerSosmedDomain) {
        return getUserInfoUseCase.createObservable(RequestParams.EMPTY)
                .flatMap(new Func1<GetUserInfoDomainModel, Observable<LoginSosmedDomain>>() {
                    @Override
                    public Observable<LoginSosmedDomain> call(GetUserInfoDomainModel getUserInfoDomainModel) {
                        registerSosmedDomain.setInfo(getUserInfoDomainModel);
                        return Observable.just(registerSosmedDomain);
                    }
                });
    }

    protected Observable<LoginSosmedDomain> getToken(final LoginSosmedDomain registerSosmedDomain,
                                                     RequestParams params) {
        return getTokenUseCase.createObservable(params)
                .flatMap(new Func1<TokenViewModel, Observable<LoginSosmedDomain>>() {
                    @Override
                    public Observable<LoginSosmedDomain> call(TokenViewModel tokenViewModel) {
                        registerSosmedDomain.setTokenModel(tokenViewModel);
                        return Observable.just(registerSosmedDomain);
                    }
                });
    }


    private static RequestParams getParamThirdParty(int socialType, String accessToken) {
        return GetTokenUseCase.getParamThirdParty(
                socialType,
                accessToken
        );
    }

    public static RequestParams getParamFacebook(AccessToken accessToken) {
        RequestParams params = RequestParams.create();
        params.putAll(
                getParamThirdParty(GetTokenUseCase.SOCIAL_TYPE_FACEBOOK,
                        accessToken.getToken())
                        .getParameters());
        return params;
    }

    public static RequestParams getParamGoogle(String accessToken) {
        RequestParams params = RequestParams.create();
        params.putAll(
                getParamThirdParty(GetTokenUseCase.SOCIAL_TYPE_GPLUS,
                        accessToken)
                        .getParameters());
        return params;
    }


}
