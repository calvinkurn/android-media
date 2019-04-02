package com.tokopedia.inbox.rescenter.detailv2.domain.interactor;

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

    public static final String PARAM_RESOLUTION_ID = "resolution_id";

    private final ResCenterRepository resCenterRepository;

    public GetResCenterDiscussionUseCase(ThreadExecutor jobExecutor,
                                         PostExecutionThread uiThread, ResCenterRepository resCenterRepository) {
        super(jobExecutor, uiThread);
        this.resCenterRepository = resCenterRepository;
    }

    @Override
    public Observable<DiscussionModel> createObservable(RequestParams requestParams) {
        String resolutionID = requestParams.getString(PARAM_RESOLUTION_ID, "");
        return resCenterRepository.getConversation(resolutionID, requestParams.getParameters());
    }
}
