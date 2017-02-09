package com.tokopedia.seller.gmsubscribe.domain.product.interactor;

import com.tokopedia.seller.common.domain.RequestParams;
import com.tokopedia.seller.common.domain.UseCase;
import com.tokopedia.seller.common.domain.executor.PostExecutionThread;
import com.tokopedia.seller.common.domain.executor.ThreadExecutor;
import com.tokopedia.seller.gmsubscribe.domain.product.GMSubscribeProductRepository;

import rx.Observable;

/**
 * Created by sebastianuskh on 2/9/17.
 */
public class ClearGMSubscribeProductCacheUseCase extends UseCase<Boolean>{
    private final GMSubscribeProductRepository gmSubscribeProductReposistory;

    public ClearGMSubscribeProductCacheUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, GMSubscribeProductRepository gmSubscribeProductReposistory) {
        super(threadExecutor, postExecutionThread);
        this.gmSubscribeProductReposistory = gmSubscribeProductReposistory;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return gmSubscribeProductReposistory.clearGMProductCache();
    }
}
