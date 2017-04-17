package com.tokopedia.seller.product.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.domain.ProductDraftRepository;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/10/17.
 */

public abstract class UploadProductUseCase<ReturnType> extends UseCase<ReturnType>{
    public static final String UPLOAD_PRODUCT_ID = "UPLOAD_PRODUCT_ID";
    public static final int UNSELECTED_PRODUCT_ID = -1;
    private final ProductDraftRepository productDraftRepository;

    public UploadProductUseCase(ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread, ProductDraftRepository productDraftRepository) {
        super(threadExecutor, postExecutionThread);
        this.productDraftRepository = productDraftRepository;
    }

    @Override
    public Observable<ReturnType> createObservable(RequestParams requestParams) {
        long productId = requestParams.getLong(UPLOAD_PRODUCT_ID, UNSELECTED_PRODUCT_ID);
        if (productId == UNSELECTED_PRODUCT_ID) {
            throw new RuntimeException("Input model is missing");
        }
        return Observable.just(productId)
                .flatMap(new GetProductModelObservable())
                .flatMap(getImageProductObservable())
                .flatMap(getUploadProductObservable());
    }

    public static RequestParams generateUploadProductParam(long productId){
        RequestParams params = RequestParams.create();
        params.putLong(UPLOAD_PRODUCT_ID, productId);
        return params;
    }

    protected abstract
    Func1<UploadProductInputDomainModel, Observable<ReturnType>>
    getUploadProductObservable();

    protected abstract
    Func1<UploadProductInputDomainModel, Observable<UploadProductInputDomainModel>>
    getImageProductObservable();

    private class GetProductModelObservable implements Func1<Long, Observable<UploadProductInputDomainModel>> {
        @Override
        public Observable<UploadProductInputDomainModel> call(Long productId) {
            return productDraftRepository.getDraft(productId);
        }
    }
}
