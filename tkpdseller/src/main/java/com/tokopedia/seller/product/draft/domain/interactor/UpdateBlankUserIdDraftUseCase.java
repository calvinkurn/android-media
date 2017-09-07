package com.tokopedia.seller.product.draft.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.product.draft.domain.model.ProductDraftRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by hendry on 4/26/17.
 */

public class UpdateBlankUserIdDraftUseCase extends UseCase<Boolean> {

    private ProductDraftRepository productDraftRepository;

    @Inject
    public UpdateBlankUserIdDraftUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                         ProductDraftRepository productDraftRepository) {
        super(threadExecutor, postExecutionThread);
        this.productDraftRepository = productDraftRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return productDraftRepository.updateBlankUserIdDraft();
    }

    public static RequestParams createRequestParams(){
        return RequestParams.create();
    }

}
