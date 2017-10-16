package com.tokopedia.session.register.domain.interactor.registerinitial;

import com.facebook.AccessToken;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.profile.model.GetUserInfoDomainModel;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;
import com.tokopedia.session.domain.interactor.GetTokenUseCase;
import com.tokopedia.session.domain.pojo.token.TokenViewModel;
import com.tokopedia.session.register.domain.model.RegisterSosmedDomain;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by nisie on 10/11/17.
 */

public class RegisterWithSosmedUseCase extends UseCase<RegisterSosmedDomain> {

    protected final GetTokenUseCase getTokenUseCase;
    protected final GetUserInfoUseCase getUserInfoUseCase;

    public RegisterWithSosmedUseCase(ThreadExecutor threadExecutor,
                                     PostExecutionThread postExecutionThread,
                                     GetTokenUseCase getTokenUseCase,
                                     GetUserInfoUseCase getUserInfoUseCase) {
        super(threadExecutor, postExecutionThread);
        this.getTokenUseCase = getTokenUseCase;
        this.getUserInfoUseCase = getUserInfoUseCase;
    }


    @Override
    public Observable<RegisterSosmedDomain> createObservable(RequestParams requestParams) {
        RegisterSosmedDomain registerSosmedDomain = new RegisterSosmedDomain();
        return getToken(registerSosmedDomain,
                GetTokenUseCase.getParamRegisterThirdParty(
                        requestParams.getInt(GetTokenUseCase.SOCIAL_TYPE, -1),
                        requestParams.getString(GetTokenUseCase.ACCESS_TOKEN, "")
                ))
                .flatMap(new Func1<RegisterSosmedDomain, Observable<RegisterSosmedDomain>>() {
                    @Override
                    public Observable<RegisterSosmedDomain> call(RegisterSosmedDomain registerSosmedDomain) {
                        return getInfo(registerSosmedDomain);
                    }
                });
    }

    protected Observable<RegisterSosmedDomain> getInfo(final RegisterSosmedDomain
                                                           registerSosmedDomain) {
        return getUserInfoUseCase.createObservable(RequestParams.EMPTY)
                .flatMap(new Func1<GetUserInfoDomainModel, Observable<RegisterSosmedDomain>>() {
                    @Override
                    public Observable<RegisterSosmedDomain> call(GetUserInfoDomainModel getUserInfoDomainModel) {
                        registerSosmedDomain.setInfo(getUserInfoDomainModel);
                        return Observable.just(registerSosmedDomain);
                    }
                });
    }

    protected Observable<RegisterSosmedDomain> getToken(final RegisterSosmedDomain registerSosmedDomain,
                                                      RequestParams params) {
        return getTokenUseCase.createObservable(params)
                .flatMap(new Func1<TokenViewModel, Observable<RegisterSosmedDomain>>() {
                    @Override
                    public Observable<RegisterSosmedDomain> call(TokenViewModel tokenViewModel) {
                        registerSosmedDomain.setTokenModel(tokenViewModel);
                        return Observable.just(registerSosmedDomain);
                    }
                });
    }


    private static RequestParams getParamRegisterThirdParty(int socialType, String accessToken) {
        return GetTokenUseCase.getParamRegisterThirdParty(
                socialType,
                accessToken
        );
    }

    public static RequestParams getParamFacebook(AccessToken accessToken) {
        RequestParams params = RequestParams.create();
        params.putAll(
                getParamRegisterThirdParty(GetTokenUseCase.SOCIAL_TYPE_FACEBOOK,
                        accessToken.getToken())
                        .getParameters());
        return params;
    }

    public static RequestParams getParamGoogle(String accessToken) {
        RequestParams params = RequestParams.create();
        params.putAll(
                getParamRegisterThirdParty(GetTokenUseCase.SOCIAL_TYPE_GPLUS,
                        accessToken)
                        .getParameters());
        return params;
    }


}
