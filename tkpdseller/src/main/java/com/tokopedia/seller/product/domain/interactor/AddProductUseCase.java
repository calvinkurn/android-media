package com.tokopedia.seller.product.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.domain.ProductDraftRepository;
import com.tokopedia.seller.product.domain.interactor.observable.AddImageProductObservable;
import com.tokopedia.seller.product.domain.interactor.observable.AddProductObservable;
import com.tokopedia.seller.product.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/10/17.
 */

public class AddProductUseCase extends UseCase<AddProductDomainModel> {

    public static final String UPLOAD_PRODUCT_ID = "UPLOAD_PRODUCT_ID";
    public static final int UNSELECTED_PRODUCT_ID = -1;

    private final AddImageProductObservable addImageProductObservable;
    private final AddProductObservable addProductObservable;
    private final ProductDraftRepository productDraftRepository;

    @Inject
    public AddProductUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            AddImageProductObservable addImageProductObservable,
            AddProductObservable addProductObservable,
            ProductDraftRepository productDraftRepository) {
        super(threadExecutor, postExecutionThread);
        this.productDraftRepository = productDraftRepository;
        this.addImageProductObservable = addImageProductObservable;
        this.addProductObservable = addProductObservable;
    }

    @Override
    public Observable<AddProductDomainModel> createObservable(RequestParams requestParams) {
        long productId = requestParams.getLong(UPLOAD_PRODUCT_ID, UNSELECTED_PRODUCT_ID);
        if (productId == UNSELECTED_PRODUCT_ID) {
            throw new RuntimeException("Input model is missing");
        }
        return Observable.just(productId)
                .flatMap(new GetProductModelObservable())
                .flatMap(addImageProductObservable)
                .flatMap(addProductObservable);
    }

    private class GetProductModelObservable implements Func1<Long, Observable<UploadProductInputDomainModel>> {
        @Override
        public Observable<UploadProductInputDomainModel> call(Long productId) {
            return productDraftRepository.getDraft(productId);
        }
    }

    public static RequestParams generateUploadProductParam(long productId) {
        RequestParams params = RequestParams.create();
        params.putLong(UPLOAD_PRODUCT_ID, productId);
        return params;
    }
}
