package com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.SendReplyReviewDomain;

import rx.Observable;

/**
 * @author by nisie on 9/28/17.
 */

public class SendReplyReviewUseCase extends UseCase<SendReplyReviewDomain> {

    private static final String PARAM_REVIEW_ID = "review_id";
    private static final String PARAM_PRODUCT_ID = "product_id";
    private static final String PARAM_SHOP_ID = "shop_id";
    private static final String PARAM_REPUTATION_ID = "reputation_id";
    private static final String PARAM_RESPONSE_MESSAGE = "response_message";
    private static final String PARAM_ACTION = "action";
    private static final String ACTION_ = "action";


    ReputationRepository reputationRepository;

    public SendReplyReviewUseCase(ThreadExecutor threadExecutor,
                                  PostExecutionThread postExecutionThread,
                                  ReputationRepository reputationRepository) {
        super(threadExecutor, postExecutionThread);
        this.reputationRepository = reputationRepository;
    }

    @Override
    public Observable<SendReplyReviewDomain> createObservable(RequestParams requestParams) {
        return reputationRepository.insertReviewResponse(requestParams);
    }

    public static RequestParams getParam(String reputationId, String productId,
                                         String shopId, String reviewId, String replyReview) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_REVIEW_ID, reviewId);
        requestParams.putString(PARAM_PRODUCT_ID, productId);
        requestParams.putString(PARAM_SHOP_ID, shopId);
        requestParams.putString(PARAM_REPUTATION_ID, reputationId);
        requestParams.putString(PARAM_RESPONSE_MESSAGE, replyReview);
        return requestParams;
    }
}
