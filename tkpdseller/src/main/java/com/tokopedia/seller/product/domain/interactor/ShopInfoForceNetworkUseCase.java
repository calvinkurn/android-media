package com.tokopedia.seller.product.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.product.domain.ShopInfoRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Hendry on 4/20/2017.
 */

public class ShopInfoForceNetworkUseCase extends ShopInfoUseCase {

    @Inject
    public ShopInfoForceNetworkUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ShopInfoRepository shopInfoRepository) {
        super(threadExecutor, postExecutionThread, shopInfoRepository);
    }

    @Override
    protected Observable<ShopModel> getShopInfo(String shopId, String shopDomain) {
        return shopInfoRepository.getShopInfoFromNetwork(shopId, shopDomain);
    }

    public static RequestParams createRequestParamByShopId(String shopId) {
        return ShopInfoUseCase.createRequestParamByShopId(shopId);
    }

    public static RequestParams createRequestParamByShopDomain(String shopDomain) {
        return ShopInfoUseCase.createRequestParamByShopDomain(shopDomain);
    }

}