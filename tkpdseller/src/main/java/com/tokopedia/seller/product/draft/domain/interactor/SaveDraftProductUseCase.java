package com.tokopedia.seller.product.draft.domain.interactor;

import com.tokopedia.core.base.domain.CompositeUseCase;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.draft.domain.model.ProductDraftRepository;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class SaveDraftProductUseCase extends CompositeUseCase<Long> {
    private static final String UPLOAD_PRODUCT_INPUT_MODEL = "UPLOAD_PRODUCT_INPUT_MODEL";
    private static final String IS_UPLOADING = "IS_UPLOADING";
    private static final String PREV_DRAFT_ID = "P_DRAFT_ID";
    private final ProductDraftRepository productDraftRepository;

    @Inject
    public SaveDraftProductUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, ProductDraftRepository productDraftRepository) {
        super(threadExecutor, postExecutionThread);
        this.productDraftRepository = productDraftRepository;
    }

    @Override
    public Observable<Long> createObservable(RequestParams requestParams) {
        UploadProductInputDomainModel inputModel;
        if (isInputProductNotNull(requestParams) &&
                isUploadProductDomainModel(requestParams)){
            inputModel = (UploadProductInputDomainModel)
                    requestParams.getObject(UPLOAD_PRODUCT_INPUT_MODEL);
        } else {
            throw new RuntimeException("Input model is missing");
        }
        long prevDraftId = requestParams.getLong(PREV_DRAFT_ID, 0);
        boolean isUploading = requestParams.getBoolean(IS_UPLOADING, false);
        return Observable.just(inputModel)
                .flatMap(new SaveDraft(prevDraftId, isUploading));
    }

    private boolean isInputProductNotNull(RequestParams requestParams) {
        return requestParams.getObject(UPLOAD_PRODUCT_INPUT_MODEL) != null;
    }

    private boolean isUploadProductDomainModel(RequestParams requestParams) {
        return requestParams.getObject(UPLOAD_PRODUCT_INPUT_MODEL)
                instanceof UploadProductInputDomainModel;
    }

    public static RequestParams generateUploadProductParam(UploadProductInputDomainModel domainModel,
                                                           long previousDraftId,
                                                           boolean isUploading){
        RequestParams params = RequestParams.create();
        params.putObject(UPLOAD_PRODUCT_INPUT_MODEL, domainModel);
        params.putLong(PREV_DRAFT_ID, previousDraftId);
        params.putObject(IS_UPLOADING, isUploading);
        return params;
    }

    private class SaveDraft implements Func1<UploadProductInputDomainModel, Observable<Long>> {
        boolean isUploading;
        long previousDraftId;
        SaveDraft(long previousDraftId, boolean isUploading){
            this.previousDraftId = previousDraftId;
            this.isUploading = isUploading;
        }
        @Override
        public Observable<Long> call(UploadProductInputDomainModel inputModel) {
            if (previousDraftId <= 0) {
                return productDraftRepository.saveDraft(inputModel, isUploading);
            } else {
                return productDraftRepository.updateDraftToUpload(previousDraftId, inputModel, isUploading);
            }
        }
    }
}
