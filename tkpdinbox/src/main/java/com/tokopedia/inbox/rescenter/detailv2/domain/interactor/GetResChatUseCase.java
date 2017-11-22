package com.tokopedia.inbox.rescenter.detailv2.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.DetailResChatDomain;

import rx.Observable;

/**
 * Created by yoasfs on 11/10/17.
 */

public class GetResChatUseCase extends UseCase<DetailResChatDomain> {

    public static final String PARAMS_RESO_ID = "resolution_id";
    public static final String PARAMS_LIMIT = "limit";

    private ResCenterRepository resCenterRepository;

    public GetResChatUseCase(ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread,
                             ResCenterRepository resCenterRepository) {
        super(threadExecutor, postExecutionThread);
        this.resCenterRepository = resCenterRepository;
    }

    @Override
    public Observable<DetailResChatDomain> createObservable(RequestParams requestParams) {
        return resCenterRepository.getConversation(requestParams);
    }

    public static RequestParams getResChatUseCaseParam(String resolutionId, int limit) {
        RequestParams params = RequestParams.create();
        params.putString(PARAMS_RESO_ID, resolutionId);
        params.putInt(PARAMS_LIMIT, limit);
        return params;
    }
}
