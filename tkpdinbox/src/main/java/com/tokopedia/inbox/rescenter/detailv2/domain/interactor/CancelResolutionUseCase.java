package com.tokopedia.inbox.rescenter.detailv2.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.ResolutionActionDomainData;

import rx.Observable;

/**
 * Created by hangnadi on 3/22/17.
 */

public class CancelResolutionUseCase extends UseCase<ResolutionActionDomainData> {

    public static final String PARAM_RESOLUTION_ID = "resolution_id";

    private final ResCenterRepository resCenterRepository;

    public CancelResolutionUseCase(ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread,
                                   ResCenterRepository resCenterRepository) {
        super(threadExecutor, postExecutionThread);
        this.resCenterRepository = resCenterRepository;
    }

    @Override
    public Observable<ResolutionActionDomainData> createObservable(RequestParams requestParams) {
        return resCenterRepository.cancelResolutionV2(requestParams);
    }

    public static RequestParams getParams(String resolutionId) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_RESOLUTION_ID, resolutionId);
        return params;
    }
}
