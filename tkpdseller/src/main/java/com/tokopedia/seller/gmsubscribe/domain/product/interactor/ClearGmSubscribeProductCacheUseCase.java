package com.tokopedia.seller.gmsubscribe.domain.product.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.gmsubscribe.domain.product.GmSubscribeProductRepository;

import rx.Observable;

/**
 * Created by sebastianuskh on 2/9/17.
 */
public class ClearGmSubscribeProductCacheUseCase extends UseCase<Boolean> {
    private final GmSubscribeProductRepository gmSubscribeProductReposistory;

    public ClearGmSubscribeProductCacheUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, GmSubscribeProductRepository gmSubscribeProductReposistory) {
        super(threadExecutor, postExecutionThread);
        this.gmSubscribeProductReposistory = gmSubscribeProductReposistory;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return gmSubscribeProductReposistory.clearGMProductCache();
    }
}
