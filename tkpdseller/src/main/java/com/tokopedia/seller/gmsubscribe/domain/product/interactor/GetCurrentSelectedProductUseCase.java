package com.tokopedia.seller.gmsubscribe.domain.product.interactor;

import com.tokopedia.seller.common.domain.RequestParams;
import com.tokopedia.seller.common.domain.UseCase;
import com.tokopedia.seller.common.domain.executor.PostExecutionThread;
import com.tokopedia.seller.common.domain.executor.ThreadExecutor;
import com.tokopedia.seller.gmsubscribe.domain.product.GMSubscribeProductRepository;
import com.tokopedia.seller.gmsubscribe.domain.product.model.GMProductDomainModel;

import rx.Observable;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public class GetCurrentSelectedProductUseCase extends UseCase<GMProductDomainModel> {
    public static final String PRODUCT_SELECTED_ID = "PRODUCT_SELECTED_ID";
    public static final int UNDEFINED_SELECTED = -1;

    protected final GMSubscribeProductRepository gmSubscribeProductRepository;

    public GetCurrentSelectedProductUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, GMSubscribeProductRepository gmSubscribeProductRepository) {
        super(threadExecutor, postExecutionThread);
        this.gmSubscribeProductRepository = gmSubscribeProductRepository;
    }

    @Override
    public Observable<GMProductDomainModel> createObservable(RequestParams requestParams) {
        return gmSubscribeProductRepository.getCurrentProductSelectedData(requestParams.getInt(PRODUCT_SELECTED_ID, UNDEFINED_SELECTED));
    }

    public static RequestParams createRequestParams(int productId) {
        RequestParams params = RequestParams.create();
        params.putInt(PRODUCT_SELECTED_ID, productId);
        return params;
    }
}
