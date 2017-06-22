package com.tokopedia.seller.product.domain.interactor.productdraft;

import com.tokopedia.core.base.domain.CompositeUseCase;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.domain.ProductDraftRepository;

import rx.Observable;

/**
 * @author sebastianuskh on 5/8/17.
 */

public class ClearAllDraftProductUseCase extends CompositeUseCase<Boolean> {
    private final ProductDraftRepository productDraftRepository;

    public ClearAllDraftProductUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, ProductDraftRepository productDraftRepository) {
        super(threadExecutor, postExecutionThread);
        this.productDraftRepository = productDraftRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return productDraftRepository.clearAllDraft();
    }
}
