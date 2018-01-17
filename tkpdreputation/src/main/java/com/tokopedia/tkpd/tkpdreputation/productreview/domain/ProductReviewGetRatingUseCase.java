package com.tokopedia.tkpd.tkpdreputation.productreview.domain;

import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.reviewstarcount.DataResponseReviewStarCount;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by zulfikarrahman on 1/17/18.
 */

public class ProductReviewGetRatingUseCase extends UseCase<DataResponseReviewStarCount> {
    public static final String PRODUCT_ID = "product_id";
    private ReputationRepository reputationRepository;

    public ProductReviewGetRatingUseCase(ReputationRepository reputationRepository) {
        this.reputationRepository = reputationRepository;
    }

    @Override
    public Observable<DataResponseReviewStarCount> createObservable(RequestParams requestParams) {
        return reputationRepository.getReviewStarCount(requestParams.getString(PRODUCT_ID, ""));
    }

    public RequestParams createRequestParams(String productId){
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PRODUCT_ID, productId);
        return requestParams;
    }
}
