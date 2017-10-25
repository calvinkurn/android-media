package com.tokopedia.inbox.rescenter.detailv2.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.discussion.domain.model.replyvalidation.ReplyDiscussionValidationModel;

import rx.Observable;

/**
 * Created by nisie on 4/3/17.
 */

public class ReplyDiscussionValidationUseCase extends UseCase<ReplyDiscussionValidationModel> {

    public static final String PARAM_RESOLUTION_ID = "resolution_id";
    public static final String PARAM_EDIT_SOL_FLAG = "edit_solution_flag";
    public static final String PARAM_FLAG_RECEIVED = "flag_received";
    public static final String PARAM_PHOTOS = "photos";
    public static final String PARAM_REFUND_AMOUNT = "refund_amount";
    public static final String PARAM_REPLY_MSG = "reply_msg";
    public static final String PARAM_SOLUTION = "solution";
    public static final String PARAM_TROUBLE_TYPE = "trouble_type";
    public static final String DEFAULT_EDIT_SOL_FLAG = "0";
    public static final String DEFAULT_REFUND_AMOUNT = "0";
    public static final String DEFAULT_SOLUTION = "0";
    public static final String DEFAULT_TROUBLE_TYPE = "0";

    private final ResCenterRepository resCenterRepository;

    public ReplyDiscussionValidationUseCase(ThreadExecutor threadExecutor,
                                            PostExecutionThread postExecutionThread,
                                            ResCenterRepository resCenterRepository) {
        super(threadExecutor, postExecutionThread);
        this.resCenterRepository = resCenterRepository;
    }

    @Override
    public Observable<ReplyDiscussionValidationModel> createObservable(RequestParams requestParams) {
        return resCenterRepository.replyConversationValidation(requestParams.getParameters());
    }
}