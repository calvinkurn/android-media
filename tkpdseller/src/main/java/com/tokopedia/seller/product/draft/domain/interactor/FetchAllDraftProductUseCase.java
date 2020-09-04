package com.tokopedia.seller.product.draft.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.product.manage.common.feature.draft.data.db.repository.AddEditProductDraftRepository;
import com.tokopedia.product.manage.item.main.draft.data.model.ProductDraftViewModel;
import com.tokopedia.seller.product.draft.domain.mapper.ProductDraftMapper;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 4/26/17.
 */

public class FetchAllDraftProductUseCase extends UseCase<List<ProductDraftViewModel>> {
    private AddEditProductDraftRepository addEditProductDraftRepository;
    private ProductDraftMapper mapper;

    @Inject
    public FetchAllDraftProductUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                       AddEditProductDraftRepository addEditProductDraftRepository, ProductDraftMapper mapper) {
        super(threadExecutor, postExecutionThread);
        this.addEditProductDraftRepository = addEditProductDraftRepository;
        this.mapper = mapper;
    }

    @Override
    public Observable<List<ProductDraftViewModel>> createObservable(RequestParams requestParams) {
        return Observable.fromCallable(() -> addEditProductDraftRepository.getAllDrafts())
                .map(productInputModels -> mapper.mapDomainDataModelToViewModel(productInputModels));
    }

    public static RequestParams createRequestParams() {
        return RequestParams.EMPTY;
    }
}
