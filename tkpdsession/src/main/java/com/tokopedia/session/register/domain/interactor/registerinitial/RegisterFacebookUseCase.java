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
import com.tokopedia.session.register.domain.model.RegisterFacebookDomain;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by nisie on 10/11/17.
 */

public class RegisterFacebookUseCase extends UseCase<RegisterFacebookDomain> {

    private final GetTokenUseCase getTokenUseCase;
    private final GetUserInfoUseCase getUserInfoUseCase;

    public RegisterFacebookUseCase(ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread,
                                   GetTokenUseCase getTokenUseCase,
                                   GetUserInfoUseCase getUserInfoUseCase) {
        super(threadExecutor, postExecutionThread);
        this.getTokenUseCase = getTokenUseCase;
        this.getUserInfoUseCase = getUserInfoUseCase;
    }


    @Override
    public Observable<RegisterFacebookDomain> createObservable(RequestParams requestParams) {
        RegisterFacebookDomain registerFacebookDomain = new RegisterFacebookDomain();
        return getToken(registerFacebookDomain,
                GetTokenUseCase.getParamRegisterThirdParty(
                        GetTokenUseCase.SOCIAL_TYPE_FACEBOOK,
                        requestParams.getString(GetTokenUseCase.ACCESS_TOKEN, "")
                ))
                .flatMap(new Func1<RegisterFacebookDomain, Observable<RegisterFacebookDomain>>() {
                    @Override
                    public Observable<RegisterFacebookDomain> call(RegisterFacebookDomain registerFacebookDomain) {
                        return getInfo(registerFacebookDomain);
                    }
                });
    }

    private Observable<RegisterFacebookDomain> getInfo(final RegisterFacebookDomain registerFacebookDomain) {
        return getUserInfoUseCase.createObservable(RequestParams.EMPTY)
                .flatMap(new Func1<GetUserInfoDomainModel, Observable<RegisterFacebookDomain>>() {
                    @Override
                    public Observable<RegisterFacebookDomain> call(GetUserInfoDomainModel getUserInfoDomainModel) {
                        registerFacebookDomain.setInfo(getUserInfoDomainModel);
                        return Observable.just(registerFacebookDomain);
                    }
                });
    }

    private Observable<RegisterFacebookDomain> getToken(final RegisterFacebookDomain registerFacebookDomain,
                                                        RequestParams paramRegisterThirdParty) {
        return getTokenUseCase.createObservable(paramRegisterThirdParty)
                .flatMap(new Func1<TokenViewModel, Observable<RegisterFacebookDomain>>() {
                    @Override
                    public Observable<RegisterFacebookDomain> call(TokenViewModel tokenViewModel) {
                        registerFacebookDomain.setTokenModel(tokenViewModel);
                        return Observable.just(registerFacebookDomain);
                    }
                });
    }


    public static RequestParams getParam(AccessToken accessToken) {
        RequestParams params = RequestParams.create();
        params.putAll(getTokenParam(accessToken).getParameters());
        return params;
    }

    private static RequestParams getTokenParam(AccessToken accessToken) {
        return GetTokenUseCase.getParamRegisterThirdParty(
                GetTokenUseCase.SOCIAL_TYPE_FACEBOOK,
                accessToken.getToken()
        );
    }
}
