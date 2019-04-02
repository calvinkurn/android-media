package com.tokopedia.inbox.rescenter.detailv2.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.DetailResponseData;

import rx.Observable;

/**
 * Created by hangnadi on 3/9/17.
 */

public class GetResCenterDetailV2UseCase extends UseCase<DetailResponseData> {

    public static final String PARAM_RESOLUTION_ID = "resolution_id";

    private final ResCenterRepository resCenterRepository;

    public GetResCenterDetailV2UseCase(ThreadExecutor threadExecutor,
                                       PostExecutionThread postExecutionThread,
                                       ResCenterRepository resCenterRepository) {
        super(threadExecutor, postExecutionThread);
        this.resCenterRepository = resCenterRepository;
    }

    @Override
    public Observable<DetailResponseData> createObservable(RequestParams requestParams) {
        return resCenterRepository.getDetailV2(requestParams);
    }
}
