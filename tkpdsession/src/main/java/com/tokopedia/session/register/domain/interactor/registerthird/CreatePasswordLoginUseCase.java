package com.tokopedia.session.register.domain.interactor.registerthird;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.session.data.viewmodel.login.MakeLoginDomain;
import com.tokopedia.session.domain.interactor.MakeLoginUseCase;
import com.tokopedia.session.register.domain.model.CreatePasswordDomain;
import com.tokopedia.session.register.domain.model.CreatePasswordLoginDomain;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by nisie on 10/23/17.
 */

public class CreatePasswordLoginUseCase extends UseCase<CreatePasswordLoginDomain> {

    private final SessionHandler sessionHandler;
    CreatePasswordUseCase createPasswordUseCase;
    MakeLoginUseCase makeLoginUseCase;

    @Inject
    public CreatePasswordLoginUseCase(ThreadExecutor threadExecutor,
                                      PostExecutionThread postExecutionThread,
                                      CreatePasswordUseCase createPasswordUseCase,
                                      MakeLoginUseCase makeLoginUseCase,
                                      SessionHandler sessionHandler) {
        super(threadExecutor, postExecutionThread);
        this.createPasswordUseCase = createPasswordUseCase;
        this.makeLoginUseCase = makeLoginUseCase;
        this.sessionHandler = sessionHandler;
    }

    @Override
    public Observable<CreatePasswordLoginDomain> createObservable(RequestParams requestParams) {
        CreatePasswordLoginDomain domain = new CreatePasswordLoginDomain();
        return createPassword(requestParams, domain)
                .flatMap(makeLogin(requestParams, domain));
    }

    private Func1<CreatePasswordLoginDomain, Observable<CreatePasswordLoginDomain>> makeLogin(final RequestParams param,
                                                                                              final CreatePasswordLoginDomain domain) {
        return new Func1<CreatePasswordLoginDomain, Observable<CreatePasswordLoginDomain>>() {
            @Override
            public Observable<CreatePasswordLoginDomain> call(CreatePasswordLoginDomain createPasswordLoginDomain) {
                return makeLoginUseCase.createObservable(MakeLoginUseCase.getParam(
                        param.getString(CreatePasswordUseCase.USER_ID, "")))
                        .flatMap(new Func1<MakeLoginDomain, Observable<CreatePasswordLoginDomain>>() {
                            @Override
                            public Observable<CreatePasswordLoginDomain> call(MakeLoginDomain makeLoginDomain) {
                                domain.setMakeLoginDomain(makeLoginDomain);
                                return Observable.just(domain);
                            }
                        });
            }
        };
    }

    private Observable<CreatePasswordLoginDomain> createPassword(final RequestParams requestParams, final CreatePasswordLoginDomain domain) {
        return createPasswordUseCase.createObservable(requestParams)
                .flatMap(new Func1<CreatePasswordDomain, Observable<CreatePasswordLoginDomain>>() {
                    @Override
                    public Observable<CreatePasswordLoginDomain> call(CreatePasswordDomain createPasswordDomain) {
                        domain.setCreatePasswordDomain(createPasswordDomain);
                        sessionHandler.setTempPhoneNumber(requestParams.getString
                                (CreatePasswordUseCase.MSISDN, ""));
                        return Observable.just(domain);
                    }
                });
    }
}
