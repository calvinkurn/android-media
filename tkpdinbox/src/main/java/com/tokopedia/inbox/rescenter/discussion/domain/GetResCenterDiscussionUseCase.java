package com.tokopedia.inbox.rescenter.discussion.domain;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.discussion.domain.model.DiscussionData;

import rx.Observable;

/**
 * Created by nisie on 3/30/17.
 */

public class GetResCenterDiscussionUseCase extends UseCase<DiscussionData> {

    private final ResCenterRepository resCenterRepository;

    public GetResCenterDiscussionUseCase(JobExecutor jobExecutor,
                                         UIThread uiThread, ResCenterRepository resCenterRepository) {
        super(jobExecutor, uiThread);
        this.resCenterRepository = resCenterRepository;
    }

    @Override
    public Observable<DiscussionData> createObservable(RequestParams requestParams) {
        return resCenterRepository.getConversation(requestParams.getParameters());
    }
}
