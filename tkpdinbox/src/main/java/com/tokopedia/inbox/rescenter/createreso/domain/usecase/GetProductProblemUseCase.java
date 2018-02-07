package com.tokopedia.inbox.rescenter.createreso.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.createreso.data.repository.ProductProblemRepository;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.ProductProblemResponseDomain;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class GetProductProblemUseCase extends UseCase<ProductProblemResponseDomain> {
    public static final String ORDER_ID = "order_id";
    public static final String PARAM_RESOLUTION_ID = "resolutionID";

    private ProductProblemRepository productProblemRepository;

    public GetProductProblemUseCase(ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread,
                                    ProductProblemRepository productProblemRepository) {
        super(threadExecutor, postExecutionThread);
        this.productProblemRepository = productProblemRepository;
    }

    @Override
    public Observable<ProductProblemResponseDomain> createObservable(RequestParams requestParams) {
        return productProblemRepository.getProductProblemFromCloud(requestParams);
    }

    public RequestParams getProductProblemUseCaseParam(String orderId, String resolutionId) {
        RequestParams params = RequestParams.create();
        params.putString(ORDER_ID, orderId);
        if (!resolutionId.isEmpty()) {
            params.putString(PARAM_RESOLUTION_ID, resolutionId);
        }
        return params;
    }

}
