package com.tokopedia.seller.gmsubscribe.domain.product.interactor;

import com.tokopedia.seller.common.domain.RequestParams;
import com.tokopedia.seller.common.domain.UseCase;
import com.tokopedia.seller.common.domain.executor.PostExecutionThread;
import com.tokopedia.seller.common.domain.executor.ThreadExecutor;
import com.tokopedia.seller.gmsubscribe.domain.product.GMSubscribeProductRepository;
import com.tokopedia.seller.gmsubscribe.domain.product.model.GMProductDomainModel;

import java.util.List;

import rx.Observable;

/**
 * Created by sebastianuskh on 2/2/17.
 */
public class GetGMSubscribeExtendProductUseCase extends UseCase<List<GMProductDomainModel>> {
    private final GMSubscribeProductRepository GMSubscribeProductRepository;

    public GetGMSubscribeExtendProductUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, GMSubscribeProductRepository GMSubscribeProductRepository) {
        super(threadExecutor, postExecutionThread);
        this.GMSubscribeProductRepository = GMSubscribeProductRepository;
    }

    @Override
    public Observable<List<GMProductDomainModel>> createObservable(RequestParams requestParams) {
        return GMSubscribeProductRepository.getExtendProductSelection();
    }
}
