package com.tokopedia.tkpd.tkpdreputation.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository;

import rx.Observable;

/**
 * @author by nisie on 9/29/17.
 * https://phab.tokopedia.com/w/api/jerry/post-like-dislike-review/
 */

public class LikeDislikeReviewUseCase extends UseCase<LikeDislikeDomain> {

    private static final String PARAM_REVIEW_ID = "review_id";
    private static final String PARAM_LIKE_STATUS = "like_status";
    private static final String PARAM_PRODUCT_ID = "product_id";
    private static final String PARAM_SHOP_ID = "shop_id";
    public static final int DEFAULT_NOT_LIKED = 0;
    public static final int STATUS_LIKE = 1;
    public static final int STATUS_DISLIKE= 2;
    public static final int STATUS_RESET = 3;
    private static final String PARAM_ACTION = "action";
    private static final String ACTION_LIKE_DISLIKE_REVIEW = "event_like_dislike_review";

    ReputationRepository reputationRepository;

    public LikeDislikeReviewUseCase(ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread,
                                    ReputationRepository reputationRepository) {
        super(threadExecutor, postExecutionThread);
        this.reputationRepository = reputationRepository;
    }

    @Override
    public Observable<LikeDislikeDomain> createObservable(RequestParams requestParams) {
        return reputationRepository.likeDislikeReview(requestParams);
    }

    public static RequestParams getParam(String reviewId, int likeStatus, 
                                         String productId, String shopId) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_REVIEW_ID, reviewId);
        params.putInt(PARAM_LIKE_STATUS, likeStatus);
        params.putString(PARAM_PRODUCT_ID, productId);
        params.putString(PARAM_SHOP_ID, shopId);
        params.putString(PARAM_ACTION, ACTION_LIKE_DISLIKE_REVIEW);
        return params;
    }
}
