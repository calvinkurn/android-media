package com.tokopedia.seller.shop.open.domain.interactor;


import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.shop.open.constant.ShopExtraConstant;
import com.tokopedia.seller.shop.open.data.repository.ShopOpenRepository;
import com.tokopedia.seller.shop.open.data.model.response.ResponseReserveDomain;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/20/17.
 */

public class ReserveShopNameDomainUseCase extends UseCase<ResponseReserveDomain> {
    private final ShopOpenRepository shopOpenRepository;

    @Inject
    public ReserveShopNameDomainUseCase(ThreadExecutor threadExecutor,
                                        PostExecutionThread postExecutionThread,
                                        ShopOpenRepository shopOpenRepository) {
        super(threadExecutor, postExecutionThread);
        this.shopOpenRepository = shopOpenRepository;
    }

    @Override
    public Observable<ResponseReserveDomain> createObservable(RequestParams requestParams) {
        String shopName = requestParams.getString(ShopExtraConstant.EXTRA_SHOP_NAME, "");
        String shopDomainName = requestParams.getString(ShopExtraConstant.EXTRA_DOMAIN_NAME, "");
        return shopOpenRepository.reserveShopNameDomain(shopName, shopDomainName);
    }

    public static RequestParams createRequestParams(String shopName, String shopDomainName){
        RequestParams params = RequestParams.create();
        params.putString(ShopExtraConstant.EXTRA_SHOP_NAME, shopName);
        params.putString(ShopExtraConstant.EXTRA_DOMAIN_NAME, shopDomainName);
        return params;
    }
}
