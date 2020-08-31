package com.tokopedia.seller.product.draft.domain.interactor;

import com.tokopedia.seller.manageitem.domain.repository.ProductDraftRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

public class DeleteSingleDraftProductLegacyUseCase extends UseCase<Boolean> {

    public static final String DRAFT_PRODUCT_ID = "DRAFT_PRODUCT_ID";
    private ProductDraftRepository productDraftRepository;

    @Inject
    public DeleteSingleDraftProductLegacyUseCase(ProductDraftRepository productDraftRepository) {
        this.productDraftRepository = productDraftRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return productDraftRepository.deleteDraft(requestParams.getLong(DRAFT_PRODUCT_ID, Long.MIN_VALUE));
    }

    public static RequestParams createRequestParams(long draftProductId){
        RequestParams requestParams = RequestParams.create();
        requestParams.putLong(DRAFT_PRODUCT_ID, draftProductId);
        return requestParams;
    }

}
