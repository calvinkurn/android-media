package com.tokopedia.tkpd.tkpdreputation.productreview.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.reviewlist.DataResponseReviewProduct;

import rx.Observable;

/**
 * Created by zulfikarrahman on 1/15/18.
 */

public class ProductReviewGetListUseCase extends UseCase<DataResponseReviewProduct> {

    private final ReputationRepository reputationRepository;

    public ProductReviewGetListUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                       ReputationRepository reputationRepository) {
        super(threadExecutor, postExecutionThread);
        this.reputationRepository = reputationRepository;
    }

    @Override
    public Observable<DataResponseReviewProduct> createObservable(RequestParams requestParams) {
        return reputationRepository.getReviewProductList(requestParams);
    }
}
