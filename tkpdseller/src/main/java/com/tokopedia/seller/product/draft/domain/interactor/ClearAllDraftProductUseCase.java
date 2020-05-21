package com.tokopedia.seller.product.draft.domain.interactor;

import com.tokopedia.product.manage.common.draft.data.db.repository.AddEditProductDraftRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 5/8/17.
 */

public class ClearAllDraftProductUseCase extends UseCase<Boolean> {
    private final AddEditProductDraftRepository addEditProductDraftRepository;

    @Inject
    public ClearAllDraftProductUseCase(AddEditProductDraftRepository addEditProductDraftRepository) {
        this.addEditProductDraftRepository = addEditProductDraftRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return Observable.just(addEditProductDraftRepository.deleteAllDrafts());
    }
}
