package com.tokopedia.gm.subscribe.domain.product.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.gm.subscribe.domain.product.GmSubscribeProductRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 2/9/17.
 */
public class ClearGmSubscribeProductCacheUseCase extends UseCase<Boolean> {
    private final GmSubscribeProductRepository gmSubscribeProductReposistory;

    @Inject
    public ClearGmSubscribeProductCacheUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, GmSubscribeProductRepository gmSubscribeProductReposistory) {
        super(threadExecutor, postExecutionThread);
        this.gmSubscribeProductReposistory = gmSubscribeProductReposistory;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return gmSubscribeProductReposistory.clearGMProductCache();
    }
}
