package com.tokopedia.inbox.rescenter.detailv2.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.DetailResCenter;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;

import rx.Observable;

/**
 * Created by hangnadi on 3/9/17.
 */

public class GetResCenterDetailUseCase extends UseCase<DetailResCenter> {

    public static final String PARAM_RESOLUTION_ID = "resolution_id";

    private final ResCenterRepository resCenterRepository;

    public GetResCenterDetailUseCase(ThreadExecutor threadExecutor,
                                     PostExecutionThread postExecutionThread,
                                     ResCenterRepository resCenterRepository) {
        super(threadExecutor, postExecutionThread);
        this.resCenterRepository = resCenterRepository;
    }

    @Override
    public Observable<DetailResCenter> createObservable(RequestParams requestParams) {
        String resolutionID = requestParams.getString(PARAM_RESOLUTION_ID, "");
        return resCenterRepository.getDetail(resolutionID, requestParams.getParameters());
    }
}
