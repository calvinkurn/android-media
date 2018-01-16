package com.tokopedia.session.login;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.session.login.domain.repository.LoginRepository;
import com.tokopedia.session.login.domain.model.MakeLoginDomainModel;

import rx.Observable;

/**
 * @author by nisie on 5/26/17.
 * @deprecated use MakeLoginUseCase in interactor.
 */

@Deprecated
public class MakeLoginUseCase extends UseCase<MakeLoginDomainModel> {

    private final LoginRepository loginRepository;

    public MakeLoginUseCase(ThreadExecutor threadExecutor,
                            PostExecutionThread postExecutionThread,
                            LoginRepository loginRepository) {
        super(threadExecutor, postExecutionThread);
        this.loginRepository = loginRepository;
    }

    @Override
    public Observable<MakeLoginDomainModel> createObservable(RequestParams requestParams) {
        return loginRepository.makeLogin(requestParams.getParameters());
    }

}
