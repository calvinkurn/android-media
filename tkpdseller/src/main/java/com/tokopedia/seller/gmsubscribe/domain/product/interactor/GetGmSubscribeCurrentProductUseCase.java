package com.tokopedia.seller.gmsubscribe.domain.product.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.gmsubscribe.domain.product.GmSubscribeProductRepository;
import com.tokopedia.seller.gmsubscribe.domain.product.model.GmProductDomainModel;

import java.util.List;

import rx.Observable;

/**
 * Created by sebastianuskh on 2/2/17.
 */
public class GetGmSubscribeCurrentProductUseCase extends UseCase<List<GmProductDomainModel>> {
    private final GmSubscribeProductRepository GmSubscribeProductRepository;

    public GetGmSubscribeCurrentProductUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, GmSubscribeProductRepository GmSubscribeProductRepository) {
        super(threadExecutor, postExecutionThread);
        this.GmSubscribeProductRepository = GmSubscribeProductRepository;
    }

    @Override
    public Observable<List<GmProductDomainModel>> createObservable(RequestParams requestParams) {
        return GmSubscribeProductRepository.getCurrentProductSelection();
    }
}
