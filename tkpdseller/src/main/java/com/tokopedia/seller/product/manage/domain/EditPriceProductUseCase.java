package com.tokopedia.seller.product.manage.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.common.constant.ProductNetworkConstant;
import com.tokopedia.seller.shop.common.domain.repository.ShopInfoRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 9/20/17.
 */

public class EditPriceProductUseCase extends UseCase<Boolean> {
    private ActionProductManageRepository actionProductManageRepository;
    private ShopInfoRepository shopInfoRepository;

    @Inject
    public EditPriceProductUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                   ActionProductManageRepository actionProductManageRepository,
                                   ShopInfoRepository shopInfoRepository) {
        super(threadExecutor, postExecutionThread);
        this.actionProductManageRepository = actionProductManageRepository;
        this.shopInfoRepository = shopInfoRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        requestParams.putString(ProductNetworkConstant.SHOP_ID, shopInfoRepository.getShopId());
        return actionProductManageRepository.editPrice(requestParams.getParamsAllValueInString());
    }

    public static RequestParams createRequestParams(String price, String priceCurrency, String productId){
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(ProductNetworkConstant.PRODUCT_ID, productId);
        requestParams.putString(ProductNetworkConstant.PRODUCT_PRICE, price);
        requestParams.putString(ProductNetworkConstant.PRODUCT_PRICE_CURRENCY, priceCurrency);
        return requestParams;
    }
}
