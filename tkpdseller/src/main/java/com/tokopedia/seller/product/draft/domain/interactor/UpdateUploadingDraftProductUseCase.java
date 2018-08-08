package com.tokopedia.seller.product.draft.domain.interactor;

import com.tokopedia.seller.product.draft.domain.model.ProductDraftRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 4/26/17.
 */

public class UpdateUploadingDraftProductUseCase extends UseCase<Boolean> {

    private static final String DRAFT_PRODUCT_ID = "DRAFT_PRODUCT_ID";
    private static final String IS_UPLOADING = "IS_UPLOADING";

    private ProductDraftRepository productDraftRepository;

    @Inject
    public UpdateUploadingDraftProductUseCase(ProductDraftRepository productDraftRepository) {
        this.productDraftRepository = productDraftRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return productDraftRepository.updateuploadingStatusDraft(requestParams.getLong(DRAFT_PRODUCT_ID, 0),
                requestParams.getBoolean(IS_UPLOADING, false));
    }

    public static RequestParams createRequestParams(long draftProductId,
                                                    boolean isUploading) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putLong(DRAFT_PRODUCT_ID, draftProductId);
        requestParams.putBoolean(IS_UPLOADING, isUploading);
        return requestParams;
    }

    public static RequestParams createRequestParamsUpdateAll(boolean isUploading) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putBoolean(IS_UPLOADING, isUploading);
        return requestParams;
    }

}
