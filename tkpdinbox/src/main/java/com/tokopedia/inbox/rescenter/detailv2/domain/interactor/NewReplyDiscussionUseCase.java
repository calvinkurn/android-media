package com.tokopedia.inbox.rescenter.detailv2.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.discussion.domain.model.NewReplyDiscussionModel;

import rx.Observable;

/**
 * Created by hangnadi on 6/16/17.
 */

public class NewReplyDiscussionUseCase extends UseCase<NewReplyDiscussionModel> {

    public static final String PARAM_RESOLUTION_ID = "resolution_id";
    public static final String PARAM_REPLY_MSG = "message";
    public static final String PARAM_ATTACHMENT = "attachment_count";

    private final ResCenterRepository repository;

    public NewReplyDiscussionUseCase(ThreadExecutor threadExecutor,
                                     PostExecutionThread postExecutionThread,
                                     ResCenterRepository repository) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    @Override
    public Observable<NewReplyDiscussionModel> createObservable(RequestParams requestParams) {
        return repository.replyResolution(requestParams.getString(PARAM_RESOLUTION_ID, ""), requestParams.getParameters());
    }
}
