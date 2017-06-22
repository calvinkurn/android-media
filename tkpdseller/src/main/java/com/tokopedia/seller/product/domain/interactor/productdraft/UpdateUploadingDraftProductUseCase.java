package com.tokopedia.seller.product.domain.interactor.productdraft;

import com.tokopedia.core.base.domain.CompositeUseCase;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.domain.ProductDraftRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 4/26/17.
 */

public class UpdateUploadingDraftProductUseCase extends CompositeUseCase<Boolean> {

    private static final String DRAFT_PRODUCT_ID = "prd_id";
    private static final String IS_UPLOADING = "is_uploading";
    private ProductDraftRepository productDraftRepository;

    @Inject
    public UpdateUploadingDraftProductUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                              ProductDraftRepository productDraftRepository) {
        super(threadExecutor, postExecutionThread);
        this.productDraftRepository = productDraftRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return productDraftRepository.updateuploadingStatusDraft(requestParams.getLong(DRAFT_PRODUCT_ID, 0),
                requestParams.getBoolean(IS_UPLOADING, false) );
    }

    public static RequestParams createRequestParams(String draftProductId,
                                                    boolean isUploading){
        RequestParams requestParams = RequestParams.create();
        requestParams.putLong(DRAFT_PRODUCT_ID, Long.parseLong(draftProductId));
        requestParams.putBoolean(IS_UPLOADING, isUploading);
        return requestParams;
    }

}
