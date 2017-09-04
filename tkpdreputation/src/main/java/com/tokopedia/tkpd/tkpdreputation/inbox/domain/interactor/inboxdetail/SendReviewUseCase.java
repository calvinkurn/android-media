package com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.ReputationRepository;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.SendReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.SendReviewRequestModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.SendReviewValidateDomain;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by nisie on 8/31/17.
 */

public class SendReviewUseCase extends UseCase<SendReviewDomain> {

    private static final String PARAM_REPUTATION_ID = "reputation_id";
    private static final String PARAM_REVIEW_ID = "review_id";
    private static final String PARAM_SHOP_ID = "shop_id";
    private static final String PARAM_PRODUCT_ID = "product_id";
    private static final String PARAM_REVIEW_MESSAGE = "review_message";
    private static final String PARAM_RATING = "rating";

    private static final String PARAM_HAS_PRODUCT_REVIEW_PHOTO = "has_product_review_photo";
    private static final String PARAM_REVIEW_PHOTO_ALL = "product_review_photo_all";
    private static final String PARAM_REVIEW_PHOTO_OBJ = "product_review_photo_obj";
    private static final String PARAM_POST_KEY = "post_key";
    private static final String PARAM_FILE_UPLOADED = "file_uploaded";


    private final SendReviewValidateUseCase sendReviewValidateUseCase;

    public SendReviewUseCase(ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread,
                             SendReviewValidateUseCase sendReviewValidateUseCase) {
        super(threadExecutor, postExecutionThread);
        this.sendReviewValidateUseCase = sendReviewValidateUseCase;
    }

    @Override
    public Observable<SendReviewDomain> createObservable(RequestParams requestParams) {
        final SendReviewRequestModel sendReviewRequestModel = new
                SendReviewRequestModel();
        return getObservableValidateReview(requestParams)
                .flatMap(new Func1<SendReviewRequestModel, Observable<SendReviewDomain>>() {
                    @Override
                    public Observable<SendReviewDomain> call(
                            SendReviewRequestModel sendReviewRequestModel) {
                        return null;
                    }
                });
    }

    private Observable<SendReviewRequestModel> getObservableValidateReview(
            RequestParams requestParams) {
        return sendReviewValidateUseCase.getExecuteObservable(requestParams)
                .flatMap(new Func1<SendReviewValidateDomain, Observable<SendReviewRequestModel>>() {
                    @Override
                    public Observable<SendReviewRequestModel> call(
                            SendReviewValidateDomain sendReviewValidateDomain) {
                        return null;
                    }
                });
    }

    public static RequestParams getParam(String reviewId,
                                         String productId,
                                         String reputationId,
                                         String shopId,
                                         String rating,
                                         String reviewMessage) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_REVIEW_ID, reviewId);
        params.putString(PARAM_PRODUCT_ID, productId);
        params.putString(PARAM_REPUTATION_ID, reputationId);
        params.putString(PARAM_SHOP_ID, shopId);
        params.putString(PARAM_RATING, rating);
        params.putString(PARAM_REVIEW_MESSAGE, reviewMessage);

//        params.putString(PARAM_TOKEN, reviewMessage);
//        params.putString(PARAM_IMAGE_UPLOAD, reviewMessage);
//        params.putString(PARAM_DELETED_IMAGE, reviewMessage);
//
        return params;
    }
}
