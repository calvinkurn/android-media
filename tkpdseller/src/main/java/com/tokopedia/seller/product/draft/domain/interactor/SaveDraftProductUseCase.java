package com.tokopedia.seller.product.draft.domain.interactor;

import com.tokopedia.seller.product.draft.domain.model.ProductDraftRepository;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class SaveDraftProductUseCase extends UseCase<Long> {
    private static final String UPLOAD_PRODUCT_INPUT_MODEL = "UPLOAD_PRODUCT_INPUT_MODEL";
    private static final String IS_UPLOADING = "IS_UPLOADING";
    private static final String PREV_DRAFT_ID = "P_DRAFT_ID";
    private final ProductDraftRepository productDraftRepository;

    @Inject
    public SaveDraftProductUseCase(ProductDraftRepository productDraftRepository) {
        this.productDraftRepository = productDraftRepository;
    }

    @Override
    public Observable<Long> createObservable(RequestParams requestParams) {
        ProductViewModel inputModel;
        if (isInputProductNotNull(requestParams) &&
                isUploadProductDomainModel(requestParams)){
            inputModel = (ProductViewModel)
                    requestParams.getObject(UPLOAD_PRODUCT_INPUT_MODEL);
        } else {
            throw new RuntimeException("Input model is missing");
        }
        long prevDraftId = requestParams.getLong(PREV_DRAFT_ID, 0);
        boolean isUploading = requestParams.getBoolean(IS_UPLOADING, false);
        if (prevDraftId <= 0) {
            return productDraftRepository.saveDraft(inputModel, isUploading);
        } else {
            return productDraftRepository.updateDraftToUpload(prevDraftId, inputModel, isUploading);
        }
    }

    private boolean isInputProductNotNull(RequestParams requestParams) {
        return requestParams.getObject(UPLOAD_PRODUCT_INPUT_MODEL) != null;
    }

    private boolean isUploadProductDomainModel(RequestParams requestParams) {
        return requestParams.getObject(UPLOAD_PRODUCT_INPUT_MODEL)
                instanceof ProductViewModel;
    }

    public static RequestParams generateUploadProductParam(ProductViewModel domainModel,
                                                           long previousDraftId,
                                                           boolean isUploading){
        RequestParams params = RequestParams.create();
        params.putObject(UPLOAD_PRODUCT_INPUT_MODEL, domainModel);
        params.putLong(PREV_DRAFT_ID, previousDraftId);
        params.putObject(IS_UPLOADING, isUploading);
        return params;
    }

}
