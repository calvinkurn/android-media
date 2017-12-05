package com.tokopedia.otp.tokocashotp.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.otp.tokocashotp.domain.model.AccessTokenTokoCashDomain;
import com.tokopedia.otp.tokocashotp.view.viewmodel.LoginTokoCashViewModel;
import com.tokopedia.otp.tokocashotp.view.viewmodel.RequestOtpTokoCashViewModel;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by nisie on 12/5/17.
 */

public class LoginTokoCashUseCase extends UseCase<LoginTokoCashViewModel> {

    GetAccessTokenTokoCashUseCase getAccessTokenTokoCashUseCase;

    @Inject
    public LoginTokoCashUseCase(ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread,
                                GetAccessTokenTokoCashUseCase getAccessTokenTokoCashUseCase) {
        super(threadExecutor, postExecutionThread);
        this.getAccessTokenTokoCashUseCase = getAccessTokenTokoCashUseCase;
    }

    @Override
    public Observable<LoginTokoCashViewModel> createObservable(RequestParams requestParams) {
        final LoginTokoCashViewModel loginTokoCashViewModel = new LoginTokoCashViewModel();
        return getAccessTokenTokoCashUseCase.createObservable(requestParams)
                .flatMap(new Func1<AccessTokenTokoCashDomain, Observable<LoginTokoCashViewModel>>() {
                    @Override
                    public Observable<LoginTokoCashViewModel> call(AccessTokenTokoCashDomain accessTokenTokoCashDomain) {
                        loginTokoCashViewModel.setAccessToken(accessTokenTokoCashDomain);
                        return Observable.just(loginTokoCashViewModel);
                    }
                });
    }
}
