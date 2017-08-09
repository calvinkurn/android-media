package com.tokopedia.seller.product.draft.domain.interactor;

import com.tokopedia.core.base.domain.CompositeUseCase;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.draft.domain.model.ProductDraftRepository;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;

import rx.Observable;

/**
 * Created by zulfikarrahman on 4/26/17.
 */

public class FetchDraftProductUseCase extends CompositeUseCase<UploadProductInputDomainModel> {

    public static final String DRAFT_PRODUCT_ID = "DRAFT_PRODUCT_ID";
    private ProductDraftRepository productDraftRepository;

    public FetchDraftProductUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                    ProductDraftRepository productDraftRepository) {
        super(threadExecutor, postExecutionThread);
        this.productDraftRepository = productDraftRepository;
    }

    @Override
    public Observable<UploadProductInputDomainModel> createObservable(RequestParams requestParams) {
        return productDraftRepository.getDraft(requestParams.getLong(DRAFT_PRODUCT_ID, 0));
    }

    public static RequestParams createRequestParams(String draftProductId){
        RequestParams requestParams = RequestParams.create();
        requestParams.putLong(DRAFT_PRODUCT_ID, Long.parseLong(draftProductId));
        return requestParams;
    }

}
