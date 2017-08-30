package com.tokopedia.seller.product.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.domain.ShopInfoRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 7/7/17.
 */

public class DeleteShopInfoUseCase extends UseCase<Boolean> {
    private final ShopInfoRepository shopInfoRepository;

    @Inject
    public DeleteShopInfoUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                 ShopInfoRepository shopInfoRepository) {
        super(threadExecutor, postExecutionThread);
        this.shopInfoRepository = shopInfoRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return shopInfoRepository.clearCacheShopInfo();
    }
}
