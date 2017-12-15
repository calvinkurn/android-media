package com.tokopedia.inbox.rescenter.detailv2.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.DetailResChatDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.NextActionDomain;

import rx.Observable;

/**
 * Created by yoasfs on 11/10/17.
 */

public class GetNextActionUseCase extends UseCase<NextActionDomain> {

    public static final String RESO_ID = "resolution_id";

    private ResCenterRepository resCenterRepository;

    public GetNextActionUseCase(ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread,
                                ResCenterRepository resCenterRepository) {
        super(threadExecutor, postExecutionThread);
        this.resCenterRepository = resCenterRepository;
    }

    @Override
    public Observable<NextActionDomain> createObservable(RequestParams requestParams) {
        return resCenterRepository.getNextAction(requestParams);
    }

    public static RequestParams getResChatUseCaseParam(String resolutionId) {
        RequestParams params = RequestParams.create();
        params.putString(RESO_ID, resolutionId);
        return params;
    }
}
