package com.tokopedia.session.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.session.data.repository.SessionRepository;
import com.tokopedia.session.data.viewmodel.login.MakeLoginDomain;

import rx.Observable;

/**
 * @author by nisie on 5/26/17.
 */

public class MakeLoginUseCase extends UseCase<MakeLoginDomain> {

    private final SessionRepository sessionRepository;

    public MakeLoginUseCase(ThreadExecutor threadExecutor,
                            PostExecutionThread postExecutionThread,
                            SessionRepository sessionRepository) {
        super(threadExecutor, postExecutionThread);
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Observable<MakeLoginDomain> createObservable(RequestParams requestParams) {
        return sessionRepository.makeLogin(requestParams.getParameters());
    }

    public static RequestParams getParam() {
        return RequestParams.EMPTY;
    }
}
