package com.tokopedia.shop.info.domain.interactor;

import com.tokopedia.interfaces.merchant.shop.info.ShopInfo;
import com.tokopedia.shop.info.domain.repository.ShopInfoRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by User on 9/8/2017.
 */

public class GetShopInfoUseCase extends UseCase<ShopInfo> {

    private static final String SHOP_ID = "SHOP_ID";

    private ShopInfoRepository shopInfoRepository;

    @Inject
    public GetShopInfoUseCase(ShopInfoRepository shopInfoRepository) {
        super();
        this.shopInfoRepository = shopInfoRepository;
    }

    @Override
    public Observable<ShopInfo> createObservable(RequestParams requestParams) {
        String shopId = requestParams.getString(SHOP_ID, null);
        return shopInfoRepository.getShopInfo(shopId);
    }

    public static RequestParams createRequestParam(String shopId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(SHOP_ID, shopId);
        return requestParams;
    }
}
