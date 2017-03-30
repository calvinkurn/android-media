package com.tokopedia.inbox.rescenter.discussion.domain;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.discussion.domain.model.ActionDiscussionModel;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nisie on 3/30/17.
 */

public class SendResCenterDiscussionUseCase extends UseCase<ActionDiscussionModel> {

    private final ResCenterRepository resCenterRepository;

    public SendResCenterDiscussionUseCase(JobExecutor jobExecutor,
                                          UIThread uiThread,
                                          ResCenterRepository resCenterRepository) {
        super(jobExecutor, uiThread);
        this.resCenterRepository = resCenterRepository;
    }

    @Override
    public Observable<ActionDiscussionModel> createObservable(RequestParams requestParams) {
        return replyConversationValidation(getReplyValidationParam(requestParams))
                .flatMap(new Func1<ActionDiscussionModel, Observable<ActionDiscussionModel>>() {
                    @Override
                    public Observable<ActionDiscussionModel> call(ActionDiscussionModel actionDiscussionModel) {
                        return null;
                    }
                });
    }

    private Observable<ActionDiscussionModel> replyConversationValidation(RequestParams replyValidationParam) {
        return resCenterRepository.replyConversationValidation(replyValidationParam.getParameters());
    }

    private RequestParams getReplyValidationParam(RequestParams requestParams) {
        RequestParams params = RequestParams.create();

        return params;
    }
}