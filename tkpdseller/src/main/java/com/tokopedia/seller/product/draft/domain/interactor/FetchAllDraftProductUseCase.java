package com.tokopedia.seller.product.draft.domain.interactor;

import com.tokopedia.core.base.domain.CompositeUseCase;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.draft.domain.model.ProductDraftRepository;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 4/26/17.
 */

public class FetchAllDraftProductUseCase extends CompositeUseCase<List<UploadProductInputDomainModel>> {
    private ProductDraftRepository productDraftRepository;

    public FetchAllDraftProductUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                       ProductDraftRepository productDraftRepository) {
        super(threadExecutor, postExecutionThread);
        this.productDraftRepository = productDraftRepository;
    }

    @Override
    public Observable<List<UploadProductInputDomainModel>> createObservable(RequestParams requestParams) {
        return productDraftRepository.getAllDraft();
    }

    public static RequestParams createRequestParams(){
        RequestParams requestParams = RequestParams.create();
        return requestParams;
    }

}
