package com.tokopedia.seller.product.draft.domain.interactor;

import com.tokopedia.seller.product.draft.domain.model.ProductDraftRepository;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by zulfikarrahman on 4/26/17.
 */

public class FetchDraftProductUseCase extends UseCase<ProductViewModel> {

    public static final String DRAFT_PRODUCT_ID = "DRAFT_PRODUCT_ID";
    private ProductDraftRepository productDraftRepository;

    public FetchDraftProductUseCase(ProductDraftRepository productDraftRepository) {
        this.productDraftRepository = productDraftRepository;
    }

    @Override
    public Observable<ProductViewModel> createObservable(RequestParams requestParams) {
        return productDraftRepository.getDraft(requestParams.getLong(DRAFT_PRODUCT_ID, 0));
    }

    public static RequestParams createRequestParams(long draftProductId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putLong(DRAFT_PRODUCT_ID, draftProductId);
        return requestParams;
    }

}
