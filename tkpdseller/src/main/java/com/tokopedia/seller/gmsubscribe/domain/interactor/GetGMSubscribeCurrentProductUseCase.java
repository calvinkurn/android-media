package com.tokopedia.seller.gmsubscribe.domain.interactor;

import com.tokopedia.seller.gmsubscribe.common.domain.RequestParams;
import com.tokopedia.seller.gmsubscribe.common.domain.UseCase;
import com.tokopedia.seller.gmsubscribe.common.domain.executor.PostExecutionThread;
import com.tokopedia.seller.gmsubscribe.common.domain.executor.ThreadExecutor;
import com.tokopedia.seller.gmsubscribe.domain.ProductRepository;
import com.tokopedia.seller.gmsubscribe.domain.model.product.GMProductDomainModel;

import java.util.List;

import rx.Observable;

/**
 * Created by sebastianuskh on 2/2/17.
 */
public class GetGMSubscribeCurrentProductUseCase extends UseCase<List<GMProductDomainModel>> {
    private final ProductRepository productRepository;

    public GetGMSubscribeCurrentProductUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, ProductRepository productRepository) {
        super(threadExecutor, postExecutionThread);
        this.productRepository = productRepository;
    }

    @Override
    public Observable<List<GMProductDomainModel>> createObservable(RequestParams requestParams) {
        return productRepository.getCurrentProductSelection();
    }
}
