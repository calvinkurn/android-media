package com.tokopedia.seller.product.domain.interactor.productdraft;

import com.tokopedia.core.base.domain.CompositeUseCase;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.domain.ProductDraftRepository;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 4/26/17.
 */

public class FetchAllDraftProductCountUseCase extends CompositeUseCase<Long> {
    private ProductDraftRepository productDraftRepository;

    public FetchAllDraftProductCountUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                            ProductDraftRepository productDraftRepository) {
        super(threadExecutor, postExecutionThread);
        this.productDraftRepository = productDraftRepository;
    }

    @Override
    public Observable<Long> createObservable(RequestParams requestParams) {
        return productDraftRepository.getAllDraftCount();
    }

    public static RequestParams createRequestParams(){
        RequestParams requestParams = RequestParams.create();
        return requestParams;
    }

}
