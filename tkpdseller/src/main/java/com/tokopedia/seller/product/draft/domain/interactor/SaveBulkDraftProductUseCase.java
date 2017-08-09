package com.tokopedia.seller.product.draft.domain.interactor;

import com.tokopedia.core.base.domain.CompositeUseCase;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.draft.domain.model.ProductDraftRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class SaveBulkDraftProductUseCase extends CompositeUseCase<List<Long>> {
    private static final String UPLOAD_PRODUCT_INPUT_MODEL_LIST = "UPLOAD_PRODUCT_INPUT_MODEL_LIST";
    private final ProductDraftRepository productDraftRepository;

    @Inject
    public SaveBulkDraftProductUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, ProductDraftRepository productDraftRepository) {
        super(threadExecutor, postExecutionThread);
        this.productDraftRepository = productDraftRepository;
    }

    @Override
    public Observable<List<Long>> createObservable(RequestParams requestParams) {
        ArrayList<UploadProductInputDomainModel> inputModelList =
                (ArrayList<UploadProductInputDomainModel>) requestParams.getObject(UPLOAD_PRODUCT_INPUT_MODEL_LIST);
        return Observable.from(inputModelList)
                .flatMap(new SaveDraft(0, false))
                .toList();
    }

    public static RequestParams generateUploadProductParam(ArrayList<UploadProductInputDomainModel> domainModel){
        RequestParams params = RequestParams.create();
        params.putObject(UPLOAD_PRODUCT_INPUT_MODEL_LIST, domainModel);
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
