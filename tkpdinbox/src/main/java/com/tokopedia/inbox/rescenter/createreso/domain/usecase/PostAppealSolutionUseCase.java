package com.tokopedia.inbox.rescenter.createreso.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.createreso.data.repository.PostAppealSolutionRepository;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.EditAppealResolutionSolutionDomain;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class PostAppealSolutionUseCase extends UseCase<EditAppealResolutionSolutionDomain> {
    public static final String RESO_ID = "reso_id";
    public static final String PARAM_SOLUTION = "solution";
    public static final String PARAM_REFUND_AMOUNT = "refund_amount";

    private PostAppealSolutionRepository postAppealSolutionRepository;

    public PostAppealSolutionUseCase(ThreadExecutor threadExecutor,
                                     PostExecutionThread postExecutionThread,
                                     PostAppealSolutionRepository postAppealSolutionRepository) {
        super(threadExecutor, postExecutionThread);
        this.postAppealSolutionRepository = postAppealSolutionRepository;
    }

    @Override
    public Observable<EditAppealResolutionSolutionDomain> createObservable(RequestParams requestParams) {
        return postAppealSolutionRepository.postAppealSolutionDataCloud(requestParams);
    }

    public static RequestParams postAppealSolutionUseCaseParams(String resoId,
                                                                int solutionId,
                                                                long refundAmount) {
        RequestParams params = RequestParams.create();
        params.putInt(PARAM_SOLUTION, solutionId);
        params.putLong(PARAM_REFUND_AMOUNT, refundAmount);
        params.putString(RESO_ID, resoId);
        return params;
    }

    public static RequestParams postAppealSolutionUseCaseParamsWithoutRefund(String resoId,
                                                                             int solutionId) {
        RequestParams params = RequestParams.create();
        params.putInt(PARAM_SOLUTION, solutionId);
        params.putString(RESO_ID, resoId);
        return params;
    }

}
