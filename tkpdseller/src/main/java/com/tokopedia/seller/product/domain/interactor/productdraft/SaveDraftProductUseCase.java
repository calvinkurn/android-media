package com.tokopedia.seller.product.domain.interactor.productdraft;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.domain.ProductDraftRepository;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class SaveDraftProductUseCase extends UseCase<Long> {
    private static final String UPLOAD_PRODUCT_INPUT_MODEL = "UPLOAD_PRODUCT_INPUT_MODEL";
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
        return Observable.just(inputModel)
                .flatMap(new SaveDraft());
    }

    private boolean isInputProductNotNull(RequestParams requestParams) {
        return requestParams.getObject(UPLOAD_PRODUCT_INPUT_MODEL) != null;
    }

    private boolean isUploadProductDomainModel(RequestParams requestParams) {
        return requestParams.getObject(UPLOAD_PRODUCT_INPUT_MODEL)
                instanceof UploadProductInputDomainModel;
    }

    public static RequestParams generateUploadProductParam(UploadProductInputDomainModel domainModel){
        RequestParams params = RequestParams.create();
        params.putObject(UPLOAD_PRODUCT_INPUT_MODEL, domainModel);
        return params;
    }

    private class SaveDraft implements Func1<UploadProductInputDomainModel, Observable<Long>> {
        @Override
        public Observable<Long> call(UploadProductInputDomainModel inputModel) {
            return productDraftRepository.saveDraft(inputModel);
        }
    }
}
