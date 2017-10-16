package com.tokopedia.session.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.session.data.viewmodel.DiscoverViewModel;
import com.tokopedia.session.data.repository.SessionRepository;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by nisie on 10/10/17.
 */

public class DiscoverUseCase extends UseCase<DiscoverViewModel> {
    SessionRepository sessionRepository;


    public DiscoverUseCase(ThreadExecutor threadExecutor,
                           PostExecutionThread postExecutionThread,
                           SessionRepository sessionRepository) {
        super(threadExecutor, postExecutionThread);
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Observable<DiscoverViewModel> createObservable(RequestParams requestParams) {
        return sessionRepository.getDiscoverFromLocal()
                .onErrorResumeNext(new Func1<Throwable, Observable<DiscoverViewModel>>() {
                    @Override
                    public Observable<DiscoverViewModel> call(Throwable throwable) {
                        return sessionRepository.getDiscoverFromCloud();
                    }
                });
    }
}
