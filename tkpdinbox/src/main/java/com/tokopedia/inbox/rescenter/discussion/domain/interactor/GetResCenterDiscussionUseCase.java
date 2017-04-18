package com.tokopedia.inbox.rescenter.discussion.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.discussion.domain.model.getdiscussion.DiscussionModel;

import rx.Observable;

/**
 * Created by nisie on 3/30/17.
 */

public class GetResCenterDiscussionUseCase extends UseCase<DiscussionModel> {

    private final ResCenterRepository resCenterRepository;

    public GetResCenterDiscussionUseCase(ThreadExecutor jobExecutor,
                                         PostExecutionThread uiThread, ResCenterRepository resCenterRepository) {
        super(jobExecutor, uiThread);
        this.resCenterRepository = resCenterRepository;
    }

    @Override
    public Observable<DiscussionModel> createObservable(RequestParams requestParams) {
        return resCenterRepository.getConversation(requestParams.getParameters());
    }
}
