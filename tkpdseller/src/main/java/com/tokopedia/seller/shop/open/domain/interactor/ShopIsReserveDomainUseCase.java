package com.tokopedia.seller.shop.open.domain.interactor;


import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.shop.open.data.repository.ShopOpenRepository;
import com.tokopedia.seller.shop.open.data.model.response.isreservedomain.ResponseIsReserveDomain;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/20/17.
 */

public class ShopIsReserveDomainUseCase extends UseCase<ResponseIsReserveDomain> {

    private final ShopOpenRepository shopOpenRepository;

    @Inject
    public ShopIsReserveDomainUseCase(ThreadExecutor threadExecutor,
                                      PostExecutionThread postExecutionThread,
                                      ShopOpenRepository shopOpenRepository) {
        super(threadExecutor, postExecutionThread);
        this.shopOpenRepository = shopOpenRepository;
    }

    @Override
    public Observable<ResponseIsReserveDomain> createObservable(RequestParams requestParams) {
        return shopOpenRepository.isReserveDomain();
    }
}
