package com.tokopedia.inbox.rescenter.createreso.domain.usecase;

import com.tokopedia.inbox.rescenter.createreso.data.source.CreateResolutionSource;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.ProductProblemResponseDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class GetProductProblemUseCase extends UseCase<ProductProblemResponseDomain> {
    public static final String ORDER_ID = "order_id";
    public static final String PARAM_RESOLUTION_ID = "resolutionID";


    private CreateResolutionSource createResolutionSource;

    @Inject
    public GetProductProblemUseCase(CreateResolutionSource createResolutionSource) {
        this.createResolutionSource = createResolutionSource;
    }

    @Override
    public Observable<ProductProblemResponseDomain> createObservable(RequestParams requestParams) {
        return createResolutionSource.getProductProblemList(requestParams);
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
