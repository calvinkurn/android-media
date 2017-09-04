package com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.ReputationRepository;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.SendReviewValidateDomain;

import rx.Observable;

/**
 * @author by nisie on 8/31/17.
 */

public class SendReviewValidateUseCase extends UseCase<SendReviewValidateDomain> {

    private ReputationRepository reputationRepository;

    public SendReviewValidateUseCase(ThreadExecutor threadExecutor, PostExecutionThread
            postExecutionThread, ReputationRepository reputationRepository) {
        super(threadExecutor, postExecutionThread);
        this.reputationRepository = reputationRepository;
    }

    @Override
    public Observable<SendReviewValidateDomain> createObservable(RequestParams requestParams) {
        return reputationRepository.sendReviewValidation(requestParams);
    }
}
