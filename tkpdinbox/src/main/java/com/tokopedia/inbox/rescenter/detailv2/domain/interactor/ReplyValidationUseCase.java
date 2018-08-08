package com.tokopedia.inbox.rescenter.detailv2.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.discussion.domain.model.replyvalidation.ReplyDiscussionValidationModel;

import rx.Observable;

/**
 * Created by hangnadi on 6/12/17.
 */

public class ReplyValidationUseCase extends UseCase<ReplyDiscussionValidationModel> {

    private final ResCenterRepository repository;

    public ReplyValidationUseCase(ThreadExecutor threadExecutor,
                                  PostExecutionThread postExecutionThread,
                                  ResCenterRepository repository) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    @Override
    public Observable<ReplyDiscussionValidationModel> createObservable(RequestParams requestParams) {
        return repository.replyConversationValidation(requestParams.getParameters());
    }
}
