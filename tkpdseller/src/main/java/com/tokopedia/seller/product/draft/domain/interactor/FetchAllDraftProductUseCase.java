package com.tokopedia.seller.product.draft.domain.interactor;

import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.product.manage.item.main.draft.data.model.ProductDraftViewModel;
import com.tokopedia.product.manage.item.main.draft.domain.ProductDraftRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 4/26/17.
 */

public class FetchAllDraftProductUseCase extends UseCase<List<ProductDraftViewModel>> {
    private ProductDraftRepository productDraftRepository;

    @Inject
    public FetchAllDraftProductUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                       ProductDraftRepository productDraftRepository) {
        super(threadExecutor, postExecutionThread);
        this.productDraftRepository = productDraftRepository;
    }

    @Override
    public Observable<List<ProductDraftViewModel>> createObservable(RequestParams requestParams) {
        return productDraftRepository.getAllDraft();
    }

    public static RequestParams createRequestParams(){
        return RequestParams.EMPTY;
    }

}
