package com.tokopedia.inbox.rescenter.createreso.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.createreso.data.repository.EditSolutionRepository;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.EditSolutionResponseDomain;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class GetEditSolutionUseCase extends UseCase<EditSolutionResponseDomain> {
    public static final String RESO_ID = "reso_id";

    private EditSolutionRepository editSolutionRepository;

    public GetEditSolutionUseCase(ThreadExecutor threadExecutor,
                                  PostExecutionThread postExecutionThread,
                                  EditSolutionRepository editSolutionRepository) {
        super(threadExecutor, postExecutionThread);
        this.editSolutionRepository = editSolutionRepository;
    }

    @Override
    public Observable<EditSolutionResponseDomain> createObservable(RequestParams requestParams) {
        return editSolutionRepository.getEditSolutionFromCloud(requestParams);
    }

    public RequestParams getEditSolutionUseCaseParams(String resoId) {
        RequestParams params = RequestParams.create();
        params.putString(RESO_ID, resoId);
        return params;
    }

}
