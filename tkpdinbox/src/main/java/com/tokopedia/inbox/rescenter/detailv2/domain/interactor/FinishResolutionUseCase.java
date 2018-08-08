package com.tokopedia.inbox.rescenter.detailv2.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.ResolutionActionDomainData;

import rx.Observable;

/**
 * Created by yoasfs on 11/3/17.
 */

public class FinishResolutionUseCase extends UseCase<ResolutionActionDomainData> {

    public static final String RESO_ID = "resolution_id";

    private final ResCenterRepository repository;

    public FinishResolutionUseCase(ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread,
                                   ResCenterRepository resCenterRepository) {
        super(threadExecutor, postExecutionThread);
        this.repository = resCenterRepository;
    }

    @Override
    public Observable<ResolutionActionDomainData> createObservable(RequestParams requestParams) {
        return repository.finishResolution(requestParams);
    }
    public static RequestParams getParams(String resolutionId) {
        RequestParams params = RequestParams.create();
        params.putString(RESO_ID, resolutionId);
        return params;
    }
}
