package com.tokopedia.tkpd.tkpdreputation.productreview.domain;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.reviewlist.DataResponseReviewHelpful;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by zulfikarrahman on 1/17/18.
 */

public class ProductReviewGetHelpfulUseCase extends UseCase<DataResponseReviewHelpful> {
    public static final String PRODUCT_ID = "product_id";
    private ReputationRepository reputationRepository;
    private SessionHandler sessionHandler;

    public ProductReviewGetHelpfulUseCase(ReputationRepository reputationRepository,
                                          SessionHandler sessionHandler) {
        this.reputationRepository = reputationRepository;
        this.sessionHandler = sessionHandler;
    }

    @Override
    public Observable<DataResponseReviewHelpful> createObservable(RequestParams requestParams) {
        return reputationRepository.getReviewHelpful(sessionHandler.getShopID(), requestParams.getString(PRODUCT_ID, ""));
    }

    public RequestParams createRequestParams(String productId){
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PRODUCT_ID, productId);
        return requestParams;
    }
}
