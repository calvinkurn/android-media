package com.tokopedia.seller.product.draft.domain.interactor;

import com.tokopedia.product.manage.common.draft.data.db.repository.AddEditProductDraftRepository;
import com.tokopedia.seller.manageitem.data.db.ProductDraftViewModel;
import com.tokopedia.seller.product.draft.domain.mapper.ProductDraftMapper;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 4/26/17.
 */

public class FetchAllDraftProductUseCase extends UseCase<List<ProductDraftViewModel>> {
    private AddEditProductDraftRepository addEditProductDraftRepository;

    @Inject
    public FetchAllDraftProductUseCase(AddEditProductDraftRepository addEditProductDraftRepository) {
        this.addEditProductDraftRepository = addEditProductDraftRepository;
    }

    public static RequestParams createRequestParams() {
        return RequestParams.EMPTY;
    }

    @Override
    public Observable<List<ProductDraftViewModel>> createObservable(RequestParams requestParams) {
        return Observable.fromCallable(() -> addEditProductDraftRepository.getAllDrafts()).map(ProductDraftMapper.INSTANCE::mapDomainDataModelToViewModel);
    }
}
