package com.tokopedia.seller.product.draft.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.manageitem.data.db.ProductDraftViewModel;
import com.tokopedia.seller.manageitem.domain.repository.ProductDraftRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Yehezkiel on 03/06/20
 */
public class FetchAllDraftProductLegacyUseCase extends UseCase<List<ProductDraftViewModel>> {
    private ProductDraftRepository productDraftRepository;

    @Inject
    public FetchAllDraftProductLegacyUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                             ProductDraftRepository productDraftRepository) {
        super(threadExecutor, postExecutionThread);
        this.productDraftRepository = productDraftRepository;
    }

    @Override
    public Observable<List<ProductDraftViewModel>> createObservable(RequestParams requestParams) {
        return productDraftRepository.getAllDraft();
    }

    public static RequestParams createRequestParams() {
        return RequestParams.EMPTY;
    }
}