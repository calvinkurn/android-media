package com.tokopedia.discovery.newdiscovery.hotlist.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.discovery.newdiscovery.data.repository.ProductRepository;
import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;

import rx.Observable;

/**
 * Created by hangnadi on 10/10/17.
 */

public class GetHotlistLoadMoreUseCase extends UseCase<SearchResultModel> {

    private final ProductRepository productRepository;

    public GetHotlistLoadMoreUseCase(ThreadExecutor threadExecutor,
                                       PostExecutionThread postExecutionThread,
                                       ProductRepository productRepository) {
        super(threadExecutor, postExecutionThread);
        this.productRepository = productRepository;
    }

    @Override
    public Observable<SearchResultModel> createObservable(RequestParams requestParams) {
        return productRepository.getProduct(requestParams.getParameters());
    }
}
