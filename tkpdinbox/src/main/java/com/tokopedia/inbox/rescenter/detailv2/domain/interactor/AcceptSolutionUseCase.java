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

public class AcceptSolutionUseCase extends UseCase<ResolutionActionDomainData> {

    public static final String PARAM_RESOLUTION_ID = "resolution_id";

    private final ResCenterRepository repository;

    public AcceptSolutionUseCase(ThreadExecutor jobExecutor,
                                 PostExecutionThread uiThread,
                                 ResCenterRepository resCenterRepository) {
        super(jobExecutor, uiThread);
        this.repository = resCenterRepository;
    }

    @Override
    public Observable<ResolutionActionDomainData> createObservable(RequestParams requestParams) {
        return repository.acceptSolution(requestParams.getParameters());
    }

    public static RequestParams getParams(String resolutionId) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_RESOLUTION_ID, resolutionId);
        return params;
    }
}
