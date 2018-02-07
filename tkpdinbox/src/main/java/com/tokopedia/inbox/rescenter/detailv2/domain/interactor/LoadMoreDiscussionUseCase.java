package com.tokopedia.inbox.rescenter.detailv2.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.discussion.domain.model.loadmore.LoadMoreModel;

import rx.Observable;

/**
 * Created by nisie on 3/31/17.
 */

public class LoadMoreDiscussionUseCase extends UseCase<LoadMoreModel> {

    public static final String PARAM_RESOLUTION_ID = "resolution_id";
    public static final String PARAM_LAST_CONVERSATION_ID = "resConvId";

    private final ResCenterRepository resCenterRepository;

    public LoadMoreDiscussionUseCase(ThreadExecutor jobExecutor,
                                     PostExecutionThread uiThread, ResCenterRepository resCenterRepository) {
        super(jobExecutor, uiThread);
        this.resCenterRepository = resCenterRepository;
    }

    @Override
    public Observable<LoadMoreModel> createObservable(RequestParams requestParams) {
        String resolutionID = requestParams.getString(PARAM_RESOLUTION_ID, "");
        return resCenterRepository.getConversationMore(resolutionID, requestParams.getParameters());
    }

}
