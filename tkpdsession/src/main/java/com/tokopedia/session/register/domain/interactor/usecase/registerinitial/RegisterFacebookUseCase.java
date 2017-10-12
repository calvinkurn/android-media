package com.tokopedia.session.register.domain.interactor.usecase.registerinitial;

import com.facebook.AccessToken;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.session.domain.GetTokenUseCase;
import com.tokopedia.session.domain.model.TokenViewModel;
import com.tokopedia.session.register.view.viewmodel.RegisterFacebookDomain;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by nisie on 10/11/17.
 */

public class RegisterFacebookUseCase extends UseCase<RegisterFacebookDomain> {

    private final GetTokenUseCase getTokenUseCase;

    public RegisterFacebookUseCase(ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread,
                                   GetTokenUseCase getTokenUseCase) {
        super(threadExecutor, postExecutionThread);
        this.getTokenUseCase = getTokenUseCase;
    }


    @Override
    public Observable<RegisterFacebookDomain> createObservable(RequestParams requestParams) {
        RegisterFacebookDomain registerFacebookDomain = new RegisterFacebookDomain();
        return getToken(registerFacebookDomain,
                GetTokenUseCase.getParamRegisterThirdParty(
                        GetTokenUseCase.SOCIAL_TYPE_FACEBOOK,
                        requestParams.getString(GetTokenUseCase.ACCESS_TOKEN, "")
                ));
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
