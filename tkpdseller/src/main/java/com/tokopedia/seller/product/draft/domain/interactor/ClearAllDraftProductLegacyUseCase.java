package com.tokopedia.seller.product.draft.domain.interactor;

import com.tokopedia.product.manage.item.main.draft.domain.ProductDraftRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

public class ClearAllDraftProductLegacyUseCase extends UseCase<Boolean> {

    private final ProductDraftRepository productDraftRepository;

    @Inject
    public ClearAllDraftProductLegacyUseCase(ProductDraftRepository productDraftRepository) {
        this.productDraftRepository = productDraftRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return productDraftRepository.clearAllDraft();
    }
}