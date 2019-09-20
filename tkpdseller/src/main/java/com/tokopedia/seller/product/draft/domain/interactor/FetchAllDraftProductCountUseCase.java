package com.tokopedia.seller.product.draft.domain.interactor;

import com.tokopedia.product.manage.item.main.draft.domain.ProductDraftRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 4/26/17.
 */

public class FetchAllDraftProductCountUseCase extends UseCase<Long> {
    private ProductDraftRepository productDraftRepository;

    @Inject
    public FetchAllDraftProductCountUseCase(ProductDraftRepository productDraftRepository) {
        this.productDraftRepository = productDraftRepository;
    }

    @Override
    public Observable<Long> createObservable(RequestParams requestParams) {
        return productDraftRepository.getAllDraftCount();
    }

    public static RequestParams createRequestParams(){
        return RequestParams.EMPTY;
    }

}
