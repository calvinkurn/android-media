package com.tokopedia.tkpd.tkpdreputation.productreview.domain;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.reviewlist.DataResponseReviewProduct;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by zulfikarrahman on 1/15/18.
 */

public class ProductReviewGetListUseCase extends UseCase<DataResponseReviewProduct> {

    public static final String PRODUCT_ID = "product_id";
    public static final String PAGE = "page";
    public static final String PER_PAGE = "per_page";
    public static final String RATING = "rating";
    public static final String DEFAULT_PER_PAGE = "10";
    private final ReputationRepository reputationRepository;

    public ProductReviewGetListUseCase(ReputationRepository reputationRepository) {
        this.reputationRepository = reputationRepository;
    }

    @Override
    public Observable<DataResponseReviewProduct> createObservable(RequestParams requestParams) {
        return reputationRepository.getReviewProductList(
                requestParams.getString(PRODUCT_ID, ""),
                requestParams.getString(PAGE, ""),
                requestParams.getString(PER_PAGE, ""),
                requestParams.getString(RATING, "")
                );
    }

    public RequestParams createRequestParams(String productId, String page, String rating){
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PRODUCT_ID, productId);
        requestParams.putString(PAGE, page);
        requestParams.putString(PER_PAGE, DEFAULT_PER_PAGE);
        requestParams.putString(RATING, rating);
        return requestParams;
    }
}
