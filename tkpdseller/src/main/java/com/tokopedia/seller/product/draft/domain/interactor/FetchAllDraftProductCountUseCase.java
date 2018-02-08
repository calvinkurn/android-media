package com.tokopedia.seller.product.draft.domain.interactor;

import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.draft.domain.model.ProductDraftRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 4/26/17.
 */

public class FetchAllDraftProductCountUseCase extends UseCase<Long> {
    private ProductDraftRepository productDraftRepository;

    @Inject
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
        return RequestParams.EMPTY;
    }

}
