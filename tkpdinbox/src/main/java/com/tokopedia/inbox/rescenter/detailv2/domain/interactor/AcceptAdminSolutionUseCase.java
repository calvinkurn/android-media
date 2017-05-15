package com.tokopedia.inbox.rescenter.detailv2.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.ResolutionActionDomainData;

import rx.Observable;

/**
 * Created by hangnadi on 3/23/17.
 */

public class AcceptAdminSolutionUseCase extends UseCase<ResolutionActionDomainData> {

    public static final String PARAM_RESOLUTION_ID = "resolution_id";

    private final ResCenterRepository repository;

    public AcceptAdminSolutionUseCase(ThreadExecutor threadExecutor,
                                      PostExecutionThread postExecutionThread,
                                      ResCenterRepository resCenterRepository) {
        super(threadExecutor, postExecutionThread);
        this.repository = resCenterRepository;
    }

    @Override
    public Observable<ResolutionActionDomainData> createObservable(RequestParams requestParams) {
        return repository.acceptAdminSolution(requestParams.getParameters());
    }
}
