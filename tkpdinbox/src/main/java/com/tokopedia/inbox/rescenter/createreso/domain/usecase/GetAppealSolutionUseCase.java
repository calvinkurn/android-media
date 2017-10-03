package com.tokopedia.inbox.rescenter.createreso.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.createreso.data.repository.AppealSolutionRepository;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.AppealSolutionResponseDomain;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class GetAppealSolutionUseCase extends UseCase<AppealSolutionResponseDomain> {
    public static final String RESO_ID = "reso_id";

    private AppealSolutionRepository appealSolutionRepository;

    public GetAppealSolutionUseCase(ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread,
                                    AppealSolutionRepository appealSolutionRepository) {
        super(threadExecutor, postExecutionThread);
        this.appealSolutionRepository = appealSolutionRepository;
    }

    @Override
    public Observable<AppealSolutionResponseDomain> createObservable(RequestParams requestParams) {
        return appealSolutionRepository.getAppealSolutionFromCloud(requestParams);
    }

    public RequestParams getAppealSolutionUseCaseParams(String resoId) {
        RequestParams params = RequestParams.create();
        params.putString(RESO_ID, resoId);
        return params;
    }

}
