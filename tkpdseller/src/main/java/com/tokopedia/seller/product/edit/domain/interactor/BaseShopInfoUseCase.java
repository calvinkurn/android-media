package com.tokopedia.seller.product.edit.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.shop.common.domain.repository.ShopInfoRepository;

import rx.Observable;

/**
 * Created by Hendry on 4/20/2017.
 */

public abstract class BaseShopInfoUseCase<T> extends UseCase<T> {
    protected ShopInfoRepository shopInfoRepository;

    public BaseShopInfoUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ShopInfoRepository shopInfoRepository) {
        super(threadExecutor, postExecutionThread);
        this.shopInfoRepository = shopInfoRepository;
    }

    @Override
    public Observable<T> createObservable(RequestParams requestParams) {
        return getShopInfo();
    }

    // to be overridden by other use cases
    abstract Observable<T> getShopInfo() ;

}