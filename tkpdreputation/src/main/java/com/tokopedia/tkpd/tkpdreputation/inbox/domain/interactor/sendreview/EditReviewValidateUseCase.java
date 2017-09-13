package com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewValidateDomain;

import rx.Observable;

/**
 * @author by nisie on 9/12/17.
 */

public class EditReviewValidateUseCase extends SendReviewValidateUseCase {

    public EditReviewValidateUseCase(ThreadExecutor threadExecutor,
                                     PostExecutionThread postExecutionThread,
                                     ReputationRepository reputationRepository) {
        super(threadExecutor, postExecutionThread, reputationRepository);
    }

    @Override
    public Observable<SendReviewValidateDomain> createObservable(RequestParams requestParams) {
        return reputationRepository.editReviewValidation(requestParams);
    }
}
