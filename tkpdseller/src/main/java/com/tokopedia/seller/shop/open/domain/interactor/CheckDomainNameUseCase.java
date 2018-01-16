package com.tokopedia.seller.shop.open.domain.interactor;


import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.shop.open.constant.ShopExtraConstant;
import com.tokopedia.seller.shop.open.data.repository.ShopOpenRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/20/17.
 */

public class CheckDomainNameUseCase extends UseCase<Boolean> {

    private final ShopOpenRepository shopOpenRepository;

    @Inject
    public CheckDomainNameUseCase(ThreadExecutor threadExecutor,
                                  PostExecutionThread postExecutionThread,
                                  ShopOpenRepository shopOpenRepository) {
        super(threadExecutor, postExecutionThread);
        this.shopOpenRepository = shopOpenRepository;
    }

    public static RequestParams createRequestParams(String domainName){
        RequestParams params = RequestParams.create();
        params.putString(ShopExtraConstant.EXTRA_DOMAIN_NAME, domainName);
        return params;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return shopOpenRepository.checkDomain(requestParams.getString(ShopExtraConstant.EXTRA_DOMAIN_NAME, ""));
    }
}
