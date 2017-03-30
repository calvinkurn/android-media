package com.tokopedia.inbox.rescenter.product.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.product.domain.model.ProductDetailData;

import rx.Observable;

/**
 * Created by hangnadi on 3/28/17.
 */

public class GetProductDetailUseCase extends UseCase<ProductDetailData> {

    public static final String PARAM_TROUBLE_ID = "trouble_id";
    private final ResCenterRepository resCenterRepository;

    public GetProductDetailUseCase(ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread,
                                   ResCenterRepository resCenterRepository) {
        super(threadExecutor, postExecutionThread);
        this.resCenterRepository = resCenterRepository;
    }

    @Override
    public Observable<ProductDetailData> createObservable(RequestParams requestParams) {
        return resCenterRepository.getDetailProduct(
                requestParams.getString(PARAM_TROUBLE_ID, ""),
                requestParams.getParameters()
        );
    }
}
