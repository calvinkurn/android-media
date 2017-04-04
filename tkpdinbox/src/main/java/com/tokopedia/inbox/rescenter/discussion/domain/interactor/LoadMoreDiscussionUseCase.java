package com.tokopedia.inbox.rescenter.discussion.domain.interactor;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.discussion.domain.model.loadmore.LoadMoreModel;

import rx.Observable;

/**
 * Created by nisie on 3/31/17.
 */

public class LoadMoreDiscussionUseCase extends UseCase<LoadMoreModel> {

    public static final String PARAM_LAST_CONVERSATION_ID = "resConvId";
    private final ResCenterRepository resCenterRepository;

    public LoadMoreDiscussionUseCase(JobExecutor jobExecutor,
                                     UIThread uiThread, ResCenterRepository resCenterRepository) {
        super(jobExecutor, uiThread);
        this.resCenterRepository = resCenterRepository;
    }

    @Override
    public Observable<LoadMoreModel> createObservable(RequestParams requestParams) {
        return resCenterRepository.getConversationMore(
                requestParams.getParameters());
    }

}
