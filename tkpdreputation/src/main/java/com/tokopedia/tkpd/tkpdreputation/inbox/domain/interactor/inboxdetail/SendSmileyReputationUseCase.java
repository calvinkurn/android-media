package com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.ReputationRepository;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.SendSmileyReputationDomain;

import rx.Observable;

/**
 * @author by nisie on 8/31/17.
 */

public class SendSmileyReputationUseCase extends UseCase<SendSmileyReputationDomain> {

    private static final String PARAM_VALUE = "smiley";
    private static final String PARAM_REPUTATION_ID = "reputationId";

    private ReputationRepository reputationRepository;

    public SendSmileyReputationUseCase(ThreadExecutor threadExecutor,
                                       PostExecutionThread postExecutionThread,
                                       ReputationRepository reputationRepository) {
        super(threadExecutor, postExecutionThread);
        this.reputationRepository = reputationRepository;
    }

    @Override
    public Observable<SendSmileyReputationDomain> createObservable(RequestParams requestParams) {
        return reputationRepository.sendSmiley(requestParams);
    }

    public static RequestParams getParam(String reputationId, String value) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_VALUE, value);
        params.putString(PARAM_REPUTATION_ID, reputationId);
        return params;
    }
}
