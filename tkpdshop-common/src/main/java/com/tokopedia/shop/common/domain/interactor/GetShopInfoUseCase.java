package com.tokopedia.shop.common.domain.interactor;

import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.domain.repository.ShopCommonRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by User on 9/8/2017.
 */

public class GetShopInfoUseCase extends UseCase<ShopInfo> {

    private static final String SHOP_ID = "SHOP_ID";

    private ShopCommonRepository shopRepository;

    @Inject
    public GetShopInfoUseCase(ShopCommonRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    @Override
    public Observable<ShopInfo> createObservable(RequestParams requestParams) {
        String shopId = requestParams.getString(SHOP_ID, null);
        return shopRepository.getShopInfo(shopId);
    }

    public static RequestParams createRequestParam(String shopId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(SHOP_ID, shopId);
        return requestParams;
    }
}
