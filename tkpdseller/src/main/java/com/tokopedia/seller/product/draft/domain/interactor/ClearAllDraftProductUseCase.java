package com.tokopedia.seller.product.draft.domain.interactor;

import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.draft.domain.model.ProductDraftRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 5/8/17.
 */

public class ClearAllDraftProductUseCase extends UseCase<Boolean> {
    private final ProductDraftRepository productDraftRepository;

    @Inject
    public ClearAllDraftProductUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, ProductDraftRepository productDraftRepository) {
        super(threadExecutor, postExecutionThread);
        this.productDraftRepository = productDraftRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return productDraftRepository.clearAllDraft();
    }
}
