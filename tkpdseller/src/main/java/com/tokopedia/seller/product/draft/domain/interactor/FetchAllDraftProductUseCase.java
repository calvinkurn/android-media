package com.tokopedia.seller.product.draft.domain.interactor;

import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.draft.domain.model.ProductDraftRepository;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 4/26/17.
 */

public class FetchAllDraftProductUseCase extends UseCase<List<ProductViewModel>> {
    private ProductDraftRepository productDraftRepository;

    @Inject
    public FetchAllDraftProductUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                       ProductDraftRepository productDraftRepository) {
        super(threadExecutor, postExecutionThread);
        this.productDraftRepository = productDraftRepository;
    }

    @Override
    public Observable<List<ProductViewModel>> createObservable(RequestParams requestParams) {
        return productDraftRepository.getAllDraft();
    }

    public static RequestParams createRequestParams(){
        return RequestParams.EMPTY;
    }

}
